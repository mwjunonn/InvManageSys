
package com.project.inventory;

public class LocalSupplier extends Supplier {
    
    private static final String SUPPLIER_TYPE = "Local";
    private double importDuty = 0.00;
    //constructor
    public LocalSupplier(String supplierId, String supplierName, String supplierAddress, String supplierEmail){
        super(supplierId, supplierName, supplierAddress, supplierEmail);
    }
    
    
     public double getImportDuty(){
        return importDuty;
    }
    
     
     @Override
    public String toString(){
        return super.toString() + String.format("\nSupplier Type: %s", SUPPLIER_TYPE);
    }
    
    
    
    
}