
package com.project.inventory;

public class LocalSupplier extends Supplier {
    
    private static final String SUPPLIER_TYPE = "Local";
    
    //constructor
    public LocalSupplier(String supplierId, String supplierName, String supplierAddress, String supplierEmail){
        super(supplierId, supplierName, supplierAddress, supplierEmail);
    }
    
     @Override
    public String toString(){
        return super.toString() + String.format("\nSupplier Type: %s", SUPPLIER_TYPE);
    }
    
    
    
    
}