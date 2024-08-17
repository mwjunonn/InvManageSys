package com.project.inventory;

import java.sql.*;
import java.util.ArrayList;

/*
* Made by Mr. Wong Jun Onn(Marco)
* To achieve CRUD action. For this system, create are not required.
* Can use the database methods directly with using execute() methods
*/

public class Database {
    private Connection con;
    private String tablename;
    public static final String all = "*";
    /**
     *  | in regex form
     *  The split character that to split the column when the ArrayList returned
     *  Can be used for split in String to get String Array
     */
    public static final String spliter = " \\| " ;
    private ResultSet result = null;

    /**
     * Try using another constructor that parameter is Table Name(String)
     */
    public Database() {
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

    /**
     * Constructor of Database
     * @param tablename Table name that would like to perform CRUD, can be declared again using setter
     */
    public Database(String tablename) {
        this();
        this.tablename = tablename;
    }

    /**
     * Execute SQL command with avoiding sql injection (More secure)
     * @param sql SQL Query, '?' will be the parameter Example: SELECT * FROM USER WHERE username = ?
     * @param parameter Parameter that will replace the '?' respectively. Parameter will automatically add enclosed marks ( ' ). Example: marcowong will be 'marcowong'
     */

    public void execute(String sql, String[] parameter) {
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            for (int i = 0; i < parameter.length; i++) {
                stmt.setString(i + 1, parameter[i]);
            }
            stmt.execute();
            result = stmt.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute SQL command directly
     * <p>WARNING : Use this method and directly with concat the string may be caused SQL injection happen. To avoid this, please use alternative execute method {@code execute(String sql, String[] parameter)}</p>
     * @param sql SQL command that would like to execute
     */

    protected boolean execute(String sql){
        try {
            for(String str:Table.DANGERCLAUSE.getKeyword()){
                int index = sql.toUpperCase().indexOf(str);
                if(index != -1)
                    return false;
            }
            Statement stmt = con.createStatement();
            if(stmt.execute(sql))
                result = stmt.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Read the table with additional condition <p>Eg: JOIN</p>
     * @param columnName Column Name that would like to get in Result
     * @param condition <p>First Array : Which column name would like to check condition</p><p>Second Array : Condition of the column name respectively</p>
     * @param additional Statement after WHERE clause condition. Eg: JOIN ...
     */

    public void readTable(String[] columnName, String[][] condition, String additional) {
        String sql = "SELECT ";
        String[] prmt = new String[condition.length];
        for (int i = 0; i < columnName.length; i++) {
            sql += columnName[i];
            if (i < columnName.length - 1)
                sql += ", ";
        }
        sql += " FROM " + tablename;
        if(condition.length != 0) {
            sql += " WHERE ";
            for (int i = 0; i < condition.length; i++) {
                sql += condition[i][0] + " LIKE ? ";
                prmt[i] = condition[i][1];
                if (i < condition.length - 1)
                    sql += "AND ";
            }
        }
        sql += additional;
        execute(sql,  prmt);
    } // Read record with condition

    /**
     * Read the table with condition<p>Eg: JOIN</p>
     * @param columnName Column Name that would like to get in Result
     * @param condition <p>First Array : Which column name would like to check condition</p><p>Second Array : Condition of the column name respectively</p>
     */
    public void readTable(String[] columnName, String[][] condition){
        readTable(columnName, condition, "");
    }

    /**
     * Read the table with specifying which column returns<p>Eg: JOIN</p>
     * @param columnName Column Name that would like to get in Result
     */
    public void readTable(String[] columnName){
        readTable(columnName, new String[0][0] , "");
    }

    /**
     *  Single result with one column
     * @param columnName  Column name that would like to get the result
     * @return  The value of the column in first row of Result
     */
    public String getResult(String columnName) {
        try {
            if(!result.isFirst()){
                return null;
            }
            else{
                return result.getString(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Return whole result with the column name
     * @return ArrayList of the Result. Index in ArrayList is the particular row of Result and the columns split with spliter '|' . First row is Column name
     * <br>
     * <p>You can use split in String in particular array index to get the particular value.</p>
     * <p>{@code SPLITER}constant for split is provided in this class </p>
     */
    public ArrayList<String> getResult() {
        /*
            You can get single value with split method, use public constant variable "Spliter" to perform
        */
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

    /**
     * Insert value to the table
     * @param columnName Which column would like to insert in the table
     * @param values The values of the column respectively
     */
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

    /**
     * Update the table with condition (Update the table without condition is not allowed)
     * @param values  <p>First Array : Which column name would like to update value</p><p>Second Array : Value of the column name respectively</p>
     * @param condition <p>First Array : Which column name would like to check condition</p><p>Second Array : Condition of the column name respectively</p>
     */
    public void updateTable(String[][] values, String[][] condition){
        String[] prmt = new String[condition.length + values.length];
        String sql = "UPDATE " + tablename + " SET ";
        for(int i = 0 ; i < values.length; i++) {
            sql += values[i][0] + " = ? ";
            prmt[i] = values[i][1];
            if (i < values.length - 1) {
                sql += ", ";
            }
        }
        if(condition.length != 0){
            sql += " WHERE ";
            for(int i = 0 ; i < condition.length; i++){
                sql += condition[i][0] + " LIKE ? ";
                prmt[i + values.length] = condition[i][1];
                if(i < condition.length - 1){
                    sql += ", ";
                }
            }
        }
        execute(sql, prmt);
    }

    /**
     * Delete the record (Must be with condition)
     * @param condition <p>First Array : Which column name would like to check condition</p><p>Second Array : Condition of the column name respectively</p>
     */
    public void deleteRecord(String[][] condition){
        String[] prmt = new String[condition.length];
        String sql = "DELETE FROM " + tablename + " WHERE ";
        if(condition.length != 0){
            for(int i = 0 ; i < condition.length; i++){
                sql += condition[i][0] + " LIKE ? ";
                if(! condition[i][1].equals("%%") && ! condition[i][1].isEmpty() && condition[i][1] != null) {
                    prmt[i] = condition[i][1];
                } else{
                    throw new IllegalArgumentException("Condition must have values");
                }
                if(i < condition.length - 1){
                    sql += ", ";
                }
            }
        }
        else{
            throw new IllegalArgumentException("Condition must have values");
        }
        execute(sql, prmt);
    }
}
