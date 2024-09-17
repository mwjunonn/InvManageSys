
package com.project.inventory;

public class ForeignSupplier extends Supplier{

    private double importDuty = 20.00;
    private String supplierType;
    
    //constructor
    public ForeignSupplier(){

    }


    public ForeignSupplier(String supplierId, String supplierName, String supplierAddress, String supplierEmail){
        super(supplierId, supplierName, supplierAddress, supplierEmail);

    }

    public String getSupplierType(){
        return supplierType;
    }
    
    public double getImportDuty(){
        return importDuty;
    }


    public void setImportDuty(double importDuty){
        this.importDuty = importDuty;
    }
    public void setSupplierType(){
        this.supplierType = "Foreign";
    }
    
    @Override
    public String toString(){
        return super.toString() + String.format("\nSupplier Type: %s \nImport Duty: RM%.2f", "Foreign",importDuty);
    }
    
}