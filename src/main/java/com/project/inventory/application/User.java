package com.project.inventory.application;
import com.project.inventory.dao.Database;

import java.util.ArrayList;

public abstract class User {
    static Database db = new Database("user");
    public static int arrayCounter = 0;
    public static int currentUserLoginCounter = 0;
    
    protected static int userCount = 2300000;
    protected String user_id;
    protected String name;
    protected String password;
    protected String email;
    protected String position;
    
    //Initialize use constructor
    public User(String user_id, String name, String password, String email){
        this.user_id = user_id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.position = "";
    }
    
    public User(String name, String password, String email){
        this.user_id = idToString();
        this.name = name;
        this.password = password;
        this.email = email;
        position = "";
        
    }
    
    public User(String name, String password){
        this(name, password, "");
    }
    
    public User(){
        this("", "");
    }
    
    //Current login user
    public void getCurrentLoginUser(){
        currentUserLoginCounter = arrayCounter;
    }
    
    //Permission?
    public abstract Permission permission();
    
    //Initialize object usage
    public static ArrayList<User> initializeUser(){
        ArrayList<User> user = new ArrayList<User>();
        db.readTable(new String[]{"user_id"});  //get total column count, user_id != null
        
        String[] name = getAllName();
        String[] position = getAllPosition();
        String[] password = getAllPassword();
        String[] email = getAllEmail();
        String[] userID = getAllId();
        int size = db.getResult().size() + 1;
        
        for (int i = 1; i < size; i++) {
            if (position[i].equals("Manager")) {
                user.add(new Manager(userID[i], name[i], password[i], email[i]));
            }
            if (position[i].equals("InventoryAdmin")) {
                user.add(new InventoryAdmin(userID[i], name[i], password[i], email[i]));
            }
        }
        
        return user;
    }
    
    //Verify Name
    public static void nameValidation(String name){
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Name is not allow to be empty.");
        }
    }
    
    public static void passwordValidation(String password){
        if (password == null || password.equals("")) {
            throw new IllegalArgumentException("Password is not allow to be empty.");
        }
    }
    
    //Verify ID
    public abstract boolean checkRoles(Object obj, ArrayList<User> userArr);
    
    //Verify Password
    public boolean passwordValid(String password, ArrayList<User> userArr){
        if (userArr.get(arrayCounter).getPassword().equals(password)) {
            return true;
        }
        return false;
    }
    
    public void writeToDatabase(){
        String[] insertColumn = {"user_id", "name", "password", "position", "email"};
        String[] insertValue = {this.user_id, this.name, this.password, this.position, this.email};
        db.insertTable(insertColumn, insertValue);
    }
    
    //Mutator (ID and Position auto assign)
    public void setName(String name){
        nameValidation(name);
        this.name = name;
    }
    
    public void setPassword(String password){
        passwordValidation(password);
        this.password = password;
    }
    
    public void setEmail(String email){
        this.email = email;         //email can be empty in database (some people might not have email)
    }
    
    public void setALL (String name, String password, String email){
        setName(name);
        setPassword(password);
        setEmail(email);
    }
    
    protected static String[] getAllId(){
        String[] verifyId = {"user_id"};
        db.readTable(verifyId);
        ArrayList<String> list = db.getResult();
        String[] idList = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            idList[i] = list.get(i).replaceAll(Database.delimiter, "");
        }
        return idList;
    }
    
    protected static String[] getAllName(){
        String[] tempName = {"name"};
        db.readTable(tempName);
        ArrayList<String> list = db.getResult();
        String[] nameList = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            nameList[i] = list.get(i).replaceAll(Database.delimiter, "");
        }
        return nameList;
    }
    
    protected static String[] getAllPosition(){
        String[] tempPosition = {"position"};
        db.readTable(tempPosition);
        ArrayList<String> list = db.getResult();
        String[] positionList = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            positionList[i] = list.get(i).replaceAll(Database.delimiter, "");
        }
        return positionList;
    }
    
    protected static String[] getAllPassword(){
        String[] tempPassword = {"password"};
        db.readTable(tempPassword);
        ArrayList<String> list = db.getResult();
        String[] passwordList = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            passwordList[i] = list.get(i).replaceAll(Database.delimiter, "");
        }
        return passwordList;
    }
    
    protected static String[] getAllEmail(){
        String[] tempEmail = {"email"};
        db.readTable(tempEmail);
        ArrayList<String> list = db.getResult();
        String[] emailList = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).replaceAll(Database.delimiter, "").equals("")) {
                emailList[i] = "None";
            }
            else
                emailList[i] = list.get(i).replaceAll(Database.delimiter, "");
        }
        return emailList;
    }
    
    //Accessor
    public String getName(){
        return name;
    }
    
    public String getPassword(){
        return password;
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getId(){
        return user_id;
    }
    
    public String getPosition(){
        return position;
    }
    
    public String getCurrentName(){
        return initializeUser().get(arrayCounter).getName();
    }
    
    public String getCurrentID(){
        return initializeUser().get(arrayCounter).getId();
    }
    
    public String getCurrentPassword(){
        return initializeUser().get(arrayCounter).getPassword();
    }
    
    public String getCurrentEmail(){
        return initializeUser().get(arrayCounter).getEmail();
    }
    
    // For assigning userID automatically without duplication causing any errors
    public int checkNonDuplicateID(){
        int tempId = userCount;
        getAllId();
        for(int i = 1; i < getAllId().length; i++){
            if (tempId == Integer.valueOf(getAllId()[i])) {
                tempId++;
            }
        }
        return tempId;
    }
    
    public String idToString(){
        return "" + checkNonDuplicateID();
    }
    
    public String toString(){
        return "Id: " + user_id +
                "\nName: " + name +
                "\nPassword: " + password + 
                "\nPosition: " + position;
    }

    /**
     * Use this enum to divide the permission of Inventory
     */
    public enum Permission {
        FULL_CONTROL,
        ADMIN,
        GENERAL
    }
}
