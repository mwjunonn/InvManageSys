package com.project.inventory;


public class Item{
    private String itemName, itemType;
    Database db = new Database(Table.ITEM.getTableName());
    public Item() {
        itemName = "";
    }

    public Item(String itemName){
        this.itemName = itemName;
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


}
