package com.project.inventory;

import java.util.*;

/**
 * A ItemBuilder, one and only one.
 */
public class Inventory implements Runnable{
    private static ArrayList<Item> itemList = new ArrayList<>();
    private static Database db = new Database("inventory");
    private static Inventory inventory = new Inventory();

    private Inventory() {
    }

    public static ArrayList<Object> getItemListWithColumns() {
        ArrayList<Object> obj = (ArrayList<Object>)itemList.clone();
        obj.addFirst(new String[]{
               "Item Name",
                "Item Type",
                "Latest Price",
                "Item quantity"
        });
        return obj;
    }

    public static ArrayList<Item> getItemList() {
        return (ArrayList<Item>)itemList.clone();
    }

    public static void restartInventory() {
        HashMap<String, Integer> attributeIndex = new HashMap<>();
        db.readTable(new String[]{Database.all});
        ArrayList<ArrayList<Object>> database = db.getObjResult();
        for (int i = 0; i < database.getFirst().size(); i++) {
            attributeIndex.put((String) database.getFirst().get(i), i);
        }
        for (int i = 1; i < database.size(); i++) {
            ArrayList<Object> row = database.get(i);
            if(!row.get(attributeIndex.get("status")).equals(true))
                continue;
            itemList.add(new Item((String) row.get(attributeIndex.get("item_id")),
                    (String) row.get(attributeIndex.get("item_name")),
                    (String) row.get(attributeIndex.get("item_type")),
                    (Double) row.get(attributeIndex.get("quantity")),
                    (Double) row.get(attributeIndex.get("cost")),
                    (Double) row.get(attributeIndex.get("per_unit")),
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



    public static boolean addInventory(Item item) {
        if(item.getItemId().equals("null") || item.getItemName().isEmpty()){
            itemList.add(item);
            return true;
        }else{
            return false;
        }
    }

    public static double adjustQuantity(int index, double newPerUnit){
        Item item = itemList.get(index);
        return item.getQuantity() / (newPerUnit / item.getPer_unit());
    }

    public static Item getItem(int index) {
        if (itemList.isEmpty()) {
            restartInventory();
            return getItem(index);
        } else if (itemList.size() < index + 1)
            return new Item();
        else
            return itemList.get(index);
    }

    public static void deleteItem(Item item) {
        item.delete();
        itemList.remove(item);
    }

    public static boolean checkIdUnique(String id) {
        for(Item i : itemList){
            if(i.getItemId().equals(id)){
                return false;
            }
        }
        return true;
    }

    public static void closeInventory(){
        itemList.clear();
    }

    public static Inventory getInstance(){
        return inventory;
    }

    @Override
    public void run() {
        restartInventory();
    }
}

