package com.project.inventory;

import java.util.ArrayList;
import java.util.HashMap;

public class Inventory {
    public static ArrayList<Item> itemList = new ArrayList<>();
    private static Database db = new Database("inventory");

    public Inventory() {
        startInventory();
    }

    public void printInventory() {
        String[][] column = {
                {"-30", "Item Name"},
                { "-10", "Item Type"},
                { "-10", "Item quantity"},
                {"", "Latest cost"}
        };
        for(String[] str1 : column){
            System.out.printf("%" + str1[0] +"s\t", str1[1]);

        }
        System.out.println();
        for (Item item : itemList) {
            System.out.println(item);
        }
    }

    public static void startInventory() {
        ArrayList<String> database;
        HashMap<String, Integer> attributeIndex = new HashMap<>();
        db.readTable(Table.ITEM.getColumnName());
        database = db.getResult();
        String[] columnName = database.get(0).split(Database.delimiter);
        for (int i = 0; i < columnName.length; i++) {
            for (int j = 0; j < Table.ITEM.getColumnName().length; j++) {
                if (columnName[i].equals(columnName[j])) {
                    attributeIndex.put(columnName[j], i);
                    break;
                }
            }
        }
        
        for (int i = 1; i < database.size(); i++) {
            String[] value = database.get(i).split(Database.delimiter);
            if (value[attributeIndex.get(Table.ITEM.getSpecifiedColumn("type"))].equals("Frozen"))
                itemList.add(new FrozenItem(value[attributeIndex.get(Table.ITEM.getSpecifiedColumn("name"))],
                        value[attributeIndex.get(Table.ITEM.getSpecifiedColumn("type"))],
                        value[attributeIndex.get(Table.ITEM.getSpecifiedColumn("quantity"))],
                        Double.parseDouble(value[attributeIndex.get(Table.ITEM.getSpecifiedColumn("cost"))])));
            else if (value[attributeIndex.get(Table.ITEM.getSpecifiedColumn("type"))].equals("Dry")) {
                itemList.add(new DryItem(value[attributeIndex.get(Table.ITEM.getSpecifiedColumn("name"))],
                        value[attributeIndex.get(Table.ITEM.getSpecifiedColumn("type"))],
                        value[attributeIndex.get(Table.ITEM.getSpecifiedColumn("quantity"))],
                        Double.parseDouble(value[attributeIndex.get(Table.ITEM.getSpecifiedColumn("cost"))])));
            } else {
                itemList.add(new Item(value[attributeIndex.get(Table.ITEM.getSpecifiedColumn("name"))],
                        value[attributeIndex.get(Table.ITEM.getSpecifiedColumn("type"))],
                        value[attributeIndex.get(Table.ITEM.getSpecifiedColumn("quantity"))],
                        Double.parseDouble(value[attributeIndex.get(Table.ITEM.getSpecifiedColumn("cost"))])));
            }
        }

//        for (int i = 1; i < database.size(); i++) {
//            String[] value =  database.get(i).split(Database.delimiter);
//            if(value[indexOfType].equals("Frozen"))
//                itemList.add(new FrozenItem(value[indexOfName]));
//            else if (value[indexOfType].equals("Dry")) {
//                itemList.add(new DryItem(value[indexOfName]));
//            }else{
//                itemList.add(new Item(value[indexOfName]));
//            }
//        }
    }

    public void addInventory(Item item) {
        itemList.add(item);
    }

    public Item getItem(int index) {
        if (itemList.isEmpty()) {
            startInventory();
            return getItem(index);
        } else if (itemList.size() < index + 1)
            return null;
        else
            return itemList.get(index);
    }

    public Item getItem(String name) {
        if (itemList.isEmpty()) {
            startInventory();
            return getItem(name);
        }
        for (Item item : itemList) {
            if(item.getItemName().equals(name))
                return item;
        }
        return null;
    }
}

