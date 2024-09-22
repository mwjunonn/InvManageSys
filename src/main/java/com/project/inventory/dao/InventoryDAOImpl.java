package com.project.inventory.dao;

import com.project.inventory.application.Item;

import java.util.ArrayList;
import java.util.HashMap;

public class InventoryDAOImpl implements InventoryDAO {

    public ArrayList<Item> getAllInventory(Boolean bool) {
        ArrayList<Item> items = new ArrayList<>();
        Database db = new Database("inventory");
        if(bool != null){
            db.readTable(new String[]{"item_id", "item_name", "item_type", "quantity", "cost", "per_unit", "unit"}, new Object[][]{{"status", bool}});
        }else{
            db.readTable(new String[]{"item_id", "item_name", "item_type", "quantity", "cost", "per_unit", "unit"});
        }
        ArrayList<ArrayList<Object>> database = db.getObjResult();
        for (int i = 1; i < database.size(); i++) {
            ArrayList<Object> row = database.get(i);
            items.add(new Item((String) row.getFirst(),
                    (String) row.get(1),
                    (String) row.get(2),
                    (Double) row.get(3),
                    (Double) row.get(4),
                    (Double) row.get(5),
                    (String) row.get(6)
            ));
        }
        return items;
    }

    @Override
    public ArrayList<Item> getAllInventory () {
        return getAllInventory(null);
    }

    @Override
    public void updateInventory(Object[][] columnNameWIthValue, String itemId) {
        DatabaseThread dbb = new DatabaseThread("inventory", DatabaseThread.TypeOfQuery.UPDATE);
        dbb.updateTable(
                columnNameWIthValue,
                new Object[][]{
                        {"item_id", itemId}
                });
        dbb.start();
    }

    @Override
    public ArrayList<String> selectById(String itemId) {
        Database db = new Database("inventory");
        db.readTable(new String[]{Database.all}, new String[][]{{"item_id", itemId}});
        return db.getResult();
    }

    @Override
    public void addItem(Item item) {
        DatabaseThread dbb = new DatabaseThread("inventory", DatabaseThread.TypeOfQuery.INSERT);
        dbb.insertTable(new String[]{"item_id", "item_name", "item_type", "quantity", "per_unit", "unit"},
                new Object[]{item.getItemId(), item.getItemName(), item.getItemType(), item.getQuantity(), item.getPer_unit(), item.getUnit()});
        dbb.start();
    }
}
