package com.dyuvarov.travelatorbot.model;

import java.util.ArrayList;
import java.util.List;

public class TravelCost {
    Integer averagePrice;
    List<Organisation> economyList;
    List<Organisation> averageList;
    List<Organisation> premiumList;

    public TravelCost() {
        averagePrice = 0;
        economyList = new ArrayList<>();
        averageList = new ArrayList<>();
        premiumList = new ArrayList<>();
    }

    public Integer getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Integer averagePrice) {
        this.averagePrice = averagePrice;
    }

    public List<Organisation> getEconomyList() {
        return economyList;
    }

    public void setEconomyList(List<Organisation> economyList) {
        this.economyList = economyList;
    }

    public List<Organisation> getAverageList() {
        return averageList;
    }

    public void setAverageList(List<Organisation> averageList) {
        this.averageList = averageList;
    }

    public List<Organisation> getPremiumList() {
        return premiumList;
    }

    public void setPremiumList(List<Organisation> premiumList) {
        this.premiumList = premiumList;
    }
}
