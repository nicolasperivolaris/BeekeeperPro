package com.beekeeperpro.utils;

import androidx.annotation.NonNull;

public class Location extends android.location.Location {
    public Location() {
        super("provider");
    }

    @NonNull
    @Override
    public String toString() {
        return getLongitude() + " " + getLatitude();
    }
}
