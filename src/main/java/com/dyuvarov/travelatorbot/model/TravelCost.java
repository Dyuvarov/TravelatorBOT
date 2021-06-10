package com.dyuvarov.travelatorbot.model;

import java.util.ArrayList;
import java.util.List;

public class TravelCost {
    Integer         minPrice;
    Integer         maxPrice;
    Float           averagePrice;
    Organisation    economy;
    Organisation    premium;

    public TravelCost() {
        averagePrice = 0.0f;
        maxPrice = 0;
        minPrice = 0;
    }

    public String createMsg() {
        return "Эконом сегмент: \"" + economy.getName() + "\"" + minPrice + "руб. \n"
                + "Премиум сегмент: \"" + premium.getName() + "\"" + maxPrice + "руб. \n"
                + "Средний чек по всем заведениям: " + averagePrice;
    }

    public Float getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Float averagePrice) {
        this.averagePrice = averagePrice;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Organisation getEconomy() {
        return economy;
    }

    public void setEconomy(Organisation economy) {
        this.economy = economy;
    }

    public Organisation getPremium() {
        return premium;
    }

    public void setPremium(Organisation premium) {
        this.premium = premium;
    }
}
