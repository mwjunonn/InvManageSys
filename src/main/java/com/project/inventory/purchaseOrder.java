/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.inventory;
import java.util.Date;
import java.util.ArrayList;
import java.util.Scanner;


/**
 *
 * @author MAK
 */
public class PurchaseOrder {
        private String orderNo;
        private Date orderDate;
        private String stringDate;
        private String userId;
        private String status;
        private double totalCost;

        private Scanner scan = new Scanner(System.in);
        private static Order order = new Order();        
        private static Database db;
        private Database dbi = new Database("inventory");
        private Database dbo = new Database("order_item");

        
        public PurchaseOrder() {
            db = new Database("purchase_order");
        }
        
        public PurchaseOrder(String orderNo, Date orderDate, String userId, String status, double totalCost){
            this.orderNo = orderNo;
            this.orderDate = orderDate;
            this.userId = userId;
            this.status = status;
            this.totalCost = totalCost;
        }
        //Create
        
        public boolean createPurchaseOrder(String orderNo, Date orderDate, String userId, String status) {
            String[] columns = {"order_no", "order_date", "user_id", "status"};
            Object[] values = {orderNo, orderDate, userId, status};

            return db.insertTable(columns, values);
        }
       
        
        //update 
        public boolean updatePurchaseOrder(String orderNo, Date newOrderDate, String newUserId, String newStatus, double totalCost) {
               ArrayList<Object[]> values = new ArrayList<>();

            if (newOrderDate != null) {
                values.add(new Object[]{"order_date", newOrderDate});
            }
            if (newUserId != null) {
                values.add(new Object[]{"user_id", newUserId});
            }
            if (newStatus != null) {
                values.add(new Object[]{"status", newStatus});
            }
            if (totalCost != 0) {
                values.add(new Object[]{"total_cost", totalCost});
            }

            if (values.isEmpty()) {
                System.out.println("No updates were provided.");
                return false;
        }

            Object[][] valuesArray = values.toArray(new Object[0][]);

            Object[][] condition = {
                {"order_no", orderNo}
            };

            return db.updateTable(valuesArray, condition);
        }


        public void updateTotalCost(String orderNo, double totalCost) {            
            updatePurchaseOrder(orderNo, null, null, null, totalCost);
        }
        
        public boolean updateInventoryQuantity(String orderNo) {
            String[] orderItemColumns = {"item_id", "quantity"};
            Object[][] orderItemCondition = {{"order_no", orderNo}};
            
            //order item table first
            if (!dbo.readTable(orderItemColumns, orderItemCondition)) {
                System.out.println("Failed to fetch order item details for order_no: " + orderNo);
                return false;
            }

            ArrayList<ArrayList<Object>> orderResults = dbo.getObjResult();

            for (int i = 1; i < orderResults.size(); i++) {
                ArrayList<Object> row = orderResults.get(i);
                String itemId = (String) row.get(0);   
                int orderQuantity = (int) row.get(1); 

                String[] inventoryColumns = {"quantity"};
                Object[][] inventoryCondition = {{"item_id", itemId}};

                if (!dbi.readTable(inventoryColumns, inventoryCondition)) {
                    System.out.println("Failed to fetch inventory details for product_id: " + itemId);
                    return false;
                }

                ArrayList<ArrayList<Object>> inventoryResults = dbi.getObjResult();
                if (inventoryResults.size() <= 1) {
                    System.out.println("No inventory record found for product_id: " + itemId);
                    return false;
                }

                // Get the current quantity from the inventory
                int currentInventoryQuantity = (int) inventoryResults.get(1).get(0); // First row is column labels

                //Add order_item quantity to current inventory quantity
                int updatedQuantity = currentInventoryQuantity + orderQuantity;

                Object[][] values = {
                    {"quantity", updatedQuantity} 
                };

                Object[][] condition = {
                    {"item_id", itemId}  
                };

                // update invenotry quantity
                if (!dbi.updateTable(values, condition)) {
                    System.out.println("Failed to update inventory for item_id: " + itemId);
                    return false;  
                }
            }

            return true;  
        }
        
