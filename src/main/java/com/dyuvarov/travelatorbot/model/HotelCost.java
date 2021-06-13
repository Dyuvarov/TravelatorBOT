package com.dyuvarov.travelatorbot.model;

public class HotelCost extends TravelCost{
    @Override
    public String createMsg(String city) {
        return "Средняя стоимость номера в отеле в " + city + ": " + this.averagePrice;
    }
}
