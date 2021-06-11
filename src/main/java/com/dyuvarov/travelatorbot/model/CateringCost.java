package com.dyuvarov.travelatorbot.model;

public class CateringCost extends TravelCost{
    @Override
    public String createMsg(String city) {
        return "Средний чек в заведениях общественного питания в г. " + city + ": " + this.averagePrice + " руб.";
    }
}
