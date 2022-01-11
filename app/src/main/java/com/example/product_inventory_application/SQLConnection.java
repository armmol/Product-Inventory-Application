package com.example.product_inventory_application;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {

    /*Context context;// getApplicationContext();
    WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    String ip = Formatter.formatIpAddress (wm.getConnectionInfo().getIpAddress());*/

    @SuppressLint("NewApi")
    public static Connection CONN() {

        String ip = "34.88.47.61";
        String Classes = "net.sourceforge.jtds.jdbc.Driver";
        String database = "DB_Apps";
        String username = "log";
        String password = "1234";
        String url = "jdbc:jtds:sqlserver://"+ip+"/"+database;



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection conn = null;
        String ConnURL = null;

        try {
            Class.forName(Classes);
            conn = DriverManager.getConnection(url, username,password);
        } catch (Exception se) {
            Log.e("ERROR", se.getMessage());
        }
        return conn;
    }
}
