package com.project.inventory;

import java.util.*;

/**
 * A ItemBuilder, one and only one.
 */
public class Inventory implements Runnable{
    private ArrayList<Item> itemList = new ArrayList<>();
    private HashMap<String, Integer> itemNameWIthIndex= new HashMap<>();
    private HashMap<String, Integer> itemIdWithIndex = new HashMap<>();
    private Database db = new Database("inventory");
    private static Inventory inventory = new Inventory();

    private Inventory() {
        run();
    }

    public ArrayList<Object> getItemListWithColumns() {
        ArrayList<Object> obj = (ArrayList<Object>)itemList.clone();
        obj.addFirst(new String[]{
               "Item Name",
                "Item Type",
                "Latest Price",
                "Item quantity"
        });
        return obj;
    }

    public ArrayList<Item> getItemList() {
        return (ArrayList<Item>)itemList.clone();
    }

    public void restartInventory() {
        closeInventory();
        HashMap<String, Integer> attributeIndex = new HashMap<>();
        db.readTable(new String[]{Database.all}, new Object[][]{{"status", Boolean.TRUE}});
        ArrayList<ArrayList<Object>> database = db.getObjResult();
        for (int i = 0; i < database.getFirst().size(); i++) {
            attributeIndex.put((String) database.getFirst().get(i), i);
        }
        for (int i = 1; i < database.size(); i++) {
            ArrayList<Object> row = database.get(i);
            itemNameWIthIndex.put((String) row.get(attributeIndex.get("item_name")), i - 1);
            itemIdWithIndex.put((String) row.get(attributeIndex.get("item_id")), i - 1);
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



    public boolean addInventory(Item item) {
        if (item != null) {
            if (!(item.getItemId().equals("null") || item.getItemName().isEmpty())) {
                itemList.add(item);
                itemNameWIthIndex.put(item.getItemName(), itemList.indexOf(item));
                return true;
            }
        }
        return false;
    }

    public double adjustQuantity(int index, double newPerUnit){
        Item item = itemList.get(index);
        return item.getQuantity() / (newPerUnit / item.getPer_unit());
    }

    public Item getItem(int index) {
        if (itemList.isEmpty()) {
            restartInventory();
            return getItem(index);
        } else if (itemList.size() < index + 1)
            return new Item();
        else
            return itemList.get(index);
    }

    public Item getItem(String itemName) {
        Integer index = itemNameWIthIndex.get(itemName);
        if(itemName != null && index != null) {
            if(! itemName.equalsIgnoreCase("null") && ! itemList.isEmpty())
                return getItem(index);
        }
        return new Item();
    }

    public void deleteItem(Item item) {
        item.delete();
        itemList.remove(item);
    }

    public boolean checkIdUnique(String id) {
        for(Item i : itemList){
            if(i.getItemId().equals(id)){
                return false;
            }
        }
        return true;
    }

    public void closeInventory(){
        itemList.clear();
    }

    public boolean checkNameUnique(String itemName){
        if(itemNameWIthIndex.containsKey(itemName)) //If this item name is contain in the HashMap, means it is not unique
            return false;
        for(String str : itemNameWIthIndex.keySet()){
            if(itemName.equalsIgnoreCase(str)) {
                itemNameWIthIndex.put(itemName, itemNameWIthIndex.get(str)); //add a different case to the hashmap but point to the same
                return false;
            }
        }
        return true;
    }


    public static Inventory getInstance(){
        return inventory;
    }


    @Override
    public void run() {
        restartInventory();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Item i : itemList){
            sb.append(i).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Inventory inventory = (Inventory) o;
        return itemList.equals(inventory.itemList);
    }

}

