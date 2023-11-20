package com.botProject.REminder_tg_bot.data_base_mod;

import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByTime(Timestamp time);

}
