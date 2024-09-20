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
        private static Database db = new Database("purchase_order");


        
        public PurchaseOrder() {
        }
        
        public PurchaseOrder(String orderNo, Date orderDate, String userId, String status, double totalCost){
            this.orderNo = orderNo;
            this.orderDate = orderDate;
            this.userId = userId;
            this.status = status;
            this.totalCost = totalCost;
            createPurchaseOrder();
        }
        //Create
        
        public boolean createPurchaseOrder() {
            String[] columns = {"order_no", "order_date", "user_id", "status"};
            Object[] values = {orderNo, orderDate, userId, status};

            return db.insertTable(columns, values);
        }
       
        
        //update 
        public boolean updatePurchaseOrder(String orderNo, Date newOrderDate, String newUserId, String newStatus, double totalCost) {
               ArrayList<Object[]> values = new ArrayList<>();

            if (newOrderDate != null) {
                values.add(new Object[]{"order_date", newOrderDate});
                setOrderDate(newOrderDate);
            }
            if (newStatus != null) {
                values.add(new Object[]{"status", newStatus});
                setStatus(newStatus);
            }
            if (totalCost != 0) {
                values.add(new Object[]{"total_cost", totalCost});
                setTotalCost(totalCost);
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
            setTotalCost(totalCost);
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
                    //Nullpointerexception Solved
                    Object totalCostObj = row.get(4);
                    if (totalCostObj == null || totalCostObj.toString().isEmpty()) {
                        po.setTotalCost(0.0); // Default to 0.0 if totalCost is null or empty
                    } else {
                        po.setTotalCost(Double.parseDouble(totalCostObj.toString()));
                    }
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
                    //Nullpointerexception Solved
                    Object totalCostObj = row.get(4);
                    if (totalCostObj == null || totalCostObj.toString().isEmpty()) {
                        po.setTotalCost(0.0); // Default to 0.0 if totalCost is null or empty
                    } else {
                        po.setTotalCost(Double.parseDouble(totalCostObj.toString()));
                    }
                    orders.add(po);
                }
            }
            return orders;
        }
 

         //Delete
        public void deletePurchaseOrder(String orderNo) {      
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
        
        public void setOrderDate(Date orderDate) {
            this.orderDate = orderDate;
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
        
        public double getTotalCost(){
            return totalCost;
        }
        
        public void setTotalCost(double totalCost) {
            this.totalCost = totalCost;
        }
        
        @Override
        public String toString() {
            return String.format("| %-10s | %-15s | %-10s | %-20s | %-15.2f |", orderNo, stringDate, userId, status, totalCost);
        }
}
