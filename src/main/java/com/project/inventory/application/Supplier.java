/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.inventory.application;
import com.project.inventory.dao.Database;

import java.util.ArrayList;

/**
 *
 * @author Lee
 */
public class Supplier {
    private String supplierId, supplierName, supplierAddress, supplierEmail;
    public static int numSupplier = 0;
    private static Database db = new Database("supplier");


    
    //constructor
    public Supplier(){
    
    }
    
    public Supplier(String supplierId, String supplierName, String supplierAddress, String supplierEmail){
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierAddress = supplierAddress;
        this.supplierEmail = supplierEmail;


    }
    
    //getter
    public String getSupplierId(){
        return supplierId;
    }
    
    public String getSupplierName(){
        return supplierName;
    }
    
    public String getSupplierAddress(){
        return supplierAddress;
    }
    
    public String getSupplierEmail(){
        return supplierEmail;
    }
    
    //setter
    public void setSupplierId(String supplierId){
        this.supplierId = supplierId;
    }
    
    public void setSupplierName(String supplierName){
        this.supplierName = supplierName;
    }
    
    public void setSupplierAddress(String supplierAddress){
        this.supplierAddress = supplierAddress;
    }
    
    public void setSupplierEmail(String supplierEmail){
        this.supplierEmail = supplierEmail;
    }



  
    //methods
    @Override
    public String toString() {
        return String.format("| %-11s | %-25s | %-78s | %-30s |",
            supplierId, supplierName, supplierAddress, supplierEmail);
    }

    public ArrayList<Supplier> getAllSupplierInfo() {
        String[] columns = {"supplier_id", "supplier_name", "supplier_address", "email_address", "supplier_type", "import_duty"};
        ArrayList<Supplier> suppliers = new ArrayList<>();
        Supplier supplier;
        double importDuty = 0.0;

        // Read table using the columns specified
        db.readTable(columns);

        // Fetch the result as an ArrayList of ArrayLists (each row as an ArrayList<Object>)
        ArrayList<ArrayList<Object>> resultList = db.getObjResult();

        // Start from 1 to skip the column labels (first row)
        for (int j = 1; j < resultList.size(); j++) {
            ArrayList<Object> row = resultList.get(j); // Get each row

            // Ensure the row has the correct number of columns
            if (row.size() == columns.length) {
                String supplierId2 = (String) row.get(0);
                String supplierName2 = (String) row.get(1);
                String supplierAddress2 = (String) row.get(2);
                String emailAddress = (String) row.get(3);
                String supplierType = (String) row.get(4);
                Object importDutyObj = row.get(5);

                // Parse import duty, handling possible null or empty values
                if (importDutyObj != null && !importDutyObj.toString().isEmpty()) {
                    try {
                        importDuty = Double.parseDouble(importDutyObj.toString());
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing import duty for supplier: " + supplierId2);
                    }
                } else {
                    importDuty = 0.0; // Default to 0.0 if no import duty provided
                }

                // Create supplier objects based on supplier type
                if (supplierType.equals("Local")) {
                    supplier = new LocalSupplier(supplierId2, supplierName2, supplierAddress2, emailAddress, supplierType, importDuty);
                } else {
                    supplier = new ForeignSupplier(supplierId2, supplierName2, supplierAddress2, emailAddress, supplierType, importDuty);
                }
                // Add supplier to the list
                suppliers.add(supplier);
            } else {
                System.out.println("Error: Incorrect data format in row " + (j + 1));
            }
        }

        numSupplier = resultList.size() - 1; // Adjust the count of suppliers
        return suppliers;
    }
    
    public boolean isSupplierExists(ArrayList<Supplier> suppliers, String supplierId) {
        
        for(int i = 0; i < suppliers.size(); i++){
            if(suppliers.get(i).getSupplierId().equals(supplierId))
                return true;
            
        }
        return false;
    }

    public Supplier getAllSupplierInfo(ArrayList<Supplier> suppliers, String supplierId) {
        
        for(int i =0 ; i < suppliers.size(); i++){
            if(suppliers.get(i).getSupplierId().equals(supplierId)){
                return suppliers.get(i);
            }           
        }
        
        return null;
    }
    
    public void addSupplier(Supplier supplier){

        String importDutyStr, type;


        numSupplier++;

        if(supplier instanceof ForeignSupplier){
            importDutyStr = String.valueOf(((ForeignSupplier)supplier).getImportDuty());
            type = ((ForeignSupplier)supplier).getSupplierType();
        }else{
            importDutyStr = String.valueOf(((LocalSupplier)supplier).getImportDuty());
            type = ((LocalSupplier)supplier).getSupplierType();
        }


        String[] supplierColumn = {"supplier_id", "supplier_name", "supplier_address", "email_address", "supplier_type", "import_duty"};
        String[] value = {supplier.getSupplierId(), supplier.getSupplierName(), supplier.getSupplierAddress(), supplier.getSupplierEmail(), type, importDutyStr};
        db.insertTable(supplierColumn, value);
        System.out.println("Added Successfully");
    }

    public boolean validateSupplierInfo(ArrayList<Supplier> suppliers, String id, String name, String address, String email, String type){


        if(!validateSupplierName(name) || !validateSupplierAddress(suppliers, address) || !validateSupplierEmail(email))
            return false;

        if(!type.equals("Local") && !type.equals("Foreign")){
            System.out.println("Invalid Supplier Type!");
            return false;
        }

        return true;
    }

    public boolean validateSupplierName(String name){

        if(name.isEmpty()|| name.isBlank() || name == null ){
            System.out.println("Supplier Name cannot be empty!");
            return false;
        }
        if (name.length() > 50){
            System.out.println("Error: Supplier's Name exceeds the limit of 45 characters.");
            return false;
        }

        return true;

    }

    public boolean validateSupplierAddress(ArrayList<Supplier> suppliers, String address){        
        
        address = address.trim();
        
        if(address.isEmpty()|| address.isBlank() || address == null ){
            System.out.println("Supplier Address cannot be empty!");
            return false;
        }
        if (address.length() < 10 || address.length() > 100){
            System.out.println("Error: Supplier's Address must contain 10 - 100 characters.");
            return false;
        }
        for(int i = 0; i < suppliers.size();i++){
            if(suppliers.get(i).getSupplierAddress().trim().equalsIgnoreCase(address)){
                System.out.println("Cannot have a duplicate address!");
                return false;
            }
        }
        return true;
    }

    public boolean validateSupplierEmail(String email){
        if(email.isEmpty()|| email.isBlank() || email == null ){
            System.out.println("Supplier Email cannot be empty!");
            return false;
        }
        if (email.length() > 45){
            System.out.println("Error: Supplier Email exceeds the limit of 45 characters.");
            return false;
        }

        if(!email.contains("@") || !email.contains(".com")){
            System.out.println("Invalid Supplier Email!");
            return false;
        }
        return true;
    }

    public boolean  modifySupplier(String columnName, String supplierId, String temp){
        Object[][] values = {{columnName, temp}};

        Object[][] condition = {{"supplier_id", supplierId}};

        return db.updateTable(values, condition);

        
    }

    public boolean deleteSupplier(String supplierId){
        Object[][] condition = {{"supplier_id", supplierId}};

        return db.deleteRecord(condition);

    }
}
