package com.umn.csci5801.uadmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by arjun on 4/24/2016.
 */
public class DBConnection {
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/uadmt";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin";

    public Connection createDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);

        }
        catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            dbConnection = DriverManager.getConnection( DB_CONNECTION, DB_USER,DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return dbConnection;
    }
}
