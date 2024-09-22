package com.project.inventory.application;

import java.util.ArrayList;

public class InventoryAdmin extends User {
    private final static String POSITION = "InventoryAdmin";
    
    //Initialize use constructor
    public InventoryAdmin(String user_id, String name, String password, String email){
        super(user_id, name, password, email);
        super.position = this.POSITION;
    }
    
    public InventoryAdmin(String name, String password, String email){
        super(name, password, email);
        super.position = this.POSITION;
        userCount++;
    }
    
    public InventoryAdmin(String name, String password){
        super(name, password, "");
        userCount++;
    }
    
    public InventoryAdmin(){
        super();
    }
    
    public void setALL(){
        super.setALL(name, password, email);
        super.position = POSITION;
    }
    
    public Permission permission(){
        return Permission.ADMIN;
    }
    
    public boolean checkRoles(Object verify, ArrayList<User> userArr){
        for(int i = 0; i < userArr.size(); i++){
            if (userArr.get(i).getId().equals(verify) && 
                    userArr.get(i).getPosition().equals("InventoryAdmin")) {
                arrayCounter = i;
                return true;
            }
        }
        return false;
    }
}
