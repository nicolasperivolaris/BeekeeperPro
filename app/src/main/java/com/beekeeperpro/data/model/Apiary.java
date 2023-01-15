package com.beekeeperpro.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.beekeeperpro.data.ConnectionHelper;
import com.beekeeperpro.data.LoginRepository;
import com.beekeeperpro.utils.Location;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Apiary extends ApiaryEntity implements Parcelable {
    public final static Apiary Stock = new Apiary(0, "Stock", "Stock", 0);

    private int id;
    private String name;
    private String location;
    private Location coordinate;
    private int hivesCount;
    private Bitmap picture;

    public Apiary() {
        name = "";
        location = "";
        coordinate = new Location();
    }

    public Apiary(int id, String name, String location, int hivesCount) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.coordinate = new Location();
        this.hivesCount = hivesCount;
    }

    protected Apiary(Parcel in) {
        id = in.readInt();
        name = in.readString();
        location = in.readString();
        coordinate = in.readParcelable(Location.class.getClassLoader());
        hivesCount = in.readInt();
        picture = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Apiary> CREATOR = new Creator<Apiary>() {
        @Override
        public Apiary createFromParcel(Parcel in) {
            return new Apiary(in);
        }

        @Override
        public Apiary[] newArray(int size) {
            return new Apiary[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Location getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Location coordinate) {
        this.coordinate = coordinate;
    }

    public void setCoordinate(double lat, double lon) {
        this.coordinate = new Location();
        this.coordinate.setLatitude(lat);
        this.coordinate.setLongitude(lon);
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public int getHivesCount() {
        return hivesCount;
    }

    public void setHivesCount(int hivesCount) {
        this.hivesCount = hivesCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeParcelable(coordinate, flags);
        dest.writeInt(hivesCount);
        dest.writeParcelable(picture, flags);
    }

    public boolean delete() throws SQLException {
        Connection connect = ConnectionHelper.CONN();
        String sql = "DELETE FROM BeekeeperPro.dbo.Apiary WHERE id = ?";
        PreparedStatement statement = connect.prepareStatement(sql);
        statement.setInt(1, getId());

        int rows = statement.executeUpdate();
        if (rows > 0) {
            System.out.println("Apiary deleted successfully.");
            return true;
        } else {
            throw new SQLException();
        }
    }

    public boolean insert() throws SQLException {
        Connection connect = ConnectionHelper.CONN();
        String sql;
        if (picture != null)
            sql = "INSERT INTO BeekeeperPro.dbo.Apiary (name, user_id, location, latitude, longitude, photo) VALUES (?, ?, ?, ?, ?, ?)";
        else
            sql = "INSERT INTO BeekeeperPro.dbo.Apiary (name, user_id, location, latitude, longitude) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connect.prepareStatement(sql);

        statement.setString(1, getName());
        statement.setInt(2, LoginRepository.getLoggedInUser().getUserId());
        statement.setString(3, getLocation());
        statement.setDouble(4, getCoordinate().getLatitude());
        statement.setDouble(5, getCoordinate().getLongitude());
        if (picture != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            statement.setBytes(6, stream.toByteArray());
        }

        int result = statement.executeUpdate();
        statement.close();
        if (result > 0) {
            return true;
        } else {
            throw new SQLException();
        }
    }

    public List<Hive> selectHives() throws SQLException {
        String queryStmt = "Select * from dbo.[Hive] where apiary_id = " + getId();
        Connection connect = ConnectionHelper.CONN();

        ResultSet resultSet = connect.createStatement().executeQuery(queryStmt);
        List<Hive> list = new ArrayList<>();
        while (resultSet.next()) {
            Hive hive = new Hive(resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(6),
                    resultSet.getDate(7),
                    resultSet.getDate(8));
            hive.setApiary(this);
            list.add(hive);
        }
        return list;
    }

    @Override
    public boolean update() throws SQLException {
        Connection connect = ConnectionHelper.CONN();
        String sql;
        if (picture != null)
            sql = "UPDATE BeekeeperPro.dbo.Apiary SET name = ?, location = ?, latitude = ?, longitude = ?, photo = ? WHERE id = ?";
        else
            sql = "UPDATE BeekeeperPro.dbo.Apiary SET name = ?, location = ?, latitude = ?, longitude = ? WHERE id = ?";
        PreparedStatement statement = connect.prepareStatement(sql);

        statement.setString(1, getName());
        statement.setString(2, getLocation());
        statement.setDouble(3, getCoordinate().getLatitude());
        statement.setDouble(4, getCoordinate().getLongitude());
        if (picture != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            statement.setBytes(5, stream.toByteArray());
        }
        statement.setInt(6, getId());

        int result = statement.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            throw new SQLException();
        }
    }

}
