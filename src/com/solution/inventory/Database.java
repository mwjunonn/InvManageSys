package com.solution.inventory;

import java.sql.*;

public class Database {
    private Connection con;
    public Database(){ //
        final String dbURL = "jdbc:mysql://127.0.0.1:3306/?user=assignment";
        final String username = "assignment";
        final String pws = "123456";

        try {
            con = DriverManager.getConnection(dbURL, username, pws);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.out.println("Error occur");
            e.printStackTrace();
        }
    }
}
