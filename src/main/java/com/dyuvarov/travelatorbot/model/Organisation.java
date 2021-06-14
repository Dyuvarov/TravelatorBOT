package com.dyuvarov.travelatorbot.model;


/**
 * Class for organisations which bot find and calculate
 */
public abstract class Organisation {
    private String name;
    private Integer cost;
    private String url;

    public Organisation() {}

    public Organisation(String name, Integer cost, String url) {
        this.name = name;
        this.cost = cost;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
