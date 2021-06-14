package com.dyuvarov.travelatorbot.repository;

import com.dyuvarov.travelatorbot.bot.BudgetType;
import com.dyuvarov.travelatorbot.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchesRepository extends JpaRepository<Search, Long> {

    List<Search> findByBudgetTypeAndChatIdAndMessageIdOrderByCost(BudgetType budgetType, Long chatId, Integer messageId);
}
