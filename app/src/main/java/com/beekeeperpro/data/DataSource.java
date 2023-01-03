package com.beekeeperpro.data;

import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.data.model.Inspection;
import com.beekeeperpro.data.model.User;
import com.beekeeperpro.utils.Location;
import com.google.android.gms.common.api.Api;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            return new Result.Error(new SQLException("Error logging in :", e));
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
            return new Result.Error(new SQLException("Error SQL :", e));
        } catch (Exception e) {
            return new Result.Error(new IOException("Unknown error at getting apiary from db :", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
        ConnectionHelper.Disconnect();
    }

    public Result getHives(Apiary apiary) {
        try {
            String queryStmt = "Select * from dbo.[Hive] where apiary_id = " + apiary.getId();
            Connection connect = ConnectionHelper.CONN();

            ResultSet resultSet = connect.createStatement().executeQuery(queryStmt);
            List<Hive> list = new ArrayList<>();
            while (resultSet.next()) {
                Hive hive = new Hive(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(6),
                        resultSet.getDate(7));
                hive.setApiary(apiary);
                list.add(hive);
            }
            return new Result.Success<>(list);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result.Error(new SQLException("Error SQL :", e));
        } catch (Exception e) {
            return new Result.Error(new IOException("Unknown error at getting hive from db :", e));
        }
    }

    public Result insert(Apiary apiary) {
        try {
            String queryStmt = "INSERT INTO BeekeeperPro.dbo.Apiary (name, coordinate, user_id, location)" +
                    "VALUES ('" + apiary.getName() + "', geography::STGeomFromText('POINT(" + apiary.getCoordinate() +
                    ")', 4326), " + LoginRepository.getLoggedInUser().getUserId() + ", '" + apiary.getLocation() + "');";
            Connection connect = ConnectionHelper.CONN();
//todo change to prepared statements
            /*String sql = "INSERT INTO BeekeeperPro.dbo.Apiary (name, coordinate, user_id, location) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connect.prepareStatement(sql);

            statement.setString(1, apiary.getName());
            statement.setObject(2, "geography::STGeomFromText('POINT('" + apiary.getCoordinate() + "')', 4326)");
            statement.setInt(3, LoginRepository.getLoggedInUser().getUserId());
            statement.setString(4, apiary.getLocation());

            int result = statement.executeUpdate();*/
            Statement statement = connect.createStatement();
            if (statement.executeUpdate(queryStmt) > 0) {
                Apiary result = new Apiary();
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    String selectQuery = "SELECT id, name, coordinate, location FROM BeekeeperPro.dbo.Apiary WHERE id = ?";
                    PreparedStatement selectStatement = connect.prepareStatement(selectQuery);
                    selectStatement.setLong(1, id);
                    ResultSet resultSet = selectStatement.executeQuery();
                    if (resultSet.next()) {
                        result.setId(resultSet.getInt(1));
                        result.setName(String.valueOf(resultSet.getString(2)));
                        result.setCoordinate((Location) resultSet.getObject(3));
                        result.setLocation(resultSet.getString(4));
                    }
                }
                return new Result.Success<>(result);
            } else {
                return new Result.Error(new SQLException());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result.Error(new SQLException("Error SQL :", e));
        } catch (Exception e) {
            return new Result.Error(new IOException("Unknown error when inserting apiary :", e));
        }
    }

    public Result insert(Hive hive) {
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
            preparedStatement.setDate(6, (hive.getHivingDate() != null ? new Date(hive.getHivingDate().getTime()) : null));
            preparedStatement.setDate(7, (hive.getAcquisitionDate() != null ? new Date(hive.getAcquisitionDate().getTime()) : null));

            if (preparedStatement.executeUpdate() > 0) {
                Hive result = new Hive();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    String selectQuery = "SELECT id, name, code, strength, hiving_date, acquisition_date FROM BeekeeperPro.dbo.Hive WHERE id = ?";
                    PreparedStatement selectStatement = connect.prepareStatement(selectQuery);
                    selectStatement.setLong(1, id);
                    ResultSet resultSet = selectStatement.executeQuery();
                    if (resultSet.next()) {
                        result.setId(resultSet.getInt(1));
                        result.setName(String.valueOf(resultSet.getString(2)));
                        result.setCode(resultSet.getString(3));
                        result.setHivingDate(resultSet.getDate(4));
                        result.setAcquisitionDate(resultSet.getDate(5));
                    }
                }
                return new Result.Success<Hive>(result);
            } else {
                return new Result.Error(new SQLException());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result.Error(new SQLException("Error SQL :", e));
        } catch (Exception e) {
            return new Result.Error(new IOException("Unknown error when inserting apiary :", e));
        }
    }

    public Result delete(Apiary apiary) {
        Connection connect = ConnectionHelper.CONN();

        try {
            String sql = "DELETE FROM BeekeeperPro.dbo.Apiary WHERE id = ?";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setInt(1, apiary.getId());

            int rows = statement.executeUpdate();
            if (rows > 0) {
                System.out.println("Apiary deleted successfully.");
                return getApiaries(LoginRepository.getLoggedInUser());
            } else {
                System.out.println("An error occurred while deleting the apiary.");
                return new Result.Error(new SQLException());
            }
        } catch (Exception e) {
            System.out.println("An error occurred while deleting the apiary.");
            e.printStackTrace();
            return new Result.Error(e);
        }
    }

    public Result delete(Hive hive) {
        Apiary apiary = hive.getApiary();
        Connection connect = ConnectionHelper.CONN();

        try {
            String sql = "DELETE FROM BeekeeperPro.dbo.Hive WHERE id = ?";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setInt(1, hive.getId());

            int rows = statement.executeUpdate();
            if (rows > 0) {
                return getHives(apiary);
            } else {
                return new Result.Error(new SQLException());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(e);
        }
    }

    public Result insert(Inspection inspection) throws SQLException {
        Connection connect = ConnectionHelper.CONN();
        try {
            String queryStmt = "INSERT INTO BeekeeperPro.dbo.Inspection (date, temper, hive_condition, queen_condition, phytosanitary_used, hive_condition_remarks, queen_condition_remarks, phytosanitary_used_remarks, hive_id, user_id)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            connect.setAutoCommit(false);
            PreparedStatement preparedStatement = connect.prepareStatement(queryStmt, Statement.RETURN_GENERATED_KEYS);
            if(inspection.getInspectionDate() != null)
                preparedStatement.setDate(1, new Date(inspection.getInspectionDate().getTime()));
            else throw new Exception("No date provided");

            preparedStatement.setString(2, inspection.getTemper());
            preparedStatement.setString(3, inspection.getHiveCondition());
            preparedStatement.setString(4, inspection.getQueenCondition());
            preparedStatement.setString(5, inspection.getPhytosanitaryUsed());
            preparedStatement.setString(6, inspection.getHiveConditionRemarks());
            preparedStatement.setString(7, inspection.getQueenConditionRemarks());
            preparedStatement.setString(8, inspection.getPhytosanitaryRemarks());
            preparedStatement.setInt(9, inspection.getHive().getId());
            preparedStatement.setInt(10, LoginRepository.getLoggedInUser().getUserId());

            if (preparedStatement.executeUpdate() > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    inspection.setId(id);
                }
                for (String point: inspection.getAttentionPoints()) {
                    String pointsInsert = "INSERT INTO BeekeeperPro.dbo.Inspection_AttentionPoints (inspection_id, name)" +
                            "VALUES (?, ?);";
                    PreparedStatement pIPrepStat = connect.prepareStatement(pointsInsert, Statement.RETURN_GENERATED_KEYS);
                    pIPrepStat.setInt(1, inspection.getId());
                    pIPrepStat.setString(2, point);
                    if(pIPrepStat.executeUpdate() == 0) throw new SQLException("Error while inserting AttentionPoint");
                }

                connect.commit();
                return new Result.Success<>(inspection);
            } else {
                throw new SQLException("Error while inserting Inspection");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            connect.rollback();
            return new Result.Error(new SQLException("Error SQL :", e));
        } catch (Exception e) {
            return new Result.Error(new IOException("Unknown error when inserting inspection :", e));
        } finally {
            connect.setAutoCommit(true);
        }
    }

}