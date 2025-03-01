package com.project.inventory.ui;

import com.project.inventory.application.*;
import com.project.inventory.dao.Database;

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
            ArrayList<User> userArr = user.initializeUser();
                System.out.println("[Welcome to MIXUE Inventory System]");
                System.out.println("-----------------------------------");
                choice = getMenuInput(new String[]{
                        "Login",
                        "Register",
                        "Exit"
                });
            System.out.println();
            switch(choice){
                case 1:
                    if (loginMenu(userArr))
                        menu(userArr);
                    break;
                case 2:
                    registerMenu(userArr);
                    break;
                case 0:
                    System.out.println("Exit Program.");
                    break;
                default: 
                    System.out.println("Invalid Input! Try Again");
                    break;
            }
        }while (choice != 0);
    }

    public static boolean loginMenu(ArrayList<User> userArr) {
        int choice = 0;
        String id = "";
        String password = "";
        //If found out how to clear screen, enable this two
        //System.out.println("[Welcome to MIXUE Inventory System]");
        //System.out.println("-----------------------------------");
        do {
                System.out.println("Login as: ");
                choice = getMenuInput(new String[]{
                        "Manager",
                        "Inventory Admin",
                        "Return to last page"
                });
            switch (choice) {
                case 1:
                case 2:
                    if(choice == 1) {
                        user = new Manager();// Polymorphism
                        System.out.println("Login as Manager");
                    }else{
                        user = new InventoryAdmin(); // Polymorphism
                        System.out.println("Login as Inventory Admin");
                    }
                    System.out.print("ID: ");
                    id = scan.nextLine();
                    System.out.print("Password: ");
                    password = scan.nextLine();
                    System.out.println();
                    break;
                case 0:
                    return false;
                default:
                    System.out.println("Invalid Input! Try Again");
                    break;
            }
        } while (choice < 1 || choice > 3);
        if (user.checkRoles(id, userArr) && user.passwordValidation(password, userArr)) {
            System.out.println("Login Successful");
            System.out.println("Welcome " + user.getCurrentName() + ".");
            System.out.println("---------------------------------------");
            user.getCurrentLoginUser();
            return true;
        } else if (user.checkRoles(id, userArr) && !(user.passwordValidation(password, userArr))) {
            System.out.println("Wrong Password!");
        } else
            System.out.println("Login Failed");
        System.out.println();
        return false;
    }

    public static void menu(ArrayList<User> userArr) {
        //userArr.get(0).getPosition().equals(id);
        int decision;
        do{
            Thread.startVirtualThread(Inventory.getInstance());
            decision = permissionMenu(user.permission());
            if (user.permission().equals(User.Permission.FULL_CONTROL)) { //Manager
                Manager manager = (Manager) user;
                    switch (decision) {
                        case 0:
                            System.out.println("Returning to last page");
                            break;
                        case 1:     //Restock Inventory
                            inventoryMenu();
                            break;
                        case 2:     //Current Stock Report
                            currentStockList();
                            break;
                        case 3:     //Supplier Menu
                            supplierMenu();
                            break;
                        case 4:     //All staff details
                            manager.displayAllUser(userArr);
                            System.out.println("\n");
                            break;
                        case 5:     //Modify staff details
                            manager.displayAllUser(userArr);
                            System.out.println("------------------------------------------------------------------------\n");
                            modifyUser(userArr);
                            break;
                        case 6:     //Delete staff
                            manager.displayAllUser(userArr);
                            System.out.println("------------------------------------------------------------------------\n");
                            deleteUser(userArr);
                            break;
    //                    case 7:
    //                        System.out.println("\nReturning to last page.\n");
    //                        break;
                        default:
                            break;
                    }
            } else if (user.permission().equals(User.Permission.ADMIN)) { //Admin
                ArrayList<PurchaseOrder> purchaseOrders = PurchaseOrder.getAllPO();
                ArrayList<Order> orders = Order.getAllOrder();
                switch (decision) {
                    case 1:     //Restock inventory\
                        generatePurchaseOrder(userArr, purchaseOrders, orders);
                        break;
                    case 2:     //Purchase order status
                        poMenu(purchaseOrders, orders);
                        break;
                    case 3:
                        orderMenu(orders);
                        break;
                    case 4:
                        inventoryMenu();
                        break;
                    case 0:
                        System.out.println("Returning to last page.");
                        break;
                    default:
                        break;
                }
            }
        } while (decision != 0);
    }

    public static void registerMenu(ArrayList<User> userArr){
        String managerID = new String();
        String managerPassword = new String();
        String name = "";
        String position = new String();
        String password = "";
        String confirmPassword = new String();
        String email = new String();
        int selection;
        String user_id = new String();
        boolean valid = false;
        User manager = new Manager();
        
        //If found out how to clear screen, enable this two
        //System.out.println("[Welcome to MIXUE Inventory System]");
        //System.out.println("-----------------------------------");
        System.out.println("Require manager to approve registration.");
        System.out.print("Enter Manager ID: ");
        managerID = scan.nextLine();
        if (manager.checkRoles(managerID, userArr)) {
            System.out.print("Password: ");
            managerPassword = scan.nextLine();
        }
        else
            System.out.println("You are not a Manager.");
        
        System.out.println();
        if (manager.checkRoles(managerID, userArr) && manager.passwordValidation(managerPassword, userArr)) {
            System.out.println("[Registration Portal]");
            System.out.println("---------------------");
            while(!valid){
                System.out.print("Enter name: ");
                name = scan.nextLine();

                try{
                    User.nameValidation(name);
                    valid = true;
                }catch(IllegalArgumentException ex){
                    System.out.println(ex.getMessage());
                }
            }
            do {
                System.out.println("Position: ");
                System.out.println("1. Manager");
                System.out.println("2. Inventory Admin");
                while(true){
                    System.out.print("Selection: ");
                    try{
                        selection = scan.nextInt();
                        break;
                    }catch(InputMismatchException ex){
                        System.out.println("Please select by enter a integer.");
                        scan.next();
                    }
                }
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
            valid = false;
            while(!valid){
                System.out.print("Create password: ");
                password = scan.nextLine();
                try{
                    User.passwordValidation(password);
                    valid = true;
                }catch(IllegalArgumentException ex){
                    System.out.println(ex.getMessage());
                }
            }
            System.out.print("Confirm password: ");
            confirmPassword = scan.nextLine();
            while(!(confirmPassword.equals(password))){
                System.out.println("Your password isn't match. Try again");
                valid = false;
                while(!valid){
                    System.out.print("Create password: ");
                    password = scan.nextLine();
                    try{
                        User.passwordValidation(password);
                        valid = true;
                    }catch(IllegalArgumentException ex){
                        System.out.println(ex.getMessage());
                    }
                }
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
        else if (manager.checkRoles(managerID, userArr) && !(manager.passwordValidation(managerPassword, userArr))) {
            System.out.println("Wrong Password.");
        }
            
    }

    public static int permissionMenu(User.Permission permission) {
        int decision = 0;
        switch (permission) {
            // 1 = Manager, 2 = Inventory Admin
            case FULL_CONTROL:
                decision = getMenuInput(new String[]{
                        "Inventory", //order item @ purchase order
                        "Current stock report",
                        "Supplier Menu",
                        "All staff details",
                        "Modify staff details",
                        "Delete staff",
                        "Return to last page"
                });
                return decision;
            case ADMIN:
                decision = getMenuInput(new String[]{
                        "Order items", //order item @ purchase order
                        "Purchase Order Menu",
                        "Order Menu",
                        "Inventory",
                        "Return to last page"
                });
                return decision;
            default:
                throw new IllegalArgumentException("The permission haven't setup yet");
        }
    }
    
    public static void modifyUser(ArrayList<User> userArr){
        Manager manager = new Manager();
        InventoryAdmin inventoryAdmin = new InventoryAdmin();
        
        String modifyID = new String();
        String modifyData = new String();
        String tempPasswordHolder = new String();
        int modifyAttributes;
        int confirmation;
        
        System.out.print("Enter an employee's ID to start modify: ");
        modifyID = scan.nextLine();
        if (!(manager.checkRoles(modifyID, userArr)) && !(inventoryAdmin.checkRoles(modifyID, userArr))) {
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
                            manager.modifyStaff(modifyID, modifyAttributes, userArr);
                            userArr.get(User.arrayCounter).setName(modifyData);
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
                            manager.modifyStaff(modifyID, modifyAttributes, userArr);
                            userArr.get(User.arrayCounter).setPassword(modifyData);
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
                            manager.modifyStaff(modifyID, modifyAttributes, userArr);
                            userArr.get(User.arrayCounter).setEmail(modifyData);
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
    
    public static void deleteUser(ArrayList<User> userArr){
        Manager manager = new Manager();
        InventoryAdmin inventoryAdmin = new InventoryAdmin();
        
        String deleteID = new String();
        int confirmation = 0;
        
        System.out.print("Enter an employee's ID to delete: ");
        deleteID = scan.nextLine();
        if (!(manager.checkRoles(deleteID, userArr)) && !(inventoryAdmin.checkRoles(deleteID, userArr))) {
            System.out.println("User not exist.");
        }
        else if (deleteID.equals(userArr.get(User.currentUserLoginCounter).getId())) {
            System.out.println("\nYou cannot delete yourself!\n");
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
                manager.deleteStaff(deleteID, userArr);
                userArr.remove(User.arrayCounter);
            }
            else
                System.out.println("Action Ignore.");
        }
    }

    private static void inventoryMenu(){
        Inventory inventory = Inventory.getInstance();
        Thread.startVirtualThread(inventory);
        String[] option;
        if(user.permission().equals(User.Permission.FULL_CONTROL)){
        option = new String[]{
                "Restock inventory",
                "Create item",
                "Item operation",
                "Exit"
        };
        }else if(user.permission().equals(User.Permission.ADMIN)){
            option = new String[]{
                    "Restock inventory",
                    "Item operation",
                    "Exit"
            };
        }else{
            option = new String[]{
                    "Exit"
            };
        }
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
            System.out.println("Item quantity = " + item.getQuantity() + " in " + item.getItemUnit());
            System.out.print("Current item quantity (in " + item.getItemUnit() + ") = " + item.getQuantity() + " + ");
            if (scan.hasNextDouble()) {
                restockQuantity = scan.nextDouble();
                validation = true;
            } else {
                scan.next();
                System.out.println("Error input! Please try again.");
            }
        } while (!validation);
        item.setQuantity(item.getQuantity() + restockQuantity);
        System.out.println("Inventory restock successfully");
    }

    private static void inventoryOperation(int index) {
        System.out.println("What you would like to do ?");
        String[] option;
        if(user.permission().equals(User.Permission.FULL_CONTROL)) {
            option = new String[]{
                    "Update item",
                    "Delete item",
                    "Exit"
            };
        } else if (user.permission().equals(User.Permission.ADMIN)) {
            option = new String[]{
                    "Update item",
                    "Exit"
            };
        }else{
            option = new String[]{
                    "Exit"
            };
        }
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
        String strInput;
        Inventory inventory = Inventory.getInstance();
        Item item = inventory.getItem(index);
        String[] itemDataName;
        if(user.permission().equals(User.Permission.FULL_CONTROL)) {
            itemDataName = new String[]{
                    "Item name",
                    "Item type",
                    "Quantity",
                    "Unit",
                    "Exit"
            };
        } else if (user.permission().equals(User.Permission.ADMIN)) {
            itemDataName = new String[]{
                    "Item name",
                    "Item type",
                    "Quantity",
                    "Exit"
            };
        }else{
            itemDataName = new String[]{
                    "Exit"
            };
        }
        String[] itemData = new String[]{
                item.getItemName(),
                item.getItemType(),
                String.valueOf(item.getQuantity()),
                item.getItemUnit()
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
            }
            switch (itemDataName[input]) {
                case "Item name":
                    strInput = scan.nextLine();
                    if(!strInput.equals("0")) {
                        Item tempItem = inventory.checkNameUnique(strInput);
                        if (tempItem == null) {
                            item.setItemName(strInput);
                        } else {
                            System.out.println("Same item already exist! This is the item details...");
                            showItemDetails(tempItem);
                            validation = false;
                        }
                    } else
                        validation = false;
                    break;

                case "Item type":
                    System.out.println();
                    String itemType = promptItemType();
                    if(!itemType.equals("0"))
                        item.setItemType(itemType);
                    else
                        validation = false;
                    break;

                case "Quantity":
                    strInput = scan.nextLine();
                    System.out.println("Do you confirm ?");
                    input = getMenuInput(new String[]{
                            "Yes",
                            "Exit"
                    });
                    if(input != 0) {
                        try {
                            item.setQuantity(Double.parseDouble(strInput));
                        } catch (NumberFormatException e) {
                            System.out.println("Input is not number");
                            validation = false;
                        }
                    }else
                        validation = false;
                    break;

                case "Unit":
                    strInput = scan.nextLine();
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
                String.valueOf(item.getQuantity()),
                item.getItemUnit()
        });
        System.out.println("Do you want to delete this item? ");
        System.out.print("If yes, Please type 'Yes' : ");
        String input = scan.nextLine();
        if(input.equals("Yes")){
            inventory.deleteItem(item);
            System.out.println("Item deleted sucussfully...");
        }else{
            System.out.println("Item are not deleted...");
        }
    }

    public static int selectInventory(){
        Inventory inventory = Inventory.getInstance();
        InventoryUIController controller = new InventoryUIController(InventoryUIController.TableMode.INVENTORY);
        InventoryUI ui = new InventoryUI(controller);
        ui.showInventoryListWithSearch();
        return controller.getSelectedIndex();
    }

    private static void showItemDetails(Item item){
        showItemDetails(new String[]{
                item.getItemName(),
                item.getItemType(),
                String.valueOf(item.getQuantity()),
                item.getItemUnit()
        });
    }

    private static void showItemDetails(String[] itemData){
        String[] itemDataName = new String[]{
                "Item name ",
                "Item type",
                "Quantity",
                "Unit"
        };
        showItemDetails(itemDataName, itemData);
    }

    private static String promptItemType(){
        ArrayList<String> tempOption = new ArrayList<>();
        for (Item.ItemTypeConstant i : Item.ItemTypeConstant.values()) {
            tempOption.add(i.getValue());
        }
        tempOption.add("Other");
        int option = getMenuInput(tempOption.toArray(new String[0]));
        if(option == tempOption.size()){ // Means other
            System.out.print("Please type the item type: ");
            return scan.nextLine();
        }else if(option == 0 || option == 6){
            return "0";
        }else{
            return tempOption.get(--option);
        }
    }

    private static void showItemDetails(String[] itemDataName, String[] itemData){
        for (int i = 0; i < itemData.length; i++)
            System.out.printf("%s: %s\n", itemDataName[i], itemData[i]);
    }

    private static void createItem(){
        boolean validation;
        Inventory inventory = Inventory.getInstance();
        System.out.println("Please provide the item detail you want to create...");
        String itemName;
        do {
            System.out.print("Item name : ");
            itemName = scan.nextLine();
        }while(itemName.isBlank() || itemName == null);
        Item item = inventory.checkNameUnique(itemName);
        if(item != null){ //If it is not unique, means it have already
            System.out.println("Item has been created before! This is the item detail before created..");
            showItemDetails(item);
            return;
        }
        String itemType;
        do {
        System.out.println("Item type :");
            itemType = promptItemType();
        }while(itemType.equals("0"));
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
        if(inventory.addInventory(new Item(itemName, itemType, perUnitAndUnit[1], Double.parseDouble(perUnitAndUnit[0]), quantity))){
            System.out.println("Item created successfully...");
        }else{
            System.out.println("Something went wrong");
        }
    }

    private static void currentStockList(){
        InventoryUI ui = new InventoryUI(new InventoryUIController(InventoryUIController.TableMode.INVENTORY));
        ui.showCurrentStockList();
        System.out.println("Do you want to save to file?");
        int input = getMenuInput(new String[]{
                "Save to file",
                "Exit"
        });
        if(input == 1){
            InventoryFileIO io = new InventoryFileIO();
        }
    }

    /**
     * Prompt user to select the option with print the option
     * @param option All the option that available for user to choose
     * @return User input
     */
    private static int getMenuInput(String[] option) {
        int input = 0;
        int optionLength;
        Set<String> exit = Set.of("Exit", "Return to last page");
        do{
            boolean hasExit = false;
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
        ArrayList<Supplier> suppliers = supplierManager.getAllSupplierInfo();
        ArrayList<SupplyItem> supplyItems = supplyItemManager.getAllSupplyItem();

      

        do{
        
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
                        choice = 0;
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
                                displaySupplierInfo(suppliers);
                                System.out.println("Press Enter to Continue...");
                                scan.nextLine();
                                break;
                            case 2:
                                createSupplier(suppliers, supplyItems, supplierManager);
                                break;
                            case 3:
                                displaySupplierInfo(suppliers);
                                editSupplierInfo(suppliers, supplierManager);
                                break;
                            case 4:
                                deleteSupplierDetails(suppliers, supplyItems, supplierManager);
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
                       choice = 0;

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
                                displayAllSupplyItems(supplyItems);
                                System.out.println("Press Enter to Continue...");
                                scan.nextLine();
                                break;
                            case 2:
                                createSupplyItem(supplyItems, suppliers, supplyItemManager);
                                break;
                            case 3:
                                displayAllSupplyItems(supplyItems);
                                editSupplyItem(supplyItems, suppliers);
                                break;
                            case 4:
                                displayAllSupplyItems(supplyItems);
                                deleteSupplyItem(supplyItems, suppliers, supplyItemManager);
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

    private static void displaySupplierInfo(ArrayList<Supplier> suppliers) {
        
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

    private static void createSupplier(ArrayList<Supplier> suppliers, ArrayList<SupplyItem> supplyItems, Supplier supplierManager){
        Supplier tempSupplier;
        SupplyItem supplyItemManager = new SupplyItem();
        String id, name, address, email;
        String type = "";
        double import_duty = 0.00, shipping_fee = 0.00, cost = 0.00;
        int options = 0, exit = 1;
        boolean supplierInfoValid;
        int itemIndex;
        Inventory inventory = Inventory.getInstance();


        if(Supplier.numSupplier < 100){


            do{
                id = generateSupplierID(suppliers);
                System.out.println("ID: " + id);
                System.out.print("Enter Supplier Name: ");
                name = scan.nextLine();
                System.out.print("Enter Supplier Address: ");
                address = scan.nextLine();
                System.out.print("Enter Supplier Email: ");
                email = scan.nextLine();

                do{
                    options = 0;
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


                supplierInfoValid = supplierManager.validateSupplierInfo(suppliers, id, name, address, email, type);



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
                    suppliers.add(tempSupplier);
                    supplierManager.addSupplier(tempSupplier);
                    
                    do{
                        System.out.println("Please Select Item...");
                        itemIndex = selectInventory();
                        if(itemIndex != -1){
                            Item item = inventory.getItem(itemIndex);
                            

                            System.out.println("Please Enter The Information: ");
                            System.out.println("Item ID: " + item.getItemId());

                            try{
                                System.out.print("Cost: RM");
                                cost = Double.parseDouble(scan.nextLine());
                            }catch(NumberFormatException ex){
                                System.out.println("Error: Cannot Read The Shipping Fee!");
                            }

                            try{
                                System.out.print("Shipping Fee: RM");
                                shipping_fee = Double.parseDouble(scan.nextLine());
                            }catch(NumberFormatException ex){
                                System.out.println("Error: Cannot Read The Shipping Fee!");
                            }
                            
                            if(shipping_fee > 0 && shipping_fee < 1000 && cost > 0 && cost < 1000){
                                SupplyItem newSupplyItem = new SupplyItem();

                                newSupplyItem.setSupplierId(id);
                                newSupplyItem.setItemId(item.getItemId());
                                newSupplyItem.setItemName(item.getItemName());
                                newSupplyItem.setShippingFee(shipping_fee);
                                newSupplyItem.setCost(cost);

                                if(supplyItemManager.writeData(newSupplyItem)){
                                    supplyItems.add(newSupplyItem);
                                    SupplyItem.supplyItemNum++;

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
                                else{
                                   System.out.println("This item is already associated with the supplier. Please enter a different item.");
                                }
                            }
                            else{
                                System.out.println("Please ensure the shipping fee and cost are within the valid range of RM0 to RM999.");
                                options = 1;
                            }
                        }


                    }while(options == 1);

                    Supplier.numSupplier++;
                    exit = 1;
                }
                else{
                    do{
                        options = 0;
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
    
    private static String generateSupplierID(ArrayList<Supplier> suppliers){
        int id = 1;
        String newSupplierId;
        boolean exist;
        while(true){
                newSupplierId = String.format("S%04d", id);
                exist= false;
                
                for(int i = 0; i < suppliers.size(); i++){
                    if(suppliers.get(i).getSupplierId().equals(newSupplierId)){
                        exist = true;
                        break;
                    }
                }
                
                if(!exist)
                    break;
                
                id++;
        
        }
        return newSupplierId;
    }

    private static void editSupplierInfo(ArrayList<Supplier> suppliers, Supplier supplierManager){
        int options = 0, options2 = 0, exit = 0;
        String temp, columnName, id;
        ForeignSupplier foreignSupplier;
        LocalSupplier localSupplier;


        if(Supplier.numSupplier  != 0){

            do{
                System.out.print("Please Enter The Supplier's ID to Modify: ");
                id = scan.nextLine();
                


                if(supplierManager.isSupplierExists(suppliers, id)){
                    Supplier supplier = supplierManager.getAllSupplierInfo(suppliers, id);
                    
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
                                                    supplier.setSupplierName(temp);
                                                    
                                                    if(supplierManager.modifySupplier(columnName, id, temp))
                                                        System.out.println("New Data Updated!");
                                                    else
                                                        System.out.println("Failed to Update New Data!");
                                                    
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

                                    if(supplierManager.validateSupplierAddress(suppliers, temp)){
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
                                                    supplier.setSupplierAddress(temp);
                                                    if(supplierManager.modifySupplier(columnName, id, temp))
                                                        System.out.println("New Data Updated!");
                                                    else
                                                        System.out.println("Failed to Update New Data!");
                                                    
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
                                                    supplier.setSupplierEmail(temp);
                                                    if(supplierManager.modifySupplier(columnName, id, temp))
                                                        System.out.println("New Data Updated!");
                                                    else
                                                        System.out.println("Failed to Update New Data!");
                                                    
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

    private static void deleteSupplierDetails(ArrayList<Supplier> suppliers, ArrayList<SupplyItem> supplyItems, Supplier supplierManager){
        int options = 0, exit = 0;
        String id;
        SupplyItem supplyMainManager = new SupplyItem();
  


        if(Supplier.numSupplier != 0){
            do{
                displaySupplierInfo(suppliers);
                System.out.print("Please Enter The Supplier's ID to Delete): ");
                id = scan.nextLine();
                if(supplierManager.isSupplierExists(suppliers, id)){
                    do{
                        Supplier supplier = supplierManager.getAllSupplierInfo(suppliers, id);
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
                                for (int i = supplyItems.size() - 1; i >= 0; i--) {
                                    if (supplyItems.get(i).getSupplierId().equals(id)) {
                                        supplyItems.remove(i);
                                    }
                                }
                                if(supplyMainManager.deleteSupplyItem(id)){
                                    for (int i = suppliers.size() - 1; i >= 0; i--) {
                                        if (suppliers.get(i).getSupplierId().equals(id)) {
                                            suppliers.remove(i);
                                        }
                                    }
                                    if(supplierManager.deleteSupplier(id)){
                                        System.out.printf("The Supplier of %s Has Been Deleted!", id);
                                        System.out.println();
                                    }else{
                                        System.out.printf("Failed to Delete The Data of %s Supplier\n ", id);
                                        System.out.println();

                                    }
                                    Supplier.numSupplier--;
                                    exit = 1;
                                }
                                else{
                                    System.out.println("Failed to Delete The Data!");
                                }
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
                        options = 0;
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

    private static void displayAllSupplyItems(ArrayList<SupplyItem> supplyItems){
    
        if(supplyItems.isEmpty()){
            System.out.println("No Supply Item Data Found.");
        }else{
//            InventoryUIController controller = new InventoryUIController(InventoryUIController.TableMode.INVENTORY_WITH_SUPPLIER);
//            InventoryUI ui = new InventoryUI(controller);
//            ui.showSupplyItemTable();
    
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
    
    private static void editSupplyItem(ArrayList<SupplyItem> supplyItems, ArrayList<Supplier> suppliers){
        String supplierId, shippingFee, cost;
        SupplyItem supplyItemManager = new SupplyItem();
        Supplier supplierManager = new Supplier();
        SupplyItem supplyItemInfo;
        int options = 0;
        double newValue =0.00;
        int itemIndex;
        Inventory inventory = Inventory.getInstance();

        if(SupplyItem.supplyItemNum != 0){
        
            
            System.out.print("Please Enter The Supplier's ID (EG: S0001 - > 1): ");
            supplierId = scan.nextLine();
            
            itemIndex = selectInventory();
            
            if(itemIndex!= -1){
    
                Item item = inventory.getItem(itemIndex);
                
             
                         
                    if(supplierManager.isSupplierExists(suppliers, supplierId)){
                        supplyItemInfo = supplyItemManager.getAllSupplyItem(supplyItems,supplierId, item.getItemId());
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
                                    System.out.println("2. Cost");
                                    System.out.println("3. Back");
                                    System.out.print("Enter Your Choice: ");
                                    options = Integer.parseInt(scan.nextLine());
                                }catch(NumberFormatException ex){
                                    System.out.println("Error: Your Input Choice Should Be An Integer!");
                                }
                                switch(options){
                                    case 1:

                                        try{
                                            System.out.print("Enter New Shipping Fee: RM");
                                            newValue = Double.parseDouble(scan.nextLine());
                                        }catch(NumberFormatException ex){
                                            System.out.println("Shipping Fee Only Can Be Digit!");
                                        }
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
                                                        supplyItemInfo.setShippingFee(newValue);
                                                        if(supplyItemManager.updateData("shipping_fee", supplierId, item.getItemId(), shippingFee))
                                                            System.out.println("New Data Updated!");                                                     
                                                        else
                                                            System.out.println("Failed to Update New Data!");
                                                        
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
                                        try{
                                            System.out.print("Enter New Cost: RM");
                                            newValue = Double.parseDouble(scan.nextLine());
                                        }catch(NumberFormatException ex){
                                            System.out.println("Cost Only Can Be Digit!");
                                        }
                                        if(newValue > 0 && newValue < 1000){
                                            cost = String.valueOf(newValue);
                                            do{
                                                System.out.println("-------------------------------");
                                                System.out.printf("| %-12s | %-12s |\n", "Old Data", "New Data");
                                                System.out.println("-------------------------------");
                                                System.out.printf("| %-12.2f | %-12.2f |\n", supplyItemInfo.getCost(), newValue);
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
                                                        supplyItemInfo.setCost(newValue);
                                                        if(supplyItemManager.updateData("cost", supplierId, item.getItemId(), cost))
                                                            System.out.println("New Data Updated!");
                                                        else
                                                            System.out.println("Failed to Update New Data!");

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
                                    case 3:                                    
                                        break;
                                    default:
                                        System.out.println("Invalid Options! Please Try Again...");
                                        break;
                                }

                            }while(options != 1 && options !=2 && options!= 3);
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
    
    private static void createSupplyItem(ArrayList<SupplyItem> supplyItems, ArrayList<Supplier> suppliers, SupplyItem supplyItemManager){
        int itemIndex;
        double shippingFee, cost;
        String supplierId;
        Supplier supplierManager = new Supplier();
        Inventory inventory = Inventory.getInstance();

        
        
        System.out.print("Please Enter The Supplier's ID : ");
        supplierId = scan.nextLine();
        
        
        itemIndex = selectInventory();
        
        if(itemIndex!= -1){
        
            Item item = inventory.getItem(itemIndex);

            if(supplierManager.isSupplierExists(suppliers, supplierId)){

                System.out.println("Information");
                System.out.println("-----------");
                try{
                    System.out.print("Cost: RM");
                    cost = Double.parseDouble(scan.nextLine());
                }catch(NumberFormatException ex){
                    System.out.println("You Cost Only Can Be Digit!");
                    System.out.println("You will now be exited from this function.");
                    return;
                }
                try{
                    System.out.print("Shipping Fee: RM");
                    shippingFee = Double.parseDouble(scan.nextLine());
                }catch(NumberFormatException ex){
                    System.out.println("You Shipping Fee Only Can Be Digit!");
                    System.out.println("You will now be exited from this function.");
                    return;
                }
                      
                if(shippingFee > 0 && shippingFee <1000 && cost > 0 && cost < 1000){
                    SupplyItem newSupplyItem = new SupplyItem();
                    newSupplyItem.setSupplierId(supplierId);
                    newSupplyItem.setItemId(item.getItemId());
                    newSupplyItem.setItemName(item.getItemName());
                    newSupplyItem.setShippingFee(shippingFee);
                    newSupplyItem.setCost(cost);
                    if(supplyItemManager.writeData(newSupplyItem)){
                        supplyItems.add(newSupplyItem);
                        System.out.println("Data Added Successfully"); 
                        
                    }
                    else{
                        System.out.println("This item is already associated with the supplier. Please enter a different item.");
                    }
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
        }
        
     
    } 
    
    private static void deleteSupplyItem(ArrayList<SupplyItem> supplyItems, ArrayList<Supplier> suppliers, SupplyItem supplyItemManager){
        int itemIndex, options = 0;
        String supplierId;
        Supplier supplierManager = new Supplier();
        SupplyItem tempSupplyItem;
        Inventory inventory = Inventory.getInstance();

        
        if(SupplyItem.supplyItemNum !=0 ){
            System.out.print("Please Enter The Supplier's ID : ");
            supplierId = scan.nextLine();
            System.out.println("Please Select Item...");
            itemIndex = selectInventory();
            
            if(itemIndex != -1){
                Item item = inventory.getItem(itemIndex);

                if(supplierManager.isSupplierExists(suppliers, supplierId)){
                    do{
                        tempSupplyItem = supplyItemManager.getAllSupplyItem(supplyItems, supplierId, item.getItemId());
                        if(tempSupplyItem != null){


                            System.out.println("--------------------------------------------------------------");
                            System.out.printf("| %-11s | %-10s | %-16s | %-12s |\n", "Supplier ID", "Item ID", "Shipping Fee(RM)", "Cost(RM)");
                            System.out.println("--------------------------------------------------------------");
                            System.out.printf(tempSupplyItem.toString());
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
                                    if(supplyItemManager.deleteSupplyItem(supplierId, item.getItemId())){
                                        for(int i = supplyItems.size() - 1; i >= 0; i--){
                                            if(supplyItems.get(i).getSupplierId().equals(supplierId) && supplyItems.get(i).getItemId().equals(item.getItemId())){
                                                supplyItems.remove(i);
                                            }
                                        }
                                        SupplyItem.supplyItemNum--;
                                        System.out.println("The Data Has Been Deleted!");
                                    }
                                    else{
                                        System.out.println("Failed to Delete The Data!");
                                    }
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
        private static void poMenu(ArrayList<PurchaseOrder> purchaseOrders, ArrayList<Order> orders){
        int choice = 0;
        //ArrayList<PurchaseOrder> purchaseOrders = PurchaseOrder.getAllPO();
        //ArrayList<Order> orders = Order.getAllOrder();
        do{
            try{
                System.out.println("-----------------------");
                System.out.println("| Purchase Order Menu |");
                System.out.println("-----------------------");
                System.out.println("1. Display Purchase order");
                System.out.println("2. Update Purchase Order");
                System.out.println("3. Delete Purchase order");
                System.out.println("4. Back");
                System.out.print("Enter Your Choice: ");
                choice = Integer.parseInt(scan.nextLine());
            }catch(NumberFormatException ex){
                System.out.println("Error: Your Input Choice Should Be An Integer!");
            }
            switch(choice){
                case 1:
                    displayPOMenu(purchaseOrders);
                    break;
                case 2:
                    updatePOMenu(purchaseOrders, orders);
                    break;
                case 3:
                    deletePO(purchaseOrders, orders);
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid Options! Please Try Again...");
                    break;
            }
        }while(choice != 4);
    }
    

        private static void generatePurchaseOrder(ArrayList<User> userArr, ArrayList<PurchaseOrder> purchaseOrders, ArrayList<Order> orders){
            Inventory inventory = Inventory.getInstance();
            PurchaseOrder purchaseorder = new PurchaseOrder(getNextOrderNumber(), new Date(), userArr.get(User.currentUserLoginCounter).getId(), "Ordering" , 0.0);
            purchaseOrders.add(purchaseorder);
            Order order;
            String choice = "y";
            String supplierId;
            double totalCost;
            double purchaseOrderTotalCost = 0.0;
            Item item;
            do {
                    int index = selectInventory();
                    if(index == -1)
                        break;
                    item = inventory.getItem(index);

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


                    //supplierId = "dffsd";//
                   supplierId =  Order.getSupplierIdbyItemId(item.getItemId());

                    totalCost = (Order.getItemCost(item.getItemId()) * quantityToAdd) + (Order.getShippingFee(item.getItemId()) * quantityToAdd) +Order.getImportDuty(supplierId);

                   purchaseOrderTotalCost += totalCost;

                    //po.updateTotalCost(orderNo, purchaseOrderTotalCost);
                    // Formula (quantity * price) + (shipping fee per kg * quantity) + import fee
                    if(Order.getSupplierType(supplierId).equals("Local")){
                        System.out.println("Item "+ item.getItemId() +" have a Local Supplier. No additional import duty.");
                    }
                    else{
                        System.out.println("Item " + item.getItemId() + " have a Foreign Supplier. Additional of RM20.00 import duty!");
                    }
                    
                    System.out.printf("\nTotal cost for this order: RM%.2f\n", totalCost);
                    order = new Order(item.getItemId(), purchaseorder.getOrderNo(), quantityToAdd, supplierId, totalCost);
                    orders.add(order);

                    System.out.print("Do you want to order more? (Y/N)\n>");
                    choice = scan.nextLine().toLowerCase();  

                    if (!choice.equals("y") && !choice.equals("n")) {
                        do{
                            System.out.println("Invalid choice. Please enter 'y' for yes or 'n' for no.");
                            System.out.println("Do you want to order again (Y/N)?");
                            choice = scan.nextLine().toLowerCase();  
                            if(choice.equals("y")){
                                break;
                            }
                        } while(!choice.equals("n"));
                    }

                } while(!choice.equals("n"));
            purchaseorder.setTotalCost(purchaseOrderTotalCost);
            purchaseorder.setStatus("Pending Payment");
            purchaseorder.savePO(purchaseOrders);
             
            displayOrderSearch(orders, purchaseorder.getOrderNo());
            System.out.println("Total Cost: RM" + purchaseorder.getTotalCost());

            System.out.print("Would you like to pay now? (Y/N)\n>");
            String userResponse = scan.nextLine().trim().toLowerCase();

            String status;
            if (userResponse.equals("y")) {
                status = "Pending Shipping"; 
            } else {
                status = "Unpaid";  
            }
            
            purchaseorder.setStatus(status);
            if(!purchaseorder.savePO(purchaseOrders)){
                System.out.println("Insertion of Purchase Order Failed!");
            }
            else{
                System.out.println("Insertion of Purchase Order Succeed!");
                System.out.println("This is your purchase order!");
                displayPOSearch(purchaseOrders,purchaseorder.getOrderNo());
            }
            enterToContinue();
        }
    
        private static void displayPOMenu(ArrayList <PurchaseOrder> purchaseOrders){
            int choice;

            while (true) {
                System.out.println("1. Display All Purchase Orders");
                System.out.println("2. Display by Search Order No");
                System.out.print("Enter your choice: ");

                try {
                    choice = scan.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    scan.nextLine();
                }
            }

            switch(choice){
                case 1:
                    displayPO(purchaseOrders);
                    scan.nextLine();
                    enterToContinue();
                    break;
                case 2:
                    scan.nextLine();
                    System.out.print("Please enter Order Number to find your purchase order: ");
                    String orderNo = scan.nextLine();
                    if(!orderNo.matches("^OD\\d{4}$")){
                        System.out.println("Invalid order number format. It should start with 'OD' followed by exactly 4 digits (e.g., OD0001)");
                        return;
                    }
                    displayPOSearch(purchaseOrders,orderNo);
                    enterToContinue();
                    break;
                default:
                    break;
            }
        }
        
        private static void displayPO(ArrayList<PurchaseOrder> purchaseorders) {
            if (purchaseorders.isEmpty()) {
                System.out.println("No purchase orders found.");
            } else {
                System.out.println("--------------------------------------------------------------------------------------");
                System.out.printf("| %-10s | %-15s | %-10s | %-20s | %-10s |\n", "Order No", "Order Date", "User ID", "Status", "Total Cost (RM)");
                System.out.println("--------------------------------------------------------------------------------------");

                for (PurchaseOrder po : purchaseorders) {
                    System.out.println(po.toString());
                }
                System.out.println("--------------------------------------------------------------------------------------");
            }
        }
        
        private static void displayPOSearch(ArrayList<PurchaseOrder> purchaseOrders, String orderNo){
            PurchaseOrder purchaseorder = PurchaseOrder.findPurchaseOrder(purchaseOrders, orderNo);
            if (purchaseorder == null) {
                System.out.println("Purchase order with Order No " + orderNo + " not found.");
                return;
            }
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-15s | %-10s | %-20s | %-10s |\n", "Order No", "Order Date", "User ID", "Status", "Total Cost (RM)");
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println(purchaseorder.toString());
            System.out.println("--------------------------------------------------------------------------------------");
         }
        
        private static void updatePOMenu(ArrayList<PurchaseOrder> purchaseOrders, ArrayList<Order> orders) {
            boolean success;
            double newTotalCost;
            String dateInput;
            String input;

            displayPO(purchaseOrders);

            System.out.print("Enter the order number of the purchase order to update: ");
            String orderNo = scan.nextLine();

            if (!orderNo.matches("^OD\\d{4}$")) {
                System.out.println("Invalid order number format. It should start with 'OD' followed by exactly 4 digits (e.g., OD0001).");
                return;
            }

            // Fetch the purchase order from the list
            PurchaseOrder purchaseorder = PurchaseOrder.findPurchaseOrder(purchaseOrders, orderNo);
            if (purchaseorder == null) {
                System.out.println("Order Number doesn't exist!");
                return;
            }

            displayPOSearch(purchaseOrders, orderNo);
            int choice;
            while (true) {
                System.out.println("What would you like to update?");
                System.out.println("1. Order Date\n2. Status\n3. Total Cost\n4. Update All at Once\n5. Back");
                System.out.print("Enter your choice > ");

                try {
                    choice = scan.nextInt();
                    scan.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    scan.next();
                }
            }
            
            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice. Please select a valid option.");
                return;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter new order date (yyyy-MM-dd): ");
                    dateInput = scan.nextLine();
                    try {
                        Date newOrderDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateInput);
                        purchaseorder.setOrderDate(newOrderDate);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                        return;
                    }
                    break;
                case 2:
                    updateStatus(purchaseOrders,orderNo, orders);
                    return;
                case 3:
                    System.out.print("Enter new total cost: ");
                    input = scan.nextLine();

                    while (!input.matches("\\d+(\\.\\d+)?")) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        System.out.print("Enter new total cost: ");
                        input = scan.nextLine();
                    }
                    newTotalCost = Double.parseDouble(input);
                    purchaseorder.setTotalCost(newTotalCost);
                    break;
                case 4:
                    System.out.print("Enter new order date (yyyy-MM-dd): ");
                    dateInput = scan.nextLine();
                    try {
                        Date newOrderDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateInput);
                        purchaseorder.setOrderDate(newOrderDate);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format.");
                        return;
                    }

                    System.out.println("1. Pending Payment \n2. Unpaid \n3. Pending Shipping \n4. Out for delivery\n5. Delivered");
                    System.out.print("Choose a new status: ");
                    choice = scan.nextInt();
                    scan.nextLine();

                    switch (choice) {
                        case 1: purchaseorder.setStatus("Pending Payment"); break;
                        case 2: purchaseorder.setStatus("Unpaid"); break;
                        case 3: purchaseorder.setStatus("Pending Shipping"); break;
                        case 4: purchaseorder.setStatus("Out For Delivery"); break;
                        case 5 :
                            if (!purchaseorder.getStatus().equals("Delivered")) {
                                purchaseorder.setStatus("Delivered");
                                success = updateInventoryQuantity(orderNo, orders);
                                if (success) {
                                    System.out.println("Inventory updated successfully for order number: " + orderNo);
                                } else {
                                    System.out.println("Update of quantity failed!");
                                }
                            } else {
                                System.out.println("Inventory not updated since the previous status is (Delivered)");
                            }
                            break;
                    }

                    System.out.print("Enter new total cost: ");
                    input = scan.nextLine();

                    while (!input.matches("\\d+(\\.\\d+)?")) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        System.out.print("Enter new total cost: ");
                        input = scan.nextLine();
                    }
                    newTotalCost = Double.parseDouble(input);
                    purchaseorder.setTotalCost(newTotalCost);
                    break;
                case 5:
                    return;  // Go back without making changes
            }

            // Save the updated purchase order in memory and persist changes to the database
            success = purchaseorder.savePO(purchaseOrders);
            if (success) {
                System.out.println("Purchase order updated successfully.");
            } else {
                System.out.println("Failed to update purchase order.");
            }

            enterToContinue();
        }

        
        private static void updateStatus(ArrayList<PurchaseOrder> purchaseOrders, String orderNo, ArrayList<Order> orders) {
            PurchaseOrder purchaseorder = PurchaseOrder.findPurchaseOrder(purchaseOrders, orderNo);
            int choice = 0;
            while (true) {
                System.out.println("1. Pending Payment \n2. Unpaid \n3. Pending Shipping \n4. Out for delivery\n5. Delivered\n6. Back");
                System.out.print("Choose a new status: ");
                choice = scan.nextInt();

                if (choice >= 1 && choice <= 6) {
                    switch (choice) {
                        case 1: purchaseorder.setStatus("Pending Payment");break;
                        case 2: purchaseorder.setStatus("Unpaid"); break;
                        case 3: purchaseorder.setStatus("Pending Shipping"); break;
                        case 4: purchaseorder.setStatus("Out For Delivery"); break;
                        case 5:
                            purchaseorder.setStatus("Delivered");
                            if(!Order.getStatus(orderNo).equals("Delivered")){
                                boolean success = updateInventoryQuantity(orderNo, orders);
                                if (success){
                                    System.out.println("Inventory updated successfully for order number: " + orderNo);
                                    System.out.println("New Status set to: " + purchaseorder.getStatus());
                                }
                                else{
                                    System.out.println("Update of quantitiy failed!");
                                }
                            }
                            else{
                                System.out.println("Inventory not updated since the previous status is (Delivered)");
                            }
                            break;
                            
                        case 6:
                            System.out.println("Returning to the last page...");
                            return; 
                    }
                    break; 
                } else {
                    System.out.println("Invalid choice! Please enter a number between 1 and 6.");
                }
            }
            if (choice != 6 && choice != 5) {
                System.out.println("New status set to: " + purchaseorder.getStatus());
            }
            
            boolean success = purchaseorder.savePO(purchaseOrders);
            if (success) {
                System.out.println("Status updated successfully.");
            } else {
                System.out.println("Failed to update status.");
            }
            scan.nextLine();
            enterToContinue();
        }
       
        private static void deletePO(ArrayList<PurchaseOrder> purchaseOrders, ArrayList<Order> orders){
            String userInput;

            do {
                displayPO(purchaseOrders);
                System.out.print("Enter the Order No to delete: ");
                String orderNo = scan.nextLine().trim();

                if (!orderNo.matches("OD\\d{4}")) {
                    System.out.println("Invalid order number format. It should start with 'OD' followed by exactly 4 digits (e.g., OD0001).");
                } else {
                    System.out.print("Are you sure you want to delete purchase order " + orderNo + "? (Y/N): ");
                    String confirmation = scan.nextLine().trim().toLowerCase();

                    if (confirmation.equals("y")) {
                        if(Order.deleteOrder(orders, orderNo)){
                            System.out.println("Order Item data with the Order No " + orderNo + " deleted successfully!");
                        }
                        else{
                            System.out.println("Order Item data with the Order No " + orderNo + " delete failed! Make sure the Order ID exists.");
                        }
                        if(PurchaseOrder.deletePurchaseOrder(purchaseOrders, orderNo)){
                            System.out.println("Purchase Order data with the Order No " + orderNo + " deleted successfully!");
                        }
                        else{
                            System.out.println("Purchase Order data with the Order No " + orderNo + " deleted failed!");
                        }
                    } else {
                        System.out.println("Deletion cancelled.");
                    }
                }

                System.out.print("Do you want to continue delete order? (Y/N): ");
                userInput = scan.nextLine().trim().toLowerCase();

            } while (userInput.equals("y"));
        }
        

        private static String getNextOrderNumber() {
            Database db = new Database("purchase_order");
            String[] columns = {"order_no"};
            String condition = "ORDER BY order_no ASC";  
            boolean success = db.readTable(columns, condition);

            if (!success) {
                System.out.println("Query execution failed!");
                return null;
            }
            ArrayList<ArrayList<Object>> result = db.getObjResult();

            // Debug purpose
            //System.out.println("Result: " + result);
            
            //if nothign inside 
            if (result.size() <= 1) {
                return "OD0001"; 
            }

            result.remove(0);

            ArrayList<Integer> orderNumbers = new ArrayList<>();
            for (ArrayList<Object> row : result) {
                String orderNo = row.get(0).toString();
                if (orderNo.startsWith("OD") && orderNo.length() >= 4) {
                    try {
                        int num = Integer.parseInt(orderNo.substring(2)); 
                        orderNumbers.add(num);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid order number format: " + orderNo);
                    }
                }
            }

            Collections.sort(orderNumbers);
            
            if(orderNumbers.get(0) > 1){
                return "OD0001";
            }
            //for finding missing num
            for (int i = 0; i < orderNumbers.size() - 1; i++) {
                if (orderNumbers.get(i + 1) != orderNumbers.get(i) + 1) {
                    int missingNumber = orderNumbers.get(i) + 1;
                    return String.format("OD%04d", missingNumber);
                }
            }
            //if no missing then use next order num
            int nextOrderNumber = orderNumbers.get(orderNumbers.size() - 1) + 1;
            return String.format("OD%04d", nextOrderNumber);
        }
        
        
        private static void orderMenu(ArrayList<Order> orders){
            int choice = 0;
             //ArrayList<Order> orders = Order.getAllOrder();
            do{
                try{
                    System.out.println("--------------");
                    System.out.println("| Order Menu |");
                    System.out.println("--------------");
                    System.out.println("1. Display Order");
                    System.out.println("2. Update Order");
                    System.out.println("3. Delete Order");
                    System.out.println("4. Back");
                    System.out.print("Enter Your Choice: ");
                    choice = Integer.parseInt(scan.nextLine());
                }catch(NumberFormatException ex){
                    System.out.println("Error: Your Input Choice Should Be An Integer!");
                }
                switch(choice){
                    case 1:
                        displayOrderMenu(orders);
                        break;
                    case 2:
                        displayOrders(orders);
                        updateOrder(orders);
                        break;
                    case 3:
                        displayOrders(orders);
                        deleteOrder(orders);
                        break;
                    case 4:
                        break;
                    default:
                        System.out.println("Invalid Options! Please Try Again...");
                        break;
                }
            }while(choice != 4);
        }

        private static void displayOrderMenu( ArrayList<Order> orders){
            int choice = 0;
            try{
                System.out.println("1. Display All Orders");
                System.out.println("2. Display by Search Order No");
                System.out.print("Choice > ");
                choice = Integer.parseInt(scan.nextLine());
             }catch(NumberFormatException ex){
                 System.out.println("Error: Your Input Choice Should Be An Integer!");
             }


            switch(choice){
                case 1:
                    displayOrders(orders);
                    enterToContinue();
                    break;
                case 2:
                    System.out.print("Please enter orderNo to find your purchase order: ");
                    String orderNo = scan.nextLine();
                    if (!orderNo.matches("^OD\\d{4}$")) {
                        System.out.println("Invalid order number format. It should start with 'OD' followed by exactly 4 digits (e.g., OD0001).");
                        return;  
                    }
                    displayOrderSearch(orders, orderNo);
                    enterToContinue();
                    break;
                default:
                    break;
            }
        }

        private static void displayOrders(ArrayList<Order> orders) {
            if (orders.isEmpty()) {
                System.out.println("No orders found.");
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

         private static void displayOrderSearch( ArrayList<Order> orders, String orderNo){
             ArrayList<Order> order = Order.getOrders(orders, orderNo);
            if (order.isEmpty()) {
                System.out.println("No orders found for Order No " + orderNo + ".");
            }
            else {
                System.out.println("--------------------------------------------------------------------------------------");
                System.out.printf("| %-10s | %-15s | %-10s | %-20s | %-10s |\n", "Item Id", "Order No", "Quantity", "Supplier Id", "Total Cost (RM)");
                System.out.println("--------------------------------------------------------------------------------------");

                for (Order o : order) {
                    System.out.println(o.toString());
                }
                System.out.println("--------------------------------------------------------------------------------------");
            }
       }

         private static void updateOrder(ArrayList<Order> orders) {
            System.out.print("Enter the Item ID for the order you want to update: ");
            String itemId = scan.nextLine().trim();

            if (!itemId.matches("I\\d{4}")) {
                System.out.println("Invalid item ID format. It should start with 'I' followed by exactly 4 digits (e.g., I0001).");
                return;
            }

            System.out.print("Enter the Order No for the order you want to update: ");
            String orderNo = scan.nextLine().trim();

            if (!orderNo.matches("OD\\d{4}")) {
                System.out.println("Invalid order number format. It should start with 'OD' followed by exactly 4 digits (e.g., OD0001).");
                return;
            }

            // Find the order in the list
            Order orderToUpdate = Order.findOrder(orders, orderNo, itemId);

            if (orderToUpdate == null) {
                System.out.println("Order not found in memory.");
                return;
            }

            // Set the new quantity
            int newQuantity = 0;
            boolean validInput;
            do {
                validInput = true;
                try {
                    System.out.print("Enter the new Quantity: ");
                    newQuantity = Integer.parseInt(scan.nextLine());
                } catch (NumberFormatException ex) {
                    System.out.println("Error: Your input should be an integer!");
                    validInput = false;
                }
            } while (!validInput);
            double newTotalCost = (newQuantity * Order.getItemCost(itemId)) + (newQuantity * Order.getShippingFee(itemId)) + Order.getImportDuty(Order.getSupplierIdbyItemId(itemId));

            orderToUpdate.setQuantity(newQuantity);
            orderToUpdate.setTotalCost(newTotalCost);

            // Save the updated order using saveOrder
            if (orderToUpdate.saveOrder()) {
                System.out.println("Order item updated successfully.");

                // Update the total cost in the purchase_order
                updatePurchaseOrderTotalCost(orderNo);
            } else {
                System.out.println("Failed to update the order item.");
            }
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
        
        private static void deleteOrder(ArrayList<Order> orders){
            String orderNo, itemId, userInput, confirmation;
            do {
                System.out.print("Enter Item ID for deletion: ");
                itemId = scan.nextLine();
                if (!itemId.matches("I\\d{4}")) {
                System.out.println("Invalid item ID format. It should start with 'I' followed by exactly 4 digits (e.g., I0001).");
                return; 
                }

                System.out.print("Enter Order No for deletion: ");
                orderNo = scan.nextLine();
                if (!orderNo.matches("OD\\d{4}")) {
                    System.out.println("Invalid order number format. It should start with 'OD' followed by exactly 4 digits (e.g., OD0001).");
                    return;
                }

                System.out.print("Are you sure you want to delete item " + itemId + " for " + orderNo + "? (Y/N): ");
                confirmation = scan.nextLine().trim().toLowerCase();

                if (confirmation.equals("y")) {
                    if(!Order.deleteOrder(orders, orderNo, itemId)){
                        System.out.println("Deletion Canceled");
                    }
                    else{
                        updatePurchaseOrderTotalCost(orderNo);
                        System.out.println("Item " + itemId + " has been deleted from order " + orderNo + ".");
                    }
                } else {
                    System.out.println("Deletion cancelled.");
                }
                System.out.print("Do you want to continue delete order? (Y/N): ");
                userInput = scan.nextLine().trim().toLowerCase();

            }while(userInput.equals("y"));
        }

        private static boolean updateInventoryQuantity(String orderNo, ArrayList<Order> orders) {
            ArrayList<Order> temporder = new ArrayList<>();
            for (Order order : orders) {
                if (order.getOrderNo().equals(orderNo)) {
                    temporder.add(order);
                }
            }

            if (temporder.isEmpty()) {
                System.out.println("No order items found for order_no: " + orderNo);
                return false;
            }

            Database db = new Database("inventory");

            for (Order orderItem : temporder) {
            String itemId = orderItem.getItemId();
            int orderQuantity = orderItem.getQuantity();

            // Query the database for the current item and order
            db.readTable(new String[]{"quantity"}, new Object[][]{{"item_id", itemId}});
            ArrayList<ArrayList<Object>> dbResult = db.getObjResult();

            if (dbResult.isEmpty() || dbResult.get(1).isEmpty()) {
                System.out.println("No inventory record found for item_id: " + itemId + " and order_no: " + orderNo);
                return false;
            }

            // Get current inventory quantity
            double currentInventoryQuantity = (double) dbResult.get(1).get(0);
            double updatedQuantity = currentInventoryQuantity + orderQuantity;

            // Update the database with the new quantity
            Object[][] valuesToUpdate = {{"quantity", updatedQuantity}};
            Object[][] condition = {{"item_id", itemId}}; 
            boolean dbUpdateSuccess = db.updateTable(valuesToUpdate, condition);

            if (!dbUpdateSuccess) {
                System.out.println("Failed to update database for item_id: " + itemId + " and order_no: " + orderNo);
                return false;
            }
        }

        System.out.println("Inventory quantities updated successfully.");
        return true;
        }

        private static void enterToContinue(){
            System.out.println("Press Enter to continue");
            scan.nextLine();
        }

}