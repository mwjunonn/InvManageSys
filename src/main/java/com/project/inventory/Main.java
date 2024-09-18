package com.project.inventory;

import java.util.*;

public abstract class Main {
    private static Scanner scan = new Scanner(System.in);
    //static ProcessBuilder processBuilder = new ProcessBuilder(); //For clear screen use, if cannot clear screen delete it
    
    public static void main(String[] args) {        
        Locale.setDefault(Locale.ENGLISH);
        Database.startDatabase();
        mainMenu();
        Database.closeDatabase();
        System.exit(0);
    }
    
    public static void mainMenu(){
        User manager = new Manager();
        int choice = 0;
        
        do{
            try{
                System.out.println("[Welcome to MIXUE Inventory System]");
                System.out.println("-----------------------------------");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Your Choice: ");
                choice = Integer.parseInt(scan.nextLine());
            }catch(NumberFormatException ex){
                System.out.println("Please enter integer only.");
            }

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
        int choice = 0;
        int decision;
        String id;
        String password;
        //If found out how to clear screen, enable this two
        //System.out.println("[Welcome to MIXUE Inventory System]");
        //System.out.println("-----------------------------------");
        do{
            try{
            System.out.println("Login as: ");
            System.out.println("1. Manager");
            System.out.println("2. Inventory Admin");
            System.out.println("3. Return to last page");
            System.out.print("Your choice: ");
            choice = Integer.parseInt(scan.nextLine());
            }catch (NumberFormatException ex){
                System.out.println("Please enter integer only.");
            }
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
                                    inventoryMenu();
                                    break;
                                case 2:     //Current Stock Report
                                    break;
                                case 3:     //Supplier Menu
                                    supplierMenu();
                                    break;
                                case 4:     //All staff details
                                    manager.displayAllUser();
                                    System.out.println("\n");
                                    break;
                                case 5:     //Modify staff details
                                    manager.displayAllUser();
                                    System.out.println("------------------------------------------------------------------------\n");
                                    modifyUser();
                                    break;
                                case 6:     //Delete staff
                                    manager.displayAllUser();
                                    System.out.println("------------------------------------------------------------------------\n");
                                    deleteUser();
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
                    try{
                        System.out.println("1. Inventory");         //order item @ purchase order
                        System.out.println("2. Current Stock Report");
                        System.out.println("3. Display all supplier");      //Not sure put here or wat, people incharge supplier can modify this
                        System.out.println("4. All staff details");
                        System.out.println("5. Modify staff details");
                        System.out.println("6. Delete staff");
                        System.out.println("7. Return to last page");
                        System.out.print("Choice > ");
                        decision = Integer.parseInt(scan.nextLine());
                    }catch(NumberFormatException ex){
                        System.out.println("Please enter integer only.");
                    }
                    if (decision < 1 || decision > 7) {
                        System.out.println();
                        System.out.println("Invalid input. Try again.");
                        System.out.println();
                    }
                    return decision;
                } while (decision < 1 || decision > 7);
            case 2:
                do {
                    try{
                        System.out.println("1. Inventory");
                        System.out.println("2. Purchase order status");
                        System.out.println("3. Return to last page");
                        System.out.print("Choice > ");
                        decision = Integer.parseInt(scan.nextLine());
                    }catch(NumberFormatException ex){
                        System.out.println("Please enter integer only.");
                    }
                    if (decision < 1 || decision > 3) {
                        System.out.println();
                        System.out.println("Invalid input. Try again.");
                        System.out.println();
                    }
                    return decision;
                } while (decision < 1 || decision > 3);
            default:
                break;
        }
        return decision;
    }
    
