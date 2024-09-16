package com.project.inventory;
import java.util.ArrayList;

public abstract class User {
    static Database db = new Database("user");
    static int arrayCounter;
    
    protected static int userCount = 2300000;
    protected String user_id;
    protected String name;
    protected String password;
    protected String email;
    protected String position;
    
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
    
    //Permission?
    public abstract void permission();
    
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
    public abstract boolean equals(Object obj);
    
    //Verify Password
    public boolean passwordValid(String password){
        if (getAllPassword()[arrayCounter].equals(password)) {
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
    
    public String[] getAllId(){
        String[] verifyId = {"user_id"};
        db.readTable(verifyId);
        ArrayList<String> list = db.getResult();
        String[] idList = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            idList[i] = list.get(i).replaceAll(Database.delimiter, "");
        }
        return idList;
    }
    
    public String[] getAllName(){
        String[] tempName = {"name"};
        db.readTable(tempName);
        ArrayList<String> list = db.getResult();
        String[] nameList = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            nameList[i] = list.get(i).replaceAll(Database.delimiter, "");
        }
        return nameList;
    }
    
    public String[] getAllPosition(){
        String[] tempPosition = {"position"};
        db.readTable(tempPosition);
        ArrayList<String> list = db.getResult();
        String[] positionList = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            positionList[i] = list.get(i).replaceAll(Database.delimiter, "");
        }
        return positionList;
    }
    
    public String[] getAllPassword(){
        String[] tempPassword = {"password"};
        db.readTable(tempPassword);
        ArrayList<String> list = db.getResult();
        String[] passwordList = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            passwordList[i] = list.get(i).replaceAll(Database.delimiter, "");
        }
        return passwordList;
    }
    
    public String[] getAllEmail(){
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
    
    public String getCurrentName(){
        return getAllName()[arrayCounter];
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
}
