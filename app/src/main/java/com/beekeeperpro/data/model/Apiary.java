package com.beekeeperpro.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.beekeeperpro.data.ConnectionHelper;
import com.beekeeperpro.data.LoginRepository;
import com.beekeeperpro.utils.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            String queryStmt = "INSERT INTO BeekeeperPro.dbo.Apiary (name, coordinate, user_id, location)" +
                    "VALUES ('" + getName() + "', geography::STGeomFromText('POINT(" + getCoordinate() +
                    ")', 4326), " + LoginRepository.getLoggedInUser().getUserId() + ", '" + getLocation() + "');";
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
        return false;
    }
}
