package com.project.inventory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Inventory {
    //ArrayList<Item> itemList = new ArrayList<>();
    ArrayList<String> itemID = new ArrayList<>();
    HashMap<String, Integer> index = new HashMap<>();
    int indexOfName, indexOfType;
    Database db = new Database("item");
    public Inventory() {
        ArrayList<String> database;
        db.readTable(Table.ITEM.getColumnName());
        database = db.getResult();
        String[] columnName = database.get(0).split(Database.spliter);
        for (int i = 0; i < columnName.length; i++) {
            for (int j = 0; j < Table.ITEM.getColumnName().length; j++) {
                if(columnName[i].equals(Table.ITEM.getColumnName()[j])){
                    index.put(Table.ITEM.getColumnName()[j], i);
                }
            }
        }
        int temp =  index.get(Table.ITEM.getSpecifiedColumn("id"));
        for (int i = 1; i < database.size(); i++) {
            String[] row = database.get(i).split(Database.spliter);
            itemID.add(row[temp]);
        }
    }

    public void createInventory(){
        
    }
}
