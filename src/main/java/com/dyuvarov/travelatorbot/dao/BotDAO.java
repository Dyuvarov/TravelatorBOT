package com.dyuvarov.travelatorbot.dao;

import com.dyuvarov.travelatorbot.bot.BotState;
import com.dyuvarov.travelatorbot.entity.TravelatorUser;
import com.dyuvarov.travelatorbot.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BotDAO {

    private final UsersRepository usersRepository;

    @Autowired
    public BotDAO (UsersRepository usersRepository) {
       this.usersRepository = usersRepository;
    }

    public TravelatorUser getUser(Long chatId) {
        List<TravelatorUser> travelatorUserList = usersRepository.findByChatId(chatId);
        if (travelatorUserList.isEmpty())
            return null;
        else
            return travelatorUserList.get(0);
    }

    public void addUser(TravelatorUser user) {
        usersRepository.save(user);
    }

    public void updateUsersState(TravelatorUser user, BotState newState) {
        user.setState(newState);
        usersRepository.save(user);
    }
}
