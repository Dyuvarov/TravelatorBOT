package com.dyuvarov.travelatorbot.dao.API;

import com.dyuvarov.travelatorbot.model.TravelCost;

public interface MapsAPI {

    /**
     * Find hotels in city
     * @param city - name of city to search
     * @return TravelCost object
     */
    public TravelCost calculateLiving(String city);

    /**
     * Find catering organisations in city
     * @param city - name of city to search
     * @return TravelCost object
     */
    public TravelCost calculateEating(String city);
}
