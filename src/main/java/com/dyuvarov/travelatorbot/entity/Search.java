package com.dyuvarov.travelatorbot.entity;

import com.dyuvarov.travelatorbot.bot.BudgetType;
import com.dyuvarov.travelatorbot.model.Organisation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Search {
    @Id
    @GeneratedValue
    private Long    id;

    private String      organisationName;
    private Integer     cost;
    private Long        chatId;
    private Long        messageId;
    private BudgetType  budgetType;

    public Search() {}

    Search(Organisation organisation, BudgetType budgetType) {
        //TODO: this constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public BudgetType getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(BudgetType budgetType) {
        this.budgetType = budgetType;
    }
}
