
package com.project.inventory;

public class ForeignSupplier extends Supplier{
    private static final String SUPPLIER_TYPE = "Foreign";
    private double importDuty = 20.00;
    
    //constructor
    public ForeignSupplier(String supplierId, String supplierName, String supplierAddress, String supplierEmail){
        super(supplierId, supplierName, supplierAddress, supplierEmail);
    }
    
    public double getImportDuty(){
        return importDuty;
    }
    
    public void setImportDuty(double importDuty){
        this.importDuty = importDuty;
    }
    
    @Override
    public String toString(){
        return super.toString() + String.format("\nSupplier Type: %s \nImport Duty: RM%.2f", SUPPLIER_TYPE,importDuty);
    }
    
}