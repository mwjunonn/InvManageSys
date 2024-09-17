
package com.project.inventory;

public class LocalSupplier extends Supplier {
    private double importDuty = 0.00;
    private String supplierType;
    //constructor
    public LocalSupplier(){
    }
    
    public LocalSupplier(String supplierId, String supplierName, String supplierAddress, String supplierEmail){
        super(supplierId, supplierName, supplierAddress, supplierEmail);
    }
    
    
     public double getImportDuty(){
        return importDuty;
    }
     
    public String getSupplierType(){
        return supplierType;
    }
     
   
    public void setImportDuty(double importDuty){
        this.importDuty = importDuty;
    }
    
    public void setSupplierType(){
        this.supplierType = "Local";
    }
      
      
    
     
     @Override
    public String toString(){
        return super.toString() + String.format("\nSupplier Type: %s", "Local");
    }
    
    
    
    
}