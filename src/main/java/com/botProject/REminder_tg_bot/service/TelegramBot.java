package com.botProject.REminder_tg_bot.service;

import com.botProject.REminder_tg_bot.config.BotConfig;
import com.botProject.REminder_tg_bot.config.CommentText;
import com.botProject.REminder_tg_bot.data_base_mod.User;
import com.botProject.REminder_tg_bot.data_base_mod.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;

    final BotConfig config;

    private boolean remindCheck;

    private  boolean dateCheck;

    private String remind;

    public TelegramBot(BotConfig config) {
        this.config = config;
        this.remindCheck = false;
        this.dateCheck = false;
        this.remind = null;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText() && !remindCheck && !dateCheck) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    helpCommandReceived(chatId);
                    break;
                case "/add_remind":
                    sendMessage(chatId, CommentText.ADD_REMIND_COM);
                    remindCheck = true;
                    break;
                default:
                    sendMessage(chatId, "error command =(\n please try again");
            }

        } else if (update.hasMessage() && update.getMessage().hasText() && remindCheck) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (!messageText.equals("/cansel")) {
                dateCheck = true;
                remind = messageText;
                sendMessage(chatId, CommentText.ADD_DATE_COM);
            }

            remindCheck = false;

        } else if (update.hasMessage() && update.getMessage().hasText() && dateCheck) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (!messageText.equals("/cansel")) {
                String timeMessage = messageText + ":00";

                if (!timeMessage.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                    sendMessage(chatId, CommentText.DATE_FORMAT_ERROR);
                    return;
                }

                Timestamp time = Timestamp.valueOf(timeMessage);

                if (time.before(new Timestamp(System.currentTimeMillis()))) {
                    sendMessage(chatId, CommentText.DATE_LOGIC_ERROR);
                    return;
                }

                add_remindCommandReceived(chatId, remind, time);
            }

            dateCheck = false;
            remind = null;
        }
    }

    private void add_remindCommandReceived(long chatId, String remind, Timestamp time) {

        User user = new User();

        user.setChatId(chatId);
        user.setRemind(remind);
        user.setTime(time);

        userRepository.save(user);
        sendMessage(chatId, "Напоминание успешно добавлено!");
    }

    private void helpCommandReceived(long chatId) {


        String answer = CommentText.HELP_COM;


        sendMessage(chatId, answer);
    }
    private void startCommandReceived(long chatId, String name) {


        String answer = "Привет , " + name + "!\n" + CommentText.START_COM;


        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try{
            execute(message);
        }
        catch (TelegramApiException e ) {
            System.out.println("Error send Message");
        }
    }


    @Scheduled(cron = "0 * * * * *")
    private void checkTime() {
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        nowTime.setNanos(0);

        for (User user : userRepository.findByTime(nowTime)) {
            sendMessage(user.getChatId(), "!НАПОМИНАНИЕ!\n\n" + user.getRemind());
            userRepository.delete(user);
        }
    }

}
