package com.project.inventory;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public abstract class Main {
    private static Scanner scan = new Scanner(System.in);
    private static User user;

    public static void main(String[] args) {        
        Locale.setDefault(Locale.ENGLISH);
        Database.startDatabase();
        mainMenu();
        Database.closeDatabase();
        System.exit(0);
    }
    
    public static void mainMenu(){
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
            System.out.println();
            switch(choice){
                case 1:
                    if (loginMenu()) {
                        menu();
                    }
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

    public static boolean loginMenu() {
        int choice = 0;
        int decision;
        String id = "";
        String password = "";
        //If found out how to clear screen, enable this two
        //System.out.println("[Welcome to MIXUE Inventory System]");
        //System.out.println("-----------------------------------");
        do {
            try {
                System.out.println("Login as: ");
                System.out.println("1. Manager");
                System.out.println("2. Inventory Admin");
                System.out.println("3. Return to last page");
                System.out.print("Your choice: ");
                choice = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter integer only.");
            }
            switch (choice) {
                case 1:
                    user = new Manager();
                    System.out.println("Login as Manager");
                    System.out.print("ID: ");
                    id = scan.nextLine();
                    System.out.print("Password: ");
                    password = scan.nextLine();
                    System.out.println();
                    break;
                case 2:
                    user = new InventoryAdmin();
                    System.out.println("Login as Inventory Admin");
                    System.out.print("ID: ");
                    id = scan.nextLine();
                    System.out.print("Password: ");
                    password = scan.nextLine();
                    System.out.println();
                    break;
                case 3:
                    return false;
                default:
                    System.out.println("Invalid Input! Try Again");
                    break;
            }
        } while (choice < 1 || choice > 3);
        if (user.equals(id) && user.passwordValid(password)) {
            System.out.println("Login Successful");
            System.out.println("Welcome " + user.getCurrentName() + ".");
            System.out.println("---------------------------------------");
            return true;
        } else if (user.equals(id) && !(user.passwordValid(password))) {
            System.out.println("Wrong Password!");
        } else
            System.out.println("Login Failed");
        System.out.println();
        return false;
    }

    public static void menu() {
        int decision;
        do {
            decision = permissionMenu(user.permission());
            if (user.permission().equals(User.Permission.FULL_CONTROL)) {
                Manager manager = (Manager) user;
                switch (decision) {
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
            } else if (user.permission().equals(User.Permission.ADMIN)) {
                Order order = new Order();
                switch (decision) {
                    case 1:     //Restock inventory
                        generatePurchaseOrder();
                        break;
                    case 2:     //Purchase order status
                        poMenu();
                        break;
                    case 3:
                        orderMenu();
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        System.out.println("Returning to last page.");
                        break;
                    default:
                        break;
                }
            }
        } while (decision != 7);
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
    
    public static int permissionMenu(User.Permission permission){
        int decision = 0;
        switch (permission){
            // 1 = Manager, 2 = Inventory Admin
            case FULL_CONTROL:
                do {
                    try{
                        System.out.println("1. Inventory");         //order item @ purchase order
                        System.out.println("2. Current Stock Report");
                        System.out.println("3. Supplier Menu");    
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
            case ADMIN:
                do {
                    try{
                        System.out.println("1. Inventory");
                        System.out.println("2. Purchase Order Menu");
                        System.out.println("3. Order Menu");
                        System.out.println("7. Return to last page.");
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
        ArrayList<Supplier> suppliers = supplierManager.getAllSupplierInfo();
        ArrayList<SupplyItem> supplyItems = supplyItemManager.getAllSupplyItem();
        
            try{
                System.out.println("-----------------");
                System.out.println("| Supplier Menu |");
                System.out.println("-----------------");
                System.out.println("1. Supplier Details");
                System.out.println("2. Supply Item Details");
                System.out.println("3. Exit");
                System.out.print("Enter Your Choice: ");
                choice = Integer.parseInt(scan.nextLine());
            }catch(NumberFormatException ex){
                System.out.println("Error: Your Input Choice Should Be An Integer!");
            }
            switch(choice){
                case 1:
                    do{
                        try{
                            System.out.println("-----------------");
                            System.out.println("| Supplier Menu |");
                            System.out.println("-----------------");
                            System.out.println("1. View All Supplier Details");
                            System.out.println("2. Add New Supplier");
                            System.out.println("3. Modify Supplier Information");
                            System.out.println("4. Delete Supplier Information");
                            System.out.println("5. Exit");
                            System.out.print("Enter Your Choice: ");
                            choice = Integer.parseInt(scan.nextLine());
                        }catch(NumberFormatException ex){
                            System.out.println("Error: Your Input Choice Should Be An Integer!");
                        }
                        switch(choice){
                            case 1: 
                                displaySupplierInfo(supplierManager);
                                System.out.println("Press Enter to Continue...");
                                scan.nextLine();
                                break;
                            case 2:
                                createSupplier(supplierManager);
                                break;
                            case 3:
                                displaySupplierInfo(supplierManager);
                                editSupplierInfo(supplierManager);
                                break;
                            case 4:
                                deleteSupplierDetails(supplierManager);
                                break;
                            case 5:
                                break;
                            default:
                                System.out.println("Invalid Options! Please Try Again");
                                System.out.println();
                                break;
                        }
                    }while(choice != 5);
                    break;
                case 2:
                    do{
                        try{
                            System.out.println("--------------------");
                            System.out.println("| Supply Item Menu |");
                            System.out.println("--------------------");
                            System.out.println("1. View All Supply Item Details");
                            System.out.println("2. Add Supply Item Information");
                            System.out.println("3. Modify Supply Item Information");
                            System.out.println("4. Delete Supply Item Information");
                            System.out.println("5. Exit");
                            System.out.print("Enter Your Choice: ");
                            choice = Integer.parseInt(scan.nextLine());

                        }catch(NumberFormatException ex){
                            System.out.println("Error: Your Input Choice Should Be An Integer!");
                        }
                        switch(choice){
                            case 1:
                                displayAllSupplyItems(supplyItemManager);
                                System.out.println("Press Enter to Continue...");
                                scan.nextLine();
                                break;
                            case 2:
                                createSupplyItem(supplyItemManager);
                                break;
                            case 3:
                                displayAllSupplyItems(supplyItemManager);
                                editSupplyItem();
                                break;
                            case 4:
                                displayAllSupplyItems(supplyItemManager);
                                deleteSupplyItem(supplyItemManager);
                                break;
                            case 5: 
                                break;
                            default:
                                System.out.println("Invalid Options! Please Try Again");
                                System.out.println();
                                break;
                        }
                    }while(choice != 5);
                    break;
                case 3:
                    break;
                    
                default:
                    System.out.println("Invalid Options! Please Try Again");
                    System.out.println();
                    break;

            }
            
           
        }while(choice != 3);
    }

    private static void displaySupplierInfo(Supplier supplierManager) {
        
        ArrayList<Supplier> suppliers = supplierManager.getAllSupplierInfo();


        if (suppliers.isEmpty()) {
            System.out.println("No supplier information found.");

        }else{

            System.out.println("Supplier Information:");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-2s | %-11s | %-25s | %-78s | %-30s | %-13s | %-15s |\n", "No" ,"Supplier ID", "Supplier Name", "Address", "Email Address", "Supplier Type", "Import Duty(RM)");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            


            for (int i = 0; i < suppliers.size(); i++) {
                Supplier supplier = suppliers.get(i);

                System.out.printf("| %-2d ", (i + 1));
                System.out.println(supplier.toString());
                System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            }            

            

        }
    }

    private static void createSupplier(Supplier supplierManager){
        Supplier tempSupplier;
        SupplyItem supplyItemManager = new SupplyItem();
        String id ="", name, address, email;
        String type = "";
        double import_duty = 0.00, shipping_fee = 0.00;
        int options = 0, exit = 1;
        boolean supplierInfoValid;
        int itemIndex;
        Inventory inventory = Inventory.getInstance();


        if(Supplier.numSupplier < 100){


            do{
                id = generateSupplierID(supplierManager);
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
                        System.out.println("Please Select Item...");
                        itemIndex = selectInventory();
                        if(itemIndex != -1){
                            Item item = inventory.getItem(itemIndex);
                            //System.out.print("Please Enter The Number For The Product That The Supplier Will Be Providing: ");
                            //itemIndex = Integer.parseInt(scan.nextLine());

                            //itemID = String.format("I%04d", itemIndex);

                            System.out.println("Please Enter The Information: ");
                            System.out.println("Item ID: " + item.getItemId());
                            System.out.println("Cost: RM" + item.getLatestPrice());

                            try{
                                System.out.print("Shipping Fee: RM");
                                shipping_fee = Double.parseDouble(scan.nextLine());
                            }catch(NumberFormatException ex){
                                System.out.println("Error: Cannot Read The Shipping Fee!");
                            }

                            
                                


                            supplyItemManager.setSupplierId(id);
                            supplyItemManager.setItemId(item.getItemId());
                            supplyItemManager.setShippingFee(shipping_fee);
                            supplyItemManager.setCost(item.getLatestPrice());

                            if(supplyItemManager.writeData(supplyItemManager)){
                                System.out.println("Data Has Added.");        
                                System.out.println();
                                do{
                                    try{
                                        System.out.println("Do You Want To Add Another Item? ");
                                        System.out.println("1. Yes");
                                        System.out.println("2. No");
                                        System.out.print("Enter Your Choice: ");
                                        options = Integer.parseInt(scan.nextLine());
                                    }catch(NumberFormatException ex){
                                        System.out.println("Error: Your Input Choice Should Be An Integer!");
                                    }

                                    if(options != 1 && options !=2)
                                        System.out.println("Invalid Options! Please Try Again");
                                }while(options != 1 && options != 2);
                            }
                        }


                    }while(options == 1);

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
    
    private static String generateSupplierID(Supplier supplierManager){
        String id = "";
        for(int i = 1; i < 100; i++){
                    id = String.format("S%04d", i );
                    if(!supplierManager.isSupplierExists(id)){
                        return id;
                    }
        }
        return id;
    }

    private static void editSupplierInfo(Supplier supplierManager){
        int options = 0, options2 = 0, exit = 0;
        String temp, columnName, id;
        ForeignSupplier foreignSupplier;
        LocalSupplier localSupplier;
        ArrayList<Supplier> suppliers = supplierManager.getAllSupplierInfo();


        if(Supplier.numSupplier  != 0){

            do{
                System.out.print("Please Enter The Supplier's ID to Modify: ");
                id = scan.nextLine();
                


                if(supplierManager.isSupplierExists(id)){
                    Supplier supplier = supplierManager.getAllSupplierInfo(id);
                    
                    if(supplier != null){
                        do{

                            System.out.println("Supplier Information:");
                            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                            System.out.println(supplier.toString());
                            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

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
                                            for(int i = 0; i < 57; i++)
                                                System.out.print("-");
                                            System.out.println();
                                            System.out.printf("| %-25s | %-25s |\n", "Old Name", "New Name");
                                            for(int i = 0; i < 57; i++)
                                                System.out.print("-");
                                            System.out.println();
                                            System.out.printf("| %-25s | %-25s |\n", supplier.getSupplierName(), temp);
                                            for(int i = 0; i < 57; i++)
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
                                                    supplierManager.modifySupplier(columnName, id, temp);
                                                    exit = 1;
                                                    break;
                                                case 2:
                                                    System.out.println("The Modification Is Cancelling...");
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
                                            System.out.printf("| %-78s | %-78s |\n", supplier.getSupplierAddress(), temp);
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
                                                    supplierManager.modifySupplier(columnName, id, temp);
                                                    exit = 1;
                                                    break;
                                                case 2:
                                                    System.out.println("The Modification Is Cancelling...");
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
                                            for(int i = 0; i < 67; i++)
                                                System.out.print("-");
                                            System.out.println();
                                            System.out.printf("| %-30s | %-30s |\n", "Email Address", "Email Address");
                                            for(int i = 0; i < 67; i++)
                                                System.out.print("-");
                                            System.out.println();
                                            System.out.printf("| %-30s | %-30s |\n", supplier.getSupplierEmail(), temp);
                                            for(int i = 0; i < 67; i++)
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
                                                    supplierManager.modifySupplier(columnName, id, temp);
                                                    exit = 1;
                                                    break;
                                                case 2:
                                                    System.out.println("The Modification Is Cancelling...");
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
                    }else{
                        exit =1;
                        System.out.println("No Such Supplier ID....");
                        System.out.println("You will now be exited from this function.");
                        System.out.println("To try again, please reselect the function from the menu.");
                    }

                }
                else{
                    do{
                        System.out.println("No Such Supplier ID....");
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
        else{
            System.out.println("There Is No Supplier Record...");
        }


    }

    private static void deleteSupplierDetails(Supplier supplierManager){
        int options = 0, exit = 0;
        String id;
        SupplyItem supplyMainManager = new SupplyItem();
  


        if(Supplier.numSupplier != 0){
            do{
                displaySupplierInfo(supplierManager);
                System.out.print("Please Enter The Supplier's ID to Delete): ");
                id = scan.nextLine();
                if(supplierManager.isSupplierExists(id)){
                    do{
                        Supplier supplier = supplierManager.getAllSupplierInfo(id);
                        System.out.println("Supplier Information: ");
                        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.println(supplier.toString());
                        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        try{
                            System.out.printf("Do you confirm to delete Supplier'ID : %s ?\n", supplier.getSupplierId());
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            System.out.print("Enter Your Choice: ");
                            options = Integer.parseInt(scan.nextLine());
                        }catch(NumberFormatException ex){
                            System.out.println("Error: Your Input Choice Sholud Be An Integer:!");
                        }
                        switch(options){
                            case 1:
                                supplyMainManager.deleteSupplyItem(id);
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
                }else{
                    System.out.println("No Such Supplier ID...");   
                    exit = 1;
                }
            }while(exit == 0);
        }
        else{
            System.out.println("There Is No Supplier Record...");

        }

    }

    private static void displayAllSupplyItems(SupplyItem supplyItemManager){
        ArrayList<SupplyItem> supplyItems = supplyItemManager.getAllSupplyItem(); 

        
        
        if(supplyItems.isEmpty()){
            System.out.println("No Supply Item Data Found.");
        }else{
            System.out.println();
            System.out.println("Supply Item: ");
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.printf("| %-2s | %-11s | %-10s | %-20s | %-16s | %-12s |\n", "No","Supplier ID", "Item ID", "Item Name" ,"Shipping Fee(RM)", "Cost(RM)");
            System.out.println("------------------------------------------------------------------------------------------");
            
    
            for (int i = 1; i < supplyItems.size(); i++) {
                SupplyItem supplyItem = supplyItems.get(i);
                System.out.printf("| %-2d |", i);
                System.out.println(supplyItem.toString2());
                System.out.println("------------------------------------------------------------------------------------------");
            }
   
            
        }
    }
    
    private static void editSupplyItem(){
        String supplierId, shippingFee;
        SupplyItem supplyItemManager = new SupplyItem();
        SupplyItem supplyItemInfo;
        int options = 0;
        double newValue;
        int itemIndex;
        Inventory inventory = Inventory.getInstance();

        if(SupplyItem.supplyItemNum != 0){
        
            
            System.out.print("Please Enter The Supplier's ID (EG: S0001 - > 1): ");
            supplierId = scan.nextLine();
            
            itemIndex = selectInventory();
            
            if(itemIndex!= -1){
    
                Item item = inventory.getItem(itemIndex);
                
             
                         
                    if(supplyItemManager.isSupplierExists(supplierId)){
                        supplyItemInfo = supplyItemManager.getAllSupplyItem(supplierId, item.getItemId());
                        if(supplyItemInfo != null){
                            do{
                                System.out.println("--------------------------------------------------------------");
                                System.out.printf("| %-11s | %-10s | %-16s | %-12s |\n", "Supplier ID", "Item ID", "Shipping Fee(RM)", "Cost(RM)");
                                System.out.println("--------------------------------------------------------------");
                                System.out.printf(supplyItemInfo.toString());
                                System.out.println("--------------------------------------------------------------");

                                try{
                                    System.out.println("Select The Data That You Want To Modify");
                                    System.out.println("1. Shipping Fee");
                                    System.out.println("2. Exit");
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
                                                System.out.printf("| %-16.2f | %-16.2f |\n", supplyItemInfo.getShippingFee(), newValue);
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
                                                        supplyItemManager.updateData("shipping_fee", supplierId, item.getItemId(), shippingFee);
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
                                        System.out.println("Exit The Function...");
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
                    
        }else{
           System.out.println("There Is No Supply Item Record...");
        }
    }
    
    private static void createSupplyItem(SupplyItem supplyItemManager){
        int itemIndex;
        double shippingFee;
        String supplierId;
        Inventory inventory = Inventory.getInstance();

        
        
        System.out.print("Please Enter The Supplier's ID : ");
        supplierId = scan.nextLine();
        
        
        itemIndex = selectInventory();
        
        if(itemIndex!= -1){
        
            Item item = inventory.getItem(itemIndex);

            if(supplyItemManager.isSupplierExists(supplierId)){

                System.out.println("Information");
                System.out.println("-----------");
                System.out.println("Cost: RM" + item.getLatestPrice());
                System.out.print("Shipping Fee: RM");
                shippingFee = Double.parseDouble(scan.nextLine());
                      
                if(shippingFee > 0 && shippingFee <1000){
                    supplyItemManager.setSupplierId(supplierId);
                    supplyItemManager.setItemId(item.getItemId());
                    supplyItemManager.setShippingFee(shippingFee);
                    supplyItemManager.setCost(item.getLatestPrice());
                    if(supplyItemManager.writeData(supplyItemManager)){
                        System.out.println("Data Added Successfully"); 
                    }
                }
                else{
                    System.out.println("Please ensure the shipping fee is within the valid range of RM0 to RM999.");
                    System.out.println("You will now be exited from this function.");
                    System.out.println("To try again, please reselect the function from the menu.");
                }

            }else{
                System.out.println("No Matching records found for Supplier's ID or Item's ID!");
                System.out.println("You will now be exited from this function.");
                System.out.println("To try again, please reselect the function from the menu.");
            }   
        }
        
     
    } 
    
    private static void deleteSupplyItem(SupplyItem supplyItemManager){
        int itemIndex = 0, options = 0;
        String supplierId;
        SupplyItem supplyItem;
        Inventory inventory = Inventory.getInstance();

        
        if(SupplyItem.supplyItemNum !=0 ){
            System.out.print("Please Enter The Supplier's ID : ");
            supplierId = scan.nextLine();
            System.out.println("Please Select Item...");
            itemIndex = selectInventory();
            
            if(itemIndex != -1){
                Item item = inventory.getItem(itemIndex);

                if(supplyItemManager.isSupplierExists(supplierId)){
                    do{
                        supplyItem = supplyItemManager.getAllSupplyItem(supplierId, item.getItemId());
                        if(supplyItem != null){


                            System.out.println("--------------------------------------------------------------");
                            System.out.printf("| %-11s | %-10s | %-16s | %-12s |\n", "Supplier ID", "Item ID", "Shipping Fee(RM)", "Cost(RM)");
                            System.out.println("--------------------------------------------------------------");
                            System.out.printf(supplyItem.toString());
                            System.out.println("--------------------------------------------------------------");
                            try{
                                System.out.println("Do You Confirm To Delete The Record?");
                                System.out.println("1. Yes");
                                System.out.println("2. No");
                                System.out.print("Enter Your Choice: ");
                                options = Integer.parseInt(scan.nextLine());
                            }catch(NumberFormatException ex){
                                System.out.println("Error: Your Input Choice Should Be An Integer!");
                            }
                            switch(options){
                                case 1:
                                    supplyItemManager.deleteSupplyItem(supplierId, item.getItemId());
                                    SupplyItem.supplyItemNum--;
                                    break;
                                case 2:
                                    break;
                                default: 
                                    System.out.println("Invalid Option! Please Try Again");
                                    break;
                            }
                        }
                        else{
                            System.out.println("No Matching Records Found ");
                            System.out.println("You will be now exited from this function.");
                            System.out.println("To Try Again, please reselect the function from the menu.");
                            options = 2;
                        }
                    }while(options!= 1 && options!=2);

                }else{
                    System.out.println("No Matching records found for Supplier's ID or Item's ID!");
                    System.out.println("You will now be exited from this function.");
                    System.out.println("To try again, please reselect the function from the menu.");
                }
            }
        }
        else{
            System.out.println("There Is No Record For Supply Item...");
        }
   
    }
    
    
    //------------------------------------------------------------------------------------
    private static void poMenu(){
        int choice = 0;

        do{
            try{
                System.out.println("-----------------------");
                System.out.println("| Purchase Order Menu |");
                System.out.println("-----------------------");
                System.out.println("1. Order Items");
                System.out.println("2. Display Purchase order");
                System.out.println("3. Update Purchase Order");
                System.out.println("4. Delete Purchase order");
                System.out.println("5. Back");
                System.out.print("Enter Your Choice: ");
                choice = Integer.parseInt(scan.nextLine());
            }catch(NumberFormatException ex){
                System.out.println("Error: Your Input Choice Should Be An Integer!");
            }
            switch(choice){
                case 1:
                    generatePurchaseOrder();
                    break;
                case 2:
                    displayPOMenu();
                    break;
                case 3:
                    updatePOMenu();
                    break;
                case 4:
                    deletePOMenu();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid Options! Please Try Again...");
                    break;
            }
        }while(choice != 5);
    }
    
     private static void generatePurchaseOrder(){
            PurchaseOrder purchaseorder = new PurchaseOrder();
            String orderNo = purchaseorder.getNextOrderNumber();
            purchaseorder.createPurchaseOrder(orderNo, new Date(), user.getCurrentID(), "Ordering");
            generateOrder(orderNo);
            
            if(!purchaseorder.updatePurchaseOrder(orderNo, new Date(), user.getCurrentID(), "Pending Shipping", 0)){
                System.out.println("Insertion of Purchase Order Failed!");
            }
            else{
                System.out.println("Insertion of Purchase Order Succeed!");
            }
            enterToContinue();
        }
    
     private static void displayPOMenu(){
            System.out.println("1. Display All Purchase Orders");
            System.out.println("2. Display by Search Order No");
            System.out.print("Choice > ");
            int choice = scan.nextInt();
            switch(choice){
                case 1:
                    displayPO();
                    scan.nextLine();
                    enterToContinue();
                    break;
                case 2:
                    displayPOSearch();
                    enterToContinue();
                    break;
                default:
                    break;
            }
        }
    
        private static void displayPO() {
            PurchaseOrder purchaseorder = new PurchaseOrder();
            ArrayList<PurchaseOrder> orders = purchaseorder.getAllPurchaseOrders();
            if (orders.isEmpty()) {
                System.out.println("No purchase orders found.");
            } else {
                System.out.println("--------------------------------------------------------------------------------------");
                System.out.printf("| %-10s | %-15s | %-10s | %-20s | %-10s |\n", "Order No", "Order Date", "User ID", "Status", "Total Cost (RM)");
                System.out.println("--------------------------------------------------------------------------------------");

                for (PurchaseOrder po : orders) {
                    System.out.println(po.toString());
                }
                System.out.println("--------------------------------------------------------------------------------------");
            }
        }
        
        private static void displayPOSearch(){
            PurchaseOrder purchaseorder = new PurchaseOrder();
            String orderNo;
            scan.nextLine();
            System.out.print("Please enter orderNo to find your purchase order: ");
            orderNo = scan.nextLine();
            
            if (!orderNo.matches("^OD\\d{4}$")) {
                System.out.println("Invalid order number format. It should start with 'OD' followed by exactly 4 digits (e.g., OD0001).");
                return;  
            }
            
            
            ArrayList<PurchaseOrder> orders = purchaseorder.getPurchaseOrder(orderNo);

           if (orders.isEmpty()) {
                System.out.println("No purchase orders found.");
            } else {
                System.out.println("--------------------------------------------------------------------------------------");
                System.out.printf("| %-10s | %-15s | %-10s | %-20s | %-10s |\n", "Order No", "Order Date", "User ID", "Status", "Total Cost (RM)");
                System.out.println("--------------------------------------------------------------------------------------");

                for (PurchaseOrder po : orders) {
                    System.out.println(po.toString());
                }
                System.out.println("--------------------------------------------------------------------------------------");
            }
       }
        
        private static void updatePOMenu(){
            PurchaseOrder purchaseorder = new PurchaseOrder();
            boolean success;
            double newTotalCost;
            String dateInput;

            System.out.print("Enter the order number of the purchase order to update: ");
            String orderNo = scan.nextLine();

            if (!orderNo.matches("^OD\\d{4}$")) {
                System.out.println("Invalid order number format. It should start with 'OD' followed by exactly 4 digits (e.g., OD0001).");
                return;
            }
            
            if(!isOrderNoExist(orderNo)){
                System.out.println("Order Number doesnt exist!");
                return;
            }
            
            System.out.println("What would you like to update?");
            System.out.println("1. Order Date\n2. Status\n3. Total Cost\n4. Update All at Once\n5. Back");
            System.out.print("Enter your choice > ");
            int choice = scan.nextInt();
            scan.nextLine();
            
            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice. Please select a valid option.");
                return;
            }
            switch(choice){
                case 1:
                    System.out.print("Enter new order date (yyyy-MM-dd): ");
                    dateInput = scan.nextLine();
                    Date newOrderDate = null;
                    try {
                        newOrderDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateInput);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                        return;
                    }
                    success = purchaseorder.updatePurchaseOrder(orderNo, newOrderDate,null, null, 0);
                    if (success) {
                        System.out.println("Order date updated successfully.");
                    } else {
                        System.out.println("Failed to update order date.");
                    }
                    enterToContinue();
                    break;
                case 2:
                    updateStatus(orderNo);
                    break;
                case 3:
                    System.out.print("Enter new total cost: ");
                    newTotalCost = scan.nextDouble();
                    success = purchaseorder.updatePurchaseOrder(orderNo, null, null, null, newTotalCost);
                    if (success) {
                        System.out.println("Total Cost updated successfully.");
                    } else {
                        System.out.println("Failed to update Total Cost.");
                    }
                    enterToContinue();
                    break;
                case 4: 
                    System.out.print("Enter new order date (yyyy-MM-dd):");
                    dateInput = scan.nextLine();

                    try {
                        newOrderDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateInput);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format.");
                        return;
                    }

                    System.out.print("Enter new status:");
                    String newStatus = scan.nextLine();

                    System.out.print("Enter new total cost:");
                    newTotalCost = scan.nextDouble();

                    success = purchaseorder.updatePurchaseOrder(orderNo, newOrderDate,null, newStatus, newTotalCost);
                    if (success) {
                        System.out.println("Purchase order updated successfully.");
                    } else {
                        System.out.println("Failed to update purchase order.");
                    }
                    enterToContinue();
                    break;
                case 5:
                    break;
            }
        }
        
        private static boolean isOrderNoExist(String orderNo){
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            ArrayList<PurchaseOrder> purchaseorder = purchaseOrder.getAllPurchaseOrders();
            for (PurchaseOrder purchaseorders : purchaseorder) {
                if(purchaseorders.getOrderNo().equals(orderNo)){
                    return true;
                }
            }
            return false;
        }
        
        private static void updateStatus(String orderNo) {
            PurchaseOrder purchaseorder = new PurchaseOrder();
            int choice = 0;
            String newStatus = "";
            while (true) {
                System.out.println("1. Pending Shipping \n2. Out for delivery \n3. Delivered \n4. Return to last page");
                System.out.print("Choose a new status: ");
                choice = scan.nextInt();

                if (choice >= 1 && choice <= 4) {
                    switch (choice) {
                        case 1:
                            newStatus = "Pending Shipping";
                            break;
                        case 2:
                            newStatus = "Out for delivery";
                            break;
                        case 3:
                            newStatus = "Delivered";
                            boolean success = purchaseorder.updateInventoryQuantity(orderNo);
                            if (success){
                                System.out.println("Inventory updated successfully for order number: " + orderNo);
                            }
                            else{
                                System.out.println("Update of quantitiy failed!");
                            }
                            break;
                        case 4:
                            System.out.println("Returning to the last page...");
                            return; 
                    }
                    break; 
                } else {
                    System.out.println("Invalid choice! Please enter a number between 1 and 4.");
                }
            }

            // Proceed with the new status or return logic
            if (choice != 4) {
                System.out.println("New status set to: " + newStatus);
            }
            
            boolean success = purchaseorder.updatePurchaseOrder(orderNo, null,null, newStatus, 0);
            if (success) {
                System.out.println("Status updated successfully.");
            } else {
                System.out.println("Failed to update status.");
            }
            enterToContinue();
        }
       
        private static void deletePOMenu(){
            PurchaseOrder purchaseorder = new PurchaseOrder();
            displayPO();
            System.out.print("Enter the Order No to delete: ");
            String orderNo = scan.nextLine().trim();

            if (!orderNo.matches("OD\\d{4}")) { 
                System.out.println("Invalid order number format. It should start with 'OD' followed by exactly 4 digits (e.g., OD0001).");
                return;
            }
            
            purchaseorder.deletePurchaseOrder(orderNo);
            enterToContinue();
        }
        
        private static void orderMenu(){
            int choice = 0;
            do{
                try{
                    System.out.println("--------------");
                    System.out.println("| Order Menu |");
                    System.out.println("--------------");
                    System.out.println("1. Order Items");
                    System.out.println("2. Display Order");
                    System.out.println("3. Update Order");
                    System.out.println("4. Delete Order");
                    System.out.println("5. Back");
                    System.out.print("Enter Your Choice: ");
                    choice = Integer.parseInt(scan.nextLine());
                }catch(NumberFormatException ex){
                    System.out.println("Error: Your Input Choice Should Be An Integer!");
                }
                switch(choice){
                    case 1:
                        generatePurchaseOrder();
                        break;
                    case 2:
                        displayOrderMenu();
                        break;
                    case 3:
                        displayOrder();
                        updateOrder();
                        break;
                    case 4:
                        displayOrder();
                        deleteOrder();
                        break;
                    case 5:
                        break;
                    default:
                        System.out.println("Invalid Options! Please Try Again...");
                        break;
                }
            }while(choice != 5);
        }
        
         private static void generateOrder(String orderNo) {
            Order order = new Order();
            String itemId;
            String choice = "y";
            String supplierId;
            double totalCost;
            double purchaseOrderTotalCost = 0.0;
            do {
                    System.out.print("Enter item ID to restock(999 to stop): ");
                    itemId = scan.nextLine().trim();

                    if (itemId.equals("999")){
                        System.out.println("Restocking stopped!");
                        break;
                    }

                    if (!isItemExists(itemId)) {
                    System.out.println("Item not found.");
                    continue;
                    }
                    System.out.println("Item found...");


                    System.out.print("Enter quantity to add: ");
                    int quantityToAdd;
                    try {
                        quantityToAdd = Integer.parseInt(scan.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity. Please enter a number.");
                        continue;
                    }

                    if (quantityToAdd <= 0) {
                        System.out.println("Quantity must be greater than zero.");
                        return;
                    }

                    supplierId = getSupplierIdbyItemId(itemId);

                    totalCost = (getItemCost(itemId) * quantityToAdd) + (getShippingFee(itemId) * quantityToAdd) + getImportDuty(supplierId);
                    purchaseOrderTotalCost += totalCost;

                    //po.updateTotalCost(orderNo, purchaseOrderTotalCost);
                    // Formula (quantity * price) + (shipping fee per kg * quantity) + import fee
                    //Hvaent done
                    System.out.printf("Total cost for this order: RM%.2f\n", totalCost);

                    if(order.createOrder(itemId, orderNo, quantityToAdd, supplierId, totalCost)){
                        System.out.println("Order Created for item " + itemId + " with supplier "+ supplierId);
                    }
                    else{
                        System.out.println("Order Failed to create for item "+ itemId);
                    }

                    System.out.println("Do you want to order again (y/n)?");
                    choice = scan.nextLine().toLowerCase();  

                    if (!choice.equals("y") && !choice.equals("n")) {
                        do{
                            System.out.println("Invalid choice. Please enter 'y' for yes or 'n' for no.");
                            System.out.println("Do you want to order again (y/n)?");
                            choice = scan.nextLine().toLowerCase();  
                        } while(!choice.equals("n"));
                    }

                } while(!choice.equals("n"));
            updatePurchaseOrder(orderNo, purchaseOrderTotalCost);
        }
         
        private static String[] getAllItemId() {
                Database dbi = new Database("inventory");
                String[] columns = {"item_id"};
                dbi.readTable(columns);
                ArrayList<String> list = dbi.getResult();
                String[] itemList = new String[list.size()];
                for (int i = 0; i< list.size(); i++){
                    itemList[i] = list.get(i).replaceAll(Database.delimiter, "");
                }
                return itemList;
            }

        private static Object getSingleValue(String columnName, Object[][] condition, Database db) {
            String[] columns = {columnName};

            // Use the readTable method to query the database with the column name and condition
            if (db.readTable(columns, condition)) {
                ArrayList<ArrayList<Object>> results = db.getObjResult();

                // Ensure there's more than just the header row (index 0)
                if (results.size() > 1) {
                    return results.get(1).get(0); // Return the first result of the first row
                }
            }
            return null; // Return null if no result found
        }

        private static String getSupplierIdbyItemId(String itemId) {
            Database dbsi = new Database("supplier_item"); 
            Object[][] condition = {{"item_id", itemId}};
            Object result = getSingleValue("supplier_id", condition, dbsi);
            return result != null ? result.toString().trim() : null;
        }

        private static double getItemCost(String itemId) {
            Database dbi = new Database("inventory");
            Object[][] condition = {{"item_id", itemId}};
            Object result = getSingleValue("cost", condition, dbi);
            return result != null ? Double.parseDouble(result.toString()) : 0;
        }

        private static double getShippingFee(String itemId) {
            Database dbsi = new Database("supplier_item"); 
            Object[][] condition = {{"item_id", itemId}};
            Object result = getSingleValue("shipping_fee", condition, dbsi);
            return result != null ? Double.parseDouble(result.toString()) : 0;
        }

        private static double getImportDuty(String supplierId) {
            Database dbs = new Database("supplier"); 
            Object[][] condition = {{"supplier_id", supplierId}};
            Object result = getSingleValue("import_duty", condition, dbs);
            return result != null ? Double.parseDouble(result.toString()) : 0;
        }
        
        private static boolean isItemExists(String inputItemId) {
            String[] itemIds = getAllItemId();
            for (String itemId : itemIds) {
                if (itemId.equals(inputItemId)) {
                    return true;
                }
            }
            return false;
        }
        
        private static void displayOrderMenu(){
            System.out.println("1. Display All Orders");
            System.out.println("2. Display by Search Order No");
            System.out.print("Choice > ");
            int choice = scan.nextInt();
            switch(choice){
                case 1:
                    displayOrder();
                    scan.nextLine();
                    enterToContinue();
                    break;
                case 2:
                    displayOrderSearch();
                    enterToContinue();
                    break;
                default:
                    break;
            }
        }
        
        private static void displayOrder() {
            Order order = new Order();
            ArrayList<Order> orders = order.getAllOrders();
            if (orders.isEmpty()) {
                System.out.println("No purchase orders found.");
            } else {
                System.out.println("--------------------------------------------------------------------------------------");
                System.out.printf("| %-10s | %-15s | %-10s | %-20s | %-10s |\n", "Item Id", "Order No", "Quantity", "Supplier Id", "Total Cost (RM)");
                System.out.println("--------------------------------------------------------------------------------------");

                for (Order o : orders) {
                    System.out.println(o.toString());
                }
                System.out.println("--------------------------------------------------------------------------------------");
            }
        }
        
         private static void displayOrderSearch(){
                Order order = new Order();
                String orderNo;
                scan.nextLine();
                System.out.print("Please enter orderNo to find your purchase order: ");
                orderNo = scan.nextLine();

                if (!orderNo.matches("^OD\\d{4}$")) {
                    System.out.println("Invalid order number format. It should start with 'OD' followed by exactly 4 digits (e.g., OD0001).");
                    return;  
                }

                ArrayList<Order> orders = order.getOrder(orderNo);

               if (orders.isEmpty()) {
                    System.out.println("No purchase orders found.");
                } else {
                    System.out.println("--------------------------------------------------------------------------------------");
                    System.out.printf("| %-10s | %-15s | %-10s | %-20s | %-10s |\n", "Item Id", "Order No", "Quantity", "Supplier Id", "Total Cost (RM)");
                    System.out.println("--------------------------------------------------------------------------------------");

                    for (Order o : orders) {
                        System.out.println(o.toString());
                    }
                    System.out.println("--------------------------------------------------------------------------------------");
                }
           }

         private static void updateOrder() {
             double itemCost;
            Database db = new Database("order_item");
            System.out.print("Enter the Item ID for the order you want to update:");
            String itemId = scan.nextLine().trim();

            System.out.print("Enter the Order No for the order you want to update:");
            String orderNo = scan.nextLine().trim();

            Object[][] condition = {{"item_id", itemId}, {"order_no", orderNo}};

            String[] columns = {"item_id", "order_no", "quantity", "supplier_id", "total_cost"};
            boolean orderExists = db.readTable(columns, condition);

            if (!orderExists || db.getObjResult().size() <= 1) {
                System.out.println("Order with Item ID " + itemId + " and Order No " + orderNo + " not found.");
                return;
            }

            itemCost = getItemCost(itemId);

            System.out.print("Enter the new Quantity: ");
            int newQuantity = scan.nextInt();
            scan.nextLine(); 

            // Calculate the new total cost 
            double newTotalCost = (newQuantity * getItemCost(itemId))  + (newQuantity * getShippingFee(itemId)) + getImportDuty(getSupplierIdbyItemId(itemId));

            // Update quantity and total_cost in order_item table
            Object[][] value = {{"quantity", newQuantity} ,{"total_cost", newTotalCost}};

            if (db.updateTable(value, condition)) {
                System.out.println("Order item updated successfully.");

                // Update the total cost in the purchase_order 
                updatePurchaseOrderTotalCost(orderNo);
            } else {
                System.out.println("Failed to update the order item.");
            }
        }
         
         private static void updatePurchaseOrder(String orderNo, double totalCost) {
            Database dbpo = new Database("purchase_order");
            Object[][] values = {{"total_cost", totalCost}};
            Object[][] condition = {
                {"order_no", orderNo}
            };
            dbpo.updateTable(values, condition);
        }

        // Method to update total_cost in purchase_order after updating order_item total_cost
        private static void updatePurchaseOrderTotalCost(String orderNo) {
            Database db = new Database("order_item");
            Database dbpo = new Database("purchase_order");
            // Get all order items for the given order_no
            Object[][] condition  = {{"order_no", orderNo}};
            String[] column = {"total_cost"};
            boolean itemsExist = db.readTable(column, condition);

            if (!itemsExist) {
                System.out.println("No items found for Order No " + orderNo);
                return;
            }

            // Calculate the new total cost by + total_cost of each item
            double totalCost = 0;
            ArrayList<ArrayList<Object>> resultArray = db.getObjResult();

            for (int i = 1; i < resultArray.size(); i++) {
                ArrayList<Object> row = resultArray.get(i);
                Object costObj = row.get(0); 
                if (costObj != null) {
                    try {
                        totalCost += Double.parseDouble(costObj.toString());
                    } catch (NumberFormatException e) {
                        System.out.println("Error adding total cost: " + costObj);
                        return;
                    }
                }
            }

            Object[][] updatedValue = {{"total_cost", totalCost}};
            Object[][] condition2 = {{"order_no", orderNo}};

            if (dbpo.updateTable(updatedValue, condition2)) {
                System.out.println("Total cost for purchase order updated successfully.");
            } else {
                System.out.println("Failed to update the total cost for the purchase order.");
            }
    }
        
        private static void deleteOrder(){
            Order order = new Order();
            String orderNo, itemId;
            System.out.print("Enter Order No for deletion: ");
            orderNo = scan.nextLine();
            
            System.out.print("Enter Item ID for deletion: ");
            itemId = scan.nextLine();
            
            order.deleteOrder(orderNo, itemId);
            enterToContinue();
        }
        
        private static void enterToContinue(){
            System.out.println("Press Enter to continue");
            scan.nextLine();
        }
         
}