package com.project.inventory.dao;

import java.util.ArrayList;
import com.project.inventory.application.Item;

public interface InventoryDAO {

    ArrayList<Item> getAllInventory();

    void updateInventory(Object[][] columnNameWIthValue, String itemId);

    ArrayList<String> selectById(String itemId);

    void addItem(Item item);

}