        public ArrayList<PurchaseOrder> getAllPurchaseOrders() {
            ArrayList<PurchaseOrder> orders = new ArrayList<>();
            String[] columns = {"order_no", "order_date", "user_id", "status", "total_cost"};

            if (db.readTable(columns)) {
                ArrayList<ArrayList<Object>> results = db.getObjResult();
                results.remove(0); // Remove header row

                for (ArrayList<Object> row : results) {
                    PurchaseOrder po = new PurchaseOrder();
                    po.setOrderNo(row.get(0).toString());
                    po.setOrderDate(row.get(1).toString());
                    po.setUserId(row.get(2).toString());
                    po.setStatus(row.get(3).toString());
                    po.setTotalCost(Double.parseDouble(row.get(4).toString()));
                    orders.add(po);
                }
            }
            return orders;
        }
        
        // Read with condition (e.g., by order_no)
        public ArrayList<PurchaseOrder> getPurchaseOrder(String orderNo) {
            ArrayList<PurchaseOrder> orders = new ArrayList<>();
            String[] columns = {"order_no", "order_date", "user_id", "status", "total_cost"};
            Object[][] condition = {{"order_no", orderNo}};

            if (db.readTable(columns, condition)) {
                ArrayList<ArrayList<Object>> results = db.getObjResult();
                results.remove(0); // Remove header row

                for (ArrayList<Object> row : results) {
                    PurchaseOrder po = new PurchaseOrder();
                    po.setOrderNo(row.get(0).toString());
                    po.setOrderDate(row.get(1).toString());
                    po.setUserId(row.get(2).toString());
                    po.setStatus(row.get(3).toString());
                    po.setTotalCost(Double.parseDouble(row.get(4).toString()));
                    orders.add(po);
                }
            }
            return orders;
        }
 
        public String getLastOrderNumber() {
            String[] columns = {"order_no"};
            String condition = "ORDER BY order_no DESC LIMIT 1"; 
             boolean success = db.readTable(columns, condition);
            if (!success) {
                System.out.println("Query execution failed!");
                return null;
            }
            ArrayList<ArrayList<Object>> result = db.getObjResult();

            //Debug purpose
            //System.out.println("Result: " + result);

            if (result.size() <= 1) {
                System.out.println("No previous order number found. Assigned OD0001 as Order No");
                return null; 
            }
            if (!result.isEmpty()) {
                result.remove(0);  
            }

            String lastOrderNo = result.get(0).get(0).toString();

            if (!lastOrderNo.startsWith("OD") || lastOrderNo.length() < 4) {
                System.out.println("Invalid order number format: " + lastOrderNo);
                return null;
            }
            return lastOrderNo.replaceAll(Database.delimiter, "").trim();
        }

        public String getNextOrderNumber() {
            String lastOrderNo = getLastOrderNumber();
            if (lastOrderNo == null) {
                return "OD0001";  
            }

            String numericPart = lastOrderNo.substring(2);  // delete OD
            try {
                int orderNoInt = Integer.parseInt(numericPart) + 1;
                return String.format("OD%04d", orderNoInt);
            } catch (NumberFormatException e) {
                System.out.println("Error parsing order number: " + lastOrderNo);
                return "OD0001";  // Default to "OD0001" if there's an error
            }
        }

         //Delete
        public static void deletePurchaseOrder(String orderNo) {      
            if(order.deleteOrder(orderNo)){
                System.out.println("[Order_item] with the Order No " + orderNo + " deleted successfully!");
            }
            else{
                System.out.println("[Order_item] with the Order No " + orderNo + " delete failed! Make sure the Order ID exists.");
            }

            Object[][] condition = {{"order_no", orderNo}};
            boolean success = db.deleteRecord(condition);

            if (success) {
                System.out.println("Purchase order " + orderNo + " deleted successfully.");
            } else {
                System.out.println("Failed to delete Purchase Order " + orderNo + ". Make sure the Order ID exists.");
            }
        }
       
         
        // Getter and Setter for orderNo
        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        // Getter and Setter 
        public Date getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.stringDate = orderDate;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        
        public void setTotalCost(double totalCost) {
            this.totalCost = totalCost;
        }
        
        @Override
        public String toString() {
            return String.format("| %-10s | %-15s | %-10s | %-20s | %-15.2f |", orderNo, stringDate, userId, status, totalCost);
        }
}
