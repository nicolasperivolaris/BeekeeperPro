package com.beekeeperpro.data.model;

import android.graphics.Bitmap;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Apiary {
    private int id;
    private String name;
    private String location;
    private Point coordinate;
    private List<Hive> hives;
    private int hivesCount;
    private Bitmap picture;

    public Apiary(int id, String name, String location, int hivesCount){
        this.id = id;
        this.name = name;
        this.location = location;
        this.hivesCount = hivesCount;
        hives = new ArrayList<>();
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point coordinate) {
        this.coordinate = coordinate;
    }

    public List<Hive> getHives() {
        return hives;
    }

    public void setHives(List<Hive> hives) {
        this.hives = hives;
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
}
