package com.dyuvarov.travelatorbot.dao.API;

import com.dyuvarov.travelatorbot.model.TravelCost;

public interface MapsAPI {

    public TravelCost calculateLiving(String city);

    public TravelCost calculateEating(String city);
}
