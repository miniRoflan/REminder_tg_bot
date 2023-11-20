package com.botProject.REminder_tg_bot.config;

import jakarta.xml.bind.SchemaOutputResolver;

public class CommentText {

    public static final String HELP_COM =
            "Список команд:\n\n" +
            "/start - Перезапустить бота\n" +
            "/add_remind  - добавить напоминание";

    public static final String START_COM =
            "Это бот Reminder, он поможет Вам не забыть о ваших делах <3\n\n" +
            "Для списка комманд введите: /help";

    public static final String ADD_REMIND_COM =
            "Введите напоминание которое хотите добавить.\n\n" +
            "Пример: сделать дз по алгебере!\n\n" +
            "Для отмены нажмите на -> /cansel";

    public static final String ADD_DATE_COM =
            "Введите дату и время напоминания.\n\n" +
            "Формат: гггг-мм-дд чч:мм\n" +
            "Пример: 2077-01-13 04:30\n\n" +
            "Для отмены нажмите -> /cansel";

    public static final String DATE_FORMAT_ERROR =
            "Ошибка в формате даты!!!\n" +
            "Попробуйте еще раз\n\n" +
            "Для отмены нажмите -> /cansel";

    public static final String DATE_LOGIC_ERROR =
            "Ошибка в логике даты!!! -> Дата < Текущего времени\n" +
            "Попробуйте еще раз\n\n" +
            "Для отмены нажмите -> /cansel";
}
