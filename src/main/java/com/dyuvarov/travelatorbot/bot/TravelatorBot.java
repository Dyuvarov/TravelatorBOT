package com.dyuvarov.travelatorbot.bot;

import com.dyuvarov.travelatorbot.dao.BotDAO;
import com.dyuvarov.travelatorbot.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class TravelatorBot extends TelegramLongPollingBot {
    private String botUserName;
    private String botToken;
    private BotDAO botDAO;

    public TravelatorBot(@Value("${bot.userName}") String botUserName,
                         @Value("${bot.token}") String botToken,
                         BotDAO botDAO) {
        this.botUserName = botUserName;
        this.botToken = botToken;
        this.botDAO = botDAO;
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

        switch (message.getText()){
            case ("/start"): {
                sendMessageToUser(user, "Привет! Я помогу тебе составить бюджет твоего путешествия!");
                sendMessageToUser(user, "В какой город направимся?");
                user.setState(BotState.WAITING_DESTINATION);
                break;
            }
        }
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
