package com.dyuvarov.travelatorbot.model;

import com.dyuvarov.travelatorbot.bot.BotState;
import org.telegram.telegrambots.meta.api.objects.User;

public class TravelatorUser {
    private String      userName;
    private Long      chatId;
    private BotState    state;
    private Long        id;

    public TravelatorUser(User user, Long chatId) {
        this.userName = user.getUserName();
        this.state = BotState.START;
        this.id = user.getId();
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public BotState getState() {
        return state;
    }

    public void setState(BotState state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }
}
