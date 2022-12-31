package com.beekeeperpro.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.beekeeperpro.utils.Location;

public class Apiary implements Parcelable {
    public final static Apiary Stock = new Apiary(0, "Stock", "Stock", 0);;
    private int id;
    private String name;
    private String location;
    private Location coordinate;
    private int hivesCount;
    private Bitmap picture;

    public Apiary(){
        name = "";
        location = "";
        coordinate = new Location();
    }

    public Apiary(int id, String name, String location, int hivesCount){
        this.id = id;
        this.name = name;
        this.location = location;
        this.hivesCount = hivesCount;
    }

    protected Apiary(Parcel in) {
        id = in.readInt();
        name = in.readString();
        location = in.readString();
        //coordinate = in.readParcelable(Location.class.getClassLoader());
        hivesCount = in.readInt();
        //picture = in.readParcelable(Bitmap.class.getClassLoader());
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
        //dest.writeParcelable(coordinate, flags);
        dest.writeInt(hivesCount);
        //dest.writeParcelable(picture, flags);
    }
}
