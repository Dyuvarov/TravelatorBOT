package com.dyuvarov.travelatorbot.dao;

import com.dyuvarov.travelatorbot.bot.BotState;
import com.dyuvarov.travelatorbot.bot.BudgetType;
import com.dyuvarov.travelatorbot.entity.Search;
import com.dyuvarov.travelatorbot.entity.TravelatorUser;
import com.dyuvarov.travelatorbot.model.Catering;
import com.dyuvarov.travelatorbot.model.Hotel;
import com.dyuvarov.travelatorbot.model.Organisation;
import com.dyuvarov.travelatorbot.model.TravelCost;
import com.dyuvarov.travelatorbot.repository.SearchesRepository;
import com.dyuvarov.travelatorbot.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.dyuvarov.travelatorbot.TravelatorBotApplication.LOGGER;


/**
 * Data Access Object. Provides information from database, write new information in database
 */
@Component
public class BotDAO {

    private final UsersRepository       usersRepository;
    private final SearchesRepository    searchesRepository;

    @Autowired
    public BotDAO (UsersRepository usersRepository, SearchesRepository searchesRepository) {
       this.usersRepository = usersRepository;
       this.searchesRepository = searchesRepository;
    }

    /**
     * Find user in database by chatId
     * @param chatId
     * @return TravelatorUser object
     */
    public TravelatorUser getUser(Long chatId) {
        List<TravelatorUser> travelatorUserList = usersRepository.findByChatId(chatId);
        if (travelatorUserList.isEmpty())
            return null;
        else
            return travelatorUserList.get(0);
    }

    /**
     * Add new user in database
     * @param user - user to add
     */
    public void addUser(TravelatorUser user) {
        usersRepository.save(user);
        LOGGER.info("New user: " + user.getUserName());
    }

    /**
     * Set new state for user and update it in database
     * @param user - user to update
     * @param newState - state to set
     */
    public void updateUsersState(TravelatorUser user, BotState newState) {
        user.setState(newState);
        usersRepository.save(user);
    }

    /**
     * Save search history in database. Need to reply on callback from inline keyboard
     * @param messageId - Id of message with name of city
     * @param chatId
     * @param budgetType
     * @param travelCost
     */
    public void saveSearches(Integer messageId, Long chatId, BudgetType budgetType, TravelCost travelCost) {
        List<Search> searches = new ArrayList<>();
        for (Organisation organisation : travelCost.getOrganisations()) {
            searches.add(new Search(organisation, budgetType, chatId, messageId));
        }
        LOGGER.info("Save searches for messageId= " + messageId);
        searchesRepository.saveAll(searches);
    }

    /**
     * Find organisations in database and return list of 3 organisations: cheap/expensive/all
     * @param budgetType
     * @param chatId
     * @param messageId
     * @param costType
     * @return - if costType==CHEAP - 3 cheapest organisations, EXPENSIVE - 3 most expensive organisations,
     * ALL - all organisations (max 3)
     */
    public List<Organisation> findOrganisationsInSearch(BudgetType budgetType, Long chatId,
                                                        Integer messageId, CostType costType) {
        List<Search> searchesList = searchesRepository.findByBudgetTypeAndChatIdAndMessageIdOrderByCost(budgetType,
                                                            chatId, messageId);

        if (searchesList == null)
            return null;

        List<Organisation> organisations = new ArrayList<>();

        if (costType == CostType.CHEAP) {
            for (int i = 0; i < 3; i++) {
                Search search = searchesList.get(i);
                if (budgetType == BudgetType.CATERING)
                    organisations.add(new Catering(search.getOrganisationName(),
                            search.getCost(), search.getUrl()));
                else if (budgetType == BudgetType.HOTEL)
                    organisations.add(new Hotel(search.getOrganisationName(),
                            search.getCost(), search.getUrl()));
            }
        }
        else if (costType == CostType.EXPENSIVE) {
            for (int i = searchesList.size() - 1; i > searchesList.size() - 4; i--) {
                Search search = searchesList.get(i);
                if (budgetType == BudgetType.CATERING)
                    organisations.add(new Catering(search.getOrganisationName(),
                            search.getCost(), search.getUrl()));
                else if (budgetType == BudgetType.HOTEL)
                    organisations.add(new Hotel(search.getOrganisationName(),
                            search.getCost(), search.getUrl()));
            }
        }
        else if (costType == CostType.ALL) {
            int cout = Math.min(searchesList.size(), 3);
            for (int i = 0; i < cout; i++) {
                Search search = searchesList.get(i);
                if (budgetType == BudgetType.CATERING)
                    organisations.add(new Catering(search.getOrganisationName(),
                            search.getCost(), search.getUrl()));
                else if (budgetType == BudgetType.HOTEL)
                    organisations.add(new Hotel(search.getOrganisationName(),
                            search.getCost(), search.getUrl()));
            }
        }
        return organisations;
    }
}
