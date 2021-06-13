package com.dyuvarov.travelatorbot.bot;

/**
 * Commands that bot will handle
 */
public enum BotCommands {
    START("/start"),
    CALCULATE("Узнать цены"),
    INFO("Информация"),
    HELP("Помощь");

    private String title;

    BotCommands(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
