package com.botProject.REminder_tg_bot.data_base_mod;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;



@Entity(name = "users_remind")
public class User {


    private Long chatId;

    private String remind;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }
}
