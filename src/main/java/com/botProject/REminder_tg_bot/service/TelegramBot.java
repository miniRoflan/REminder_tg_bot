package com.botProject.REminder_tg_bot.service;

import com.botProject.REminder_tg_bot.config.BotConfig;
import com.botProject.REminder_tg_bot.config.CommentText;
import com.botProject.REminder_tg_bot.data_base_mod.User;
import com.botProject.REminder_tg_bot.data_base_mod.UserCheck;
import com.botProject.REminder_tg_bot.data_base_mod.UserCheckRepository;
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

    @Autowired
    private UserCheckRepository userCheckRepository;

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
        long chatId = update.getMessage().getChatId();

        registerUser(chatId);
        UserCheck userCheck = userCheckRepository.findByChatId(chatId);

        boolean remindCheck = userCheck.isRemindCheck();
        boolean dateCheck = userCheck.isDateCheck();

        if (update.hasMessage() && update.getMessage().hasText() && !remindCheck && !dateCheck) {
            String messageText = update.getMessage().getText();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    helpCommandReceived(chatId);
                    break;
                case "/add_remind":
                    sendMessage(chatId, CommentText.ADD_REMIND_COM);
                    userCheck.setRemindCheck(true);
                    break;
                default:
                    sendMessage(chatId, "error command =(\n please try again");
            }

        } else if (update.hasMessage() && update.getMessage().hasText() && remindCheck) {
            String messageText = update.getMessage().getText();

            if (!messageText.equals("/cansel")) {
                userCheck.setDateCheck(true);
                userCheck.setRemind(messageText);

                sendMessage(chatId, CommentText.ADD_DATE_COM);
            }

            userCheck.setRemindCheck(false);

        } else if (update.hasMessage() && update.getMessage().hasText() && dateCheck) {
            String messageText = update.getMessage().getText();

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

                add_remindCommandReceived(chatId, userCheck.getRemind(), time);
            }

            userCheck.setDateCheck(false);
            userCheck.setRemind(null);
        }

        userCheckRepository.save(userCheck);
    }

    private void add_remindCommandReceived(long chatId, String remind, Timestamp time) {

        User user = new User();

        user.setChatId(chatId);
        user.setRemind(remind);
        user.setTime(time);

        userRepository.save(user);
        sendMessage(chatId, "Напоминание успешно добавлено!");
    }

    private void registerUser(Long chatId) {

        if (userCheckRepository.findById(chatId).isEmpty()) {
            UserCheck userCheck = new UserCheck();

            userCheck.setChatId(chatId);
            userCheck.setDateCheck(false);
            userCheck.setRemindCheck(false);
            userCheck.setRemind(null);

            userCheckRepository.save(userCheck);
        }
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
