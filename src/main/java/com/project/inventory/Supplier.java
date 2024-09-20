/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.inventory;
import java.util.ArrayList;
import java.util.List;

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

        db.readTable(columns); 
        ArrayList<String> resultList = db.getResult();

        for (int j = 1; j < resultList.size(); j++) { 
            String[] supplierData = resultList.get(j).split(Database.delimiter);
           
            if (supplierData.length == columns.length) {
                            
                if (!supplierData[5].isEmpty()) {
                    try {
                        importDuty = Double.parseDouble(supplierData[5]);
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing import duty for supplier: " + supplierId);
                    }
                }
              
                if (supplierData[4].equals("Local")) {
                    supplier = new LocalSupplier(supplierData[0], supplierData[1], supplierData[2], supplierData[3], supplierData[4], importDuty);
                } else {
                    supplier = new ForeignSupplier(supplierData[0], supplierData[1], supplierData[2], supplierData[3], supplierData[4], importDuty);
                }
                suppliers.add(supplier);
            } else {
                System.out.println("Error: Incorrect data format in row " + (j + 1));
            }
        }

        numSupplier = resultList.size() - 1; 
        return suppliers;
    }
    
    public boolean isSupplierExists(String supplierId) {
        

        String[] columns = {"supplier_id"};
        Object[][] condition = {{"supplier_id", supplierId}};

        if (db.readTable(columns, condition, "")) {
            ArrayList<ArrayList<Object>> result = db.getObjResult();
            return result != null && result.size() > 1;
        }
        return false;
    }

    public Supplier getAllSupplierInfo(String supplierId) {
        String[] columns = {"supplier_id", "supplier_name", "supplier_address", "email_address", "supplier_type", "import_duty"};
        Object[][] condition = {{"supplier_id", supplierId}};
        double importDuty = 0.00;

        db.readTable(columns, condition);

        ArrayList<ArrayList<Object>> resultList = db.getObjResult();

        if(resultList.size() <= 1){
          
            return null;
        }

        ArrayList<Object> supplierData = resultList.get(1);

        
        try{
            importDuty = Double.parseDouble(supplierData.get(5).toString());
        }catch(NumberFormatException ex){
            System.out.println("Error: Parsing Import Duty");
        }

        if ("Local".equalsIgnoreCase(supplierData.get(4).toString())) {
            return new LocalSupplier(supplierData.get(0).toString(), supplierData.get(1).toString(), supplierData.get(2).toString(), supplierData.get(3).toString(), supplierData.get(4).toString(), importDuty);
        } else {
            return new ForeignSupplier(supplierData.get(0).toString(), supplierData.get(1).toString(), supplierData.get(2).toString(), supplierData.get(3).toString(), supplierData.get(4).toString(), importDuty);
        }
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

    public boolean validateSupplierInfo(String id, String name, String address, String email, String type){


        if(!validateSupplierName(name) || !validateSupplierAddress(address) || !validateSupplierEmail(email))
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

    public boolean validateSupplierAddress(String address){
        String[] columns ={"supplier_address"};
        db.readTable(columns);
        ArrayList<String> resultList = db.getResult();

        if(address.isEmpty()|| address.isBlank() || address == null ){
            System.out.println("Supplier Address cannot be empty!");
            return false;
        }
        if (address.length() < 10 || address.length() > 100){
            System.out.println("Error: Supplier's Address must contain 10 - 100 characters.");
            return false;
        }
        for(String supplierAddress : resultList){
            if(supplierAddress.equals(address)){
                System.out.println("Duplicate Supplier Address");
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

    public void  modifySupplier(String columnName, String supplierId, String temp){
        boolean success;
        Object[][] values = {{columnName, temp}};

        Object[][] condition = {{"supplier_id", supplierId}};

        success = db.updateTable(values, condition);

        if(success){
            System.out.println("New Data Updated!");
        }
        else
            System.out.println("Failed to Update New Data!");
    }

    public void deleteSupplier(String supplierId){
        boolean success;
        Object[][] condition = {{"supplier_id", supplierId}};

        success = db.deleteRecord(condition);

        if(success)
            System.out.printf("The Supplier of %s Has Been Deleted!\n", supplierId);
        else
            System.out.printf("Failed to Delete The Data of %s Supplier\n", supplierId);

    }
}
