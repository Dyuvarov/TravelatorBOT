package com.dyuvarov.travelatorbot.model;

/**
 * class with List of organisations and average price where user can eat
 */
public class CateringCost extends TravelCost{
    @Override
    public String createMsg(String city) {
        return city + ": средний чек в заведениях общественного питания - " + this.averagePrice + " руб.";
    }
}
