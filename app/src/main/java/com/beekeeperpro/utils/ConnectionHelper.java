package com.beekeeperpro.utils;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {
    private static Connection conn;

    public static Connection CONN() {
        try{
            if(conn == null || conn.isClosed()) {
                String _user = "sa";
                String _pass = "DBdeBP22";
                String _DB = "BeekeeperPro";
                String _server = "server.perivolaris.be:1433";
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                String ConnURL = null;
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                ConnURL = "jdbc:jtds:sqlserver://" + _server + ";"
                        + "databaseName=" + _DB + ";user=" + _user + ";password="
                        + _pass + ";";
                conn = DriverManager.getConnection(ConnURL);
            }
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }

    public static void Disconnect() {
        try {
            if(conn != null && !conn.isClosed()){
                conn.close();
            }
        } catch (SQLException e) {
            Log.e("DB disconnection", e.getMessage());
        }
    }
}