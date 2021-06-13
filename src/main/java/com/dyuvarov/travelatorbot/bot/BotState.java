package com.dyuvarov.travelatorbot.bot;

/**
 * Bot state for specific user
 */
public enum BotState {
    START("START"),
    WAITING_DESTINATION("WAITING_DESTINATION"),
    NO_ACTION("NO_ACTION");

    private String title;

    BotState(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