    public static void modifyUser(){
        Manager manager = new Manager();
        InventoryAdmin inventoryAdmin = new InventoryAdmin();
        
        String modifyID = new String();
        String modifyData = new String();
        String tempPasswordHolder = new String();
        int modifyAttributes;
        int confirmation;
        
        System.out.print("Enter an employee's ID to start modify: ");
        modifyID = scan.nextLine();
        if (!(manager.equals(modifyID)) && !(inventoryAdmin.equals(modifyID))) {
            System.out.println("User not exist.");
        }
        else{
            do {
                System.out.println("You are currently modifying " + 
                        "\nName: " + manager.getCurrentName() + "\tID: " + manager.getCurrentID());
                System.out.println("\nSelect an attribute to modify: ");
                System.out.println("1. Name");
                System.out.println("2. User Password");
                System.out.println("3. Email");
                System.out.println("4. Return to last page");
                System.out.print("> ");
                modifyAttributes = scan.nextInt();
                scan.nextLine();
                switch(modifyAttributes){
                    case 1:
                        System.out.println("Current user name: " + manager.getCurrentName());
                        System.out.print("New user name: ");
                        modifyData = scan.nextLine();
                        System.out.print("Confirm modify? (0 for Yes / 1 for No) > ");
                        confirmation = scan.nextInt();
                        if (confirmation == 0) {
                            manager.setName(modifyData);
                            manager.modifyStaff(modifyID, modifyAttributes);
                            System.out.println("User Name Modified Successfully.");
                        }
                        else
                            System.out.println("Ignore changes.");
                        break;
                    case 2:
                        System.out.println("Current Password: " + manager.getCurrentPassword());
                        System.out.print("New Password: ");
                        modifyData = scan.nextLine();
                        System.out.println("Confirm Password: ");
                        tempPasswordHolder = scan.nextLine();
                        while(!(modifyData.equals(tempPasswordHolder))){
                            System.out.println("Password not match. Try again");
                            System.out.print("New Password: ");
                            modifyData = scan.nextLine();
                            System.out.print("Confirm Password: ");
                            tempPasswordHolder = scan.nextLine();
                        }
                        System.out.print("Confirm modify? (0 for Yes / 1 for No) > ");
                        confirmation = scan.nextInt();
                        if (confirmation == 0) {
                            manager.setPassword(modifyData);
                            manager.modifyStaff(modifyID, modifyAttributes);
                            System.out.println("User Password Modified Successfully.");
                        }
                        else
                            System.out.println("Ignore changes.");
                        break;
                    case 3:
                        System.out.println("Current Email: " + manager.getCurrentEmail());
                        System.out.print("New Email: ");
                        modifyData = scan.nextLine();
                        System.out.print("Confirm modify? (0 for Yes / 1 for No) > ");
                        confirmation = scan.nextInt();
                        if (confirmation == 0) {
                            manager.setEmail(modifyData);
                            manager.modifyStaff(modifyID, modifyAttributes);
                            System.out.println("User Password Modified Successfully.");
                        }
                        else
                            System.out.println("Ignore changes.");
                        break;
                    case 4:
                        System.out.println("Returning to last page.");
                        break;
                    default:
                        System.out.println("Invalid input. Try again!");
                        break;
                }
            }while (modifyAttributes != 4);
            
        }
    }
    
    public static void deleteUser(){
        Manager manager = new Manager();
        InventoryAdmin inventoryAdmin = new InventoryAdmin();
        
        String deleteID = new String();
        int confirmation = 0;
        
        System.out.print("Enter an employee's ID to delete: ");
        deleteID = scan.nextLine();
        if (!(manager.equals(deleteID)) && !(inventoryAdmin.equals(deleteID))) {
            System.out.println("User not exist.");
        }
        else{
            try{
                System.out.println("You are deleting user");
                System.out.println("Name: " + manager.getCurrentName() + "\t" + "ID: " + manager.getCurrentID());
                System.out.println("Confirm delete the user? (0 for yes / 1 for no) > ");
                confirmation = Integer.parseInt(scan.nextLine());
            }catch(NumberFormatException ex){
                System.out.println("Please enter integer only.");
            }
            if (confirmation == 0) {
                System.out.println("Deletion Complete.");
                manager.deleteStaff(deleteID);
            }
            else
                System.out.println("Action Ignore.");
        }
    }

    private static void inventoryMenu(){
        Inventory inventory = Inventory.getInstance();
        Thread.startVirtualThread(inventory);
        String[] option = new String[]{
                "Restock inventory",
                "Create item",
                "Item operation",
                "Exit"
        };
        while(true) {
            System.out.println("Inventory\n---------");
            int input = getMenuInput(option);
            if (--input == -1) {
                inventory.closeInventory();
                return;
            }
            switch (option[input]) {
                case "Restock inventory":
                    int index = selectInventory();
                    if (index != -1)
                        restockInventory(index);
                    break;
                case "Item operation":
                    input = selectInventory();
                    if (input != -1) {
                        inventoryOperation(input);
                    }
                    break;
                case "Create item":
                    createItem();
                    break;
            }
        }
    }

    private static void restockInventory(int index){
        boolean validation;
        Inventory inventory = Inventory.getInstance();
        Item item = inventory.getItem(index);
        double restockQuantity = 0;
        do {
            validation = false;
            System.out.println("Item name = " + item.getItemName());
            System.out.println("Item quantity = " + item.getQuantity());
            System.out.print("Current item quantity = " + item.getQuantity() + "+ ");
            if (scan.hasNextDouble()) {
                restockQuantity = scan.nextDouble();
                validation = true;
            } else {
                scan.next();
                System.out.println("Error input! Please try again.");
            }
        } while (!validation);
        item.setQuantity(item.getQuantity() + restockQuantity);
    }

/*   Deprecated : Replace with selectInventory();
    private static int chooseItem() {
        ArrayList<Item> item = inventory.getItemList();
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
*/

    private static void inventoryOperation(int index) {
        System.out.println("What you would like to do ?");
        String[] option = new String[]{
                "Update item",
                "Delete item",
                "Exit"
        };
        int input = getMenuInput(option);
        if(--input == -1)
            return;
        switch (option[input]) {
            case "Update item":
                updateItem(index);
                break;
            case "Delete item":
                deleteItem(index);
                break;
        }
    }

