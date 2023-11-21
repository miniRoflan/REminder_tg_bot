package com.botProject.REminder_tg_bot.data_base_mod;

import org.springframework.data.repository.CrudRepository;

public interface UserCheckRepository extends CrudRepository<UserCheck, Long> {

    UserCheck findByChatId(Long chatId);
}
