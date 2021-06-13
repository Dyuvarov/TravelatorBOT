package com.dyuvarov.travelatorbot.bot;

public class BotMessages {
    public static String getWelcomeMessage() {
        return "Привет! Я помогу тебе составить бюджет путешествия! Я знаю о заведениях общественного питания и " +
                "отелях по всей России и рассчитаю для тебя среднюю стоимость на питание и проживание в выбранном тобой городе.\n\n" +
                getHelpCommandMessage() + "\n\n" +
                "Мой создатель - @dyuvarov";
    }

    public static String getCalculateCommandMessage() {
        return "В какой город направимся?";
    }

    public static String getHelpCommandMessage() {
        return "Воспользуйся кнопкой \"Узнать цены\" в нижнем меню и отправь мне город. Дальше я все сделаю сам =)";
    }

    public static  String getInfoCommandMessage() {
        return "Travelator - учебный проект от @dyvuarov.\n Данные о ценах и заведениях предоставлены компанией 2GIS";
    }

    public static String getInformationNotFoundMessage() {
        return "Информация о зведениях общественного питания не найдена, " +
                "проверьте корректность введенных данных";
    }
}
