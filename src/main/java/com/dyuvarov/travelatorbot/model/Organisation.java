package com.dyuvarov.travelatorbot.model;

import java.lang.annotation.Inherited;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if ((obj == null) || (obj.getClass() != this.getClass()))
            return false;
        Organisation comp = (Organisation)obj;
        return comp.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return (name.hashCode() * (cost + 19));
    }
}
