package com.beekeeperpro.data.model;

public class User {

    private final int id;
    private final String name;
    private final String email;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getUserId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDisplayEmail() {
        return email;
    }
}