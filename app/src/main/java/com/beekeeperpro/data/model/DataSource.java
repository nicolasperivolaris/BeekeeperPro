package com.beekeeperpro.data.model;

import com.beekeeperpro.data.ConnectionHelper;
import com.beekeeperpro.data.Result;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import javax.security.auth.login.LoginException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class DataSource {
    //todo check not to delete data to other user
    //todo limit data transfer when getting list
    //todo open and close conn each time
    public Result login(String username, String password) {
        try {
            Connection connect = ConnectionHelper.CONN();

            String queryStmt = "Select * from dbo.login(?, '');";
            PreparedStatement preparedStatement = connect
                    .prepareStatement(queryStmt);
            preparedStatement.setString(1, username.toLowerCase(Locale.ROOT));

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3));

                preparedStatement.close();
                return new Result.Success<User>(user);
            } else {
                return new Result.Error(new LoginException());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new Result.Error(new SQLException("Error logging in :", e));
        } catch (Exception e) {
            return new Result.Error(new IOException("Unknown error at logging :", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
        ConnectionHelper.Disconnect();
    }
}