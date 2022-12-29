package com.beekeeperpro.data.model;

import java.util.Date;

public class Hive {
    private int id;
    private String name;
    private String code;
    private int strength;
    private Date creationDate;

    public Hive(int id, String name, String code, int strength, Date creationDate) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.strength = strength;
        this.creationDate = creationDate;
    }

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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }
}
