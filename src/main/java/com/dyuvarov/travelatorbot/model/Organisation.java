package com.dyuvarov.travelatorbot.model;

public abstract class Organisation {
    String name;
    String uri;
    Integer averageCost;

    public Organisation(String name, String uri, Integer averageCost) {
        this.name = name;
        this.uri = uri;
        this.averageCost = averageCost;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public Integer getAverageCost() {
        return averageCost;
    }
}
