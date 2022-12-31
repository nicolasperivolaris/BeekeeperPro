package com.beekeeperpro.data;

import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.data.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
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
            return new Result.Error(new IOException("Error logging in :", e));
        } catch (Exception e) {
            return new Result.Error(new IOException("Unknown error at logging :", e));
        }
    }

    public Result getApiaries(User user) {
        try {
            String queryStmt = "Select * from dbo.[Apiary] where user_id = " + user.getUserId();
            Connection connect = ConnectionHelper.CONN();

            ResultSet resultSet = connect.createStatement().executeQuery(queryStmt);
            List<Apiary> list = new ArrayList<>();
            while (resultSet.next()) {
                Apiary apiary = new Apiary(resultSet.getInt(1),
                                            resultSet.getString(2),
                                            resultSet.getString(7),
                                            resultSet.getInt(6));
                list.add(apiary);
            }
            return new Result.Success<>(list);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error SQL :", e));
        } catch (Exception e) {
            return new Result.Error(new IOException("Unknown error at getting apiary from db :", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
        ConnectionHelper.Disconnect();
    }

    public Result getHives(int apiaryId) {
        try {
            String queryStmt = "Select * from dbo.[Hive] where apiary_id = " + apiaryId;
            Connection connect = ConnectionHelper.CONN();

            ResultSet resultSet = connect.createStatement().executeQuery(queryStmt);
            List<Hive> list = new ArrayList<>();
            while (resultSet.next()) {
                Hive hive = new Hive(resultSet.getInt(1),
                                        resultSet.getString(2),
                                        resultSet.getString(3),
                                        resultSet.getInt(6),
                                        resultSet.getDate(7));
                list.add(hive);
            }
            return new Result.Success<>(list);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error SQL :", e));
        } catch (Exception e) {
            return new Result.Error(new IOException("Unknown error at getting hive from db :", e));
        }
    }

    public Result<Boolean> insert(Apiary apiary){
        try {
            Connection connect = ConnectionHelper.CONN();
            String sql = "INSERT INTO BeekeeperPro.dbo.Apiary (name, coordinate, user_id, location) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connect.prepareStatement(sql);

            statement.setString(1, apiary.getName());
            statement.setObject(2, "geography::STGeomFromText('POINT(" + apiary.getCoordinate() + ")', 4326)");
            statement.setInt(3, LoginRepository.getLoggedInUser().getUserId());
            statement.setString(4, apiary.getLocation());

            int result = statement.executeUpdate();
            return new Result.Success<Boolean>(result > 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error SQL :", e));
        } catch (Exception e) {
            return new Result.Error(new IOException("Unknown error when inserting apiary :", e));
        }
    }

    public Result<Boolean> insert(Hive hive){
        try {
            String queryStmt = "INSERT INTO BeekeeperPro.dbo.Hive (name, code, apiary_id, user_id, strength, hiving_date, acquisition_date)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";
            Connection connect = ConnectionHelper.CONN();
            PreparedStatement preparedStatement = connect.prepareStatement(queryStmt);
            preparedStatement.setString(1, hive.getName());
            preparedStatement.setString(2, hive.getCode());
            preparedStatement.setInt(3, (hive.getApiary() == null ? 0 : hive.getApiary().getId()));
            preparedStatement.setInt(4, LoginRepository.getLoggedInUser().getUserId());
            preparedStatement.setInt(5, hive.getStrength());
            preparedStatement.setDate(6, (hive.getHivingDate() != null ? new Date(hive.getHivingDate().getTime()): null));
            preparedStatement.setDate(7, (hive.getAcquisitionDate() != null ? new Date(hive.getAcquisitionDate().getTime()): null));
            int result = preparedStatement.executeUpdate();
            return new Result.Success<Boolean>(result > 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error SQL :", e));
        } catch (Exception e) {
            return new Result.Error(new IOException("Unknown error when inserting apiary :", e));
        }
    }
}