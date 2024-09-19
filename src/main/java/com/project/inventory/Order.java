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
    private static Database dbi = new Database("inventory");
    private static Database dbpo = new Database("purchase_order");
    private static Database dbs = new Database("supplier"); 
    private static Database dbsi = new Database("supplier_item"); 

    private Scanner scan = new Scanner(System.in);

    public Order(){
    }
    
    public Order(String itemId, int quantity) {
        this.itemId = itemId;
        this.orderNo = "";
        this.quantity = quantity;
        this.supplierId = "";
    }
    
    public Order(String itemId, int quantity, String supplierId) {
        this.itemId = itemId;
        this.orderNo = "";
        this.quantity = quantity;
        this.supplierId = supplierId;
    }
    
    public Order(String itemId, String orderNo, int quantity, String supplierId) {
        this.itemId = itemId;
        this.orderNo = orderNo;
        this.quantity = quantity;
        this.supplierId = supplierId;
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

    public void generateOrder(String orderNo) {
        String itemId;
        String choice = "y";
        double purchaseOrderTotalCost = 0.0;
        do {
            // Check if item exists
                System.out.print("Enter item ID to restock(999 to stop): ");
                itemId = scan.nextLine().trim();
                
                if (itemId.equals("999")){
                    System.out.println("Restocking stopped!");
                    break;
                }
                
                if (!isItemExists(itemId)) {
                System.out.println("Item not found.");
                continue;
                }
                System.out.println("Item found...");
       

                System.out.print("Enter quantity to add: ");
                int quantityToAdd;
                try {
                    quantityToAdd = Integer.parseInt(scan.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity. Please enter a number.");
                    continue;
                }

                if (quantityToAdd <= 0) {
                    System.out.println("Quantity must be greater than zero.");
                    return;
                }
                //supplier                          //--------------------------------------------------------------------------------------------------------------------------------------------------------
                String supplierId = getSupplierIdbyItemId(itemId);
                
                double totalCost = (getItemCost(itemId) * quantityToAdd) + (getShippingFee(itemId) * quantityToAdd) + getImportDuty(supplierId);
                purchaseOrderTotalCost += totalCost;
                
                //po.updateTotalCost(orderNo, purchaseOrderTotalCost);
                // Formula (quantity * price) + (shipping fee per kg * quantity) + import fee
                //Hvaent done
                System.out.printf("Total cost for this order: RM%.2f\n", totalCost);

                if(createOrder(itemId, orderNo, quantityToAdd, supplierId, totalCost)){
                    System.out.println("Order Created for item " + itemId + " with supplier "+ supplierId);
                }
                else{
                    System.out.println("Order Failed to create for item "+ itemId);
                }

                System.out.println("Do you want to order again (y/n)?");
                choice = scan.nextLine().toLowerCase();  

                if (!choice.equals("y") && !choice.equals("n")) {
                    do{
                        System.out.println("Invalid choice. Please enter 'y' for yes or 'n' for no.");
                        System.out.println("Do you want to order again (y/n)?");
                        choice = scan.nextLine().toLowerCase();  
                    } while(!choice.equals("n"));
                }
                
            } while(!choice.equals("n"));
        updatePurchaseOrder(orderNo, purchaseOrderTotalCost);
        }


    //read
    public ArrayList<ArrayList<Object>> readAllOrders(){
        String[] columns = {"item_id", "order_no", "quantity", "supplier_id", "total_cost"};
        db.readTable(columns);
        return db.getObjResult();
    }
    
    public ArrayList<ArrayList<Object>> readOrder(String orderNo){
        String[] columns = {"item_id", "order_no", "quantity", "supplier_id", "total_cost"};
        Object[][] condition = {{"order_no", orderNo}};
        db.readTable(columns, condition);
        return db.getObjResult();
    }
    
    public String[] getAllItemId() {
            String[] columns = {"item_id"};
            dbi.readTable(columns);
            ArrayList<String> list = dbi.getResult();
            String[] itemList = new String[list.size()];
            for (int i = 0; i< list.size(); i++){
                itemList[i] = list.get(i).replaceAll(Database.delimiter, "");
            }
            return itemList;
        }
    

    private Object getSingleValue(String columnName, Object[][] condition, Database db) {
        String[] columns = {columnName};

        // Use the readTable method to query the database with the column name and condition
        if (db.readTable(columns, condition)) {
            ArrayList<ArrayList<Object>> results = db.getObjResult();

            // Ensure there's more than just the header row (index 0)
            if (results.size() > 1) {
                return results.get(1).get(0); // Return the first result of the first row
            }
        }
        return null; // Return null if no result found
    }
    
    private String getSupplierIdbyItemId(String itemId) {
        Object[][] condition = {{"item_id", itemId}};
        Object result = getSingleValue("supplier_id", condition, dbsi);
        return result != null ? result.toString().trim() : null;
    }

    private double getItemCost(String itemId) {
        Object[][] condition = {{"item_id", itemId}};
        Object result = getSingleValue("cost", condition, dbi);
        return result != null ? Double.parseDouble(result.toString()) : 0;
    }

    private double getShippingFee(String itemId) {
        Object[][] condition = {{"item_id", itemId}};
        Object result = getSingleValue("shipping_fee", condition, dbsi);
        return result != null ? Double.parseDouble(result.toString()) : 0;
    }

    private double getImportDuty(String supplierId) {
        Object[][] condition = {{"supplier_id", supplierId}};
        Object result = getSingleValue("import_duty", condition, dbs);
        return result != null ? Double.parseDouble(result.toString()) : 0;
    }
    
    public void updatePurchaseOrder(String orderNo, double totalCost) {
        Object[][] values = {{"total_cost", totalCost}};

        Object[][] condition = {
            {"order_no", orderNo}
        };

        dbpo.updateTable(values, condition);
    }

                                     //Update
    public void updateOrder() {
        System.out.print("Enter the Item ID for the order you want to update:");
        String itemId = scan.nextLine().trim();

        System.out.print("Enter the Order No for the order you want to update:");
        String orderNo = scan.nextLine().trim();

        Object[][] condition = {{"item_id", itemId}, {"order_no", orderNo}};

        String[] columns = {"item_id", "order_no", "quantity", "supplier_id", "total_cost"};
        boolean orderExists = db.readTable(columns, condition);

        if (!orderExists || db.getObjResult().size() <= 1) {
            System.out.println("Order with Item ID " + itemId + " and Order No " + orderNo + " not found.");
            return;
        }

        double itemCost = getItemCost(itemId);

        System.out.print("Enter the new Quantity: ");
        int newQuantity = scan.nextInt();
        scan.nextLine(); 

        // Calculate the new total cost 
        double newTotalCost = (newQuantity * getItemCost(itemId))  + (newQuantity * getShippingFee(itemId)) + getImportDuty(getSupplierIdbyItemId(itemId));

        // Update quantity and total_cost in order_item table
        Object[][] value = {{"quantity", newQuantity} ,{"total_cost", newTotalCost}};

        if (db.updateTable(value, condition)) {
            System.out.println("Order item updated successfully.");

            // Update the total cost in the purchase_order 
            updatePurchaseOrderTotalCost(orderNo);
        } else {
            System.out.println("Failed to update the order item.");
        }
    }

    // Method to update total_cost in purchase_order after updating order_item total_cost
    private void updatePurchaseOrderTotalCost(String orderNo) {
        // Get all order items for the given order_no
        Object[][] condition  = {{"order_no", orderNo}};
        String[] column = {"total_cost"};
        boolean itemsExist = db.readTable(column, condition);

        if (!itemsExist) {
            System.out.println("No items found for Order No " + orderNo);
            return;
        }

        // Calculate the new total cost by + total_cost of each item
        double totalCost = 0;
        ArrayList<ArrayList<Object>> resultArray = db.getObjResult();
        
        for (int i = 1; i < resultArray.size(); i++) {
            ArrayList<Object> row = resultArray.get(i);
            Object costObj = row.get(0); 
            if (costObj != null) {
                try {
                    totalCost += Double.parseDouble(costObj.toString());
                } catch (NumberFormatException e) {
                    System.out.println("Error adding total cost: " + costObj);
                    return;
                }
            }
        }

        Object[][] updatedValue = {{"total_cost", totalCost}};
        Object[][] condition2 = {{"order_no", orderNo}};

        if (dbpo.updateTable(updatedValue, condition2)) {
            System.out.println("Total cost for purchase order updated successfully.");
        } else {
            System.out.println("Failed to update the total cost for the purchase order.");
        }
    }

 
    
    
    
            
    
    
                                      //Display
    
        public void displayMenu(){
            System.out.println("1. Print All Orders");
            System.out.println("2. Print by Search Order No");
            System.out.print("Choice > ");
            int choice = scan.nextInt();
            switch(choice){
                case 1:
                    displayOrder();
                    scan.nextLine();
                    enterToContinue();
                    break;
                case 2:
                    displayOrderBySearch();
                    enterToContinue();
                    break;
                default:
                    break;
            }
        }
        
        public void displayOrder(){
        ArrayList<ArrayList<Object>> orders = readAllOrders();
        String[] header = {"item_id", "order_no", "quantity", "supplier_id", "total_cost"};
        
        int[] columnWidths = {12,15,10,20,20};
        
        if(!orders.isEmpty() && isHeaderRow(orders.get(0), header)){
            orders.remove(0);
        }
        
        String[] displayHeaders = {"Item ID", "Order No", "Quantity", "Supplier ID", "Total Cost (RM)"};
        printTable(displayHeaders, orders, columnWidths);
    }
    
        public void displayOrderBySearch(){
         scan.nextLine();
         String orderNo;
         System.out.print("Please enter Order No to find your Order List: ");
         orderNo = scan.nextLine();
         
          if (!orderNo.matches("^OD\\d{4}$")) {
                System.out.println("Invalid order number format. It should start with 'OD' followed by exactly 4 digits (e.g., OD0001).");
                return;  
            }
            
            ArrayList<ArrayList<Object>> orders = readOrder(orderNo);
            String[] header = {"item_id", "order_no", "quantity", "supplier_id", "total_cost"};
            
            if (orders.size() < 2) {
                System.out.println("Order number " + orderNo + " does not exist.");
                return;  
            }

            int[] columnWidths = {12, 15, 10, 20, 20};

            if (!orders.isEmpty() && isHeaderRow(orders.get(0), header)) {
                orders.remove(0);
            }

            String[] displayHeaders = {"Item ID", "Order No", "Quantity", "Supplier ID", "Total Cost (RM)"};
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
    
        public boolean deleteOrder(String orderNo) {
        Object[][] condition = {{"order_no", orderNo}};
        boolean success = db.deleteRecord(condition);

        if (success) {
            return true;
        } else {
            return false;
        }
    }

        // Method to check if the item exists in the inventory
        //validation
        public boolean isItemExists(String inputItemId) {
            String[] itemIds = getAllItemId();

            // Compare
            for (String itemId : itemIds) {
                if (itemId.equals(inputItemId)) {
                    return true;
                }
            }
            return false;
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

    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
    }
    
    public void setSupplierId(String supplierId) { 
        this.supplierId = supplierId; 
    }
    
    public void enterToContinue(){
            System.out.println("Press Enter to continue");
            scan.nextLine();
        }
}
