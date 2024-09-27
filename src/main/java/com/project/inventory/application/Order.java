/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.inventory.application;
import com.project.inventory.dao.Database;
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

        public Order(){
        }
   
        public Order(String itemId, String orderNo, int quantity, String supplierId, double totalCost) {
            this.itemId = itemId;
            this.orderNo = orderNo;
            this.quantity = quantity;
            this.supplierId = supplierId;
            this.totalCost = totalCost;
            createOrder();
        }

        public boolean createOrder() {
            String[] columns ={"item_id", "order_no", "quantity", "supplier_id", "total_cost"}; 
            Object[] values = {itemId, orderNo, quantity, supplierId, totalCost};
            
            return db.insertTable(columns, values);
        }

    
        public boolean saveOrder() {
            if (orderNo == null) {
                System.out.println("Order number is null, cannot update.");
                return false;
            }
            ArrayList<Object[]> updates = new ArrayList<>();
            if (itemId != null) updates.add(new Object[]{"item_id", itemId});
            if (quantity != 0) updates.add(new Object[]{"quantity", quantity});
            if (supplierId != null) updates.add(new Object[]{"supplier_id", supplierId});
            if (totalCost != 0) updates.add(new Object[]{"total_cost", totalCost});

            Object[][] condition = new Object[][] {{"order_no", orderNo},{"item_id",itemId}};

            return db.updateTable(updates.toArray(new Object[0][]), condition);
        }

        public static ArrayList<Order> getAllOrder() {
                ArrayList<Order> orders = new ArrayList<>();
                String[] columns ={"item_id", "order_no", "quantity", "supplier_id", "total_cost"}; 

                if (db.readTable(columns)) {
                    ArrayList<ArrayList<Object>> data = db.getObjResult();
                    data.remove(0); 

                    for (ArrayList<Object> row : data) {
                        Order o = new Order();
                        o.setItemId(row.get(0).toString());
                        o.setOrderNo(row.get(1).toString());
                        o.setQuantity(Integer.parseInt(row.get(2).toString()));
                        o.setSupplierId(row.get(3).toString());
                        o.setTotalCost(Double.parseDouble(row.get(4).toString()));
                        orders.add(o);
                    }
                }
                return orders;
        }

    
        public static ArrayList<Order> getOrders(ArrayList<Order> orders, String orderNo) {
            ArrayList<Order> matchingOrders = new ArrayList<>();

            for (Order o : orders) {
                if (o.getOrderNo().equals(orderNo)) {
                    matchingOrders.add(o);
                }
            }
            return matchingOrders;
        }
    
        
        
        public static Order findOrder(ArrayList<Order> orders, String orderNo, String itemId) {
            for (Order order : orders) {
                if (order.getOrderNo().equals(orderNo) && order.getItemId().equals(itemId)) {
                    return order;
                }
            }
            return null;
        }
   

        public static boolean deleteOrder(ArrayList<Order> orders, String orderNo) {
            ArrayList<Order> matchingOrders = getOrders(orders, orderNo);  

            if (!matchingOrders.isEmpty()) {
                orders.removeAll(matchingOrders);  

                Object[][] condition = {{"order_no", orderNo}};
                return db.deleteRecord(condition); 
            }

            return false;
        }

        public static boolean deleteOrder(ArrayList<Order> orders, String orderNo, String itemId) {
            String[] columns = {"item_id"};
            Object[][] condition = {{"order_no", orderNo}};

            boolean orderExists = db.readTable(columns, condition);
            if (!orderExists || db.getObjResult().isEmpty()) {
                System.out.println("Order No " + orderNo + " not found.");
                return false;
            }

            ArrayList<ArrayList<Object>> result = db.getObjResult();

            // Do not delete if less than 2 
            if (result.size() <= 1) {
                System.out.println("Order No " + orderNo + " contains only one item. Deletion of this item would delete the entire order, which is not allowed.");
                return false;
            }

            Object[][] deleteCondition = {
                {"order_no", orderNo},
                {"item_id", itemId}
            };

            // Delete the order item from the database
            boolean dbDeletionSuccess = db.deleteRecord(deleteCondition);
            if (dbDeletionSuccess) {
                for (int i = 0; i < orders.size(); i++) {  // deletion for arraylist
                    Order order = orders.get(i);
                    if (order.getOrderNo().equals(orderNo) && order.getItemId().equals(itemId)) {
                        orders.remove(i);
                        break; 
                    }
                }
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
    
         private static Object getSingleValue(String columnName, Object[][] condition, Database db) {
            String[] columns = {columnName};
            if (db.readTable(columns, condition)) {
                ArrayList<ArrayList<Object>> results = db.getObjResult();
                // Ensure there's more than header row 
                if (results.size() > 1) {
                    return results.get(1).get(0); // Return the first result of the first row
                }
            }
            return null; 
        }
        
        public static String getStatus(String orderNo){
            Database dbpo = new Database("purchase_order");
            Object[][] condition = {{"order_no", orderNo}};
            Object result = getSingleValue("status", condition, dbpo);
            return result != null ? result.toString().trim() : null;
        }
        
        public static String getSupplierIdbyItemId(String itemId) {
            Database dbsi = new Database("supplier_item"); 
            Object[][] condition = {{"item_id", itemId}};
            Object result = getSingleValue("supplier_id", condition, dbsi);
            return result != null ? result.toString().trim() : null;
        }

        public static double getItemCost(String itemId) {
            Database dbi = new Database("inventory");
            Object[][] condition = {{"item_id", itemId}};
            Object result = getSingleValue("cost", condition, dbi);
            return result != null ? Double.parseDouble(result.toString()) : 0;
        }

        public static double getShippingFee(String itemId) {
            Database dbsi = new Database("supplier_item"); 
            Object[][] condition = {{"item_id", itemId}};
            Object result = getSingleValue("shipping_fee", condition, dbsi);
            return result != null ? Double.parseDouble(result.toString()) : 0;
        }

        public static double getImportDuty(String supplierId) {
            Database dbs = new Database("supplier"); 
            Object[][] condition = {{"supplier_id", supplierId}};
            Object result = getSingleValue("import_duty", condition, dbs);
            return result != null ? Double.parseDouble(result.toString()) : 0;
        }
        
        public static String getSupplierType(String supplierId) {
            Database dbs = new Database("supplier"); 
            Object[][] condition = {{"supplier_id", supplierId}};
            Object result = getSingleValue("supplier_type", condition, dbs);
            return result != null ? result.toString().trim() : null;
        }
    
        public boolean isValidItemId(String itemId) {
            if (!itemId.matches("I\\d{4}")) {
                System.out.println("Invalid item ID format. It should start with 'I' followed by exactly 4 digits (e.g., I0001).");
                return false;
            }
            return true;
        }
        
        public boolean isValidOrderNo(String orderNo) {
            if (!itemId.matches("I\\d{4}")) {
                System.out.println("Invalid item ID format. It should start with 'I' followed by exactly 4 digits (e.g., I0001).");
                return false;
            }
            return true;
        }
    
        
        @Override
        public String toString() {
            return String.format("| %-10s | %-15s | %-10s | %-20s | %-15.2f |", itemId, orderNo, quantity, supplierId, totalCost);
        }
}
