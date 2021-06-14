package com.dyuvarov.travelatorbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.*;

@SpringBootApplication
public class TravelatorBotApplication {
    public static Logger LOGGER = LoggerFactory.getLogger(TravelatorBotApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TravelatorBotApplication.class, args);
    }

}
