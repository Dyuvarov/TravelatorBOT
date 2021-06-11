package com.dyuvarov.travelatorbot.dao;

import org.telegram.telegrambots.meta.api.objects.Message;
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

    public User getUser(Message message) {
        String chatId = message.getChat().getId().toString();
        User user = users.stream().filter(x -> x.getChatId().equals(chatId)).findAny().orElse(null);
        if (user == null) {
            user = new User(message.getFrom().getUserName(), chatId);
            users.add(user);
        }
        return user;
        //TODO: users in database
    }
}
