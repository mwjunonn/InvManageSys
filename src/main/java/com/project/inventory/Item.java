package com.project.inventory;



public class Item{
    private static Database db = new Database("inventory");
    private String itemId, itemName, itemType, itemUnit;
    private int quantity, per_unit;
    private double latestPrice;


    public Item(){
    }

    public Item(String itemId, String itemName, String itemType, int quantity, double latestPrice, int per_unit, String itemUnit) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.quantity = quantity;
        this.latestPrice = latestPrice;
        this.per_unit = per_unit;
        this.itemUnit = itemUnit;
    }

    private boolean checkDatabase(){
        db.readTable(new String[]{"item_id"}, new String[][]{{"item_id", itemId}});
        return db.getResult().size() != 1; //if 1 is the column name only
    }

    public boolean create() {
        if(checkDatabase()){
            return false;
        }else {
            db.insertTable(new String[]{"item_id", "item_name", "item_type", "quantity", "cost", "per_unit", "unit"},
                    new String[]{itemId, itemName, itemType, String.valueOf(quantity), String.valueOf(latestPrice), String.valueOf(per_unit), itemUnit});
            return true;
        }
    }

    public boolean updateQuantity(int quantity) {
        this.quantity = quantity;
        return modifyColumn("quantity", String.valueOf(quantity));
    }

    private boolean modifyColumn(String columnName ,Object value){
        db.updateTable(new Object[][]{
                {columnName, value}
        }, new Object[][]{
                {"item_id", itemId}
        });
        return true;
    }

    public boolean modifyType(String value){
        return modifyColumn("item_type", value);
    }

    private boolean modifyName(String value){
        itemName = value;
        return modifyColumn("item_name", value);
    }

    public boolean modifyUnit(String value){
        itemUnit = value;
        return modifyColumn("unit", value);
    }

    public boolean modifyPrice(double value){
        latestPrice = value;
        return modifyColumn("cost", value);
    }

    public String getItemName() {
        return itemName;
    }

    public boolean setItemName(String itemName) {
        if(itemName.isEmpty() || itemName == null){
            System.err.println("Item must have name");
            return false;
            }
        else {
            return modifyName(itemName);
        }
    }

    public int getItemQty(){
        return quantity;
    }

    public boolean delete(){
        return modifyColumn("status", 0);
    }

    @Override
    public String toString() {
        return String.format("%-30s\t%-10s\t%-10s\t%.02f", itemName, itemType, itemUnit, latestPrice);
    }
}
