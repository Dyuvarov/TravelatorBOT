package com.dyuvarov.travelatorbot.dao.API;

import com.dyuvarov.travelatorbot.model.TravelCost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TwoGisAPITest {
    @Autowired
    MapsAPI twoGisAPI;

    @Test
    public void calculateLivingTest() throws Exception{
        TravelCost travelCost =  twoGisAPI.calculateLiving("Казань");
        assertThat(travelCost).isNotNull();
        assertThat(travelCost.getOrganisations().size()).isNotEqualTo(0);
        assertThat(travelCost.getAveragePrice()).isNotEqualTo(0);
    }

    @Test
    public void calculateEatingTest() throws Exception{
        TravelCost travelCost =  twoGisAPI.calculateEating("Казань");
        assertThat(travelCost).isNotNull();
        assertThat(travelCost.getOrganisations().size()).isNotEqualTo(0);
        assertThat(travelCost.getAveragePrice()).isNotEqualTo(0);
    }

    public void calculateLivingTestNull() throws Exception {
        TravelCost travelCost =  twoGisAPI.calculateLiving(null);
        assertThat(travelCost).isNull();
    }

    public void calculateEatingTestNull() throws Exception {
        TravelCost travelCost =  twoGisAPI.calculateEating(null);
        assertThat(travelCost).isNull();
    }
}
