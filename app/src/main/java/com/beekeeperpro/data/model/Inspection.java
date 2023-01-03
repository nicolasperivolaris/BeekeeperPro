package com.beekeeperpro.data.model;

import java.util.Date;
import java.util.List;

public class Inspection {
    private Date inspectionDate;
    private String temper;
    private String hiveCondition;
    private String queenCondition;
    private String phytosanitaryUsed;
    private List<String> attentionPoints;
    private String hiveConditionRemarks;
    private String queenConditionRemarks;
    private String pesticidesUsedRemarks;
    private Hive hive;

    public Inspection() {}

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

    public List<String> getAttentionPoints() {
        return attentionPoints;
    }

    public void setHive(Hive hive) {
        this.hive = hive;
    }

    public Hive getHive() {
        return hive;
    }

    public void setAttentionPoints(List<String> attentionPoints) {
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

    public String getPesticidesUsedRemarks() {
        return pesticidesUsedRemarks;
    }

    public void setPesticidesUsedRemarks(String pesticidesUsedRemarks) {
        this.pesticidesUsedRemarks = pesticidesUsedRemarks;
    }
}

