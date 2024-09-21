package com.project.inventory.dao;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import java.sql.*;
import java.util.ArrayList;

/**
 * <p>Dependency : MySQL Connector/J  V8.0.31++</p>
* To achieve CRUD action. For this system, create are not required.
* Can use the database methods directly with using execute() methods
 *
 * @author Mr. Wong Jun Onn(Marco)
*/

public class Database {
    private static Connection con;
    private static boolean success = false;
    private String tableName;
    /**
     * Check if the database is success to connect or not
     * @return {@code True} if database connect successfully
     */
    public static boolean isSuccess() {
        return success;
    }

    public static final String all = "*";
    /**
     *  | in regex form
     *  The split character that to split the column when the ArrayList returned
     *  Can be used for split in String to get String Array
     */
    public static final String delimiter = " \\| " ;
    private ResultSet result = null;
    /**
     * Try using another constructor that parameter is Table Name(String)
     */
    public Database() {
    }

    public static void startDatabase(){
        final String dbURL = "jdbc:mysql://localhost/assignment";
        if(success)
            return;
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            con = DriverManager.getConnection(dbURL, "assignment", "123456");
            con.setAutoCommit(true);
            if(con.isValid(1))
                success = true;
        } catch (Exception e) {
            if(e instanceof CommunicationsException) {
                System.err.println("MySQL Server are not found...");
            }else{
                System.err.println(e.getMessage());
            }
            System.exit(-1);
        }
    }

    /**
     * Constructor of Database
     * @param tablename Table name that would like to perform CRUD, can be declared again using setter
     */
    public Database(String tablename) {
        this();
        this.tableName = tablename;
    }

    public static void closeDatabase(){
        try {
            con.close();
            if (con.isClosed())
                success = false;
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

    public String getTablename() {
        return tableName;
    }

    private Object nullToBlank(Object value){
        if(value.equals("null") || value == null){
            return "";
        }
        return value;
    }

    /**
     * Execute SQL command with avoiding sql injection (More secure)
     * @param sql SQL Query, '?' will be the parameter Example: SELECT * FROM USER WHERE username = ?
     * @param parameter Parameter that will replace the '?' respectively. Parameter will automatically suit the SQL query standard according to the data type. Eg: String "marcowong" will be converted to 'marcowong'
     *  @return {@code true} means successful, {@code false} means unsuccessful.
     */
    public  boolean execute(String sql, Object[] parameter) {
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
            else
                status = stmt.getUpdateCount() > 0;
            return status;
        } catch (SQLException e) {
            if(e instanceof CommunicationsException) {
                System.err.println("MySQL Server are not found...");
                System.exit(-1);
            }else{
                System.err.println(e.getMessage());
            }
        }
        return false;
    }

    /**
     * Execute SQL command directly
     * <p>WARNING : Use this method and directly with concat the string may be caused SQL injection happen. To avoid this, please use alternative execute method {@code execute(String sql, Object[] parameter)}</p>
     * @param sql SQL command that would like to execute
     * @return {@code true} means successful, {@code false} means unsuccessful.
     */
        boolean execute(String sql){
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
            if(e instanceof CommunicationsException){
                System.err.println("MySQL Server are not found...");
                System.exit(-1);
            }else {
                System.err.println(e.getMessage());
            }
        }
        return false;
    }

    /**
     * Read the table with condition {@code WHERE} and additional condition <p>Eg: JOIN</p>
     * @return {@code true} means successful, {@code false} means unsuccessful.
     * @param columnName Column Name that would like to get in Result
     * @param condition <p>An multiple array that coupled with column name and the condition value. <br>Eg: [[item_name, "Brown Sugar Pearl"]["item_type", "Frozen"]]</p>
     * @param additional Statement after WHERE clause condition. Eg: JOIN ...
     */

    public boolean readTable(String[] columnName, Object[][] condition, String additional) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        Object[] prmt = new Object[condition.length];
        for (int i = 0; i < columnName.length; i++) {
            sb.append(columnName[i]);
            if (i < columnName.length - 1)
                sb.append(", ");
        }
        sb.append(" FROM ");
        sb.append(tableName);
        if(condition.length != 0) {
            sb.append(" WHERE ");
            for (int i = 0; i < condition.length; i++) {
                sb.append(condition[i][0]);
                sb.append(" LIKE ?");
                prmt[i] = condition[i][1];
                if (i < condition.length - 1)
                    sb.append(" AND ");
            }
        }
        sb.append(additional);
        return execute(sb.toString(),  prmt);
    } // Read record with condition

    /**
     * Read the table with condition{@code WHERE} clause
     * @return {@code true} means successful, {@code false} means unsuccessful.
     * @param columnName Column Name that would like to get in Result
     * @param condition <p>An multiple array that coupled with column name and the condition value. <br>Eg: [[item_name, "Brown Sugar Pearl"]["item_type", "Frozen"]]</p>
     */
    public boolean readTable(String[] columnName, Object[][] condition){

        return readTable(columnName, condition, "");
    }

    /**
     * Read the table with condition<p>Eg: JOIN</p>
     * This method assuming not using {@code WHERE} clause, please use method {@code readTable(String[] columnName, Object[][] condition, String additional))} or {@code readTable(String[] columnName, Object[][] condition} for using {@code WHERE} clause to prevent SQL injection
     * @return {@code true} means successful, {@code false} means unsuccessful.
     * @param columnName Column Name that would like to get in Result
     * @param condition Statement after WHERE clause condition. Eg: JOIN ...
     */
    public boolean readTable(String[] columnName, String condition){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for (int i = 0; i < columnName.length; i++) {
            sb.append(columnName[i]);
            if (i < columnName.length - 1)
                sb.append(", ");
        }
        sb.append(" FROM ");
        sb.append(tableName).append(" ");
        sb.append(condition);
        return execute(sb.toString());
    }

    /**
     * Read the table with specifying which column returns<p>Eg: JOIN</p>
     * @return {@code true} means successful, {@code false} means unsuccessful.
     * @param columnName Column Name that would like to get in Result
     */
    public boolean readTable(String[] columnName){
        return readTable(columnName, new Object[0][0] , "");
    }

    /**
     *  Single result with one column
     * @param columnName  Column name that would like to get the result
     * @return  The value of the column in first row of Result
     */
    public Object getResult(String columnName) {
        try {
            if(!result.isFirst()){
                return null;
            }
            else{
                return result.getObject(columnName);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * Return whole result with the column name
     * @return ArrayList of the Result. Index in ArrayList is the particular row of Result and the columns split with delimiter '|' . First row is Column name
     * <br>
     * <p>You can use split in String in particular array index to get the particular value.</p>
     * <p>{@code delimiter}constant for split is provided in this class </p>
     */
    public ArrayList<String> getResult() {
        /*
            You can get single value with split method, use public constant variable "delimiter" to perform
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
     * Return whole  in object with the column name
     * @return Nested ArrayList of the Result in object. Each ArrayList means row and a row having ArrayList means column. First row is Column name
     */
    public ArrayList<ArrayList<Object>> getObjResult(){
        ArrayList<ArrayList<Object>> values = new ArrayList<>();
        try{
            ResultSetMetaData md = result.getMetaData();
            values.add(new ArrayList<>());
            int index = 0;
            for (int i = 1; i <= md.getColumnCount(); i++){
                values.get(index).add(md.getColumnLabel(i));
            }
            while(result.next()){
                values.add(new ArrayList<>());
                index++;
                for(int i = 1;i <= md.getColumnCount();i++){
                    values.get(index).add(result.getObject(i));
                }
            }
            result.first();
            return values;
        }catch(SQLException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Insert value to the table
     * @param columnName Which column would like to insert in the table
     * @param values The values of the column respectively
     */
    public boolean insertTable(String[] columnName, Object[] values){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(tableName).append(" (");
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
        return execute(sb.toString(), values);
    }

    /**
     * Update the table with condition (Update the table without condition is not allowed)
     * @param values  <p>An multiple array that coupled with column name and the update value. <br>Eg: [[item_name, "Brown Sugar Pearl"]["item_type", "Frozen"]]</p>
     * @param condition <p>An multiple array that coupled with column name and the condition value. <br>Eg: [[item_name, "Brown Sugar Pearl"]["item_type", "Frozen"]]</p>
     */
    public boolean updateTable(Object[][] values, Object[][] condition){
        Object[] prmt = new Object[condition.length + values.length];
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(tableName).append(" SET ");
        for(int i = 0 ; i < values.length; i++) {
            sb.append((String)values[i][0]).append(" = ? ");
            prmt[i] = values[i][1];
            if (i < values.length - 1) {
                sb.append(", ");
            }
        }
        if(condition.length != 0){
            sb.append(" WHERE ");
            for(int i = 0 ; i < condition.length; i++){
                sb.append(condition[i][0]).append(" LIKE ? ");
                prmt[i + values.length] = condition[i][1];
                if(i < condition.length - 1){
                    sb.append(" AND ");
                }
            }
        }
        return execute(sb.toString(), prmt);
    }

    /**
     * Delete the record (Must be with condition)
     * @param condition <p>An multiple array that coupled with column name and the condition value. <br>Eg: [[item_name, "Brown Sugar Pearl"]["item_type", "Frozen"]]</p>
     */
    public boolean deleteRecord(Object[][] condition) {
        Object[] prmt = new Object[condition.length];
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(tableName).append(" WHERE ");
        if (condition.length != 0) {
            for (int i = 0; i < condition.length; i++) {
                sb.append(condition[i][0]).append(" LIKE ? ");
                if (!condition[i][1].equals("%%") && !((String) condition[i][1]).isEmpty() && condition[i][1] != null) {
                    prmt[i] = condition[i][1];
                } else {
                    System.err.println("Condition must have value.");
                    return false;
                }
                if (i < condition.length - 1) {
                    sb.append(" AND ");
                }
            }
        } else {
            System.err.println("Condition must have values");
            return false;
        }
        return execute(sb.toString(), prmt);
    }
}
