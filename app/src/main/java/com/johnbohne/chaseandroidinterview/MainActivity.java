package com.johnbohne.chaseandroidinterview;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.johnbohne.chaseandroidinterview.rest.api.WeatherService;
import com.johnbohne.chaseandroidinterview.rest.model.Weather;
import com.johnbohne.chaseandroidinterview.rest.model.WeatherModel;
import com.squareup.picasso.Picasso;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<WeatherModel> {

    private static final String TAG = MainActivity.class.getName();

    String mQuery;

    private TextView mSearchIntroTextView;
    private TextView mPlaceName;
    private TextView mTemperature;
    private LinearLayout mWeatherIconList;
    private LinearLayout mWeatherLayout;

    private MenuItem mSearchMenuItem;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        mSearchIntroTextView = (TextView) findViewById(R.id.search_intro_text);
        mWeatherLayout = (LinearLayout) findViewById(R.id.weather_layout);
        mPlaceName = (TextView) findViewById(R.id.place_name);
        mTemperature = (TextView) findViewById(R.id.temp);
        mWeatherIconList = (LinearLayout) findViewById(R.id.weather_icon_list);



    }
    @Override
    public void onStart() {
        super.onStart();

        handleIntent(getIntent());
    }
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);
            //put the query in sharedpreferences for later storage
            SharedPreferences preferences = getSharedPreferences("MainActivity.class", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("query", mQuery).apply();
            getWeatherInfoRetrofit();
        } else if (getSharedPreferences("MainActivity.class", MODE_PRIVATE).getString("query", null) != null) {
            mQuery = getSharedPreferences("MainActivity.class", MODE_PRIVATE).getString("query", null);
            getWeatherInfoRetrofit();
        } else {
            //default text
            mSearchIntroTextView.setVisibility(View.VISIBLE);
            mSearchIntroTextView.setText(getString(R.string.search_instructions));
        }
    }

    public void getWeatherInfoRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        // prepare call in Retrofit 2.0
        WeatherService weatherServiceAPI = retrofit.create(WeatherService.class);
        Call<WeatherModel> call = weatherServiceAPI.getWeatherInfo(mQuery);
        //asynchronous call
        call.enqueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);

        mSearchMenuItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            onSearchRequested();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSearchRequested() { // Search key pressed.
        mSearchIntroTextView.setText("");
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //doing this so the text in search box is removed when activity is started again
        mSearchMenuItem.collapseActionView();
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
        if (response.isSuccessful()) {
            WeatherModel weatherModel = response.body();
            if (!weatherModel.getSys().getCountry().equalsIgnoreCase("US")) {
                //display error message since it's not a "US" city
                mSearchIntroTextView.setVisibility(View.VISIBLE);
                mSearchIntroTextView.setText(String.format(getString(R.string.incorrect_country), mQuery));
            } else {
                //setup view
                mSearchIntroTextView.setVisibility(View.GONE);
                mWeatherLayout.setVisibility(View.VISIBLE);
                mWeatherIconList.removeAllViewsInLayout();
                mPlaceName.setText(weatherModel.getName());
                mTemperature.setText(String.format(getString(R.string.temperature), weatherModel.getMain().getTemp()));
                for (Weather weather : weatherModel.getWeather()) {
                    RelativeLayout relativeLayout = new RelativeLayout(this);
                    RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                            getPixelFromDP(48));
                    //height for one-line list item with icon according to material design guidelines is 48dp
                    relativeLayout.setLayoutParams(relativeLayoutParams);
                    ImageView imageView = new ImageView(this);
                    imageView.setId(View.generateViewId());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(getPixelFromDP(40), getPixelFromDP(40));
                    imageViewParams.gravity = Gravity.CENTER_VERTICAL;
                    imageView.setLayoutParams(imageViewParams);
                    //the image size originally is 50 px by 50px
                    Picasso.with(this).load("http://openweathermap.org/img/w/" + weather.getIcon() + ".png").into(imageView);
                    relativeLayout.addView(imageView);
                    LinearLayout contentLayout = new LinearLayout(this);
                    RelativeLayout.LayoutParams contentLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getPixelFromDP(56));
                    contentLayoutParams.setMargins(getPixelFromDP(16), 0, 0, 0);
                    contentLayoutParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
                    //contentLayoutParams.addRule(RelativeLayout.ALIGN_TOP, imageView.getId());
                    contentLayout.setLayoutParams(contentLayoutParams);
                    contentLayout.setGravity(Gravity.CENTER_VERTICAL);

                    TextView textView = new TextView(this);
                    textView.setText(weather.getDescription());
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); //16sp according to guidelines
                    textView.setTextColor(getResources().getColor(R.color.primary_text));
                    textView.setGravity(Gravity.CENTER_VERTICAL);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    contentLayout.addView(textView);
                    relativeLayout.addView(contentLayout);
                    mWeatherIconList.addView(relativeLayout);
                }

            }
        } else {
            //it wasn't successful
            mSearchIntroTextView.setVisibility(View.VISIBLE);
            mSearchIntroTextView.setText(String.format(getString(R.string.no_results), mQuery));
        }
    }
    public int getPixelFromDP(int dp) {
        float d = this.getResources().getDisplayMetrics().density;
        return (int)(dp * d); // margin in pixels
    }

    @Override
    public void onFailure(Call<WeatherModel> call, Throwable t) {
            t.printStackTrace();
    }
}
