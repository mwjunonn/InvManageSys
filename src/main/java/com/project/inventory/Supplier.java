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
  
    //create
    //public void addSupplier(){
    //    String[] supplierColumn = {"supplier_id", "supplier_name", "supplier_address", "email_address", "supplier_type", "import_duty"};
    //    String[] value = {supplierId, supplierName, supplierAddress, supplierEmail};
    //    db.insertTable(supplierColumn, value);
   // }
    
}
