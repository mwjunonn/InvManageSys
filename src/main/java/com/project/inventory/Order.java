/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.inventory;
import java.util.Scanner;
import java.util.ArrayList;
/**
 *
 * @author Mak
 */
public class Order {
    private String itemId;
    private String orderNo;
    private int quantity;
    private String supplierId;
    private double totalCost;
    
    private static Database db = new Database("order_item");
    private Scanner scan = new Scanner(System.in);

    public Order(){
    }
   
    public Order(String itemId, String orderNo, int quantity, String supplierId, double totalCost) {
        this.itemId = itemId;
        this.orderNo = orderNo;
        this.quantity = quantity;
        this.supplierId = supplierId;
        this.totalCost = totalCost;
    }

    // Create a new order record
    public boolean createOrder(String itemId, String orderNo, int quantity, String supplierId, double totalCost) {
        String[] columns ={"item_id", "order_no", "quantity", "supplier_id", "total_cost"}; 
        Object[] values = {itemId, orderNo, quantity, supplierId, totalCost};
        if(!db.insertTable(columns, values)){
            return false;
        }else{
            return true;
        }
    }
   
    public ArrayList<Order> getAllOrders() {
            ArrayList<Order> orders = new ArrayList<>();
            String[] columns = {"item_id", "order_no", "quantity", "supplier_id", "total_cost"};

            if (db.readTable(columns)) {
                ArrayList<ArrayList<Object>> results = db.getObjResult();
                results.remove(0); 

                for (ArrayList<Object> row : results) {
                    Order order = new Order();
                    order.setItemId(row.get(0).toString());
                    order.setOrderNo(row.get(1).toString());
                    order.setQuantity(Integer.parseInt(row.get(2).toString()));
                    order.setSupplierId(row.get(3).toString());
                    order.setTotalCost(Double.parseDouble(row.get(4).toString()));
                    orders.add(order);
                }
            }
            return orders;
        }
    
    // Read with condition (e.g., by order_no)
    public ArrayList<Order> getOrder (String orderNo) {
            ArrayList<Order> orders = new ArrayList<>();
            String[] columns = {"item_id", "order_no", "quantity", "supplier_id", "total_cost"};
            Object[][] condition = {{"order_no", orderNo}};

            if (db.readTable(columns, condition)) {
                ArrayList<ArrayList<Object>> results = db.getObjResult();
                results.remove(0); // Remove header row

                for (ArrayList<Object> row : results) {
                    Order order = new Order();
                    order.setItemId(row.get(0).toString());
                    order.setOrderNo(row.get(1).toString());
                    order.setQuantity(Integer.parseInt(row.get(2).toString()));
                    order.setSupplierId(row.get(3).toString());
                    order.setTotalCost(Double.parseDouble(row.get(4).toString()));
                    orders.add(order);
                }
            }
            return orders;
        }
   
    

    public static boolean deleteOrder(String orderNo) {
        Object[][] condition = {{"order_no", orderNo}};
        boolean success = db.deleteRecord(condition);
        if (success) {
            return true;
        } else {
            return false;
          }
     }
    
    public static boolean deleteOrder(String orderNo, String itemId) {
        String[] columns = {"item_id"};
        Object[][] condition = {{"order_no", orderNo}};

        boolean orderExists = db.readTable(columns, condition);

        if (!orderExists) {
            System.out.println("Order No " + orderNo + " not found.");
            return false;
        }

        ArrayList<ArrayList<Object>> result = db.getObjResult();

        // If only one item is associated with the order, do not delete
        if (result.size() <= 2) { 
            System.out.println("Order No " + orderNo + " contains only one item. Deletion not allowed.");
            return false;
        }

        Object[][] deleteCondition = {
            {"order_no", orderNo},
            {"item_id", itemId}
        };

        boolean success = db.deleteRecord(deleteCondition);
        if (success) {
            System.out.println("Order with Order No " + orderNo + " and Item ID " + itemId + " deleted successfully.");
            return true;
        } else {
            System.out.println("Failed to delete the order with Order No " + orderNo + " and Item ID " + itemId + ".");
            return false;
        }
}


    // Getters and setters
    public String getItemId() { 
        return itemId;
    }
    
    public String getOrderNo() { 
        return orderNo; 
    }
    
    public int getQuantity() { 
        return quantity; 
    }
    
    public String getSupplierId() { 
        return supplierId; 
    }
    
    public double getTotalCost(){
        return totalCost;
    }
    
   public void setOrderNo(String orderNo){
        this.orderNo = orderNo;
    }
   
    public void setItemId(String itemId){
        this.itemId = itemId;
    }
    
    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
    }
     
    public void setSupplierId(String supplierId) { 
        this.supplierId = supplierId; 
    }
    
    public void setTotalCost(double totalCost){
        this.totalCost = totalCost;
    }
    
    
    @Override
        public String toString() {
            return String.format("| %-10s | %-15s | %-10s | %-20s | %-15.2f |", itemId, orderNo, quantity, supplierId, totalCost);
        }
}
