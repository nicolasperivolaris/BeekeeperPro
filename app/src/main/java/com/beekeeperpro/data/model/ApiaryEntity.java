package com.beekeeperpro.data.model;

import com.beekeeperpro.data.Result;

import java.sql.SQLException;
import java.util.List;

public abstract class ApiaryEntity {
    public abstract boolean delete() throws SQLException;
    public abstract boolean insert() throws SQLException;
    public abstract boolean update() throws SQLException;
}
