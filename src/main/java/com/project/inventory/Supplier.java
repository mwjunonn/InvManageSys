/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.inventory;
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
    public String toString(){
        return "Supplier Name: " + supplierName + "Supplier Address: " + supplierAddress + "Supplier Email:" + supplierEmail;
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
            
            if(i == 0)
                numSupplier = resultList.size();
            
           
        }
        String supplierInfo2[][] = new String[supplierInfo[0].length][columns.length];
    
        for (int i = 0; i < supplierInfo[0].length; i++) {
           for (int j = 0; j < columns.length; j++) {
            supplierInfo2[i][j] = supplierInfo[j][i];
            }
        }
    
        return supplierInfo2;
    }
   
    public String[][] getAllSupplierInfo(String supplierId) {
        String[] columns = {"supplier_id", "supplier_name", "supplier_address", "email_address", "supplier_type", "import_duty"};
        Object[][] condition = {{"supplier_id", supplierId}};

      
        db.readTable(columns, condition);

        
        ArrayList<ArrayList<Object>> result = db.getObjResult();

       
        String[][] supplierInfo = new String[result.size()][columns.length];

        for (int i = 0; i < result.size(); i++) {
            for (int j = 0; j < result.get(i).size(); j++) {
                supplierInfo[i][j] = result.get(i).get(j).toString();
            }
    }
    
    return supplierInfo;
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
