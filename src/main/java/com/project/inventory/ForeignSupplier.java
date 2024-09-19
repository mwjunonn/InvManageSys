
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
    public ForeignSupplier(String supplierId, String supplierName, String supplierAddress, String supplierEmail, String supplierType, double importDuty){
        super(supplierId, supplierName, supplierAddress, supplierEmail);
        this.supplierType = supplierType;
        this.importDuty = importDuty;
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
    public String toString() {
        return super.toString() + String.format(" %-13s | %-15.2f |",
             supplierType, importDuty);
    }
    
}