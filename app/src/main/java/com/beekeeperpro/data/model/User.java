package com.beekeeperpro.data.model;

import com.beekeeperpro.data.ConnectionHelper;
import com.beekeeperpro.data.LoginRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User {

    private final int id;
    private final String name;
    private final String email;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getUserId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDisplayEmail() {
        return email;
    }

    public List<Apiary> getApiaries() throws SQLException {
        String queryStmt = "Select * from dbo.[Apiary] where user_id = " + LoginRepository.getLoggedInUser().getUserId();
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
        return list;
    }
}