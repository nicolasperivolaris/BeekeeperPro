package com.beekeeperpro.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.beekeeperpro.data.ConnectionHelper;
import com.beekeeperpro.data.LoginRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;

public class Inspection extends ApiaryEntity implements Parcelable {
    private int id;
    private Date inspectionDate;
    private String temper;
    private String hiveCondition;
    private String queenCondition;
    private String phytosanitaryUsed;
    private HashSet<String> attentionPoints;
    private String hiveConditionRemarks;
    private String queenConditionRemarks;
    private String phytosanitaryUsedRemarks;
    private Hive hive;

    public Inspection() {
        inspectionDate = new Date();
        attentionPoints = new HashSet<>();
    }

    protected Inspection(Parcel in) {
        id = in.readInt();
        temper = in.readString();
        hiveCondition = in.readString();
        queenCondition = in.readString();
        phytosanitaryUsed = in.readString();
        hiveConditionRemarks = in.readString();
        queenConditionRemarks = in.readString();
        phytosanitaryUsedRemarks = in.readString();
        hive = in.readParcelable(Hive.class.getClassLoader());
    }

    public static final Creator<Inspection> CREATOR = new Creator<Inspection>() {
        @Override
        public Inspection createFromParcel(Parcel in) {
            return new Inspection(in);
        }

        @Override
        public Inspection[] newArray(int size) {
            return new Inspection[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getTemper() {
        return temper;
    }

    public void setTemper(String temper) {
        this.temper = temper;
    }

    public String getHiveCondition() {
        return hiveCondition;
    }

    public void setHiveCondition(String hiveCondition) {
        this.hiveCondition = hiveCondition;
    }

    public String getQueenCondition() {
        return queenCondition;
    }

    public void setQueenCondition(String queenCondition) {
        this.queenCondition = queenCondition;
    }

    public String getPhytosanitaryUsed() {
        return phytosanitaryUsed;
    }

    public void setPhytosanitaryUsed(String phytosanitaryUsed) {
        this.phytosanitaryUsed = phytosanitaryUsed;
    }

    public HashSet<String> getAttentionPoints() {
        return attentionPoints;
    }

    public void setHive(Hive hive) {
        this.hive = hive;
    }

    public Hive getHive() {
        return hive;
    }

    public void setAttentionPoints(HashSet<String> attentionPoints) {
        this.attentionPoints = attentionPoints;
    }

    public String getHiveConditionRemarks() {
        return hiveConditionRemarks;
    }

    public void setHiveConditionRemarks(String hiveConditionRemarks) {
        this.hiveConditionRemarks = hiveConditionRemarks;
    }

    public String getQueenConditionRemarks() {
        return queenConditionRemarks;
    }

    public void setQueenConditionRemarks(String queenConditionRemarks) {
        this.queenConditionRemarks = queenConditionRemarks;
    }

    public String getPhytosanitaryRemarks() {
        return phytosanitaryUsedRemarks;
    }

    public void setPhytosanitaryRemarks(String pesticidesUsedRemarks) {
        this.phytosanitaryUsedRemarks = pesticidesUsedRemarks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(temper);
        dest.writeString(hiveCondition);
        dest.writeString(queenCondition);
        dest.writeString(phytosanitaryUsed);
        dest.writeString(hiveConditionRemarks);
        dest.writeString(queenConditionRemarks);
        dest.writeString(phytosanitaryUsedRemarks);
        dest.writeParcelable(hive, flags);
    }

    public boolean delete() throws SQLException {
        Hive hive = getHive();
        Connection connect = ConnectionHelper.CONN();

        String sql = "DELETE FROM BeekeeperPro.dbo.Inspection WHERE id = ?";
        PreparedStatement statement = connect.prepareStatement(sql);
        statement.setInt(1, getId());

        int rows = statement.executeUpdate();
        if (rows > 0) return true;
        else {
            throw new SQLException();
        }
    }

    public boolean insert() throws SQLException {
        Connection connect = ConnectionHelper.CONN();
        try {
            String queryStmt = "INSERT INTO BeekeeperPro.dbo.Inspection (date, temper, hive_condition, queen_condition, phytosanitary_used, hive_condition_remarks, queen_condition_remarks, phytosanitary_used_remarks, hive_id, user_id)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            connect.setAutoCommit(false);
            PreparedStatement preparedStatement = connect.prepareStatement(queryStmt, Statement.RETURN_GENERATED_KEYS);
            if (getInspectionDate() != null)
                preparedStatement.setDate(1, new java.sql.Date(getInspectionDate().getTime()));
            else throw new SQLException("No date provided");

            preparedStatement.setString(2, getTemper());
            preparedStatement.setString(3, getHiveCondition());
            preparedStatement.setString(4, getQueenCondition());
            preparedStatement.setString(5, getPhytosanitaryUsed());
            preparedStatement.setString(6, getHiveConditionRemarks());
            preparedStatement.setString(7, getQueenConditionRemarks());
            preparedStatement.setString(8, getPhytosanitaryRemarks());
            preparedStatement.setInt(9, getHive().getId());
            preparedStatement.setInt(10, LoginRepository.getLoggedInUser().getUserId());

            if (preparedStatement.executeUpdate() > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next())
                    setId(generatedKeys.getInt(1));

                for (String point : getAttentionPoints()) {
                    String pointsInsert = "INSERT INTO BeekeeperPro.dbo.Inspection_AttentionPoints (inspection_id, name)" +
                            "VALUES (?, ?);";
                    PreparedStatement pIPrepStat = connect.prepareStatement(pointsInsert, Statement.RETURN_GENERATED_KEYS);
                    pIPrepStat.setInt(1, getId());
                    pIPrepStat.setString(2, point);
                    if (pIPrepStat.executeUpdate() == 0)
                        throw new SQLException("Error while inserting AttentionPoint");
                }

                connect.commit();
                return true;
            } else {
                throw new SQLException("Error while inserting Inspection");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            connect.rollback();
            throw e;
        } finally {
            connect.setAutoCommit(true);
        }
    }

    @Override
    public boolean update() throws SQLException {
        return false;
    }


}

