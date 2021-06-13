package com.dyuvarov.travelatorbot.model;

import com.dyuvarov.travelatorbot.bot.BotState;
import org.telegram.telegrambots.meta.api.objects.User;

public class TravelatorUser {
    private String      userName;
    private Long        chatId;
    private BotState    state;
    private Long        id;

    public TravelatorUser(User user, Long chatId) {
        this.userName = user.getUserName();
        this.state = BotState.START;
        this.id = user.getId();
        this.chatId = chatId;
    }

    /**
     * Constructor for creating user from data base
     */
    public TravelatorUser(String userName, String chatId, String state, String id) {
        this.userName = userName;
        this.chatId = Long.parseLong(chatId);
        switch(state) {
            case("START"):
                this.state = BotState.START;
            case("WAITING_DESTINATION"):
                this.state = BotState.WAITING_DESTINATION;
            case("NO_ACTION"):
                this.state = BotState.NO_ACTION;
        }
        this.id = Long.parseLong(id);
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
