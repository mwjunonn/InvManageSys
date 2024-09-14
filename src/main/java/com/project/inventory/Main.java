package com.project.inventory;
import java.util.Scanner;

public class Main {
    static Scanner scan = new Scanner(System.in);
    static ProcessBuilder processBuilder = new ProcessBuilder(); //For clear screen use, if cannot clear screen delete it
    
    public static void main(String[] args) {
        mainMenu();
        Inventory inv = new Inventory();
        inv.printInventory();
    }
    
    public static void mainMenu(){
        User manager = new Manager();
        int choice;
        
        do{
            System.out.println("[Welcome to MIXUE Inventory System]");
            System.out.println("-----------------------------------");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Your Choice: ");
            choice = scan.nextInt();
            
            System.out.println("");
            
            switch(choice){
                case 1:
                    loginMenu();
                    break;
                case 2:
                    registerMenu();
                    break;
                case 3:
                    break;
                default: 
                    System.out.println("Invalid Input! Try Again");
                    break;
            }
        }while (choice != 3);
    }
    
    public static void loginMenu(){
        int choice;
        String id;
        String password;
        //If found out how to clear screen, enable this two
        //System.out.println("[Welcome to MIXUE Inventory System]");
        //System.out.println("-----------------------------------");
        do{
            System.out.println("Login as: ");
            System.out.println("1. Manager");
            System.out.println("2. Inventory Admin");
            System.out.println("3. Return to last page");
            System.out.print("Your choice: ");
            choice = scan.nextInt();
            scan.nextLine();
            System.out.println("");
            switch(choice){
                case 1:
                    User manager = new Manager();
                    
                    System.out.println("Login as Manager"); 
                    System.out.print("ID: ");
                    id = scan.nextLine();
                    System.out.print("Password: ");
                    password = scan.nextLine();
                    System.out.println();
                    if (manager.equals(id) && manager.passwordValid(password)) {
                        System.out.println("Login Successful");
                        switch (permissionMenu(choice)){
                            case 1:     //Restock Inventory
                                break;
                            case 2:     //Current Stock Report
                                break;
                            case 3:     //Display all supplier (If don't need cut this out)
                                break;
                            case 4:     //All staff details
                                break;
                            case 5:     //Modify staff details
                                break;
                            case 6:     //Delete staff
                                break;
                            default:
                                break;
                        }
                    }
                    else if (manager.equals(id) && !(manager.passwordValid(password))) {
                        System.out.println("Wrong Password!");
                    }
                    else
                        System.out.println("Login Failed");
                    System.out.println("");
                    break;
                case 2:
                    User inventoryAdmin = new InventoryAdmin();
                    
                    System.out.println("Login as Inventory Admin");
                    System.out.print("ID: ");
                    id = scan.nextLine();
                    System.out.print("Password: ");
                    password = scan.nextLine();
                    System.out.println();
                    if (inventoryAdmin.equals(id) && inventoryAdmin.passwordValid(password)) {
                        System.out.println("Login Successful");
                        switch (permissionMenu(choice)){
                            case 1:     //Restock inventory
                                break;
                            case 2:     //Purchase order status
                                break;
                            default:
                                break;
                        }
                    }
                    else if (inventoryAdmin.equals(id) && !(inventoryAdmin.passwordValid(password))) {
                        System.out.println("Wrong Password! ");
                    }
                    else
                        System.out.println("Login Failed");
                    System.out.println();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid Input! Try Again");
                    break;
            }
        }while (choice != 3);
    }
    