    private static void updateItem(int index) {
        boolean validation;
        String strInput = "";
        Inventory inventory = Inventory.getInstance();
        Item item = inventory.getItem(index);
        String[] itemDataName = new String[]{
                "Item name",
                "Item type",
                "Price",
                "Quantity",
                "Unit",
                "Exit"
        };
        String[] itemData = new String[]{
                item.getItemName(),
                item.getItemType(),
                String.valueOf(item.getLatestPrice()),
                String.valueOf(item.getQuantity()),
                item.getItemType()
        };
        showItemDetails(itemDataName, itemData);
        //Methods should be start at here
        do {
            validation = true;
            System.out.println();
            System.out.println("What you would like to update ?");
            int input = getMenuInput(itemDataName);
            input--;
            if (input == -1)
                return;
            else if (itemDataName[input].equals("Unit")) {
                System.out.println("Current unit = " + itemData[itemData.length - 1]);
                System.out.print("New unit (Eg : 1kg)= ");
            } else {
                System.out.print("New " + itemDataName[input] + " = ");
                if (itemDataName[input].equals("Price"))
                    System.out.print("RM ");
            }
            strInput = scan.nextLine();
            switch (itemDataName[input]) {
                case "Item name":
                    if(inventory.checkNameUnique(strInput))
                        validation = item.setItemName(strInput);
                    else{
                        System.out.println("Same item already exist! This is the item details...");
                        showItemDetails(inventory.getItem(strInput));
                        validation = false;
                    }
                    break;

                case "Item type":
                    validation = item.setItemType(strInput);
                    break;

                case "Quantity":
                    try {
                        validation = item.setQuantity(Double.parseDouble(strInput));
                    } catch (NumberFormatException e) {
                        System.out.println("Input is not number");
                        validation = false;
                    }
                    break;

                case "Price":
                    try {
                        validation = item.setLatestPrice(Double.parseDouble(strInput));
                    } catch (NumberFormatException e) {
                        System.out.println("Input is not number");
                        validation = false;
                    }
                    break;

                case "Unit":
                    validation = updateUnit(index, strInput);
                    break;
            }
            System.out.println(validation ? "Item updated successfully" : "Item update failed");
        }while (!validation);
    }

    public static boolean updateUnit(int index, String unit){
            int indexOfSplit = Item.indexOfSplit(unit);
            Inventory inventory = Inventory.getInstance();
            if(indexOfSplit == -1) {
                System.out.println("Input is not following the format");
                return false;
            }
            String[] subStr = {unit.substring(0, indexOfSplit), unit.substring(indexOfSplit)};
            if(inventory.getItem(index).getUnit().equalsIgnoreCase(subStr[1])) {
                double adjustQuantity = promptAdjustQuantity(index, Double.parseDouble(subStr[0]));
                if(adjustQuantity != 0.0)
                    inventory.getItem(index).setQuantity(adjustQuantity);
            }
            inventory.getItem(index).setItemUnit(Double.parseDouble(subStr[0]), subStr[1]);
            return true;
    }

    public static double promptAdjustQuantity(int index, double per_unit) {
        Inventory inventory = Inventory.getInstance();
        System.out.println("Looks like same units is entered, do you want to get the adjustment of quantity ?");
        String[] option = new String[]{
                "Yes, I want the latest quantity according to my edited unit.",
                "No, just ignored it."
        };
        int input;
        do {
            input = getMenuInput(option);
        }while(input == 0);
        if(option[--input].equals("Yes, I want the latest quantity according to my edited unit.")) {
                System.out.printf("Current quantity : %.2f/%s\n", inventory.getItem(index).getQuantity(), inventory.getItem(index).getItemUnit());
                System.out.printf("The adjusted quantity is : %.2f/%.2f%s\n", inventory.adjustQuantity(index, per_unit), per_unit, inventory.getItem(index).getUnit());
                System.out.println("Do you accept the adjustment?");
                String[] option2 = new String[]{
                        "Yes", "No"
                };
                input = getMenuInput(option2);
                if(input == 1)
                    return inventory.adjustQuantity(index, per_unit);
        }
        return 0.0;
    }

    private static void deleteItem(int index){
        Inventory inventory = Inventory.getInstance();
        Item item = inventory.getItem(index);
        showItemDetails(new String[]{
                item.getItemName(),
                item.getItemType(),
                String.valueOf(item.getLatestPrice()),
                String.valueOf(item.getQuantity()),
                item.getItemUnit()
        });
        System.out.println("Do you want to delete this item? ");
        System.out.print("If yes, Please type 'Yes' : ");
        String input = scan.nextLine();
        if(input.equals("Yes")){
            inventory.deleteItem(item);
        }
    }

    public static int selectInventory(){
        Inventory inventory = Inventory.getInstance();
        InventoryUI inventoryUI = new InventoryUI(inventory.getItemListWithColumns());
        inventoryUI.inventoryListGui();
        return inventoryUI.getItemSelectedIndex();
    }

    private static void showItemDetails(Item item){
        showItemDetails(new String[]{
                item.getItemName(),
                item.getItemType(),
                String.valueOf(item.getLatestPrice()),
                String.valueOf(item.getQuantity()),
                item.getItemUnit()
        });
    }

