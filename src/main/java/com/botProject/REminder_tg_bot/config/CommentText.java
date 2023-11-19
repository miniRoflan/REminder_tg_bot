package com.botProject.REminder_tg_bot.config;

import jakarta.xml.bind.SchemaOutputResolver;

public class CommentText {
    public static final String HELP_COM =
            "Список команд: \n\n" +
            "/start - Перезапустить бота\n" +
            "/list  - Список напоминаний";
    public static final String START_COM =
            "Это бот Reminder, он поможет Вам не забыть о ваших делах \n\n" +
            "Для списка комманд введите: /help";
    public static final String ADD_REMIND_COM =
            "Напоминание успешно добавлено, полный список можете посмотреть по команде: /list";

}
