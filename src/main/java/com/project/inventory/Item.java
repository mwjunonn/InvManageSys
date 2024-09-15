package com.project.inventory;



public class Item{
    private static Database db = new Database("inventory");
    private String itemId ="null", itemName, itemType, itemUnit;
    private int quantity, per_unit;
    private double latestPrice;


    public Item(){
    }

    public Item(String itemName, String itemType, String itemUnit, double latestPrice, int per_unit, int quantity) {
        this.itemName =  checkItemName(itemName) ? itemName : "null";
        this.itemType = itemType;
        this.quantity = quantity;
        this.latestPrice = latestPrice;
        this.per_unit = per_unit;
        this.itemUnit = itemUnit;
        this.itemId = generateID();
        create();
    }

    public Item(String itemId, String itemName, String itemType, int quantity, double latestPrice, int per_unit, String itemUnit) {
        this.itemId = itemId;
        this.itemName =  checkItemName(itemName) ? itemName : "null";
        this.itemType = itemType;
        this.quantity = quantity;
        this.latestPrice = latestPrice;
        this.per_unit = per_unit;
        this.itemUnit = itemUnit;
        create();
    }

    //Getter
    public String getItemId() {
        return itemId;
    }

    public int getQuantity(){
        return quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public double getLatestPrice() {
        return latestPrice;
    }

    public int getPer_unit() {
        return per_unit;
    }

    //Setter
    public boolean setQuantity(int quantity) {
        this.quantity = quantity;
        return modifyColumn("quantity", quantity);
    }

    public boolean setItemType(String type){
        this.itemType = type;
        return modifyColumn("item_type", type);
    }

    public boolean setItemUnit(String unit){
        this.itemUnit = unit;
        return modifyColumn("unit", unit);
    }

    public boolean setLatestPrice(double price){
        latestPrice = price;
        return modifyColumn("cost", price);
    }

    public boolean setItemName(String itemName) {
        if (checkItemName(itemName)) {
            this.itemName = itemName;
            return modifyColumn("item_name", itemName);
        }else
            return false;
    }

    public boolean setPer_unit(int per_unit) {
        this.per_unit = per_unit;
        return modifyColumn("cost", per_unit);
    }

    private boolean checkItemName(String itemName){
        return !(itemName.isEmpty() || itemName.equals("null"));
    }

    private boolean modifyColumn(String columnName ,Object value){
        db.updateTable(new Object[][]{
                {columnName, value}
        }, new Object[][]{
                {"item_id", itemId}
        });
        return true;
    }

    public boolean delete(){
        if(modifyColumn("status", 0)){
            Inventory.restartInventory();
            return true;
        }else{
            return false;
        }
    }

    public String generateID(){
        StringBuilder id = new StringBuilder("I");
        do {
            id.append(Math.random() * 10000);
        }while (Inventory.checkIdUnique(id.toString()));
        return id.toString();
    }

    private boolean checkDatabase(){
        db.readTable(new String[]{"item_id"}, new String[][]{{"item_id", itemId}});
        return db.getResult().size() != 1; //if 1 is the column name only
    }

    private boolean create() {
        if(checkDatabase()){
            return false;
        }else {
            db.insertTable(new String[]{"item_id", "item_name", "item_type", "quantity", "cost", "per_unit", "unit"},
                    new String[]{itemId, itemName, itemType, String.valueOf(quantity), String.valueOf(latestPrice), String.valueOf(per_unit), itemUnit});
            return true;
        }
    }

    @Override
    public String toString() {
        return String.format("%s\t%s\tRM%.2f\t%d/%d%s", itemName, itemType, latestPrice,quantity,per_unit, itemUnit);
    }
}
