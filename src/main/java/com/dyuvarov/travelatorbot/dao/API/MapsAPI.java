package com.dyuvarov.travelatorbot.dao.API;

import com.dyuvarov.travelatorbot.model.TravelCost;

public interface MapsAPI {

    /**
     *
     * @param city - name of city to search
     * @return TravelCost object
     */
    public TravelCost calculateLiving(String city);

    public TravelCost calculateEating(String city);
}
