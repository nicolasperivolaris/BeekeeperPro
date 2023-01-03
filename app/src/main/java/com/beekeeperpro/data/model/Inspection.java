package com.beekeeperpro.data.model;

import java.util.Date;
import java.util.HashSet;

public class Inspection {
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
}

