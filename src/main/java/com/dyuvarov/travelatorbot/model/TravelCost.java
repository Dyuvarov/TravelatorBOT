package com.dyuvarov.travelatorbot.model;

import java.util.*;

public abstract class TravelCost {
    Integer               averagePrice;
    Set<Organisation>   organisations;

    public TravelCost() {
        organisations = new HashSet<>();
        averagePrice = 0;
    }

    /**
     * Create anwser message for user
     * @param city - name of city
     * @return String message
     */
    public abstract String createMsg(String city);

    /**
     * Calculate average of all organisations prices
     */
    public void calculateAvgPrice() {
        if (organisations.isEmpty())
            return;
        int sum = 0;
        for (Organisation org : organisations) {
            sum += org.getCost();
        }
        averagePrice = sum / organisations.size();
    }

    public Integer getAveragePrice() {
        return averagePrice;
    }

    public Set<Organisation> getOrganisations() {
        return organisations;
    }

    public void addOrganisation(Organisation organisation) {
        if (organisation != null)
            organisations.add(organisation);
    }

    public void addOrganisation(Set<Organisation> organisations) {
        this.organisations.addAll(organisations);
    }
}