    private static void showItemDetails(String[] itemData){
        String[] itemDataName = new String[]{
                "Item name ",
                "Item type",
                "Price",
                "Quantity",
                "Unit"
        };
        showItemDetails(itemDataName, itemData);
    }

    private static void showItemDetails(String[] itemDataName, String[] itemData){
        Inventory inventory = Inventory.getInstance();
        for (int i = 0; i < itemData.length; i++)
            System.out.printf("%s: %s\n", itemDataName[i], itemData[i]);
    }

    private static void createItem(){
        boolean validation;
        Inventory inventory = Inventory.getInstance();
        System.out.println("Please provide the item detail you want to create...");
        System.out.print("Item name : ");
        String itemName = scan.nextLine();
        if(!inventory.checkNameUnique(itemName)){ //If it is not unique, means it have already
            System.out.println("Item has been created before! This is the item detail before created..");
            showItemDetails(inventory.getItem(itemName));
            return;
        }
        System.out.println("Item type: ");
        ArrayList<String> tempOption = new ArrayList<>();
        for (Item.itemTypeConstant i : Item.itemTypeConstant.values()) {
            tempOption.add(i.getValue());
        }
        tempOption.add("Other");
        String itemType;
        int option = getMenuInput(tempOption.toArray(new String[0]));
        if(option == Item.itemTypeConstant.values().length){ // Means other
            System.out.print("Please type the item type: ");
            itemType = scan.nextLine();
        }else{
            itemType = tempOption.get(--option);
        }
        double price = 0;
        do {
            System.out.print("Latest price =  RM ");
            if (scan.hasNextDouble()) {
                price = scan.nextDouble();
                validation = true;
                scan.nextLine(); //Clear the buffer
            } else {
                scan.next();
                System.out.println("Input are not in number...");
                validation = false;
            }
        }while(!validation);
        String[] perUnitAndUnit = new String[0];
        String unit;
        do {
            System.out.print("Please type the unit quantity for this item (Eg: 1kg) : ");
            unit = scan.nextLine();
            int index = Item.indexOfSplit(unit);
            if (index == -1) {
                validation = false;
                System.out.println("Input is not following the format");
            } else {
                validation = true;
                perUnitAndUnit = new String[]{unit.substring(0, index), unit.substring(index)};
            }
        }while(!validation);
        double quantity = 0;
        do{
        System.out.printf("Please type the current item quantity with the unit in %s : ", unit);
        if(scan.hasNextDouble()) {
            validation = true;
            quantity = scan.nextDouble();
        }else{
            scan.next();
            System.out.println("Input is not number");
            validation = false;
        }
        }while(!validation);
        if(inventory.addInventory(new Item(itemName, itemType, perUnitAndUnit[1], price, Double.parseDouble(perUnitAndUnit[0]), quantity))){
            System.out.println("Item created successfully...");
        }else{
            System.out.println("Something went wrong");
        }
    }

    private static int getMenuInput(String[] option) {
        int input = 0;
        int optionLength;
        String exit = "ExitQuitReturn";
        do{
            boolean hasExit = false;
            System.out.println("Please choose your option: ");
            for (int i = 0; i < option.length; i++) {
                if(exit.contains(option[i]) && !hasExit) {
                    System.out.printf("0. %s\n", option[i]);
                    hasExit = true;
                }else {
                    System.out.printf("%d. %s\n", i + 1, option[i]);
                }
            }
            if(hasExit)
                optionLength = option.length - 1;
            else
                optionLength = option.length + 1;
            System.out.print("Choice > ");
            if (scan.hasNextInt())
                input = scan.nextInt();
            else {
                System.out.println("Invalid input. Please try again.");
                scan.next();
                input = option.length + 8;
            }
            if(input > optionLength){
                System.out.println();
                System.out.println("Invalid input. Please try again.");
                System.out.println();
            }
        }while(input < 0 || input > optionLength);
        scan.nextLine(); //Clear the buffer
        return input;
    }

