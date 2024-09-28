package com.project.inventory.application;

import com.project.inventory.dao.Database;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PurchaseOrder {
        private String orderNo;
        private Date orderDate;
        private String stringDate;
        private String userId;
        private String status;
        private double totalCost;

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
            String[] columns = {"order_no", "order_date", "user_id", "status", "total_cost"};
            Object[] values = {orderNo, orderDate, userId, status, totalCost};
            return db.insertTable(columns, values);
        }

        public boolean savePO(ArrayList<PurchaseOrder> purchaseOrders) {
            if (orderNo == null) {
                System.out.println("Order number is null, cannot update.");
                return false;
            }

            for (PurchaseOrder po : purchaseOrders) {
                if (po.getOrderNo().equals(orderNo)) {
                    // Update only if the value is not null
                    if (orderDate != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate = sdf.format(orderDate);
                        po.setOrderDate(formattedDate);
                    }
                    if (userId != null) po.setUserId(userId);
                    if (status != null) po.setStatus(status);
                    if (totalCost != 0) po.setTotalCost(totalCost);
                    break;
                }
            }

            ArrayList<Object[]> updates = new ArrayList<>();
            if (orderDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(orderDate);
                setOrderDate(formattedDate);
                updates.add(new Object[]{"order_date", stringDate});
            }
            if (userId != null) updates.add(new Object[]{"user_id", userId});
            if (status != null) updates.add(new Object[]{"status", status});
            if (totalCost != 0) updates.add(new Object[]{"total_cost", totalCost});

            Object[][] condition = new Object[][] {{"order_no", orderNo}};

            return db.updateTable(updates.toArray(new Object[0][]), condition);
        }
        
        
        public static ArrayList<PurchaseOrder> getAllPO() {
                ArrayList<PurchaseOrder> purchaseOrders = new ArrayList<>();
                String[] columns = {"order_no", "order_date", "user_id", "status", "total_cost"};

                if (db.readTable(columns)) {
                    ArrayList<ArrayList<Object>> data = db.getObjResult();
                    data.remove(0); // Remove header

                    for (ArrayList<Object> row : data) {
                        PurchaseOrder po = new PurchaseOrder();
                        po.setOrderNo(row.get(0).toString());

                        // Handle date conversion
                        String stringDate = row.get(1).toString();
                        po.setOrderDate(stringDate); 
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
                            po.setOrderDate(date);
                        } catch (ParseException e) {
                            System.out.println("Error parsing date: " + stringDate);
                        }
                        po.setUserId(row.get(2).toString());
                        po.setStatus(row.get(3).toString());

                        Object totalCostObj = row.get(4);
                        if (totalCostObj == null || totalCostObj.toString().isEmpty()) {
                            po.setTotalCost(0.0); // Default to 0 if null
                        } else {
                            po.setTotalCost(Double.parseDouble(totalCostObj.toString()));
                        }

                        purchaseOrders.add(po);
                    }
                }

                return purchaseOrders;
            }

        
        public static PurchaseOrder findPurchaseOrder(ArrayList<PurchaseOrder> purchaseOrders, String orderNo) {
            for (PurchaseOrder po : purchaseOrders) {
                if (po.getOrderNo().equals(orderNo)) {
                    return po;
                }
            }
            return null;
        }

        
        public static boolean deletePurchaseOrder(ArrayList<PurchaseOrder> purchaseOrders, String orderNo) {
            PurchaseOrder po = findPurchaseOrder(purchaseOrders, orderNo);
            if (po != null) {
                purchaseOrders.remove(po); // Remove from the passed list

                Object[][] condition = {{"order_no", orderNo}};
                return db.deleteRecord(condition);
            }
            return false;
        }
        
        public boolean isValidItemId(String itemId) {
            if (!orderNo.matches("I\\d{4}")) {
                System.out.println("Invalid item ID format. It should start with 'I' followed by exactly 4 digits (e.g., I0001).");
                return false;
            }
            return true;
        }
        
        public boolean isValidOrderNo(String orderNo) {
            if (!orderNo.matches("^OD\\d{4}$")) {
                System.out.println("Invalid order number format. It should start with 'OD' followed by exactly 4 digits (e.g., OD0001).");
                return false;
            }
            return true;
        }
     
       
        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

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

        public double getTotalCost() {
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
