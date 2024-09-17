/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.inventory;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.NumberFormatException ;

/**
 *
 * @author User
 */
public class SupplierManager {
    
    //public Supplier[] supplier = new Supplier[100];
    //public Supplier tempSupplier = new Supplier();
    public static int numSupplier = 0;
    private static Database db = new Database("supplier");
    Scanner scan = new Scanner(System.in);
    private Item item = new Item();
    
    public SupplierManager(){
    
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
      
    public void displaySupplierInfo() {
        String[][] supplier = getAllSupplierInfo();

    
        if (supplier.length == 0) {
            System.out.println("No supplier information found.");
            
        }else{

            System.out.println("Supplier Information:");
             System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
             System.out.printf("| %-2s | %-11s | %-25s | %-78s | %-30s | %-13s | %-10s |\n", "No" ,"Supplier ID", "Supplier Name", "Address", "Email Address", "Supplier Type", "Import Duty");
             System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            for (int i = 1; i < supplier.length;i ++) {
                  String[] tempSupplier = supplier[i];
                
                
                System.out.printf("| %-2d | %-11s | %-25s | %-78s | %-30s | %-13s | %-11s |\n", i ,tempSupplier[0], tempSupplier[1], tempSupplier[2], tempSupplier[3], tempSupplier[4], tempSupplier[5]);
                System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            }
        }
    }
    
    //create
    public void createSupplier(){
        String id, name, address, email;
        String type = "";
        double import_duty = 0.00;
        int options = 0, exit = 1;
        boolean supplierInfoValid = true;
        
        if(numSupplier < 100){
            
            
            do{
                id = String.format("S%04d", numSupplier);
                
                System.out.print("Enter Supplier Name: ");
                name = scan.nextLine();
                System.out.print("Enter Supplier Address: ");
                address = scan.nextLine();
                System.out.print("Enter Supplier Email: ");
                email = scan.nextLine();

                do{
                    try{
                        System.out.println("Select the type of supplier: ");
                        System.out.println("1. Local Supplier");
                        System.out.println("2. Foreign Supplier");
                        System.out.print("Your Choice: ");
                        options = Integer.parseInt(scan.nextLine());
                    }catch(NumberFormatException ex){
                        System.out.println("Error: Your Input Choice Should Be An Integer!");
                    }

                    switch(options){
                        case 1: 
                            type = "Local";
                            import_duty = 0.00;
                            break;
                        case 2:
                            type = "Foreign";
                            import_duty = 20.00;
                            break;
                        default:
                            System.out.println("Invalid Options! Please Try Again");
                            System.out.println();
                            break;
                    }

                }while(options < 1 ||  options > 2);


                supplierInfoValid = validateSupplierInfo(id, name, address, email, type);
                


                if(supplierInfoValid){
                    
               
                        //choose item and add item 
                        addSupplier(id, name, address, email, type, import_duty);
                        numSupplier++;
                        exit = 1;
                }
                else{
                    do{
                        try{
                            System.out.println("Do you want to try again?");
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            System.out.print("Enter your choice: ");
                            options = Integer.parseInt(scan.nextLine());
                        }catch(NumberFormatException ex){
                            System.out.println("Error: Your Input Choice Should Be An Integer!");
                        }
                        
                            switch(options){
                                case 1: 
                                    exit = 0;
                                    break;
                                case 2:
                                    exit = 1;
                                    break;
                                default:
                                    System.out.println("Invalid Choice. Please Try Again.");
                                    break;
                            }

                    }while(options < 1 || options > 2);
                }
            }while(exit == 0);
            
        }
        else{
            System.out.println("Error: The number of supplier has exceeded the limit!");
            System.out.println("You cannot add supplier.");
        }
        
        
    }
    
    public void addSupplier(String id, String name, String address, String email, String type, double import_duty){
        Supplier tempSupplier1;
        
        if(type.equalsIgnoreCase("Local")){
            tempSupplier1 = new LocalSupplier(id, name, address, email);
        }else{
            tempSupplier1 = new ForeignSupplier(id, name, address, email);
        }
        
        numSupplier++;
        
        String importDutyStr = String.valueOf(import_duty);
       
        String[] supplierColumn = {"supplier_id", "supplier_name", "supplier_address", "email_address", "supplier_type", "import_duty"};
        String[] value = {id, name, address, email, type, importDutyStr};
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
    
    //modify
    public void editSupplierInfo(){
        int supplierIndex = 0;
        int options = 0, options2 = 0, exit = 0;
        String temp, columnName, id;
        ForeignSupplier foreignSupplier;
        LocalSupplier localSupplier; 
        
        if(numSupplier  != 0){
            
            do{
                System.out.print("Select the supplier to modify: ");
                supplierIndex = Integer.parseInt(scan.nextLine());


                if(supplierIndex > 0 && supplierIndex < numSupplier){
                    do{
                        id = String.format("S%04d", supplierIndex);
                        String[][] supplier = getAllSupplierInfo(id);
                        System.out.println("Supplier Information:");
                        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.printf("| %-11s | %-25s | %-78s | %-30s | %-13s | %-10s |\n", supplier[1][0], supplier[1][1], supplier[1][2], supplier[1][3], supplier[1][4], supplier[1][5]);
                        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

                        try{
                           System.out.println("1. Supplier's Name");
                           System.out.println("2. Supplier's Address");
                           System.out.println("3. Supplier's Email");
                           System.out.print("Enter Your Choice: ");
                           options2 = Integer.parseInt(scan.nextLine());
                        }catch(NumberFormatException ex){
                           System.out.println("Error: Your Input Choice Should Be An Integer!");
                        }
                        switch(options2){
                            case 1: 
                                System.out.print("Enter Name: ");
                                temp = scan.nextLine();


                                if(validateSupplierName(temp)){
                                    do{
                                       System.out.println();
                                       for(int i = 0; i < 62; i++)
                                           System.out.print("-");
                                       System.out.println();
                                       System.out.printf("| %-25s | %-25s |\n", "Old Name", "New Name");
                                       for(int i = 0; i < 62; i++)
                                           System.out.print("-");
                                       System.out.println();
                                       System.out.printf("| %-25s | %-25s |\n", supplier[1][1], temp);
                                       for(int i = 0; i < 62; i++)
                                           System.out.print("-");
                                       System.out.println();
                                       try{
                                          System.out.println("Do you confirm to modify the data?");
                                          System.out.println("1. Yes");
                                          System.out.println("2. No");
                                          System.out.print("Enter your choice: ");
                                          options = Integer.parseInt(scan.nextLine());
                                       }catch(NumberFormatException ex){
                                               System.out.println("Error: Your Input Choice Should Be An Integer!");
                                       }

                                       switch(options){
                                           case 1: 
                                               columnName = "supplier_name";
                                               id = String.format("S%04d", supplierIndex);
                                               modifySupplier(columnName, id, temp);
                                               exit = 1;
                                               break;
                                           case 2:
                                               exit = 1;
                                               break;
                                           default:
                                               System.out.println("Invalid Choice! Please Try Again");
                                               break;
                                       }
                                    }while(options != 1 && options !=2 );
                                }
                                else
                                    exit = 1;
                                

                                break;
                            case 2:
                                System.out.print("Enter Address: ");
                                temp = scan.nextLine();

                                if(validateSupplierAddress(temp)){
                                    do{
                                       System.out.println();
                                       for(int i = 0; i < 163; i++)
                                           System.out.print("-");
                                       System.out.println();
                                       System.out.printf("| %-78s | %-78s |\n", "Old Address", "New Address");
                                       for(int i = 0; i < 163; i++)
                                           System.out.print("-");
                                       System.out.println();
                                       System.out.printf("| %-78s | %-78s |\n", supplier[1][2], temp);
                                       for(int i = 0; i < 163; i++)
                                           System.out.print("-");
                                       System.out.println();
                                       try{
                                          System.out.println("Do you confirm to modify the data?");
                                          System.out.println("1. Yes");
                                          System.out.println("2. No");
                                          System.out.print("Enter your choice: ");
                                          options = Integer.parseInt(scan.nextLine());
                                       }catch(NumberFormatException ex){
                                               System.out.println("Error: Your Input Choice Should Be An Integer!");
                                       }

                                       switch(options){
                                           case 1: 
                                               columnName = "supplier_address";
                                               id = String.format("S%04d", supplierIndex);
                                               modifySupplier(columnName, id, temp);
                                               exit = 1;
                                               break;
                                           case 2:
                                               exit = 1;
                                               break;
                                           default:
                                               System.out.println("Invalid Choice! Please Try Again");
                                               break;
                                       }
                                    }while(options != 1 && options !=2 );
                                }
                                else
                                    exit = 1;
                                

                                break;
                            case 3:
                                System.out.print("Enter Email Address: ");
                                temp = scan.nextLine();


                                if(validateSupplierEmail(temp)){
                                    do{
                                       System.out.println();
                                       for(int i = 0; i < 62; i++)
                                           System.out.print("-");
                                       System.out.println();
                                       System.out.printf("| %-30s | %-30s |\n", "Email Address", "Email Address");
                                       for(int i = 0; i < 62; i++)
                                           System.out.print("-");
                                       System.out.println();
                                       System.out.printf("| %-30s | %-30s |\n", supplier[1][3], temp);
                                       for(int i = 0; i < 62; i++)
                                           System.out.print("-");
                                       System.out.println();
                                       try{
                                          System.out.println("Do you confirm to modify the data?");
                                          System.out.println("1. Yes");
                                          System.out.println("2. No");
                                          System.out.print("Enter your choice: ");
                                          options = Integer.parseInt(scan.nextLine());
                                       }catch(NumberFormatException ex){
                                               System.out.println("Error: Your Input Choice Should Be An Integer!");
                                       }

                                       switch(options){
                                           case 1: 
                                               columnName = "email_address";
                                               id = String.format("S%04d", supplierIndex);
                                               modifySupplier(columnName, id, temp);
                                               exit = 1;
                                               break;
                                           case 2:
                                               exit = 1;
                                               break;
                                           default:
                                               System.out.println("Invalid Choice! Please Try Again");
                                               break;
                                       }
                                    }while(options != 1 && options !=2 );
                                }
                                else
                                    exit = 1;

                                break;
                        }
                    }while(options2 < 1 || options2 > 3);

                }
                else{
                    do{
                        System.out.println("Invalid choice!");
                        try {
                            System.out.println("Do you want to try again?");
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            System.out.print("Enter Your Choice: ");
                            options = Integer.parseInt(scan.nextLine());
                        }catch(NumberFormatException ex){
                                System.out.println("Error: Your Input Choice Should Be An Integer!");
                        }

                        switch(options){
                            case 1: 
                                exit = 0;
                                break; 
                            case 2:
                                exit = 1;
                                break;
                            default: 
                                System.out.println("Invalid Choice. Please Try Again.");
                                break;
                        }
                    }while(options < 1 || options > 2);
                }
            }while(exit == 0);
        }
        
        
    }
    
    private void  modifySupplier(String columnName, String supplierId, String temp){
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
    
    public void deleteSupplier2(){
        int supplierIndex;
        int options = 0, exit = 0;
        String id; 
        
        
        if(numSupplier != 0){
            do{
                System.out.print("Select the supplier to delete: ");
                supplierIndex = Integer.parseInt(scan.nextLine());
                if(supplierIndex > 0 && supplierIndex < numSupplier){
                    do{
                        id = String.format("S%04d", supplierIndex);
                        String[][] supplier = getAllSupplierInfo(id);
                        System.out.println("Supplier Information: ");
                        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.printf("| %-11s | %-25s | %-78s | %-30s | %-13s | %-10s |\n", supplier[1][0], supplier[1][1], supplier[1][2], supplier[1][3], supplier[1][4], supplier[1][5]);
                        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        try{
                            System.out.printf("Do you confirm to delete Supplier'ID : %s ?\n", supplier[1][0]);
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            System.out.print("Enter Your Choice: ");
                            options = Integer.parseInt(scan.nextLine());
                        }catch(NumberFormatException ex){
                            System.out.println("Error: Your Input Choice Sholud Be An Integer:!");
                        }
                        switch(options){
                            case 1: 
                                id = String.format("S%04d", supplierIndex);
                                deleteSupplier(id);
                                exit = 1;
                                break;
                            case 2:
                                exit = 1;
                                break;
                            default:
                                System.out.println("Invalid Choice! Please Try Again");
                                break;
                                
                        }
                        
                    }while(options != 1 && options !=2 );
                        do{
                        try{
                            System.out.println("Do You Want to Continue Delete Another Supplier?");
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            System.out.print("Enter Your Choice: ");
                            options = Integer.parseInt(scan.nextLine());
                        }catch(NumberFormatException ex){
                            System.out.println("Error: Your Input Choice Sholud Be An Integer:!");
                        }

                        switch(options){
                            case 1:
                                exit = 0;
                                break;
                            case 2: 
                                exit = 1;
                                break;
                            default:
                                System.out.println("Invalid Choice! Please Try Again");
                                break;
                        }
                     }while(options != 1 && options !=2 );
                }else
                    exit = 1;
            }while(exit == 0);
        }
       else{
            do{     
                System.out.println("Invalid choice!");
                try {
                     System.out.println("Do you want to try again?");
                     System.out.println("1. Yes");
                     System.out.println("2. No");
                     System.out.print("Enter Your Choice: ");
                     options = Integer.parseInt(scan.nextLine());
                    }catch(NumberFormatException ex){
                           System.out.println("Error: Your Input Choice Should Be An Integer!");
                    }

                    switch(options){
                        case 1: 
                            exit = 0;
                            break; 
                        case 2:
                            exit = 1;
                            break;
                        default: 
                            System.out.println("Invalid Choice. Please Try Again.");
                            break;
                        }
                    }while(options < 1 || options > 2);
              
        }
       
    }
    
    private void deleteSupplier(String supplierId){
        boolean success;
        Object[][] condition = {{"supplier_id", supplierId}};
        
        success = db.deleteRecord(condition);
        
        if(success)
            System.out.printf("The Supplier of %s Has Been Deleted!\n", supplierId);
        else
            System.out.printf("Failed to Delete The Data of %s Supplier\n", supplierId);
        
    }
    
 
    
    
    
    
}