    private static void supplierMenu(){
        int choice = 0;
        Supplier supplierManager = new Supplier();
        SupplyItem supplyItemManager = new SupplyItem();
      

        do{
        String[][] supplier = supplierManager.getAllSupplierInfo();
        ArrayList<ArrayList<Object>> supplyItem = supplyItemManager.getAllSupplyItem();
            try{
                System.out.println("-----------------");
                System.out.println("| Supplier Menu |");
                System.out.println("-----------------");
                System.out.println("1. View All Supplier Details");
                System.out.println("2. View All Supply Item Details");
                System.out.println("3. Add New Supplier");
                System.out.println("4. Add Supply Item Information");
                System.out.println("5. Modify Supplier Information");
                System.out.println("6. Modify Supply Item Information");
                System.out.println("7. Delete Supplier Information");
                System.out.println("8. Delete Supply Item Information");
                System.out.println("9. Exit");
                System.out.print("Enter Your Choice: ");
                choice = Integer.parseInt(scan.nextLine());
            }catch(NumberFormatException ex){
                System.out.println("Error: Your Input Choice Should Be An Integer!");
            }
            switch(choice){
                case 1:
                    displaySupplierInfo(supplierManager,supplier);
                    System.out.println("Press Enter to Continue...");
                    scan.nextLine();
                    break;
                case 2:
                    displayAllSupplyItems(supplyItemManager, supplyItem);
                    System.out.println("Press Enter to Continue...");
                    scan.nextLine();
                    break;
                case 3:
                    createSupplier(supplierManager,supplier);
                    break;
                case 4:
                    createSupplyItem();
                    break;
                case 5:
                    editSupplierInfo(supplierManager,supplier);
                    break;
                case 6:
                    displayAllSupplyItems(supplyItemManager, supplyItem);
                    editSupplyItem();
                    break;
                case 7:
                    deleteSupplierDetails(supplierManager,supplier);
                    break;
                case 8:
                    displayAllSupplyItems(supplyItemManager, supplyItem);
                    deleteSupplyItem();
                    break;
                case 9:
                    break;
                default:
                    System.out.println("Invalid Options! Please Try Again...");
                    break;
            }
        }while(choice != 9);
    }

