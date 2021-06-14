package com.dyuvarov.travelatorbot.bot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TravelatorBotTest {

    @Autowired
    TravelatorBot travelatorBot;
    @Test
    public void createInlineMessageButtonsTestNullTrue() throws Exception{
        InlineKeyboardMarkup inlineKeyboardMarkup =  travelatorBot.createInlineMessageButtons(null,
                null, true);
        assertThat(inlineKeyboardMarkup).isNull();

        inlineKeyboardMarkup =  travelatorBot.createInlineMessageButtons(null,
                null, false);
        assertThat(inlineKeyboardMarkup).isNull();
    }

    @Test
    public void createInlineMessageButtonsTestOneButton() throws Exception{
        InlineKeyboardMarkup inlineKeyboardMarkup =  travelatorBot.createInlineMessageButtons(BudgetType.CATERING,
                11111, true);
        assertThat(inlineKeyboardMarkup).isNotNull();
        assertThat(inlineKeyboardMarkup.getKeyboard()).isNotNull();
        assertThat(inlineKeyboardMarkup.getKeyboard().get(0).size()).isEqualTo(1);
        assertThat(inlineKeyboardMarkup.getKeyboard().get(0).get(0).getText()).isEqualTo("см. примеры");
        assertThat(inlineKeyboardMarkup.getKeyboard().get(0).get(0).getCallbackData()).isEqualTo("All_Catering_11111");

    }

    @Test
    public void createInlineMessageButtonsTestTwoButtons() throws Exception {
        InlineKeyboardMarkup inlineKeyboardMarkup =  travelatorBot.createInlineMessageButtons(BudgetType.HOTEL,
                11111, false);

        assertThat(inlineKeyboardMarkup).isNotNull();
        assertThat(inlineKeyboardMarkup.getKeyboard()).isNotNull();
        assertThat(inlineKeyboardMarkup.getKeyboard().get(0).size()).isEqualTo(2);
        assertThat(inlineKeyboardMarkup.getKeyboard().get(0).get(0).getText()).isEqualTo("см. недорогие");
        assertThat(inlineKeyboardMarkup.getKeyboard().get(0).get(1).getText()).isEqualTo("см. дорогие");
        assertThat(inlineKeyboardMarkup.getKeyboard().get(0).get(0).getCallbackData()).isEqualTo("Cheap_Hotel_11111");
        assertThat(inlineKeyboardMarkup.getKeyboard().get(0).get(1).getCallbackData()).isEqualTo("Expensive_Hotel_11111");
    }

    @Test
    public void createMainMenuTest() throws Exception {
        ReplyKeyboardMarkup replyKeyboardMarkup = travelatorBot.createMainMenu();

        assertThat(replyKeyboardMarkup.getKeyboard().size()).isEqualTo(3);
        assertThat(replyKeyboardMarkup.getKeyboard().get(0).size()).isEqualTo(1);
        assertThat(replyKeyboardMarkup.getKeyboard().get(0).get(0).getText()).isEqualTo("Узнать цены");
        assertThat(replyKeyboardMarkup.getKeyboard().get(1).size()).isEqualTo(1);
        assertThat(replyKeyboardMarkup.getKeyboard().get(1).get(0).getText()).isEqualTo("Помощь");
        assertThat(replyKeyboardMarkup.getKeyboard().get(2).size()).isEqualTo(1);
        assertThat(replyKeyboardMarkup.getKeyboard().get(2).get(0).getText()).isEqualTo("Информация");
    }
}
