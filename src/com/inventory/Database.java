package com.inventory;

import java.sql.*;

public class Database {
    private Connection con;
    private String tablename;
    public final String all = "*";
    private ResultSet result = null;
    public Database(){
    }

    public Database(String tablename)  {
        this.tablename = tablename;
    }

    public void startDatabase(){
        final String dbURL = "jdbc:mysql://localhost/assignment";
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

    public void readRecord(String[] columnName, String[][] condition) {
        try {
           String sql = "SELECT ";
            if(columnName.length > 1){
                for(int i = 0; i < columnName.length; i++) {
                    sql += columnName[i];
                    if(i < columnName.length - 1)
                        sql += ", ";
                }
            }
            else
                sql += columnName[0];
            sql += " FROM " + tablename +" WHERE ";
            if(condition.length > 1){
                for(int i = 0; i< condition.length; i++) {
                    sql += condition[i][0] + " LIKE ? ";
                    if(i < condition.length - 1)
                        sql += "AND ";
                }
            }
            else
                sql += condition[0][0] + " LIKE ? ";
            PreparedStatement stmt = con.prepareStatement(sql);
            for(int i = 0; i < condition.length; i++){
                stmt.setString(i+1,  condition[i][1]);
            }
            stmt.execute();
            result =  stmt.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // Read record with condition

    public String getResult(String columnName){
        try {
            if(result.next()) {
                return result.getString(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getResult(){
        try {
            if(result.next()) {
                return result.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
