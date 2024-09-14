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
        HashMap<String, Integer> attributeIndex = new HashMap<>();
        db.readTable(new String[]{Database.all});
        ArrayList<ArrayList<Object>> database = db.getObjResult();
        for (int i = 0; i < database.getFirst().size(); i++) {
            attributeIndex.put((String) database.getFirst().get(i), i);
        }
        for (int i = 1; i < database.size(); i++) {
            ArrayList<Object> row = database.get(i);
            if(!row.get(attributeIndex.get("status")).equals(1))
                continue;
            itemList.add(new Item((String) row.get(attributeIndex.get("item_id")),
                    (String) row.get(attributeIndex.get("item_name")),
                    (String) row.get(attributeIndex.get("item_type")),
                    (Integer) row.get(attributeIndex.get("quantity")),
                    (Double) row.get(attributeIndex.get("cost")),
                    (Integer) row.get(attributeIndex.get("per_unit")),
                    (String) row.get(attributeIndex.get("unit"))
            ));
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

