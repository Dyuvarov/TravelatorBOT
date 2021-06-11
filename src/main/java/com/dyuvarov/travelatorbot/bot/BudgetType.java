package com.dyuvarov.travelatorbot.bot;

public enum BudgetType {
    HOTEL("Hotel"),
    CATERING("Catering");

    private String title;

    BudgetType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
