package com.dyuvarov.travelatorbot.bot;

import com.dyuvarov.travelatorbot.dao.BotDAO;
import com.dyuvarov.travelatorbot.dao.API.MapsAPI;
import com.dyuvarov.travelatorbot.model.TravelCost;
import com.dyuvarov.travelatorbot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TravelatorBot extends TelegramLongPollingBot {
    private String  botUserName;
    private String  botToken;
    private BotDAO  botDAO;
    private MapsAPI mapsAPI;

    @Autowired
    public TravelatorBot(@Value("${bot.userName}") String botUserName,
                         @Value("${bot.token}") String botToken,
                         BotDAO botDAO, @Qualifier("twoGisAPI")MapsAPI mapsAPI) {
        this.botUserName = botUserName;
        this.botToken = botToken;
        this.botDAO = botDAO;
        this.mapsAPI = mapsAPI;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        User user = botDAO.getUser(message);
        TravelCost catering = mapsAPI.calculateEating(message.getText());
//        if (message.getText().equals("/start")){
//            sendMessageToUser(user, "Привет! Я помогу тебе составить бюджет твоего путешествия!");
//            sendMessageToUser(user, "В какой город направимся?");
//            user.setState(BotState.WAITING_DESTINATION);
//        }
//        else {
//            if (user.getState() == BotState.WAITING_DESTINATION){
//                TravelCost catering = mapsAPI.calculateEating(message.getText());
//                sendMessageToUser(user, catering.createMsg());
//            }
//
//        }
    }

    public void sendMessageToUser(User user, String message) {
        SendMessage sendMessage = new SendMessage(user.getChatId(), message);
        try {
            execute(sendMessage);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
