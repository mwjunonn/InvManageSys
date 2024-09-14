package com.project.inventory;

import java.sql.*;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

/*
* Made by Mr. Wong Jun Onn(Marco)
* To achieve CRUD action. For this system, create are not required.
* Can use the database methods directly with using execute() methods
*/

public class Database{
    private static Connection con;
    private static boolean success = false;
    private String tablename;

    /**
     * Check if the database is success to connect or not
     * @return {@code True} if database connect successfully
     */
    public boolean isSuccess() {
        return success;
    }

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
        if(!success)
            startDatabase();
    }

    private static void startDatabase(){
        final String dbURL = "jdbc:mysql://localhost/assignment";

        try {
            con = DriverManager.getConnection(dbURL, "assignment", "123456");
            con.setAutoCommit(true);
            if(con.isValid(1))
                success = true;
        } catch (SQLException e) {
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

    public static void closeDatabase() throws SQLException {
        con.close();
        if(con.isClosed())
            success = false;
    }

    private String nullToBlank(String value){
        if(value.equals("null") || value.isBlank()){
            return "";
        }
        return value;
    }

    /**
     * Execute SQL command with avoiding sql injection (More secure)
     * @param sql SQL Query, '?' will be the parameter Example: SELECT * FROM USER WHERE username = ?
     * @param parameter Parameter that will replace the '?' respectively. Parameter will automatically add enclosed marks ( ' ). Example: marcowong will be 'marcowong'
     *  @return {@code true} means successful, {@code false} means unsuccessful.
     */

    public boolean execute(String sql, String[] parameter) {
        try {
            PreparedStatement stmt = con.prepareStatement(sql,ResultSet. TYPE_SCROLL_INSENSITIVE,
                    ResultSet. CONCUR_UPDATABLE);
            for (int i = 0; i < parameter.length; i++) {
                parameter[i] =  nullToBlank(parameter[i]);
                stmt.setObject(i + 1, parameter[i]);
            }
            boolean status =  stmt.execute();
            if(status)
                result = stmt.getResultSet();
            return status;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Execute SQL command directly
     * <p>WARNING : Use this method and directly with concat the string may be caused SQL injection happen. To avoid this, please use alternative execute method {@code execute(String sql, String[] parameter)}</p>
     * @param sql SQL command that would like to execute
     * @return {@code true} means successful, {@code false} means unsuccessful.
     */

    protected boolean execute(String sql){
        String[] dangerClause = {"DROP", "DELETE", "GRANT", "TRUNCATE"};
        try {
            for(String str: dangerClause){
                int index = sql.toUpperCase().indexOf(str);
                if(index != -1)
                    return false;
            }
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            boolean status = stmt.execute(sql);
            if(status)
                result = stmt.getResultSet();
            return status;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Read the table with additional condition <p>Eg: JOIN</p>
     * @param columnName Column Name that would like to get in Result
     * @param condition <p>An multiple array that coupled with column name and the condition value. <br>Eg: [[item_name, "Brown Sugar Pearl"]["item_type", "Frozen"]]</p>
     * @param additional Statement after WHERE clause condition. Eg: JOIN ...
     */

    public void readTable(String[] columnName, String[][] condition, String additional) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        String[] prmt = new String[condition.length];
        for (int i = 0; i < columnName.length; i++) {
            sb.append(columnName[i]);
            if (i < columnName.length - 1)
                sb.append(", ");
        }
        sb.append(" FROM ");
        sb.append(tablename);
        if(condition.length != 0) {
            sb.append(" WHERE ");
            for (int i = 0; i < condition.length; i++) {
                sb.append(condition[i][0]);
                sb.append(" LIKE ");
                prmt[i] = condition[i][1];
                if (i < condition.length - 1)
                    sb.append("AND ");
            }
        }
        sb.append(additional);
        execute(sb.toString(),  prmt);
    } // Read record with condition

    /**
     * Read the table with condition<p>Eg: JOIN</p>
     * @param columnName Column Name that would like to get in Result
     * @param condition <p>An multiple array that coupled with column name and the condition value. <br>Eg: [[item_name, "Brown Sugar Pearl"]["item_type", "Frozen"]]</p>
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
            StringBuilder arrBuilder = new StringBuilder();
            for (int j = 0; j < md.getColumnCount(); j++){
                arrBuilder.append(md.getColumnLabel(j + 1)).append(" | ");
            }
            values.add(arrBuilder.toString());
            while (result.next()) {
                arrBuilder = new StringBuilder();
                for (int j = 0 ; j < md.getColumnCount();j++){
                     arrBuilder.append(result.getString(j + 1)).append(" | ");
                }
                values.add(arrBuilder.toString());
            }
            result.first();
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
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(tablename).append(" (");
        for(int i = 0; i < columnName.length; i++){
            sb.append(columnName[i]);
            if(i < columnName.length - 1){
                sb.append(", ");
            }
        }
        sb.append(") VALUES (");
        for(int i = 0; i < values.length; i++){
            sb.append("?");
            if(i < values.length - 1){
                sb.append(", ");
            }
        }
        sb.append(")");
        execute(sb.toString(), values);
    }

    /**
     * Update the table with condition (Update the table without condition is not allowed)
     * @param values  <p>An multiple array that coupled with column name and the update value. <br>Eg: [[item_name, "Brown Sugar Pearl"]["item_type", "Frozen"]]</p>
     * @param condition <p>An multiple array that coupled with column name and the condition value. <br>Eg: [[item_name, "Brown Sugar Pearl"]["item_type", "Frozen"]]</p>
     */
    public void updateTable(String[][] values, String[][] condition){
        String[] prmt = new String[condition.length + values.length];
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(tablename).append(" SET ");
        for(int i = 0 ; i < values.length; i++) {
            sb.append(prmt[i]).append(" = ? ");
            prmt[i] = values[i][1];
            if (i < values.length - 1) {
                sb.append(", ");
            }
        }
        if(condition.length != 0){
            sb.append(" WHERE ");
            for(int i = 0 ; i < condition.length; i++){
                sb.append(condition[i][0]).append("LIKE ? ");
                prmt[i + values.length] = condition[i][1];
                if(i < condition.length - 1){
                    sb.append(" AND ");
                }
            }
        }
        execute(sb.toString(), prmt);
    }

    /**
     * Delete the record (Must be with condition)
     * @param condition <p>An multiple array that coupled with column name and the condition value. <br>Eg: [[item_name, "Brown Sugar Pearl"]["item_type", "Frozen"]]</p>
     */
    public void deleteRecord(String[][] condition){
        String[] prmt = new String[condition.length];
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(tablename).append(" WHERE ");
        if(condition.length != 0){
            for(int i = 0 ; i < condition.length; i++){
                sb.append(condition[i][0]).append(" LIKE ? ");
                if(! condition[i][1].equals("%%") && ! condition[i][1].isEmpty() && condition[i][1] != null) {
                    prmt[i] = condition[i][1];
                } else{
                    throw new IllegalArgumentException("Condition must have values");
                }
                if(i < condition.length - 1){
                    sb.append(" AND ");
                }
            }
        }
        else{
            throw new IllegalArgumentException("Condition must have values");
        }
        execute(sb.toString(), prmt);
    }
}
