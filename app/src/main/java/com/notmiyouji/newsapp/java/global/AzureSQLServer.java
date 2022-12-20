package com.notmiyouji.newsapp.java.global;

import android.os.StrictMode;
import android.util.Base64;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AzureSQLServer {

    static {
        System.loadLibrary("keys");
    }

    public native String getServerName();
    public native String getDatabaseName();
    public native String getUserName();
    public native String getPassword();

    public Connection getConnection() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection con = null;

        String server = new String(Base64.decode(getServerName(), Base64.DEFAULT));
        String port = "1433";
        String data = new String(Base64.decode(getDatabaseName(), Base64.DEFAULT));
        String username = new String(Base64.decode(getUserName(), Base64.DEFAULT));
        String password = new String(Base64.decode(getPassword(), Base64.DEFAULT));
        String dburl = "jdbc:jtds:sqlserver://" + server + ":" + port + ";DatabaseName=" + data
                + ";user=" + username
                + ";password=" + password
                + ";encrypt=true;trustServerCertificate=false;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;ssl=request;";
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            con = DriverManager.getConnection(dburl);
            System.out.println("Connection Success");
            return con;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return con;
    }
}
