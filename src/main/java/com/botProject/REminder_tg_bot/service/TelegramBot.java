package com.botProject.REminder_tg_bot.service;

import com.botProject.REminder_tg_bot.config.BotConfig;
import com.botProject.REminder_tg_bot.config.CommentText;
import com.botProject.REminder_tg_bot.data_base_mod.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;

    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
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

        if (update.hasMessage() && update.getMessage().hasText()) {
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
                    add_remindCommandReceived(chatId, update.getMessage().getText());
                    break;
                default:
                    sendMessage(chatId, "sha budet");
            }
        }

    }

    private void add_remindCommandReceived(long chatId, String text) {



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

}
