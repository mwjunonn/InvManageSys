/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
        return db.getObjResult();
        
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
     
        if(supplyItem != null){
            
             String supplierId = supplyItem.getSupplierId();
             String itemId = supplyItem.getItemId();
             double shippingFee = supplyItem.getShippingFee();
             double cost = supplyItem.getCost();
             
             if (supplierId == null || itemId == null) {
                System.out.println("Error: Supplier ID or Item ID is null.");
                return;
            }
            
            String shippingFeeStr = String.valueOf(supplyItem.getShippingFee());
            String costStr = String.valueOf(supplyItem.getCost());
            String[] columns = {"supplier_id", "item_id", "shipping_fee", "cost"};
            String[] value = {supplyItem.getSupplierId(), supplyItem.getItemId(), shippingFeeStr, costStr};


            try {
                db.insertTable(columns, value);    
            } catch (Exception e) {
                System.out.println("Error inserting data: " + e.getMessage());
                e.printStackTrace();
            }
        
        }else{
                System.out.println("Error: SupplyItem object is null.");

        }
       
    }
    

}



