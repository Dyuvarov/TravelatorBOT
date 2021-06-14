package com.dyuvarov.travelatorbot.model;

/**
 * class with List of organisations and average price where user can live
 */
public class HotelCost extends TravelCost{
    @Override
    public String createMsg(String city) {
        return city + ": средняя стоимость номера в отеле - " + this.averagePrice + " руб.";
    }
}
