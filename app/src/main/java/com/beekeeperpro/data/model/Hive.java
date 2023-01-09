package com.beekeeperpro.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.beekeeperpro.data.ConnectionHelper;
import com.beekeeperpro.data.LoginRepository;
import com.beekeeperpro.data.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Hive extends ApiaryEntity implements Parcelable {
    private int id;
    private String name;
    private String code;
    private int strength;
    private Date hivingDate;
    private Date acquisitionDate;

    private Apiary apiary = Apiary.Stock;
    private List<Inspection> inspectionList;

    public Hive() {
        name = "";
        code = "";
        hivingDate = Calendar.getInstance().getTime();
        inspectionList = new ArrayList<>();
    }

    public Hive(int id, String name, String code, int strength, Date hivingDate, Date acquisitionDate) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.strength = strength;
        this.hivingDate = hivingDate;
        this.acquisitionDate = acquisitionDate;
        inspectionList = new ArrayList<>();
    }

    protected Hive(Parcel in) {
        id = in.readInt();
        name = in.readString();
        code = in.readString();
        strength = in.readInt();
        hivingDate = new Date(in.readLong());
        acquisitionDate = new Date(in.readLong());
        apiary = in.readParcelable(Apiary.class.getClassLoader());
    }

    public static final Creator<Hive> CREATOR = new Creator<Hive>() {
        @Override
        public Hive createFromParcel(Parcel in) {
            return new Hive(in);
        }

        @Override
        public Hive[] newArray(int size) {
            return new Hive[size];
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getHivingDate() {
        return hivingDate;
    }

    public void setHivingDate(Date hivingDate) {
        this.hivingDate = hivingDate;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public Date getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(Date acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public Apiary getApiary() {
        return apiary;
    }

    public void setApiary(Apiary apiary) {
        this.apiary = apiary;
    }

    public List<Inspection> getInspectionList() {
        return inspectionList;
    }

    public void setInspectionList(List<Inspection> inspectionList) {
        this.inspectionList = inspectionList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(code);
        dest.writeInt(strength);
        dest.writeLong(hivingDate.getTime());
        dest.writeLong(acquisitionDate.getTime());
        dest.writeParcelable(apiary, flags);
    }

    public boolean delete() throws SQLException {
        Connection connect = ConnectionHelper.CONN();

        String sql = "DELETE FROM BeekeeperPro.dbo.Hive WHERE id = ?";
        PreparedStatement statement = connect.prepareStatement(sql);
        statement.setInt(1, getId());

        int rows = statement.executeUpdate();
        if (rows > 0) {
            return true;
        } else {
            throw new SQLException();
        }
    }

    public boolean insert() throws SQLException {
            String queryStmt = "INSERT INTO BeekeeperPro.dbo.Hive (name, code, apiary_id, user_id, strength, hiving_date, acquisition_date)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";
            Connection connect = ConnectionHelper.CONN();
            PreparedStatement preparedStatement = connect.prepareStatement(queryStmt);
            preparedStatement.setString(1, getName());
            preparedStatement.setString(2, getCode());
            preparedStatement.setInt(3, (getApiary() == null ? 0 : getApiary().getId()));
            preparedStatement.setInt(4, LoginRepository.getLoggedInUser().getUserId());
            preparedStatement.setInt(5, getStrength());
            preparedStatement.setDate(6, (getHivingDate() != null ? new java.sql.Date(getHivingDate().getTime()) : null));
            preparedStatement.setDate(7, (getAcquisitionDate() != null ? new java.sql.Date(getAcquisitionDate().getTime()) : null));

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
                return true;
            } else {
                throw new SQLException();
            }
    }


    public boolean update() throws SQLException {
        String queryStmt = "UPDATE BeekeeperPro.dbo.Hive SET name = ?, code = ?, strength = ?, hiving_date = ?, acquisition_date = ? WHERE id = ?";
        Connection connect = ConnectionHelper.CONN();
        PreparedStatement preparedStatement = connect.prepareStatement(queryStmt);
        preparedStatement.setString(1, getName());
        preparedStatement.setString(2, getCode());
        preparedStatement.setInt(3, getStrength());
        preparedStatement.setDate(4, (getHivingDate() != null ? new java.sql.Date(getHivingDate().getTime()) : null));
        preparedStatement.setDate(5, (getAcquisitionDate() != null ? new java.sql.Date(getAcquisitionDate().getTime()) : null));
        preparedStatement.setInt(6, getId());

        if (preparedStatement.executeUpdate() > 0) {
            ResultSet r = preparedStatement.getResultSet();
            System.err.println(r);
            return true;
        } else {
            throw new SQLException();
        }
    }

    public List<Inspection> selectInspections() throws SQLException {
        String queryStmt = "Select * from dbo.[Inspection] where hive_id = " + getId();
        Connection connect = ConnectionHelper.CONN();

        ResultSet resultSet = connect.createStatement().executeQuery(queryStmt);
        List<Inspection> list = new ArrayList<>();
        while (resultSet.next()) {
            Inspection inspection = new Inspection();
            inspection.setId(resultSet.getInt(resultSet.findColumn("id")));
            inspection.setInspectionDate(resultSet.getDate(resultSet.findColumn("date")));
            inspection.setTemper(resultSet.getString(resultSet.findColumn("temper")));
            inspection.setHiveCondition(resultSet.getString(resultSet.findColumn("hive_condition")));
            inspection.setQueenCondition(resultSet.getString(resultSet.findColumn("queen_condition")));
            inspection.setPhytosanitaryUsed(resultSet.getString(resultSet.findColumn("phytosanitary_used")));
            inspection.setHiveConditionRemarks(resultSet.getString(resultSet.findColumn("hive_condition_remarks")));
            inspection.setQueenConditionRemarks(resultSet.getString(resultSet.findColumn("queen_condition_remarks")));
            inspection.setPhytosanitaryRemarks(resultSet.getString(resultSet.findColumn("phytosanitary_used_remarks")));
            inspection.setHive(this);
            list.add(inspection);
        }
        inspectionList = list;
        return list;
    }

    public static Hive select(int id) throws SQLException {
        Hive hive = new Hive();
        String queryStmt = "Select * from dbo.[Hive] where id = " + id;
        Connection connect = ConnectionHelper.CONN();
        ResultSet resultSet = connect.createStatement().executeQuery(queryStmt);
        if(!resultSet.next())
            throw new SQLException("No hive with id " + id);
        hive.setId(resultSet.getInt(1));
        hive.setName(resultSet.getString(2));
        hive.setCode(resultSet.getString(3));
        hive.setStrength(resultSet.getInt(6));
        hive.setHivingDate(resultSet.getDate(7));
        hive.setAcquisitionDate(resultSet.getDate(8));
        return hive;
    }
}
