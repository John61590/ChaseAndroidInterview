package com.johnbohne.chaseandroidinterview.rest.model;

import java.util.List;

/**
 * Created by john on 1/10/17.
 */

public class WeatherModel {
    Coordinate coord;
    List<Weather> weather; //can be "mist" and "light rain" for example
    String base;
    Main main;
    int id; //city id
    String name; //city name
    Sys sys;
    int cod; //200 is OK, 502 is "Error: Not found city"
    String message;

    public Coordinate getCoord() {
        return coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Sys getSys() {
        return sys;
    }
    public int getCod() {
        return cod;
    }

    public String getMessage() {
        return message;
    }

}
