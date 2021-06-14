package com.dyuvarov.travelatorbot.bot;

/**
 * Types of organisations to calculate average prices
 */
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
