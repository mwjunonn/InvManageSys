package com.inventory;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private Connection con;
    private String tablename;
    public final String all = "*";
    private ResultSet result = null;

    public Database() {
    }

    public Database(String tablename) {
        this.tablename = tablename;
    }

    public void execute(String sql, String[] parameter){
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            for (int i = 0; i < parameter.length; i++) {
                stmt.setString(i + 1, parameter[i]);
            }
            stmt.execute();
            result = stmt.getResultSet();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void startDatabase() {
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

    public void readTable(String[] columnName, String[][] condition, String additional) {
        String sql = "SELECT ";
        String[] prmt = new String[condition.length];
        for (int i = 0; i < columnName.length; i++) {
            sql += columnName[i];
            if (i < columnName.length - 1)
                sql += ", ";
        }
        sql += " FROM " + tablename + " WHERE ";
        for (int i = 0; i < condition.length; i++) {
            sql += condition[i][0] + " LIKE ? ";
            prmt[i] = condition[i][1];
            if (i < condition.length - 1)
                sql += "AND ";
        }
        sql += additional;
        execute(sql,  prmt);
    } // Read record with condition

    public void readTable(String[] columnName, String[][] condition){
        readTable(columnName, condition, "");
    }

    public String getResult(String columnName) {
        try {
            if (result.next()) {
                return result.getString(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getResult() {
        try {
            ResultSetMetaData md = result.getMetaData();
            ArrayList<String> values = new ArrayList<>();
            String arr = "";
            for (int j = 0 ; j < md.getColumnCount();j++){
                arr += md.getColumnLabel(j + 1) + " | ";
            }
            values.add(arr);
            while (result.next()) {
                arr = "";
                for (int j = 0 ; j < md.getColumnCount();j++){
                     arr += result.getString(j + 1) + " | ";
                }
                values.add(arr);
            }
            return values;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertTable(String[] columnName, String[] values){
        String sql = "INSERT INTO " + tablename + " (";
        for(int i = 0; i < columnName.length; i++){
            sql += columnName[i];
            if(i < columnName.length - 1){
                sql += ", ";
            }
        }
        sql += ") ";
        sql += "VALUES (";
        for(int i = 0; i < values.length; i++){
            sql += "?";
            if(i < values.length - 1){
                sql += ", ";
            }
        }
        sql += ")";
        execute(sql, values);
    }
}
