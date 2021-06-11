package com.dyuvarov.travelatorbot.bot;

import com.dyuvarov.travelatorbot.dao.BotDAO;
import com.dyuvarov.travelatorbot.dao.API.MapsAPI;
import com.dyuvarov.travelatorbot.model.TravelCost;
import com.dyuvarov.travelatorbot.model.TravelatorUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TravelatorBot extends TelegramLongPollingBot {
    private final String  botUserName;
    private final String  botToken;
    private final BotDAO  botDAO;
    private final MapsAPI mapsAPI;

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
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        }
        else if (update.hasMessage())
        {
            Message message = update.getMessage();
            TravelatorUser travelatorUser = botDAO.getUser(message.getChatId());
            if (travelatorUser == null) {
                travelatorUser = new TravelatorUser(message.getFrom(), message.getChatId());
                botDAO.addUser(travelatorUser);
            }
            String city = message.getText();

            TravelCost cateringCost = mapsAPI.calculateEating(city);
            if (cateringCost == null)
                sendMessageToUser(travelatorUser, "Информация о зведениях общественного питания не найдена, " +
                        "проверьте корректность введенных данных", null);
            InlineKeyboardMarkup cateringMarkp = createInlineMessageButtons(BudgetType.CATERING, message.getMessageId());
            sendMessageToUser(travelatorUser, cateringCost.createMsg(city), cateringMarkp);

            TravelCost hotelCost = mapsAPI.calculateLiving(city);
            if (hotelCost == null)
                sendMessageToUser(travelatorUser, "Информация о зведениях общественного питания не найдена, " +
                        "проверьте корректность введенных данных", null);
            InlineKeyboardMarkup hotelMarkup = createInlineMessageButtons(BudgetType.CATERING, message.getMessageId());
            sendMessageToUser(travelatorUser, hotelCost.createMsg(city), hotelMarkup);

//        if (message. ().equals("/start")){
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
    }

    private void sendMessageToUser(TravelatorUser travelatorUser, String message, InlineKeyboardMarkup markup) {
        SendMessage sendMessage = new SendMessage(travelatorUser.getChatId().toString(), message);
        if (markup != null)
            sendMessage.setReplyMarkup(markup);

        try {
            execute(sendMessage);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup createInlineMessageButtons(BudgetType budgetType, Integer msgId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonEconomy = new InlineKeyboardButton();
        buttonEconomy.setText("см. недорогие");
        InlineKeyboardButton buttonPremium = new InlineKeyboardButton();
        buttonPremium.setText("см. дорогие");

        //add button identifiers to understand which button was used by user
        buttonEconomy.setCallbackData("Cheep_" + budgetType.getTitle() + "_" + msgId.toString()); //TODO: add ID of search
        buttonPremium.setCallbackData("Expensive_" + budgetType.getTitle() + "_" + msgId.toString()); //

        List<InlineKeyboardButton> row = new ArrayList<>(Arrays.asList(buttonEconomy, buttonPremium));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(Arrays.asList(row));

        markup.setKeyboard(rowList);
        return markup;
    }

    private void handleCallbackQuery(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        TravelatorUser travelatorUser = botDAO.getUser(update.getCallbackQuery().getMessage().getChatId());
        if (travelatorUser == null) {
            travelatorUser = new TravelatorUser(update.getCallbackQuery().getFrom(), update.getCallbackQuery().getMessage().getChatId());
            botDAO.addUser(travelatorUser);
        }
        sendMessageToUser(travelatorUser, "Функция в разработке", false);
        return ;
    }
}
