/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.inventory;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class SupplierManager {
    
    public Supplier[] supplier = new Supplier[100];
    public static int numSupplier = 0;
    private static Database db = new Database("supplier");
    
    public SupplierManager(){
    
    }

    
    public String[][] getAllSupplierInfo(){
        String[] columns = {"supplier_id", "supplier_name", "supplier_address", "email_address", "supplier_type", "import_duty"};
        String[][] supplierInfo = new String[6][];
        
        for(int i = 0; i < columns.length; i++){
            String[] columnName = {columns[i]};
            
            db.readTable(columnName);
            
            ArrayList<String> resultList = db.getResult();
            
            supplierInfo[i] = new String[resultList.size()];
            
            for(int j = 0; j < resultList.size(); j++){
                supplierInfo[i][j] = resultList.get(j).replaceAll(Database.delimiter, "");
            }
            
           
        }
        String supplierInfo2[][] = new String[supplierInfo[0].length][columns.length];
    
        for (int i = 0; i < supplierInfo[0].length; i++) {
           for (int j = 0; j < columns.length; j++) {
            supplierInfo2[i][j] = supplierInfo[j][i];
            }
        }
    
        return supplierInfo2;
    }
    
    public void populateSupplierArray() {
        String[][] supplierInfo = getAllSupplierInfo(); // Get the supplier data

        for (int i = 0; i < supplierInfo.length; i++) {
            String supplier_id = supplierInfo[i][0];        // Column 0: supplier_id
            String supplier_name = supplierInfo[i][1];      // Column 1: supplier_name
            String supplier_address = supplierInfo[i][2];   // Column 2: supplier_address
            String email_address = supplierInfo[i][3];      // Column 3: email_address
            String supplier_type = supplierInfo[i][4];      // Column 4: supplier_type         

            // Create Supplier object (could be LocalSupplier or ForeignSupplier based on supplier_type)
            Supplier newSupplier;
            if (supplier_type.equalsIgnoreCase("Local")) {
                newSupplier = new LocalSupplier(supplier_id, supplier_name, supplier_address, email_address);
            } else{
                newSupplier = new ForeignSupplier(supplier_id, supplier_name, supplier_address, email_address);
            }


            // Assign the new supplier object to the supplier[] array
            supplier[i] = newSupplier;
            numSupplier++;
        }
    }
    
    
    public void displaySupplierInfo() {
        
    
        if (supplier.length == 0) {
            System.out.println("No supplier information found.");
            
        }else{

            System.out.println("Supplier Information:");
             System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
             System.out.printf("| %-2s | %-11s | %-25s | %-78s | %-30s | %-13s |\n", "No" ,"Supplier ID", "Supplier Name", "Address", "Email Address", "Supplier Type");
             System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            for (int i = 1; i < numSupplier;i ++) {
                  Supplier tempSupplier = supplier[i];
                  String supplierType = tempSupplier instanceof LocalSupplier ? "Local" : "Foreign";
                
                System.out.printf("| %-2d | %-11s | %-25s | %-78s | %-30s | %-13s |\n", i ,tempSupplier.getSupplierId(), tempSupplier.getSupplierId(), tempSupplier.getSupplierAddress(), tempSupplier.getSupplierEmail(), supplierType);
                System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            }
        }
    }
}
