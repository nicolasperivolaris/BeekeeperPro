package com.beekeeperpro.data;

import com.beekeeperpro.data.model.LoggedInUser;
import com.beekeeperpro.utils.ConnectionHelper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import javax.security.auth.login.LoginException;

import kotlin.NotImplementedError;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        try {
            Connection connect = ConnectionHelper.CONN();

            String queryStmt = "Select * from dbo.login(?, '');";
            PreparedStatement preparedStatement = connect
                    .prepareStatement(queryStmt);
            String s = username.toLowerCase(Locale.ROOT);
            preparedStatement.setString(1, s);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                LoggedInUser user = new LoggedInUser(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
                preparedStatement.close();
                return new Result.Success<>(user);
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

    public void logout() {
        // TODO: revoke authentication
        throw new RuntimeException("Not implemented");
    }
}