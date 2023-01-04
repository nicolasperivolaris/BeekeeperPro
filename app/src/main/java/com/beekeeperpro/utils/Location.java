package com.beekeeperpro.utils;

import androidx.annotation.NonNull;

public class Location extends android.location.Location {
    public Location() {
        super("provider");
    }

    public Location(android.location.Location location) {
        super(location);
    }

    @NonNull
    @Override
    public String toString() {
        return getLongitude() + " " + getLatitude();
    }
}
