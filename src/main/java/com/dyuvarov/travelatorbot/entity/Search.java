package com.dyuvarov.travelatorbot.entity;

import com.dyuvarov.travelatorbot.bot.BudgetType;
import com.dyuvarov.travelatorbot.model.Organisation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * class to store search history in database
 */
@Entity
public class Search {
    @Id
    @GeneratedValue
    private Long    id;

    private String      organisationName;
    private Integer     cost;
    private Long        chatId;
    private Integer     messageId;
    private BudgetType  budgetType;
    private String      url;

    public Search() {}

    public Search(Organisation organisation, BudgetType budgetType, Long chatId, Integer messageId) {
        this.organisationName = organisation.getName();
        this.cost = organisation.getCost();
        this.url = organisation.getUrl();
        this.budgetType = budgetType;
        this.chatId = chatId;
        this.messageId = messageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public BudgetType getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(BudgetType budgetType) {
        this.budgetType = budgetType;
    }
}