    private static void displaySupplierInfo(Supplier supplierManager, String[][] supplier) {


        //because the first row is the table columns name
        if (supplier.length == 1) {
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

    private static void createSupplier(Supplier supplierManager, String[][] supplier){
        Supplier tempSupplier;
        SupplyItem supplyItemManager = new SupplyItem();
        String id, name, address, email;
        String type = "";
        String itemID;
        double import_duty = 0.00, shipping_fee = 0.00, cost = 0.00;
        int options = 0, exit = 1;
        boolean supplierInfoValid;
        int itemIndex;

        if(Supplier.numSupplier < 100){


            do{
                id = String.format("S%04d", Supplier.numSupplier);
                System.out.println("ID: " + id);
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


                supplierInfoValid = supplierManager.validateSupplierInfo(id, name, address, email, type);



                if(supplierInfoValid){


                    if(type.equals("Local")){
                        tempSupplier = new LocalSupplier();
                    }else
                        tempSupplier = new ForeignSupplier();

                    tempSupplier.setSupplierId(id);
                    tempSupplier.setSupplierName(name);
                    tempSupplier.setSupplierAddress(address);
                    tempSupplier.setSupplierEmail(email);
                    if(tempSupplier instanceof LocalSupplier){
                        ((LocalSupplier)tempSupplier).setImportDuty(import_duty);
                        ((LocalSupplier)tempSupplier).setSupplierType();
                    }else{
                        ((ForeignSupplier)tempSupplier).setImportDuty(import_duty);
                        ((ForeignSupplier)tempSupplier).setSupplierType();
                    }





                    //choose item and add item
                    supplierManager.addSupplier(tempSupplier);

                    do{
                        System.out.print("Please Enter The Number For The Product That The Supplier Will Be Providing: ");
                        itemIndex = Integer.parseInt(scan.nextLine());

                        itemID = String.format("I%04d", itemIndex);

                        System.out.println("Please Enter The Information: ");
                        System.out.println("Item ID: " + itemID);
                        try{
                            System.out.print("Shipping Fee: RM");
                            shipping_fee = Double.parseDouble(scan.nextLine());
                        }catch(NumberFormatException ex){
                            System.out.println("Error: Cannot Read The Shipping Fee!");
                        }
                        
                        try{
                            System.out.println("Cost: RM");
                            cost = Double.parseDouble(scan.nextLine());
                        }catch(NumberFormatException ex){
                            System.out.println("Error: Cannot Read The Cost !");
                        }


                        supplyItemManager.setSupplierId(id);
                        supplyItemManager.setItemId(itemID);
                        supplyItemManager.setShippingFee(shipping_fee);
                        supplyItemManager.setCost(cost);

                        supplyItemManager.writeData(supplyItemManager);

                        System.out.println();
                        do{
                            try{
                                System.out.println("Do You Want To Add Another Item? ");
                                System.out.println("1. Yes");
                                System.out.println("2. No");
                                System.out.println("Enter Your Choice: ");
                                options = Integer.parseInt(scan.nextLine());
                            }catch(NumberFormatException ex){
                                System.out.println("Error: Your Input Choice Should Be An Integer!");
                            }

                            if(options != 1 && options !=2)
                                System.out.println("Invalid Options! Please Try Again");
                        }while(options != 1 && options != 2);


                    }while(options == 1);

                    //addSupplier(id, name, address, email, type, import_duty);
                    Supplier.numSupplier++;
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

    private static void editSupplierInfo(Supplier supplierManager, String[][] supplier){
        int supplierIndex = 0;
        int options = 0, options2 = 0, exit = 0;
        String temp, columnName, id;
        ForeignSupplier foreignSupplier;
        LocalSupplier localSupplier;

        if(Supplier.numSupplier  != 0){

            do{
                System.out.print("Select the supplier to modify: ");
                supplierIndex = Integer.parseInt(scan.nextLine());


                if(supplierIndex > 0 && supplierIndex < Supplier.numSupplier){
                    do{
                        id = String.format("S%04d", supplierIndex);
                        supplier = supplierManager.getAllSupplierInfo(id);
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


                                if(supplierManager.validateSupplierName(temp)){
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
                                                supplierManager.modifySupplier(columnName, id, temp);
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
                                else{
                                    System.out.println("You will now be exited from this function.");
                                    System.out.println("To try again, please reselect the function from the menu.");
                                    exit = 1;
                                }


                                break;
                            case 2:
                                System.out.print("Enter Address: ");
                                temp = scan.nextLine();

                                if(supplierManager.validateSupplierAddress(temp)){
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
                                                supplierManager.modifySupplier(columnName, id, temp);
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
                                else{
                                    System.out.println("You will now be exited from this function.");
                                    System.out.println("To try again, please reselect the function from the menu.");
                                    exit = 1;
                                }

                                break;
                            case 3:
                                System.out.print("Enter Email Address: ");
                                temp = scan.nextLine();


                                if(supplierManager.validateSupplierEmail(temp)){
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
                                                supplierManager.modifySupplier(columnName, id, temp);
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
                                else{
                                    System.out.println("You will now be exited from this function.");
                                    System.out.println("To try again, please reselect the function from the menu.");
                                     exit = 1;
                                }
                                   

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

    private static void deleteSupplierDetails(Supplier supplierManager, String[][] supplier){
        int supplierIndex;
        int options = 0, exit = 0;
        String id;
  


        if(Supplier.numSupplier != 0){
            do{
                System.out.print("Select the supplier to delete: ");
                supplierIndex = Integer.parseInt(scan.nextLine());
                if(supplierIndex > 0 && supplierIndex < Supplier.numSupplier){
                    do{
                        id = String.format("S%04d", supplierIndex);
                        supplier = supplierManager.getAllSupplierInfo(id);
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
                                supplierManager.deleteSupplier(id);
                                Supplier.numSupplier--;
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

    private static void displayAllSupplyItems(SupplyItem supplyItemManager, ArrayList<ArrayList<Object>> supplyItems){
       
        if(supplyItems.size() == 1){
            System.out.println("No Supply Item Data Found.");
        }else{
            System.out.println();
            System.out.println("Supply Item: ");
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.printf("| %-2s | %-11s | %-10s | %-20s | %-16s | %-12s |\n", "No","Supplier ID", "Item ID", "Item Name" ,"Shipping Fee(RM)", "Cost(RM)");
            System.out.println("------------------------------------------------------------------------------------------");
            for (int i = 1; i < supplyItems.size(); i++) {
                ArrayList<Object> row = supplyItems.get(i);
                System.out.printf("| %-2d | %-11s | %-10s | %-20s | %-16s | %-12s |\n", i, row.get(0).toString(),  row.get(1).toString(), row.get(2).toString(),row.get(3).toString(), row.get(4).toString());
            }

            System.out.println("------------------------------------------------------------------------------------------");
            
        }
    }
    
    private static void editSupplyItem(){
        String supplierId, itemId, shippingFee, cost;
        SupplyItem supplyItemManager = new SupplyItem();
        String[][] supplyItemInfo;
        int options = 0;
        double newValue;
        int supplierIndex = 0, itemIDIndex = 0;
        
        if(SupplyItem.supplyItemNum != 0){
            
                try{
                    System.out.print("Please Enter The Supplier's ID (EG: S0001 - > 1): ");
                    supplierIndex = Integer.parseInt(scan.nextLine());
                }catch(NumberFormatException ex){
                    System.out.println("You Can Only Enter Integer!");
                }
               
                
                if(supplierIndex  > 0 && supplierIndex < Supplier.numSupplier){
                    
                    try{
                     System.out.print("Please Enter The Item's ID (EG: I0001 - > 1): ");
                     itemIDIndex= Integer.parseInt(scan.nextLine());
                    }catch(NumberFormatException ex){
                       System.out.println("You Can Only Enter Integer!");

                    }
                     
                    if(itemIDIndex > 0 && itemIDIndex < SupplyItem.supplyItemNum){
                         supplierId = String.format("S%04d", supplierIndex);
                         itemId =  String.format("I%04d", itemIDIndex);
                         
                        if(supplyItemManager.isSupplierExists(supplierId) && supplyItemManager.isItemExists(itemId)){
                            supplyItemInfo = supplyItemManager.getAllSupplyItem(supplierId, itemId);
                            if(supplyItemInfo != null && supplyItemInfo.length > 1){
                                do{
                                    System.out.println("--------------------------------------------------------------");
                                    System.out.printf("| %-11s | %-10s | %-16s | %-12s |\n", "Supplier ID", "Item ID", "Shipping Fee(RM)", "Cost(RM)");
                                    System.out.println("--------------------------------------------------------------");
                                    System.out.printf("| %-11s | %-10s | %-16s | %-12s |\n",  supplyItemInfo[1][0],  supplyItemInfo[1][1], supplyItemInfo[1][2], supplyItemInfo[1][3]);
                                    System.out.println("--------------------------------------------------------------");


                                 
                                    try{
                                       System.out.println("Select The Data That You Want To Modify");
                                       System.out.println("1. Shipping Fee");
                                       System.out.println("2. Cost");
                                       System.out.print("Enter Your Choice: ");
                                       options = Integer.parseInt(scan.nextLine());
                                    }catch(NumberFormatException ex){
                                       System.out.println("Error: Your Input Choice Should Be An Integer!");
                                    }
                                    switch(options){
                                        case 1:
                                            System.out.print("Enter New Shipping Fee: RM");
                                            newValue = Double.parseDouble(scan.nextLine());
                                            if(newValue > 0 && newValue < 1000){
                                                shippingFee = String.valueOf(newValue);
                                                do{
                                                    System.out.println("---------------------------------------");
                                                    System.out.printf("| %-16s | %-16s |\n", "Old Data", "New Data");
                                                    System.out.println("---------------------------------------");
                                                    System.out.printf("| %-16s | %-16.2f |\n", supplyItemInfo[1][2], newValue);
                                                    System.out.println("---------------------------------------");
                                                    try{
                                                        System.out.println("Do You Confirm The Modification?");
                                                        System.out.println("1. Yes");
                                                        System.out.println("2. No");
                                                        System.out.print("Enter Your Choice: ");
                                                        options = Integer.parseInt(scan.nextLine());
                                                    }catch(NumberFormatException ex){
                                                        System.out.println("Error: Your Input Choice Should Be An Integer!");
                                                    }
                                                    switch(options){
                                                        case 1: 
                                                            supplyItemManager.updateData("shipping_fee", supplierId, itemId, shippingFee);
                                                            break;
                                                        case 2:
                                                            System.out.println("Modification has been canceled. No changes were made.");
                                                            System.out.println("You will now be exited from this function.");
                                                            System.out.println("To try again, please reselect the function from the menu.");
                                                            break;
                                                        default:
                                                            System.out.println("Invalid options! Please Try Again...");
                                                            break;
                                                    }
                                                }while(options != 1 && options != 2);
                                            }else{
                                                System.out.println("Please ensure the shipping fee is within the valid range of RM0 to RM999.");
                                                System.out.println("You will now be exited from this function.");
                                                System.out.println("To try again, please reselect the function from the menu.");
                                            }

                                            break;
                                        case 2:
                                            System.out.print("Enter New Cost: RM");
                                            newValue = Double.parseDouble(scan.nextLine());
                                            if(newValue > 0 && newValue < 1000){
                                                cost = String.valueOf(newValue);

                                                do{
                                                    System.out.println("-------------------------------");
                                                    System.out.printf("| %-12s | %-12s |\n", "Old Data", "New Data");
                                                    System.out.println("-------------------------------");
                                                    System.out.printf("| %-12s | %-12.2f |\n", supplyItemInfo[1][3], newValue);
                                                    System.out.println("-------------------------------");
                                                    try{
                                                        System.out.println("Do You Confirm The Modification?");
                                                        System.out.println("1. Yes");
                                                        System.out.println("2. No");
                                                        System.out.print("Enter Your Choice: ");
                                                        options = Integer.parseInt(scan.nextLine());
                                                    }catch(NumberFormatException ex){
                                                        System.out.println("Error: Your Input Choice Should Be An Integer!");
                                                    }
                                                    switch(options){
                                                        case 1: 
                                                            supplyItemManager.updateData("cost", supplierId, itemId, cost);
                                                            break;
                                                        case 2:
                                                            System.out.println("Modification has been canceled. No changes were made.");
                                                            System.out.println("You will now be exited from this function.");
                                                            System.out.println("To try again, please reselect the function from the menu.");
                                                            break;
                                                        default:
                                                            System.out.println("Invalid options! Please Try Again...");
                                                            break;
                                                    }
                                                }while(options != 1 && options != 2);
                                            }else{
                                                System.out.println("Please ensure the shipping fee is within the valid range of RM0 to RM999.");
                                                System.out.println("You will now be exited from this function.");
                                                System.out.println("To try again, please reselect the function from the menu.");
                                            }
                                            break;
                                        default:
                                            System.out.println("Invalid Options! Please Try Again...");
                                            break;
                                    }

                                 }while(options != 1 && options !=2);
                            }else{
                                 System.out.println("No Matching records found for Supplier's ID or Item's ID!");
                                 System.out.println("You will now be exited from this function.");
                                 System.out.println("To try again, please reselect the function from the menu.");
                            }
                        }else{
                            System.out.println("No Matching records found for Supplier's ID or Item's ID!");
                            System.out.println("You will now be exited from this function.");
                            System.out.println("To try again, please reselect the function from the menu.");
                        }
                    }
                    else{
                        System.out.println("Invalid Item's ID!");
                        System.out.println("You will now be exited from this function.");
                        System.out.println("To try again, please reselect the function from the menu.");
                    }
                     
                }else{
                    System.out.println("Invalid Supplier's ID");
                    System.out.println("You will now be exited from this function.");
                    System.out.println("To try again, please reselect the function from the menu.");
                }
 
            }else{
                System.out.println("No Supply Item Record!");
            }
        }
    
    private static void createSupplyItem(){
        int supplierIndex = 0, itemIDIndex = 0;
        double shippingFee, cost;
        String supplierId, itemId;
        SupplyItem supplyItemManager = new SupplyItem();
        ArrayList<ArrayList<Object>> supplyItem = supplyItemManager.getAllSupplyItem();

     
        try{
            System.out.print("Please Enter The Supplier's ID (EG: S0001 - > 1): ");
            supplierIndex = Integer.parseInt(scan.nextLine());
        }catch(NumberFormatException ex){
            System.out.println("You Can Only Enter Integer!");
        }
        if(supplierIndex > 0 && supplierIndex < Supplier.numSupplier){
            try{
                System.out.print("Please Enter The Item's ID (EG: I0001 - > 1): ");
                itemIDIndex= Integer.parseInt(scan.nextLine());
            }catch(NumberFormatException ex){
                System.out.println("You Can Only Enter Integer!");

            }
            if(itemIDIndex > 0 && itemIDIndex < SupplyItem.supplyItemNum){
                supplierId = String.format("S%04d", supplierIndex);
                itemId =  String.format("I%04d", itemIDIndex);
                
                if(supplyItemManager.isSupplierExists(supplierId) && supplyItemManager.isItemExists(itemId)){
                    
                        System.out.println("Information");
                        System.out.println("-----------");
                        System.out.print("Shipping Fee: RM");
                        shippingFee = Double.parseDouble(scan.nextLine());
                        System.out.print("Cost: RM");
                        cost = Double.parseDouble(scan.nextLine());
                        
                        if(shippingFee > 0 && shippingFee <1000 && cost > 0 && cost <1000){
                            supplyItemManager.setSupplierId(supplierId);
                            supplyItemManager.setItemId(itemId);
                            supplyItemManager.setShippingFee(shippingFee);
                            supplyItemManager.setCost(cost);
                            supplyItemManager.writeData(supplyItemManager);
                            System.out.println("Updated Successfully");
                        }
                        else{
                            System.out.println("Please ensure the shipping fee and cost are within the valid range of RM0 to RM999.");
                            System.out.println("You will now be exited from this function.");
                            System.out.println("To try again, please reselect the function from the menu.");
                        }
           
                }else{
                     System.out.println("No Matching records found for Supplier's ID or Item's ID!");
                     System.out.println("You will now be exited from this function.");
                     System.out.println("To try again, please reselect the function from the menu.");
                }

            }else{
                 System.out.println("Invalid Item's ID!");
                 System.out.println("You will now be exited from this function.");
                 System.out.println("To try again, please reselect the function from the menu.");
            }
        }else{
            System.out.println("Invalid Supplier's ID");
            System.out.println("You will now be exited from this function.");
            System.out.println("To try again, please reselect the function from the menu.");
        }
        
    } 
    
    private static void deleteSupplyItem(){
        int supplierIndex = 0, itemIDIndex = 0;
        String supplierId, itemId;
        SupplyItem supplyItemManager = new SupplyItem();
        ArrayList<ArrayList<Object>> supplyItem = supplyItemManager.getAllSupplyItem();
        
        if(SupplyItem.supplyItemNum > 0){
            try{
                System.out.print("Please Enter The Supplier's ID (EG: S0001 - > 1): ");
                supplierIndex = Integer.parseInt(scan.nextLine());
            }catch(NumberFormatException ex){
                System.out.println("You Can Only Enter Integer!");
            }
            if(supplierIndex > 0 && supplierIndex < Supplier.numSupplier){
                try{
                    System.out.print("Please Enter The Item's ID (EG: I0001 - > 1): ");
                    itemIDIndex= Integer.parseInt(scan.nextLine());
                }catch(NumberFormatException ex){
                    System.out.println("You Can Only Enter Integer!");

                }
                if(itemIDIndex > 0 && itemIDIndex < SupplyItem.supplyItemNum){
                    supplierId = String.format("S%04d", supplierIndex);
                    itemId =  String.format("I%04d", itemIDIndex);

                    if(supplyItemManager.isSupplierExists(supplierId) && supplyItemManager.isItemExists(itemId)){
                        supplyItemManager.deleteSupplyItem(supplierId, itemId);
                        SupplyItem.supplyItemNum--;
                    }else{
                        System.out.println("No Matching records found for Supplier's ID or Item's ID!");
                        System.out.println("You will now be exited from this function.");
                        System.out.println("To try again, please reselect the function from the menu.");
                    }
                }else{
                    System.out.println("Invalid Item's ID!");
                    System.out.println("You will now be exited from this function.");
                    System.out.println("To try again, please reselect the function from the menu.");
                }
            }else{
                System.out.println("Invalid Supplier's ID");
                System.out.println("You will now be exited from this function.");
                System.out.println("To try again, please reselect the function from the menu.");
            }
        }else{
            System.out.println("No Supply Item Record...");
        }
        
    }   
}