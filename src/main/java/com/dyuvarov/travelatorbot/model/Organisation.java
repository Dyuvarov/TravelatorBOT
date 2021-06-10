package com.dyuvarov.travelatorbot.model;

import java.util.List;

public abstract class Organisation {
    private String name;
    private Integer cost;

    public Organisation() {}

    public Organisation(String name, Integer cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
