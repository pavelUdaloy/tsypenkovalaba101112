package org.example.tsypenkovalaba101112.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    public static final String URL = "jdbc:mysql://localhost:3307/laba101112?allowPublicKeyRetrieval=true&useSSL=false";
    public static final String USER = "mysql";
    public static final String PASSWORD = "12345678";

    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        while (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    // ignore
                }
            }
        }

        System.out.println("DB connected.");
        return connection;
    }
}
