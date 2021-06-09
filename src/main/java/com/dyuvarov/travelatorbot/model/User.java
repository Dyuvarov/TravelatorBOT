package com.dyuvarov.travelatorbot.model;

import com.dyuvarov.travelatorbot.bot.BotState;

public class User {
    private String userName;
    private String chatId;
    private BotState state;

    public User(String userName, String chatId) {
        this.userName = userName;
        this.chatId = chatId;
        this.state = BotState.START;
    }

    public String getUserName() {
        return userName;
    }

    public String getChatId() {
        return chatId;
    }

    public BotState getState() {
        return state;
    }

    public void setState(BotState state) {
        this.state = state;
    }
}
