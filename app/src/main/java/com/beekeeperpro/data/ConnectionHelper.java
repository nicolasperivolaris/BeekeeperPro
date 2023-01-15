package com.beekeeperpro.data;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {
    private static Connection conn;

    public static Connection CONN() {
        try {
            for (int i = 0; (conn == null || conn.isClosed()) && i < 10; i++) {
                String _user = "sa";
                String _pass = "DBdeBP22";
                String _DB = "BeekeeperPro";
                String _server = "server.perivolaris.be:1433";
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                String ConnURL = "jdbc:jtds:sqlserver://" + _server + ";"
                        + "databaseName=" + _DB + ";user=" + _user + ";password="
                        + _pass + ";";
                conn = DriverManager.getConnection(ConnURL);
            }
            if (conn.isClosed())
                System.err.println("Connection closed");
        } catch (Exception se) {
            Log.e("ERRO", se.getMessage());
        }
        return conn;
    }

    public static void Disconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            Log.e("DB disconnection", e.getMessage());
        }
    }
}