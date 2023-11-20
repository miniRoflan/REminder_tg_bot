package com.botProject.REminder_tg_bot.data_base_mod;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Timestamp;


@Entity(name = "users_remind")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long chatId;

    private String remind;

    private Timestamp time;


    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getRemind() {
        return remind;
    }

    public Long getId() {
        return id;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
