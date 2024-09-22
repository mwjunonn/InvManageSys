package com.project.inventory.application;


import com.project.inventory.dao.InventoryDAO;
import com.project.inventory.dao.InventoryDAOImpl;

import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Item implements Cloneable{
    private String itemId ="null", itemName, itemType, itemUnit;
    private double quantity, per_unit;


    public Item(){
    }

    public Item(String itemName, String itemType, String itemUnit, double per_unit, double quantity) {
        this.itemName =  checkItemName(itemName) ? itemName : "null";
        this.itemType = itemType;
        this.quantity = quantity;
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

    //Setter
    public void setQuantity(double quantity) {
        this.quantity = quantity;
        modifyColumn("quantity", quantity);
    }

    public void setItemType(String type){
        this.itemType = type;
        modifyColumn("item_type", type);
    }

    public void setItemUnit(double per_unit, String unit){
        this.per_unit = per_unit;
        this.itemUnit = unit;
        modifyColumn(new Object[][]{
                {"unit", unit},
                {"per_unit", per_unit},
        });
    }

    public void setItemName(String itemName) {
        if (checkItemName(itemName)) {
            this.itemName = itemName;
            modifyColumn("item_name", itemName);
        }
    }

    private boolean checkItemName(String itemName){
        return !(itemName.isEmpty() || itemName.equals("null"));
    }

    private void modifyColumn(String columnName ,Object value){
        modifyColumn(new Object[][]{{columnName, value}});
    }

    private void modifyColumn(Object[][] columnNameWIthValue){
        InventoryDAO inventoryDAO = new InventoryDAOImpl();
        inventoryDAO.updateInventory(columnNameWIthValue, itemId);
    }

    public void delete(){
        Inventory inventory = Inventory.getInstance();
        modifyColumn("status", 0);
        Thread.startVirtualThread(inventory);
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
        InventoryDAO inventoryDAO = new InventoryDAOImpl();
        return inventoryDAO.selectById(itemId).size() != 1; //if 1 is the column name only
    }

    private void create(){
        try{
        if (!checkDatabase()) {
            InventoryDAO inventoryDAO = new InventoryDAOImpl();
            inventoryDAO.addItem((Item)this.clone());
        }} catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            System.err.println(e.getMessage());
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
            return String.format("%s\t%s\t%.2f/%.2f%s", itemName, itemType, quantity, per_unit, itemUnit);
        else if(per_unit % 1 != 0) //Quantity is integer, per_unit is double
            return String.format("%s\t%s\t%d/%.2f%s", itemName, itemType, (int)quantity, per_unit, itemUnit);
        else if(quantity % 1 != 0) //Quantity is double, per_unit is integer
            return String.format("%s\t%s\t%.2f/%d%s", itemName, itemType, quantity, (int)per_unit, itemUnit);
        else //Two is integer
            return String.format("%s\t%s\t%d/%d%s", itemName, itemType, (int)quantity, (int)per_unit, itemUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Item item = (Item) o;
        return quantity == item.quantity && per_unit == item.per_unit && itemId.equals(item.itemId) && itemName.equals(item.itemName) && itemType.equals(item.itemType) && itemUnit.equals(item.itemUnit);
    }

    public enum ItemTypeConstant {
        POWDER("Powder"),
        DRINKS("Drinks"),
        FRUIT("Fruit"),
        INGREDIENT("Ingredient");

        private final String value;

        ItemTypeConstant(String value){
            this.value = value;
        }

        public String getValue(){
            return value;
        }
    }
}