    public static void registerMenu(){
        String managerID = new String();
        String managerPassword = new String();
        String name = new String();
        String position = new String();
        String password = new String();
        String confirmPassword = new String();
        String email = new String();
        int selection;
        String user_id = new String();
        
        User manager = new Manager();
        
        //If found out how to clear screen, enable this two
        //System.out.println("[Welcome to MIXUE Inventory System]");
        //System.out.println("-----------------------------------");
        scan.nextLine();
        System.out.println("Require manager to approve registration.");
        System.out.print("Enter Manager ID: ");
        managerID = scan.nextLine();
        if (manager.equals(managerID)) {
            System.out.print("Password: ");
            managerPassword = scan.nextLine();
        }
        else
            System.out.println("You are not a Manager.");
        
        System.out.println();
        if (manager.equals(managerID) && manager.passwordValid(managerPassword)) {
            System.out.println("[Registration Portal]");
            System.out.println("---------------------");
            System.out.print("Enter name: ");
            name = scan.nextLine();
            User.nameValidation(name);
            do {
                System.out.println("Position: ");
                System.out.println("1. Manager");
                System.out.println("2. Inventory Admin");
                System.out.print("Selection: ");
                selection = scan.nextInt();
                scan.nextLine();
                switch(selection){
                    case 1:
                        position = "Manager";
                        break;
                    case 2:
                        position = "InventoryAdmin";
                        break;
                    default:
                        System.out.println("Invalid Input. Try again.");
                        break;
                }
            } while (selection != 1 && selection!= 2);
            System.out.print("Create password: ");
            password = scan.nextLine();
            User.passwordValidation(password);
            System.out.print("Confirm password: ");
            confirmPassword = scan.nextLine();
            while(!(confirmPassword.equals(password))){
                System.out.println("Your password isn't match. Try again");
                System.out.print("Password: ");
                password = scan.nextLine();
                System.out.print("Confirm password: ");
                confirmPassword = scan.nextLine();
            }
            System.out.println("Any email?");
            System.out.println("0. Yes");
            System.out.println("1. No");
            System.out.print("> ");
            selection = scan.nextInt();
            scan.nextLine();
            do {
                switch(selection){
                    case 0:
                        System.out.print("Email: ");
                        email = scan.nextLine();
                        break;
                    case 1:
                        email = "";
                        break;
                    default:
                        break;
                }
            } while (selection != 0 && selection != 1);
            //Registration details
            System.out.println("Name: " + name);
            System.out.println("Password: " + password);
            System.out.println("Position: " + position);
            System.out.println("Email: " + email);
            
            //Confirmation
            System.out.print("Confirm register user? (0 for Yes / 1 for No)> ");
            selection = scan.nextInt();
            scan.nextLine();
            while(selection != 0 && selection != 1){
                System.out.print("Invalid input. Try again > ");
                selection = scan.nextInt();
                scan.nextLine();
            }
            if (selection == 0) {
                System.out.println("-----------------------");
                System.out.println("Registration Complete. ");
                System.out.println("--------Details--------");
                
                if (position.equals("Manager")) {
                    User user = new Manager(name, password, email);
                    user.writeToDatabase();
                    user_id = user.getId();
                    System.out.println(user.toString());
                }
                else if(position.equals("InventoryAdmin")){
                    User user = new InventoryAdmin(name, password, email);
                    user.writeToDatabase();
                    user_id = user.getId();
                    System.out.println(user.toString());
                }
                System.out.println("--------------------------------------------\n\n");
            }
            else if (selection == 1) {
                System.out.println("Exit registration.");
                System.out.println("------------------");
                System.out.println();
            }
        }
        else if (manager.equals(managerID) && !(manager.passwordValid(managerPassword))) {
            System.out.println("Wrong Password.");
        }
            
    }
    
    public static int permissionMenu(int choice){
        int decision = 0;
        
        switch (choice){
            // 1 = Manager, 2 = Inventory Admin
            case 1:
                do {
                System.out.println("1. Restock inventory");         //order item @ purchase order
                System.out.println("2. Current Stock Report");
                System.out.println("3. Display all supplier");      //Not sure put here or wat, people incharge supplier can modify this
                System.out.println("4. All staff details");
                System.out.println("5. Modify staff details");
                System.out.println("6. Delete staff");
                System.out.println("7. Return to last page");
                System.out.print("Choice > ");
                decision = scan.nextInt();
                    if (decision < 1 || decision > 7) {
                        System.out.println("");
                        System.out.println("Invalid input. Try again.");
                        System.out.println("");
                    }
                } while ((decision < 1 || decision > 7) || decision != 7);                
                break;
            case 2:
                do {
                    System.out.println("1. Restock inventory");
                    System.out.println("2. Purchase order status");
                    System.out.println("3. Return to last page");
                    System.out.print("Choice > ");
                    decision = scan.nextInt();
                    if (decision < 1 || decision > 3) {
                        System.out.println("");
                        System.out.println("Invalid input. Try again.");
                        System.out.println("");
                    }
                } while ((decision < 1 || decision > 3) || decision != 3);
                break;
            default:
                break;
        }
        return decision;
    }
}