package com.project.inventory.dao;

import java.util.ArrayList;

/**
 * Database multithreading....
 */
public class DatabaseThread extends Thread{
    private Database database = new Database();
    String tableName;
    private TypeOfQuery typeOfQuery;
    private String[] columnNames = null;
    private String additional = null;
    private Object[][] condition = null;
    private Object[] values = null;
    private Object[][] columnNamesWithValues = null;
    private ArrayList<ArrayList<Object>> data;
    private boolean success = false;

    public ArrayList<ArrayList<Object>> getData() {
        return data;
    }

    public boolean getSuccess(){
        return success;
    }

    public DatabaseThread(String tableName, TypeOfQuery typeOfQuery) {
        this.tableName = tableName;
        this.typeOfQuery = typeOfQuery;
    }

    public void readTable(String[] columnNames, Object[][] condition, String additional) {
        readTable(columnNames, condition);
        this.additional = additional;
    }

    public void readTable(String[] columnNames, String additional) {
        readTable(columnNames);
        this.additional = additional;
    };

    public void readTable(String[] columnNames, Object[][] condition) {
        readTable(columnNames);
        this.condition = condition;
    }

    public void readTable(String[] columnNames) {
        this.columnNames = columnNames;
        this.success = false;
    }

    public void updateTable(Object[][] columnNamesWithValues, Object[][] condition){
        this.columnNamesWithValues = columnNamesWithValues;
        this.condition = condition;
    }

    public void insertTable(String[] columnNames, Object[] values){
        this.columnNames = columnNames;
        this.values = values;
    }

    public void deleteTable(Object[][] condition){
        this.condition = condition;
        this.success = false;
    }

    @Override
    public void run(){
        if(tableName != null) {
            database = new Database(tableName);
        }else{
            return;
        }
       switch(typeOfQuery){
           case SELECT:
               if(columnNames != null){
                   if(additional != null){
                       if(condition != null){
                           database.readTable(columnNames, condition, additional);
                       }else{
                           database.readTable(columnNames, additional);
                       }
                   } else if (condition != null) {
                       database.readTable(columnNames, condition);
                   }else{
                       database.readTable(columnNames);
                   }
               }
               data = database.getObjResult();
               success = true;
               break;
           case INSERT:
               if(values != null && columnNames != null){
                   success = database.insertTable(columnNames, values);
               }
               break;
           case UPDATE:
               if (columnNamesWithValues != null && condition != null) {
                   success = database.updateTable(columnNamesWithValues, condition);
               }
               break;
           case DELETE:
               if(condition!= null) {
                  success =  database.deleteRecord(condition);
               }
               break;
       }
    }

    public enum TypeOfQuery{
        SELECT,
        INSERT,
        UPDATE,
        DELETE
    }
}
