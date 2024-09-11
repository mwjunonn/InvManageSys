package com.project.inventory;
import java.util.ArrayList;

public class Manager extends User {
    private final static String position = "Manager";

    public Manager(String name, String password, String email){
        super(name, password, email);
        super.position = position;
        super.userCount++;
    }
    
    public Manager(String name, String password){
        super(name, password, "");
        super.position = position;
        super.userCount++;
    }
    
    public Manager(){
        super();
    }
    
    public void setALL(){
        super.setALL(name, password, email);
        super.position = position;
    }
    
    public void permission(){
        
    }
    
    //Verify Id
    public boolean equals(Object verify){
        for(int i = 0; i < getAllId().length; i++){
            if (verify.equals(getAllId()[i]) && getAllPosition()[i].equals("Manager")) {
                arrayCounter = i;
                return true;
            }
        }
        return false;
    }
    
}
