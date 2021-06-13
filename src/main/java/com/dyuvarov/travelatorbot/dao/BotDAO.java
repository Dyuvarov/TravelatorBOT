package com.dyuvarov.travelatorbot.dao;

import com.dyuvarov.travelatorbot.model.TravelatorUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class BotDAO {

    private Connection dbConnection;

    public BotDAO (@Value("${db.url}") String dbUrl, @Value("${db.username}") String dbUserName,
                   @Value("${db.password}") String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            this.dbConnection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public TravelatorUser getUser(Long chatId) {
        TravelatorUser travelatorUser = null;
        try {
            PreparedStatement statement = dbConnection.prepareStatement("SELECT * FROM Users WHERE chatid=?");
            statement.setString(1, chatId.toString());
            ResultSet resultSet = statement.executeQuery();
            boolean hasNext = resultSet.next();
            if (hasNext) {
                travelatorUser = new TravelatorUser(resultSet.getString("username"),
                        resultSet.getString("chatid"),
                        resultSet.getString("state"),
                        resultSet.getString("id"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  travelatorUser;
    }

    public void addUser(TravelatorUser user) {
        try {
            PreparedStatement statement =
                    dbConnection.prepareStatement("INSERT INTO Users VALUES( ?, ?, ?, ?)");
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getChatId().toString());
            statement.setString(3, user.getState().getTitle());
            statement.setString(4, user.getId().toString());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
