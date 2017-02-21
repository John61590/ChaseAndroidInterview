package com.johnbohne.chaseandroidinterview.rest.model;

/**
 * Created by john on 1/10/17.
 */

public class Main {
    double temp; //unit default: kelvins but i have imperial to true
    double pressure; //Atmospheric pressure (on the sea level, if there is no sea_level or grnd_level data), hPa
    double humidity; //not sure if this can be a decimal percentage or not
    double temp_min; //min temp AT THE MOMENT (may be in kelvins)
    double temp_max; //max temp AT THE MOMENT (may be in kelvins)
    double sea_level; //Atmospheric pressure on the sea level, hPa
    double grnd_level; //Atmospheric pressure on the ground level, hPa

    public double getTemp() {
        return temp;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getSea_level() {
        return sea_level;
    }

    public double getGrnd_level() {
        return grnd_level;
    }

}
