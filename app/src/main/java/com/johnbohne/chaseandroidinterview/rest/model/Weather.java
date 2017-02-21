package com.johnbohne.chaseandroidinterview.rest.model;

/**
 * Created by john on 1/10/17.
 */

public class Weather {
    int id;
    String main; //ex: "Rain"
    String description; //ex: "light rain"
    String icon; //ex. "10n"

    public int getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}
