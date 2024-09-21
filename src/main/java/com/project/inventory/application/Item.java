package com.project.inventory.application;


import com.project.inventory.dao.Database;
import com.project.inventory.dao.DatabaseThread;
import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Item{
    private String itemId ="null", itemName, itemType, itemUnit;
    private static final Database db = new Database("inventory");
    private double latestPrice, quantity, per_unit;


    public Item(){
    }

    public Item(String itemName, String itemType, String itemUnit, double latestPrice, double per_unit, double quantity) {
        this.itemName =  checkItemName(itemName) ? itemName : "null";
        this.itemType = itemType;
        this.quantity = quantity;
        this.latestPrice = latestPrice;
        this.per_unit = per_unit;
        this.itemUnit = itemUnit;
        this.itemId = generateID();
        create();
    }

    public Item(String itemId, String itemName, String itemType, double quantity, double latestPrice, double per_unit, String itemUnit) {
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

    public double getQuantity(){
        return quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public double getPer_unit(){
        return per_unit;
    }

    public String getUnit(){
        return itemUnit;
    }
    public String getItemUnit() {
        if(per_unit % 1 != 0) //Per_unit is double
            return String.format("%.2f%s", per_unit, itemUnit);
        else //Per_unit is integer
            return String.format("%d%s",(int)per_unit, itemUnit);
    }

    public double getLatestPrice() {
        return latestPrice;
    }

    //Setter
    public boolean setQuantity(double quantity) {
        this.quantity = quantity;
        return modifyColumn("quantity", quantity);
    }

    public boolean setItemType(String type){
        this.itemType = type;
        return modifyColumn("item_type", type);
    }

    public boolean setItemUnit(double per_unit, String unit){
        this.per_unit = per_unit;
        this.itemUnit = unit;
        return modifyColumn(new Object[][]{
                {"unit", unit},
                {"per_unit", per_unit},
        });
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

    private boolean checkItemName(String itemName){
        return !(itemName.isEmpty() || itemName.equals("null"));
    }

    private boolean modifyColumn(String columnName ,Object value){
        return modifyColumn(new Object[][]{{columnName, value}});
    }

    private boolean modifyColumn(Object[][] columnNameWIthValue){
        DatabaseThread dbb = new DatabaseThread("inventory", DatabaseThread.TypeOfQuery.UPDATE);
        dbb.updateTable(
                columnNameWIthValue,
                new Object[][]{
                {"item_id", itemId}
        });
        dbb.start();
        return true;
    }

    public boolean delete(){
        Inventory inventory = Inventory.getInstance();
        if(modifyColumn("status", 0)){
            inventory.restartInventory();
            return true;
        }else{
            return false;
        }
    }

    public String generateID(){
        Inventory inventory = Inventory.getInstance();
        StringBuilder id = new StringBuilder("I");
        do {
            id.append(Math.round(Math.random() * 10000));
        }while (!inventory.checkIdUnique(id.toString()));
        return id.toString();
    }

    private boolean checkDatabase(){
        db.readTable(new String[]{"item_id"}, new String[][]{{"item_id", itemId}});
        return db.getResult().size() != 1; //if 1 is the column name only
    }

    private void create() {
        if (!checkDatabase()) {
            DatabaseThread dbb = new DatabaseThread("inventory", DatabaseThread.TypeOfQuery.INSERT);
            dbb.insertTable(new String[]{"item_id", "item_name", "item_type", "quantity", "cost", "per_unit", "unit"},
                    new String[]{itemId, itemName, itemType, String.valueOf(quantity), String.valueOf(latestPrice), String.valueOf(per_unit), itemUnit});
            dbb.start();
        }
    }

    public static int indexOfSplit(String unit){
        if (unit.matches("^\\d+(?:\\.\\d+)?[a-zA-Z]+$")) {//Means matching 1.2kg or 12kg
            Matcher matcher = Pattern.compile("\\d+(?:\\.\\d+)?").matcher(unit); //Number part
            if (matcher.find())//Find if there have number part, should have.
                return matcher.end();
            else
                throw new InputMismatchException("Something error when finding the number part");
        }else{
            return -1;
        }
    }

    @Override
    public String toString() {
        if(quantity % 1 != 0 && per_unit % 1 != 0) //Two is double
            return String.format("%s\t%s\tRM%.2f\t%.2f/%.2f%s", itemName, itemType, latestPrice,quantity, per_unit, itemUnit);
        else if(per_unit % 1 != 0) //Quantity is integer, per_unit is double
            return String.format("%s\t%s\tRM%.2f\t%d/%.2f%s", itemName, itemType, latestPrice, (int)quantity, per_unit, itemUnit);
        else if(quantity % 1 != 0) //Quantity is double, per_unit is integer
            return String.format("%s\t%s\tRM%.2f\t%.2f/%d%s", itemName, itemType,latestPrice, quantity, (int)per_unit, itemUnit);
        else //Two is integer
            return String.format("%s\t%s\tRM%.2f\t%d/%d%s", itemName, itemType,latestPrice, (int)quantity, (int)per_unit, itemUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Item item = (Item) o;
        return latestPrice == item.latestPrice && quantity == item.quantity && per_unit == item.per_unit && itemId.equals(item.itemId) && itemName.equals(item.itemName) && itemType.equals(item.itemType) && itemUnit.equals(item.itemUnit);
    }

    public enum itemTypeConstant{
        POWDER("Powder"),
        DRINKS("Drinks"),
        FRUIT("Fruit"),
        INGREDIENT("Ingredient");

        private final String value;

        itemTypeConstant(String value){
            this.value = value;
        }

        public String getValue(){
            return value;
        }
    }
}
