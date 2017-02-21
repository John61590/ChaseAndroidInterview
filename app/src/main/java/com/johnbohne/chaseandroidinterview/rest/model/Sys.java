package com.johnbohne.chaseandroidinterview.rest.model;

/**
 * Created by john on 1/10/17.
 */

public class Sys {
    int type;
    int id;
    double message;
    String country;
    long sunrise;
    long sunset;

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public double getMessage() {
        return message;
    }

    public String getCountry() {
        return country;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }
}
