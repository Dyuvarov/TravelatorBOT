package com.dyuvarov.travelatorbot.entity;

import com.dyuvarov.travelatorbot.bot.BotState;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TravelatorUser {
    @Id
    private Long        chatId;
    private String      userName;
    private BotState    state;
    private Long        id;

    public TravelatorUser() {}

    /**
     * Constructor for creating user from Telegram user
     * @param user  - telegram user
     * @param chatId
     */
    public TravelatorUser(User user, Long chatId) {
        this.userName = user.getUserName();
        this.state = BotState.START;
        this.id = user.getId();
        this.chatId = chatId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setId(Long id) {
        this.id = id;
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
