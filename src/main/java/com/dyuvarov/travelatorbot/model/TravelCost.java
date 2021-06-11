package com.dyuvarov.travelatorbot.model;

import java.util.*;

public abstract class TravelCost {
    Float               averagePrice;
    Set<Organisation>   organisations;

    public TravelCost() {
        organisations = new HashSet<>();
        averagePrice = 0.0f;
    }

    public abstract String createMsg(String city);

    public Float getAveragePrice() {
        return averagePrice;
    }

    public void calculateAvgPrice() {
        if (organisations.isEmpty())
            return;
        int sum = 0;
        for (Organisation org : organisations) {
            sum += org.getCost();
        }
        averagePrice = (float)sum / organisations.size();
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
