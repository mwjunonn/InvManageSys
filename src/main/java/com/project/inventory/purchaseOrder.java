/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.inventory;
import java.util.Date;
import java.util.ArrayList;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author MAK
 */
public class purchaseOrder {
        private String orderNo;
        private Date orderDate;
        private String userId;
        private String status;
        private Database db;
        private Scanner scan = new Scanner(System.in);
        Order order = new Order();
        private Database dbi = new Database("inventory");
        private Database dbo = new Database("order_item");

        
        public purchaseOrder() {
        }
        
        private String loggedInUserId; 

        public purchaseOrder(String userId) {
            db = new Database("purchase_order");
            this.loggedInUserId = userId;  
        }
        //Create
        
        public boolean createPurchaseOrder(String orderNo, Date orderDate, String userId, String status) {
            String[] columns = {"order_no", "order_date", "user_id", "status"};
            Object[] values = {orderNo, orderDate, userId, status};

            return db.insertTable(columns, values);
        }
        
        public void generatePurchaseOrder(){
            String orderNo = getNextOrderNumber();
            createPurchaseOrder(orderNo, new Date(), loggedInUserId, "Ordering");
            order.generateOrder(orderNo);
            
            if(!updatePurchaseOrder(orderNo, new Date(), loggedInUserId, "Pending Shipping", 0)){
                System.out.println("Insertion of Purchase Order Failed!");
            }
            else{
                System.out.println("Insertion of Purchase Order Succeed!");
            }
            enterToContinue();
        }
        
         
         //Read
         public ArrayList<ArrayList<Object>> readAllPurchaseOrders() {
            String[] columns = {"order_no", "order_date", "user_id", "status", "total_cost"};
            db.readTable(columns);
            return db.getObjResult();
        }

         // Read with condition (e.g., by order_no)
        public ArrayList<ArrayList<Object>> readPurchaseOrder(String orderNo) {
            String[] columns = {"order_no", "order_date", "user_id", "status", "total_cost"};
            Object[][] condition = {{"order_no", orderNo}};
            db.readTable(columns, condition);
            return db.getObjResult();
        }
        
