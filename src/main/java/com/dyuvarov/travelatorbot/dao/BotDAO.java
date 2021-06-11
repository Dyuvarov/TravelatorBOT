package com.dyuvarov.travelatorbot.dao;

import com.dyuvarov.travelatorbot.model.TravelatorUser;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotDAO {
    private List<TravelatorUser> travelatorUsers;

    public BotDAO () {
        travelatorUsers = new ArrayList<>();
    }

    public TravelatorUser getUser(Long chatId) {
        TravelatorUser travelatorUser = travelatorUsers.stream().filter(x -> (x.getId().equals(chatId))).findAny().orElse(null);
        return travelatorUser;
        //TODO: users in database
    }

    public void addUser(TravelatorUser user) {
        travelatorUsers.add(user);
    }
}
