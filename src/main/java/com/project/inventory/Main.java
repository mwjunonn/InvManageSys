package com.project.inventory;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Main {
    private static Scanner scan = new Scanner(System.in);
    //static ProcessBuilder processBuilder = new ProcessBuilder(); //For clear screen use, if cannot clear screen delete it
    
    public static void main(String[] args) {
        Database.startDatabase();
        mainMenu();
        Database.closeDatabase();
    }
    
    public static void mainMenu(){
        User manager = new Manager();
        int choice = 0;
        
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
        int decision;
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
            System.out.println();
            switch(choice){
                case 1:
                    Manager manager = new Manager();
                    
                    System.out.println("Login as Manager"); 
                    System.out.print("ID: ");
                    id = scan.nextLine();
                    System.out.print("Password: ");
                    password = scan.nextLine();
                    System.out.println();
                    if (manager.equals(id) && manager.passwordValid(password)) {
                        System.out.println("Login Successful");
                        System.out.println("Welcome " + manager.getCurrentName() + ".");
                        System.out.println("---------------------------------------");

                        do {
                            decision = permissionMenu(choice);
                           switch (decision){
                                case 1:     //Restock Inventory
                                    break;
                                case 2:     //Current Stock Report
                                    break;
                                case 3:     //Display all supplier (If don't need cut this out)
                                    break;
                                case 4:     //All staff details
                                    manager.displayAllUser();
                                    System.out.println("\n");
                                    break;
                                case 5:     //Modify staff details
                                    break;
                                case 6:     //Delete staff
                                    break;
                                case 7:
                                    System.out.println("\nReturning to last page.\n");
                                    break;
                                default:
                                    break;
                            }
                        } while (decision != 7);
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
                        System.out.println("Welcome " + inventoryAdmin.getCurrentName() + ".");
                        System.out.println("---------------------------------------");

                        do{
                            decision = permissionMenu(choice);
                            switch (decision){
                                case 1:     //Restock inventory
                                    break;
                                case 2:     //Purchase order status
                                    break;
                                case 3:
                                    System.out.println("\nReturning to last page.\n");
                                    break;
                                default:
                                    break;
                            }
                        }
                        while(decision != 3);
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
                        System.out.println();
                        System.out.println("Invalid input. Try again.");
                        System.out.println();
                    }
                    return decision;
                } while ((decision < 1 || decision > 7) || decision != 7);                
            case 2:
                do {
                    System.out.println("1. Restock inventory");
                    System.out.println("2. Purchase order status");
                    System.out.println("3. Return to last page");
                    System.out.print("Choice > ");
                    decision = scan.nextInt();
                    if (decision < 1 || decision > 3) {
                        System.out.println();
                        System.out.println("Invalid input. Try again.");
                        System.out.println();
                    }
                    return decision;
                } while ((decision < 1 || decision > 3) || decision != 3);
            default:
                break;
        }
        return decision;
    }

    private static void inventoryMenu(){
        Inventory.restartInventory();
        String[] option = new String[]{
                "Restock inventory",
                "Search item",
                "View all item",
        };
        System.out.println("Inventory\n---------");
        int input = getMenuInput(option);
        switch(option[--input]){
            case "Restock inventory":
                int index = chooseItem();
                if(index != 0)
                    restockInventory(--index);
                break;
            case "View all item":
                printInventory();
                System.out.println("Choose your operation: ");
                String[] option1 = new String[]{
                        "Select particular item",
                };
                input = getMenuInput(option1);
                if(input != 0){
                    input = chooseItem();
                    if(input != 0) {
                        inventoryOperation(input - 1);
                    }
                }
                break;
        }
        Inventory.closeInventory();
    }

    private static void restockInventory(int index){
        boolean validation;
        Item item = Inventory.getItem(index);
        int restockQuantity = 0;
        do {
            validation = false;
            System.out.println("Item name = " + item.getItemName());
            System.out.println("Item quantity = " + item.getQuantity());
            System.out.println("Restock quantity = ");
            if (scan.hasNextInt()) {
                restockQuantity = scan.nextInt();
                validation = true;
            } else {
                System.out.println("Error input! Please try again.");
            }
        } while (!validation);
        item.setQuantity(item.getQuantity() + restockQuantity);
    }

    private static int chooseItem() {
        ArrayList<Item> item = Inventory.getItemList();
        int input = 0;
        do {
            System.out.println("Choose your item with the number.");
            for (int i = 0; i < item.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, item.get(i).getItemName());
            }
            if(scan.hasNextInt()) {
                input = scan.nextInt();
            }else{
                input = item.size() + 1;
            }
            if(input > item.size()){
                System.out.println("Error input! Please try again.");
            }
        } while (input < 0 || input > item.size());
        return input;
    }

    private static void printInventory() {
/*
        ArrayList<Object> table = Inventory.getItemList();
        for(String[] str1 : ((String[][])table.getFirst())){
            System.out.printf("%" + str1[0] +"s\t", str1[1]);
        }
        System.out.println();
        table.removeFirst();
        for(Object item: table.toArray()){
            System.out.println(item);
        }
*/
        InventoryUI inventoryUI = new InventoryUI();
        inventoryUI.showInventoryGui(Inventory.getItemListWithColumns());
    }

    private static void inventoryOperation(int index) {
        System.out.println("What you would like to do ?");
        String[] option = new String[]{
                "Update item",
                "Delete item",
        };
        int input = getMenuInput(option);
        switch (option[--input]) {
            case "Update item":
                updateItem(index);
                break;
            case "Delete item":
                //deleteItem();
                break;
        }
    }

    private static void updateItem(int index) {
        boolean validation = true;
        String strInput = "";
        Item item = Inventory.getItem(index);
        List<String> list = new ArrayList<String>(Arrays.asList(item.toString().split("\t")));
        String[] temp = list.getLast().split("/");
        list.removeLast();
        Collections.addAll(list, temp);
        String[] itemData = list.toArray(new String[0]);
        String[] itemDataName = new String[]{
                "Item name ",
                "Item type",
                "Price",
                "Quantity",
                "Unit"
        };
        //Methods should be start at here
        for (int i = 0; i < itemData.length; i++)
            System.out.printf("%s: %s\n", itemDataName[i], itemData[i]);
        System.out.println();
        System.out.println("What you would like to update ?");
        int input = getMenuInput(itemDataName);
        if (input == 0)
            return;
        else if (itemDataName[--input].equals("Unit")) {
            System.out.println("Current unit = " + itemData[itemData.length - 1]);
            System.out.print("New unit (Eg : 1kg)= ");
        } else {
            System.out.print("New " + itemDataName[--input] + " = ");
            if (itemDataName[input].equals("Price"))
                System.out.print("RM ");
        }
        scan.nextLine(); //Clear the buffer
        strInput = scan.nextLine();
        switch (itemDataName[input]) {
            case "Item name":
                item.setItemName(strInput);
                break;

            case "Item type":
                item.setItemType(strInput);
                break;

            case "Quantity":
                try {
                    item.setQuantity(Double.parseDouble(strInput));
                } catch (NumberFormatException e) {
                    System.out.println("Input is not number");
                    validation = false;
                }
                break;

            case "Price":
                try {
                    item.setLatestPrice(Double.parseDouble(strInput));
                } catch (NumberFormatException e) {
                    System.out.println("Input is not number");
                    validation = false;
                }
                break;

            case "Unit":
                if(strInput.matches("^\\d+(?:\\.\\d+)?[a-zA-Z]+$")){//Means matching 1.2kg or 12kg
                    Matcher matcher = Pattern.compile("\\d+(?:\\.\\d+)?").matcher(strInput); //Number part
                    int indexOfSplit = 0;
                    if(matcher.find())
                        indexOfSplit = matcher.end();
                    String[] subStr = {strInput.substring(0, indexOfSplit), strInput.substring(indexOfSplit)};
                    item.setPer_unit(Double.parseDouble(subStr[0]));
                    item.setItemUnit(subStr[1]);
                }
        }
    }

    private static int getMenuInput(String[] option) {
        int input = 0;
        do{
            System.out.println("Press 0 to exit\n");
            System.out.println("Please choose your option: ");
            for (int i = 0; i < option.length; i++) {
                System.out.printf("%d. %s\n", i + 1, option[i]);
            }
            System.out.print("Choice > ");
            if (scan.hasNextInt())
                input = scan.nextInt();
            else {
                System.out.println("Error input, please try again !");
                scan.next();
                input = option.length + 8;
            }
            if(input > option.length + 1){
                System.out.println();
                System.out.println("Invalid input. Try again.");
                System.out.println();
            }
        }while(input < 0 || input > option.length + 1);
        return input;
    }
}