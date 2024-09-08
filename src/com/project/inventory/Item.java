package com.project.inventory;


public class Item{
    private String itemName, itemType, itemUnit = "1kg";
    private double latestPrice;
    private static Database db = new Database(Table.ITEM.getTableName());
    private boolean databaseExists = true;
    public Item() {
        this("", null);
    }

    public Item(String itemName, String itemType, String itemUnit, double latestPrice) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.itemUnit = itemUnit;
        this.latestPrice = latestPrice;
    }

    public Item(String itemName, String itemType) {
        this(itemName, itemType, "1kg", 0.0);
        databaseExists =  checkDatabase();
    }

    public Item(String itemName) {
        this(itemName, null);
    }

    public boolean isDatabaseExists() {
        return databaseExists;
    }

    private boolean checkDatabase(){
        db.readTable(Table.ITEM.getColumnName(), new String[][]{{Table.ITEM.getSpecifiedColumn("name"), itemName}});
        return db.getResult().size() != 1; //if 1 is the column name only
    }

    public boolean create() {
        if(checkDatabase()){
            return false;
        }else {
            db.insertTable(new String[]{Table.ITEM.getSpecifiedColumn("name")}, new String[]{itemName});
            return true;
        }
    }

    public boolean updateQuantity() {
        return false;
    }

    private boolean modifyColumn(String columnName ,String value){
        db.updateTable(new String[][]{
                {columnName, value}
        }, new String[][]{
                {Table.ITEM.getSpecifiedColumn("name"), this.itemName},
                {Table.ITEM.getSpecifiedColumn("type"), this.itemType},
                {Table.ITEM.getSpecifiedColumn("unit"), this.itemUnit},
                {Table.ITEM.getSpecifiedColumn("price"), String.format("%.02f", this.latestPrice)}
        });
        this.itemType = itemType;
        return true;
    }

    public boolean modifyType(String value){
        return modifyColumn(Table.ITEM.getSpecifiedColumn("type"), value);
    }

    public boolean modifyName(String value){
        return modifyColumn(Table.ITEM.getSpecifiedColumn("name"), value);
    }

    public boolean modifyUnit(String value){
        return modifyColumn(Table.ITEM.getSpecifiedColumn("unit"), value);
    }

    public boolean modifyPrice(String value){
        return modifyColumn(Table.ITEM.getSpecifiedColumn("price"), value);
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        if(itemName.isEmpty() || itemName == null)
            throw new IllegalArgumentException("Item must have name");
        else
            this.itemName = itemName;
    }

    public int getItemQty(){
        db.readTable(new String[]{"*"}, new String[][]{{Table.ITEM.getSpecifiedColumn("name"), itemName}, {"status", "Active"}});
        return db.getResult().size() - 1;
    }

    @Override
    public String toString() {
        return String.format("%-30s\t%-10s\t%-10s\t%.02f", itemName, itemType, itemUnit, latestPrice);
    }
}
