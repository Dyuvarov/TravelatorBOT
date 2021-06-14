package com.dyuvarov.travelatorbot.bot;

import com.dyuvarov.travelatorbot.dao.BotDAO;
import com.dyuvarov.travelatorbot.dao.API.MapsAPI;
import com.dyuvarov.travelatorbot.dao.CostType;
import com.dyuvarov.travelatorbot.model.Organisation;
import com.dyuvarov.travelatorbot.model.TravelCost;
import com.dyuvarov.travelatorbot.entity.TravelatorUser;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
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

             String msgText = message.getText();
            if (msgText.equals(BotCommands.CALCULATE.getTitle())) {
                sendMessageToUser(travelatorUser, BotMessages.getCalculateCommandMessage(), null);
                botDAO.updateUsersState(travelatorUser, BotState.WAITING_DESTINATION);
            }
            else if (msgText.equals(BotCommands.START.getTitle())){
                sendMessageToUser(travelatorUser, BotMessages.getWelcomeMessage(), createMainMenu());
                botDAO.updateUsersState(travelatorUser, BotState.NO_ACTION);
            }
            else if (msgText.equals(BotCommands.HELP.getTitle())) {
                sendMessageToUser(travelatorUser, BotMessages.getHelpCommandMessage(), null);
                botDAO.updateUsersState(travelatorUser, BotState.NO_ACTION);
            }
            else if (msgText.equals(BotCommands.INFO.getTitle())) {
                sendMessageToUser(travelatorUser, BotMessages.getInfoCommandMessage(), null);
                botDAO.updateUsersState(travelatorUser, BotState.NO_ACTION);
            }
            else if (travelatorUser.getState() == BotState.WAITING_DESTINATION) {
                String city = msgText;

                TravelCost cateringCost = mapsAPI.calculateEating(city);
                if (cateringCost == null)
                    sendMessageToUser(travelatorUser, BotMessages.getInfoCommandMessage(), null);
                else {
                    InlineKeyboardMarkup cateringMarkup = null;
                    if (cateringCost.getOrganisations().size() < 6)
                        cateringMarkup = createInlineMessageButtons(BudgetType.CATERING, message.getMessageId(), true);
                    else
                        cateringMarkup = createInlineMessageButtons(BudgetType.CATERING, message.getMessageId(), false);
                    sendMessageToUser(travelatorUser, cateringCost.createMsg(city), cateringMarkup);
                    botDAO.saveSearches(message.getMessageId(), travelatorUser.getChatId(), BudgetType.CATERING, cateringCost);
                }

                TravelCost hotelCost = mapsAPI.calculateLiving(city);
                if (hotelCost == null)
                    sendMessageToUser(travelatorUser, BotMessages.getInfoCommandMessage(), null);
                else {
                    InlineKeyboardMarkup hotelMarkup = null;
                    if (hotelCost.getOrganisations().size() < 6)
                        hotelMarkup = createInlineMessageButtons(BudgetType.HOTEL, message.getMessageId(), true);
                    else
                        hotelMarkup = createInlineMessageButtons(BudgetType.HOTEL, message.getMessageId(), false);
                    sendMessageToUser(travelatorUser, hotelCost.createMsg(city), hotelMarkup);
                    botDAO.saveSearches(message.getMessageId(), travelatorUser.getChatId(), BudgetType.HOTEL, hotelCost);
                }
                botDAO.updateUsersState(travelatorUser, BotState.NO_ACTION);
            }
        }
    }

    private void sendMessageToUser(TravelatorUser travelatorUser, String message, ReplyKeyboard markup) {
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

    private void sendMessageToUserMarkdown(TravelatorUser travelatorUser, String message, ReplyKeyboard markup) {
        SendMessage sendMessage = new SendMessage(travelatorUser.getChatId().toString(), message);
        sendMessage.enableMarkdown(true);
        if (markup != null)
            sendMessage.setReplyMarkup(markup);
        try {
            execute(sendMessage);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup createInlineMessageButtons(BudgetType budgetType, Integer msgId, boolean oneButton) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = null;

        if (oneButton) {
            InlineKeyboardButton buttonAll = new InlineKeyboardButton();
            buttonAll.setText("см. примеры");
            buttonAll.setCallbackData("All_" + budgetType.getTitle() + "_" + msgId.toString());
            List<InlineKeyboardButton> row = new ArrayList<>(Arrays.asList(buttonAll));
            rowList = new ArrayList<>(Arrays.asList(row));
        }
        else {
            InlineKeyboardButton buttonEconomy = new InlineKeyboardButton();
            buttonEconomy.setText("см. недорогие");
            InlineKeyboardButton buttonPremium = new InlineKeyboardButton();
            buttonPremium.setText("см. дорогие");

            //add button identifiers to understand which button was used by user
            buttonEconomy.setCallbackData("Cheap_" + budgetType.getTitle() + "_" + msgId.toString());
            buttonPremium.setCallbackData("Expensive_" + budgetType.getTitle() + "_" + msgId.toString());

            List<InlineKeyboardButton> row = new ArrayList<>(Arrays.asList(buttonEconomy, buttonPremium));
            rowList = new ArrayList<>(Arrays.asList(row));
        }

        markup.setKeyboard(rowList);
        return markup;
    }

    private ReplyKeyboardMarkup  createMainMenu() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        //one button per row
        KeyboardRow rowCalculate = new KeyboardRow();
        KeyboardRow rowHelp = new KeyboardRow();
        KeyboardRow rowInfo = new KeyboardRow();
        rowCalculate.add(new KeyboardButton("Узнать цены"));
        rowHelp.add(new KeyboardButton("Помощь"));
        rowInfo.add(new KeyboardButton("Информация"));

        final ArrayList<KeyboardRow> keyboard = new ArrayList<>(Arrays.asList(rowCalculate, rowHelp, rowInfo));
        replyKeyboardMarkup.setKeyboard(keyboard);;
        return replyKeyboardMarkup;
    }

    private void handleCallbackQuery(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        TravelatorUser travelatorUser = botDAO.getUser(update.getCallbackQuery().getMessage().getChatId());
        if (travelatorUser == null) {
            travelatorUser = new TravelatorUser(update.getCallbackQuery().getFrom(), update.getCallbackQuery().getMessage().getChatId());
            botDAO.addUser(travelatorUser);
        }
        String[] data = callbackQuery.getData().split("_");
        CostType costType = null;
        BudgetType budgetType = null;
        Integer msgId = null;
        if (data[0].equals("Cheap"))
            costType = CostType.CHEAP;
        else if (data[0].equals("Expensive"))
            costType = CostType.EXPENSIVE;
        else  if (data[0].equals("All"))
            costType = CostType.ALL;

        if (data[1].equals(BudgetType.CATERING.getTitle()))
            budgetType = BudgetType.CATERING;
        else if (data[1].equals(BudgetType.HOTEL.getTitle()))
            budgetType = BudgetType.HOTEL;

        try {msgId = Integer.parseInt(data[2]);}
        catch (NumberFormatException ex) { msgId = null;}

        if (costType == null || budgetType == null || msgId == null) {
            sendMessageToUser(travelatorUser, BotMessages.getNoDataInDataBaseMessage(), null);
            return;
        }

        List<Organisation> organisations = botDAO.findOrganisationsInSearch(budgetType, travelatorUser.getChatId(),
                msgId, costType);

        if (organisations == null ||  organisations.isEmpty()) {
            sendMessageToUser(travelatorUser, BotMessages.getNoDataInDataBaseMessage(), null);
            return;
        }

        sendMessageToUserMarkdown(travelatorUser, BotMessages.getOrganisationsMessage(organisations), null);
    }

}
