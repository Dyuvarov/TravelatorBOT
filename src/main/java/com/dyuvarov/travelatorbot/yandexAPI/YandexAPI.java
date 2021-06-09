package com.dyuvarov.travelatorbot.yandexAPI;

import com.dyuvarov.travelatorbot.model.TravelCost;
import org.springframework.web.client.RestTemplate;

public class YandexAPI {
//    static final String URL = "https://search-maps.yandex.ru/v1/";
    static final String URL = "https://search-maps.yandex.ru/v1/";

    static TravelCost calculateTravel(String city) {
        TravelCost travelCost= new TravelCost();
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject()
        return travelCost;
    }
}
