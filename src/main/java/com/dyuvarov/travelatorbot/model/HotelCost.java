package com.dyuvarov.travelatorbot.model;

public class HotelCost extends TravelCost{
    @Override
    public String createMsg(String city) {
        return city + ": средняя стоимость номера в отеле - " + this.averagePrice;
    }
}
