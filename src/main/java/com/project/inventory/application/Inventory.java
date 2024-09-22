package com.project.inventory.application;

import com.project.inventory.dao.InventoryDAOImpl;

import java.util.*;

/**
 * A ItemBuilder, one and only one.
 */
public class Inventory implements Runnable{
    private ArrayList<Item> itemList = new ArrayList<>();
    private HashMap<String, Integer> itemNameWIthIndex= new HashMap<>();
    private HashMap<String, Integer> itemIdWithIndex = new HashMap<>();
    private static Inventory inventory = new Inventory();

    private Inventory() {
        restartInventory();
    }

    public ArrayList<Object> getItemListWithColumns() {
        ArrayList<Object> obj = (ArrayList<Object>)itemList.clone();
        obj.addFirst(new String[]{
               "Item Name",
                "Item Type",
                "Item quantity"
        });
        return obj;
    }

    public ArrayList<Item> getItemList() {
        return (ArrayList<Item>)itemList.clone();
    }

    public void restartInventory() {
        closeInventory();
        InventoryDAOImpl inventoryDAO = new InventoryDAOImpl();
        itemList = inventoryDAO.getAllInventory(Boolean.TRUE);
        for (int i = 0; i < itemList.size(); i++) {
            itemNameWIthIndex.put(itemList.get(i).getItemName(), i);
            itemIdWithIndex.put(itemList.get(i).getItemId(), i);
        }
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

    public Item getItem(String itemId) {
        Integer index = itemIdWithIndex.get(itemId);
        if(itemId != null && index != null) {
            if(! itemId.equalsIgnoreCase("null") && ! itemList.isEmpty())
                return getItem(index);
        }
        return new Item();
    }

    public void deleteItem(Item item) {
        item.delete();
        itemList.remove(item);
    }

    public boolean checkIdUnique(String id) {
       return !itemIdWithIndex.containsKey(id);
    }

    public void closeInventory(){
        itemList.clear();
    }

    public Item checkNameUnique(String itemName){
        if(itemNameWIthIndex.containsKey(itemName)) //If this item name is contain in the HashMap, means it is not unique
            return getItem(itemNameWIthIndex.get(itemName));
        for(String str : itemNameWIthIndex.keySet()){
            if(itemName.equalsIgnoreCase(str)) {
                itemNameWIthIndex.put(itemName, itemNameWIthIndex.get(str)); //add a different case to the hashmap but point to the same
                return getItem(itemNameWIthIndex.get(str));
            }
        }
        return null;
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

