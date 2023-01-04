package com.beekeeperpro.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class Hive implements Parcelable {
    private int id;
    private String name;
    private String code;
    private int strength;
    private Date hivingDate;
    private Date acquisitionDate;

    private Apiary apiary = Apiary.Stock;

    public Hive() {
        name = "";
        code = "";
        hivingDate = Calendar.getInstance().getTime();
    }

    public Hive(int id, String name, String code, int strength, Date hivingDate) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.strength = strength;
        this.hivingDate = hivingDate;
    }

    protected Hive(Parcel in) {
        id = in.readInt();
        name = in.readString();
        code = in.readString();
        strength = in.readInt();
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
        dest.writeParcelable(apiary, flags);
    }
}
