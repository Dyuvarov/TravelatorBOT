package com.dyuvarov.travelatorbot.model;

public class CateringCost extends TravelCost{
    @Override
    public String createMsg(String city) {
        return city + ": средний чек в заведениях общественного питания - " + this.averagePrice + " руб.";
    }
}
