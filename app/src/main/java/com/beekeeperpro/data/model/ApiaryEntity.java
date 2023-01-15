package com.beekeeperpro.data.model;

import java.sql.SQLException;

public abstract class ApiaryEntity {
    public abstract boolean delete() throws SQLException;

    public abstract boolean insert() throws SQLException;

    public abstract boolean update() throws SQLException;
}
