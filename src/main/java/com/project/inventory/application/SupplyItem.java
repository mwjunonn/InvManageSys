package com.project.inventory.application;
import com.project.inventory.dao.Database;

import java.util.ArrayList;

/**
 *
 * @author Lee
 */
public class SupplyItem {
    private static Database db = new Database("supplier_item");
    private String supplierId, itemId;
    private double shippingFee, cost;
    private String itemName;
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
    
    public SupplyItem(String supplierId, String itemId, String itemName, double shippingFee, double cost){
        this.supplierId = supplierId;
        this.itemId = itemId;
        this.itemName = itemName;
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
    public String getItemName(){
        return itemName;
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
    
    
    
    public String toString(){
        return String.format("| %-11s | %-10s | %-16.2f | %-12.2f |\n", supplierId, itemId, shippingFee, cost);
    }
    
    public String toString2(){
        return String.format(" %-11s | %-10s | %-20s | %-16.2f | %-12.2f |", supplierId, itemId, itemName, shippingFee, cost);
    }
    
   
    
    public ArrayList<SupplyItem> getAllSupplyItem(){
        String[] columns = {"supplier_id", "supplier_item.item_id", "inventory.item_name", "shipping_fee", "supplier_item.cost"};
        String additional = " JOIN inventory ON supplier_item.item_id = inventory.item_id";
        ArrayList<SupplyItem> supplyItems = new ArrayList<>();
        SupplyItem supplyItem;
        double shipping_fee = 0.00, cost2 = 0.00;
    
        db.readTable(columns, new Object[][]{}, additional);
        
        ArrayList<String> result = db.getResult();
        
        for(int i =1; i < result.size();i++){
            String[] supplyItemData = result.get(i).split(Database.delimiter);
            
            try{
                shipping_fee = Double.parseDouble(supplyItemData[3]);
            }catch(NumberFormatException ex){
                System.out.println("Error: Parsing Shipping Fee");
            }
            try{
                cost2 = Double.parseDouble(supplyItemData[4]);
            }catch(NumberFormatException ex){
                System.out.println("Error: Parsing Cost");
            }
            
            
            
            supplyItem = new SupplyItem(supplyItemData[0], supplyItemData[1], supplyItemData[2],shipping_fee, cost2);
            supplyItems.add(supplyItem);
            
        }

        supplyItemNum = result.size() -1; 
        
        
        return supplyItems;
        
    }
    
    public SupplyItem getAllSupplyItem(String supplierId, String itemId){
        String[] columns = {"supplier_id", "supplier_item.item_id", "shipping_fee", "supplier_item.cost"};
     
        Object[][] condition ={{"supplier_id", supplierId}, {"supplier_item.item_id", itemId}};
        double shipping_fee =0.00, cost2 = 0.00; 
        
        db.readTable(columns, condition);
        
        ArrayList<ArrayList<Object>> result = db.getObjResult();
        
        if(result.size() <= 1){
          
            return null;
        }
       
        ArrayList<Object> supplyItemData = result.get(1);
        
        try{
            shipping_fee = Double.parseDouble(supplyItemData.get(2).toString());
        }catch(NumberFormatException ex){
            System.out.println("Error: Parsing Shipping Fee");
        }
        try{
            cost2 = Double.parseDouble(supplyItemData.get(3).toString());
        }catch(NumberFormatException ex){
            System.out.println("Error: Parsing Cost");
        }
        
        
        return new SupplyItem(supplyItemData.get(0).toString(), supplyItemData.get(1).toString(), shipping_fee, cost2);
        
        
    }
    
    public boolean isSupplierExists(String supplierId) {
        Database db1 = new Database("supplier");

        String[] columns = {"supplier_id"};
        Object[][] condition = {{"supplier_id", supplierId}};

        if (db1.readTable(columns, condition, "")) {
            ArrayList<ArrayList<Object>> result = db1.getObjResult();
            return result != null && result.size() > 1;
        }
        return false;
    }
  
    public boolean writeData(SupplyItem supplyItem){
           
            double shippingFee = supplyItem.getShippingFee();
            double cost = supplyItem.getCost();
            
            if (isSupplierItemExists(supplyItem.getSupplierId(), supplyItem.getItemId())) {
                System.out.println("This item is already associated with the supplier. Please enter a different item.");
                return false;
            }else{
        
                String shippingFeeStr = String.valueOf(supplyItem.getShippingFee());
                String costStr = String.valueOf(supplyItem.getCost());
                String[] columns = {"supplier_id", "item_id", "shipping_fee", "cost"};
                String[] value = {supplyItem.getSupplierId(), supplyItem.getItemId(), shippingFeeStr, costStr};

               db.insertTable(columns, value);  
               return true;
            }
     
    }
    
    public boolean isSupplierItemExists(String supplierId, String itemId) {
        String[] columns = {"supplier_id", "item_id"};
        Object[][] condition = {
            {"supplier_id", supplierId},
            {"item_id", itemId}
        };

        db.readTable(columns, condition);
        ArrayList<ArrayList<Object>> result = db.getObjResult();

        return result.size() > 1;
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
    
    public void deleteSupplyItem(String supplierId, String itemId){
        Object[][] condition = {{"supplier_id", supplierId}, {"item_id", itemId}};
        
        if(db.deleteRecord(condition))
            System.out.println("The Data Has Been Deleted!");
        else
            System.out.println("Failed to Delete The Data");
    }
    
     public void deleteSupplyItem(String supplierId){
        Object[][] condition = {{"supplier_id", supplierId}};
        
        if(db.deleteRecord(condition))
            System.out.println("The Data Has Been Deleted!");
        else
            System.out.println("Failed to Delete The Data");
    }

}

