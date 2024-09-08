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
    
    public String[] getAllId(){
        String[] verifyId = {"user_id"};
        db.readTable(verifyId);
        ArrayList<String> list = db.getResult();
        String[] idList = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            idList[i] = list.get(i).replaceAll(Database.spliter, "");
        }
        return idList;
    }
    
    public String[] getAllPosition(){
        String[] tempPosition = {"position"};
        db.readTable(tempPosition);
        ArrayList<String> list = db.getResult();
        String[] positionList = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            positionList[i] = list.get(i).replaceAll(Database.spliter, "");
        }
        return positionList;
    }
    
    public String[] getAllPassword(){
        String[] tempPassword = {"password"};
        db.readTable(tempPassword);
        ArrayList<String> list = db.getResult();
        String[] passwordList = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            passwordList[i] = list.get(i).replaceAll(Database.spliter, "");
        }
        return passwordList;
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