        //update
        public void updatePOMenu(){
            System.out.print("Enter the order number of the purchase order to update: ");
            String orderNo = scan.nextLine();

            if (!orderNo.matches("^OD\\d{4}$")) {
                System.out.println("Invalid order number format. It should start with 'OD' followed by exactly 4 digits (e.g., OD0001).");
                return;
            }
            
            System.out.println("What would you like to update?");
            System.out.println("1. Order Date\n2. User ID\n3. Status\n4. Total Cost\n5. Update All at Once");
            System.out.print("Enter your choice > ");
            int choice = scan.nextInt();
            scan.nextLine();
            
            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice. Please select a valid option.");
                return;
            }
            switch(choice){
                case 1:
                    updateOrderDate(orderNo);
                    break;
                case 2:
                    updateUserId(orderNo);
                    break;
                case 3:
                    updateStatus(orderNo);
                    break;
                case 4: 
                    updateTotalCost(orderNo);
                    break;
                case 5:
                    updatePOall(orderNo);
                    break;
                    
            }
        }
        
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
        
        private void updateOrderDate(String orderNo) {
            System.out.print("Enter new order date (yyyy-MM-dd): ");
            String dateInput = scan.nextLine();
            Date newOrderDate = null;
            try {
                newOrderDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateInput);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                return;
            }
            boolean success = updatePurchaseOrder(orderNo, newOrderDate, null,  null, 0);
            if (success) {
                System.out.println("Order date updated successfully.");
            } else {
                System.out.println("Failed to update order date.");
            }
            enterToContinue();
        }
        
        private void updateStatus(String orderNo) {
            int choice = 0;
            String newStatus = "";
            while (true) {
                System.out.println("1. Pending Shipping \n2. Out for delivery \n3. Delivered \n4. Return to last page");
                System.out.print("Choose a new status: ");
                choice = scan.nextInt();

                if (choice >= 1 && choice <= 4) {
                    switch (choice) {
                        case 1:
                            newStatus = "Pending Shipping";
                            break;
                        case 2:
                            newStatus = "Out for delivery";
                            break;
                        case 3:
                            newStatus = "Delivered";
                            boolean success = updateInventoryQuantity(orderNo);
                            if (success){
                                System.out.println("Inventory updated successfully for order number: " + orderNo);
                            }
                            else{
                                System.out.println("Update of quantitiy failed!");
                            }
                            break;
                        case 4:
                            System.out.println("Returning to the last page...");
                            return; 
                    }
                    break; 
                } else {
                    System.out.println("Invalid choice! Please enter a number between 1 and 4.");
                }
            }

            // Proceed with the new status or return logic
            if (choice != 4) {
                System.out.println("New status set to: " + newStatus);
            }
            
            boolean success = updatePurchaseOrder(orderNo, null, null, newStatus, 0);
            if (success) {
                System.out.println("Status updated successfully.");
            } else {
                System.out.println("Failed to update status.");
            }
            enterToContinue();
        }
        
        private void updateUserId(String orderNo) {
            System.out.print("Enter new user ID: ");
            String newUserId = scan.nextLine();
            boolean success = updatePurchaseOrder(orderNo, null, newUserId, null, 0);
            if (success) {
                System.out.println("User ID updated successfully.");
            } else {
                System.out.println("Failed to update user ID.");
            }
            enterToContinue();
        }
        
        private void updateTotalCost(String orderNo) {
            System.out.print("Enter new total cost: ");
            Double newTotalCost = scan.nextDouble();
            boolean success = updatePurchaseOrder(orderNo, null, null, null, newTotalCost);
            if (success) {
                System.out.println("Total Cost updated successfully.");
            } else {
                System.out.println("Failed to update Total Cost.");
            }
            enterToContinue();
        }
        
        public void updateTotalCost(String orderNo, double totalCost) {            
            updatePurchaseOrder(orderNo, null, null, null, totalCost);
        }
        
        public void updatePOall(String orderNo) {
            System.out.print("Enter new order date (yyyy-MM-dd):");
            String dateInput = scan.nextLine();
            Date newOrderDate;
            try {
                newOrderDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateInput);
            } catch (ParseException e) {
                System.out.println("Invalid date format.");
                return;
            }
            
            System.out.print("Enter new user ID:");
            String newUserId = scan.nextLine();

            System.out.print("Enter new status:");
            String newStatus = scan.nextLine();
            
            System.out.print("Enter new status:");
            double newTotalCost = scan.nextDouble();

            // Call update method
            boolean success = updatePurchaseOrder(orderNo, newOrderDate, newUserId, newStatus, newTotalCost);
            if (success) {
                System.out.println("Purchase order updated successfully.");
            } else {
                System.out.println("Failed to update purchase order.");
            }
            enterToContinue();
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
        
        //Displays
        
        public void displayMenu(){
            System.out.println("1. Print All Purchase Orders");
            System.out.println("2. Print by Search Order No");
            System.out.print("Choice > ");
            int choice = scan.nextInt();
            switch(choice){
                case 1:
                    displayPO();
                    scan.nextLine();
                    enterToContinue();
                    break;
                case 2:
                    displayPOSearch();
                    enterToContinue();
                    break;
                default:
                    break;
            }
        }
        public void displayPO(){
            ArrayList<ArrayList<Object>> orders = readAllPurchaseOrders();
            String[] headers = {"order_no", "order_date", "user_id", "status", "total_cost"};

            int[] columnWidths = {12, 15, 10, 20, 20};

            if (!orders.isEmpty() && isHeaderRow(orders.get(0), headers)) {
                orders.remove(0);
            }

            //PRint table
            String[] displayHeaders = {"Order No", "Order Date", "User ID", "Status", "Total Cost (RM)"};
            printTable(displayHeaders, orders, columnWidths);
            }
        
        public void displayPOSearch(){
            String orderNo;
            System.out.println("Please enter orderNo to find your purchase order: ");
            orderNo = scan.nextLine();
            
            if (!orderNo.matches("^OD\\d{4}$")) {
                System.out.println("Invalid order number format. It should start with 'OD' followed by exactly 4 digits (e.g., OD0001).");
                return;  
            }
            
            ArrayList<ArrayList<Object>> orders = readPurchaseOrder(orderNo);
            String[] headers = {"order_no", "order_date", "user_id", "status", "total_cost"};
            
            if (orders.size() < 2) {
                System.out.println("Order number " + orderNo + " does not exist.");
                return;  
            }

            int[] columnWidths = {12, 15, 10, 20, 20};

            if (!orders.isEmpty() && isHeaderRow(orders.get(0), headers)) {
                orders.remove(0);
            }

            String[] displayHeaders = {"Order No", "Order Date", "User ID", "Status", "Total Cost (RM)"};
            printTable(displayHeaders, orders, columnWidths);
            }
        
        public static void printTable(String[] headers, ArrayList<ArrayList<Object>> rows, int[] columnWidths) {
            printTableLine(columnWidths);
            printRow(headers, columnWidths);
            printTableLine(columnWidths);

            // Print each row of data
            for (ArrayList<Object> row : rows) {
                String[] rowArray = new String[row.size()];
                for (int i = 0; i < row.size(); i++) {
                    rowArray[i] = row.get(i).toString();
                }
                printRow(rowArray, columnWidths);
            }

            printTableLine(columnWidths);
        }

        public static void printRow(String[] row, int[] columnWidths) {
            for (int i = 0; i < row.length; i++) {
                System.out.printf("| %-"+columnWidths[i]+"s", row[i]);
            }
            System.out.println("|");
        }

        public static void printTableLine(int[] columnWidths) {
            for (int width : columnWidths) {
                System.out.print("+");
                for (int i = 0; i < width+1; i++) {
                    System.out.print("-");
                }
            }
            System.out.println("+");
        }

        // (check if the first row contains column names)
        public static boolean isHeaderRow(ArrayList<Object> firstRow, String[] headers) {
            for (int i = 0; i < headers.length; i++) {
                if (!firstRow.get(i).toString().trim().equalsIgnoreCase(headers[i].trim())) {
                    return false;
                }
            }
            return true;
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

        private String getNextOrderNumber() {
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
        public void deletePurchaseOrder() {
            System.out.print("Enter the Order No to delete: ");
            String orderNo = scan.nextLine().trim();

            if (!orderNo.matches("OD\\d{4}")) { 
                System.out.println("Invalid order number format. It should start with 'OD' followed by exactly 4 digits (e.g., OD0001).");
                return;
            }
            
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
            enterToContinue();
        }
       
        public void enterToContinue(){
            System.out.println("Press Enter to continue");
            scan.nextLine();
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
}
