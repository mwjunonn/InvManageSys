package com.project.inventory;
import java.util.ArrayList;

/**
 *
 * @author Lee
 */
public class SupplyItem {
    private static Database db = new Database("supplier_item");
    private String supplierId, itemId;
    private double shippingFee, cost;
    public static int supplyItemNum = 0;
    
    
    //constructor
    public SupplyItem(){
        
    }
    
    public SupplyItem(String supplierId, String itemId, double shippingFee, double cost ){
        this.supplierId = supplierId;
        this.itemId = itemId;
        this.shippingFee = shippingFee;
        this.cost = cost;
    }
    
    //getter
    public String getSupplierId(){
        return supplierId;
    }
    public String getItemId(){
        return itemId;
    }
    public double getShippingFee(){
        return shippingFee;
    }
    public double getCost(){
        return cost;
    }
    
    //setter
    public void setSupplierId(String supplierId){
        this.supplierId = supplierId;
    }
    public void setItemId(String itemId){
        this.itemId = itemId;
    }
    public void setShippingFee(double shippingFee){
        this.shippingFee = shippingFee;
    }
    public void setCost(double cost){
        this.cost = cost;
    }
    
    
    //setter
    
   
    
    public ArrayList<ArrayList<Object>> getAllSupplyItem(){
        String[] columns = {"supplier_id", "supplier_item.item_id", "inventory.item_name", "shipping_fee", "supplier_item.cost"};
        String additional = " JOIN inventory ON supplier_item.item_id = inventory.item_id";

        
        db.readTable(columns, new Object[][]{}, additional);
        
        ArrayList<ArrayList<Object>> result = db.getObjResult();
        
        

        supplyItemNum = result.size(); 
        
        
        return result;
        
    }
    
    public String[][] getAllSupplyItem(String supplierId, String itemId){
        String[] columns = {"supplier_id", "supplier_item.item_id", "shipping_fee", "supplier_item.cost"};
     
        Object[][] condition ={{"supplier_id", supplierId}, {"supplier_item.item_id", itemId}};
        
        db.readTable(columns, condition);
        
        ArrayList<ArrayList<Object>> result = db.getObjResult();
        
        String[][] supplyItemInfo = new String[result.size()][columns.length];
        
        for(int i = 0; i < result.size(); i++){
            for(int j = 0; j < result.get(i).size();j++){
                supplyItemInfo[i][j] = result.get(i).get(j).toString();
            }
        }
        return supplyItemInfo;
        
    }
    
    public boolean isSupplierExists(String supplierId) {
        Database db1 = new Database("supplier_item");

        String[] columns = {"supplier_id"};
        Object[][] condition = {{"supplier_id", supplierId}};

        if (db1.readTable(columns, condition, "")) {
            ArrayList<ArrayList<Object>> result = db1.getObjResult();
            return result != null && !result.isEmpty();
        }
        return false;
    }
    
    public boolean isItemExists(String itemId) {
        Database db1 = new Database("inventory");
        String[] columns = {"item_id"};
        Object[][] condition = {{"item_id", itemId}};

        if (db.readTable(columns, condition, "")) {
            ArrayList<ArrayList<Object>> result = db.getObjResult();
            return result != null && !result.isEmpty();
        }
        return false;
    }
    
    public double getItemCost(String itemID){
        Database db1 = new Database("inventory");
        String[] columns = {"cost"};
        Object[][] condition = {{"item_id", itemID}};
        double cost = 0.00;
        
        
        db1.readTable(columns, condition);
        
        ArrayList<String> costResult = db1.getResult();
        
        
        
         if (costResult != null && !costResult.isEmpty()) {
            try {
                String costStr = costResult.get(1).replace(" |", "").trim();
            
           
                cost = Double.parseDouble(costStr);
            } catch (NumberFormatException ex) {
                System.out.println("Error: Cannot Read Cost Value! - " + ex.getMessage());
            }
        } else {
            System.out.println("Error: No cost data found for item ID: " + itemID);
        }
        
        return cost;
        
    }
    
    public void writeData(SupplyItem supplyItem){
         
            String supplierId = supplyItem.getSupplierId();
            String itemId = supplyItem.getItemId();
            double shippingFee = supplyItem.getShippingFee();
            double cost = supplyItem.getCost();
        
            String shippingFeeStr = String.valueOf(supplyItem.getShippingFee());
            String costStr = String.valueOf(supplyItem.getCost());
            String[] columns = {"supplier_id", "item_id", "shipping_fee", "cost"};
            String[] value = {supplyItem.getSupplierId(), supplyItem.getItemId(), shippingFeeStr, costStr};

           db.insertTable(columns, value);    
     
    }
    
    public void updateData(String columnName, String supplierId, String itemId, String temp){
        Object[][] values = {{columnName, temp}};
        
        Object[][] condition  = {{"supplier_id", supplierId},{"item_id", itemId}};
        
        if(db.updateTable(values, condition)){
            System.out.println("New Data Updated");
        }
        else 
            System.out.println("Failed to Update New Data!");
    }

}

