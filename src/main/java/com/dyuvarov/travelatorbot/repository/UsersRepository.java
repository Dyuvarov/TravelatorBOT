package com.dyuvarov.travelatorbot.repository;

import com.dyuvarov.travelatorbot.entity.TravelatorUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository extends JpaRepository<TravelatorUser, String> {

    List<TravelatorUser>    findByChatId(Long chatId);
}
