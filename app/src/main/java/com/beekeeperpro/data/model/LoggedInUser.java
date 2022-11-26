package com.beekeeperpro.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private int id;
    private String name;
    private String email;

    public LoggedInUser(int id, String name, String email) {
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