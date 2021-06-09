package com.dyuvarov.travelatorbot.dao;

import org.springframework.stereotype.Component;
import com.dyuvarov.travelatorbot.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotDAO {
    private List<User> users;

    public BotDAO () {
        users = new ArrayList<>();
    }

    public User getUser(String chatId, String userName) {
        User user = users.stream().filter(x -> x.getChatId().equals(chatId)).findAny().orElse(null);
        if (user == null) {
            user = new User(userName, chatId);
        }
        return user;
    }
}
