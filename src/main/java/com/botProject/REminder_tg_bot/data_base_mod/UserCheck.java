package com.botProject.REminder_tg_bot.data_base_mod;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity(name = "users_check")
public class UserCheck {

    @Id
    private Long chatId;

    private boolean remindCheck;

    private boolean dateCheck;

    private String remind;

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public boolean isRemindCheck() {
        return remindCheck;
    }

    public void setRemindCheck(boolean remindCheck) {
        this.remindCheck = remindCheck;
    }

    public boolean isDateCheck() {
        return dateCheck;
    }

    public void setDateCheck(boolean dateCheck) {
        this.dateCheck = dateCheck;
    }
}
