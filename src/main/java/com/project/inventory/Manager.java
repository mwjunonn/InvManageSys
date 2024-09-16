package com.project.inventory;

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
    
    public void displayAllUser(){
        String[] userId = super.getAllId();
        String[] userName = super.getAllName();
        String[] position = super.getAllPosition();
        String[] email = super.getAllEmail();
        String[] user = new String[userId.length];
        
        System.out.println();
        System.out.printf("%-6s %-10s %-20s %-30s %s\n", "RowNo.", "User ID", "User Name", "Position", "Email");
        System.out.printf("%-6s %-10s %-20s %-30s %s\n", "------", "-------", "---------", "--------", "-----");
        
        for (int i = 1; i < userId.length; i++) {
            System.out.printf("%-6d %-10s %-20s %-30s %s\n",i, userId[i], userName[i], position[i], email[i]);
        }
    }
    
    public void modifyStaff(String name){
        
    }
    
    public void modifyStaff(String name, String password){
        
    }
    
}
