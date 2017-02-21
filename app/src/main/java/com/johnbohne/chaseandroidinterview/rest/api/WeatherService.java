package com.johnbohne.chaseandroidinterview.rest.api;

import com.johnbohne.chaseandroidinterview.rest.model.WeatherModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


/**
 * Created by john on 1/10/17.
 */

public interface WeatherService {
    @GET("data/2.5/weather?APPID=fd5e77eedb99ff300282fbc7d70ea71f&units=imperial")
    Call<WeatherModel> getWeatherInfo(@Query("q") String query);
}
