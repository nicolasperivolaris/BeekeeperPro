package com.beekeeperpro.data.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
        String queryStmt = "Select id, name, photo, count, location, latitude, longitude from dbo.[Apiary] where user_id = " + LoginRepository.getLoggedInUser().getUserId();
        Connection connect = ConnectionHelper.CONN();

        ResultSet resultSet = connect.createStatement().executeQuery(queryStmt);
        List<Apiary> list = new ArrayList<>();
        while (resultSet.next()) {
            Apiary apiary = new Apiary(resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString(5),
                    resultSet.getInt(4));
            double lat = resultSet.getDouble(6);
            double lon = resultSet.getDouble(7);
            apiary.setCoordinate(lat, lon);
            byte[] imageBytes = resultSet.getBytes("photo");
            if (imageBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                apiary.setPicture(bitmap);
            }
            list.add(apiary);
        }
        return list;
    }
}