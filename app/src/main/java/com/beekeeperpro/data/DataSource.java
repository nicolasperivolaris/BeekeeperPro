package com.beekeeperpro.data;

import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.data.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.security.auth.login.LoginException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class DataSource {

    public Result<User> login(String username, String password) {
        try {
            Connection connect = ConnectionHelper.CONN();

            String queryStmt = "Select * from dbo.login(?, '');";
            PreparedStatement preparedStatement = connect
                    .prepareStatement(queryStmt);
            preparedStatement.setString(1, username.toLowerCase(Locale.ROOT));

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                User user = new User(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3));

                preparedStatement.close();
                return new Result.Success<User>(user);
            }else {
                return new Result.Error(new LoginException());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        } catch (Exception e) {
            return new Result.Error(new IOException("Unknown error at logging", e));
        }
    }

    public Result getApiaries(User user){
        try{
            String queryStmt = "Select * from dbo.[Apiary] where user_id = " + user.getUserId();
            Connection connect = ConnectionHelper.CONN();

            ResultSet resultSet = connect.createStatement().executeQuery(queryStmt);
            List<Apiary> list = new ArrayList<>();
            while(resultSet.next()) {
                Apiary apiary = new Apiary(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(7), resultSet.getInt(6));
                list.add(apiary);
            }
                return new Result.Success<>(list);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error SQL", e));
        } catch (Exception e) {
            return new Result.Error(new IOException("Unknown error at getting apiary from db", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
        throw new RuntimeException("Not implemented");
    }

    public Result getHives(int apiaryId) {
        try{
            String queryStmt = "Select * from dbo.[Hive] where apiary_id = " + apiaryId;
            Connection connect = ConnectionHelper.CONN();

            ResultSet resultSet = connect.createStatement().executeQuery(queryStmt);
            List<Hive> list = new ArrayList<>();
            while(resultSet.next()) {
                Hive hive = new Hive(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
                list.add(hive);
            }
            return new Result.Success<>(list);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error SQL", e));
        } catch (Exception e) {
            return new Result.Error(new IOException("Unknown error at getting hive from db", e));
        }
    }
}