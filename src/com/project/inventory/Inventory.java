package com.project.inventory;

import java.util.ArrayList;

public class Inventory {
    ArrayList<Item> itemList = new ArrayList<>();
    int indexOfName, indexOfType;
    Database db = new Database("item");
    public Inventory() {
        ArrayList<String> database;
        db.readTable(Table.ITEM.getColumnName());
        database = db.getResult();
        String[] columnName = database.get(0).split(Database.spliter);
        for (int i = 0; i < columnName.length; i++) {
            for (int j = 0; j < Table.ITEM.getColumnName().length; j++) {
                if(columnName[i].equals(Table.ITEM.getSpecifiedColumn("name"))){
                    indexOfName = i;
                }else if(columnName[i].equals(Table.ITEM.getSpecifiedColumn("type"))){
                    indexOfType = j;
                }
            }
        }
        for (int i = 1; i < database.size(); i++) {
            String[] value =  database.get(i).split(Database.spliter);
            if(value[indexOfType].equals("Frozen"))
                itemList.add(new FrozenItem(value[indexOfName]));
            else if (value[indexOfType].equals("Dry")) {
                itemList.add(new DryItem(value[indexOfName]));
            }else{
                itemList.add(new Item(value[indexOfName]));
            }
        }
    }

    public void createInventory(){
        
    }
}
