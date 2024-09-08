package com.project.inventory;

public class InventoryAdmin extends User {
    private final static String position = "InventoryAdmin";
    
    public InventoryAdmin(String name, String password, String email){
        super(name, password, email);
        super.position = position;
        super.userCount++;
    }
    
    public InventoryAdmin(String name, String password){
        super(name, password, "");
        super.userCount++;
    }
    
    public InventoryAdmin(){
        super();
    }
    
    public void permission(){
        
    }
    
    public boolean equals(Object verify){
        for(int i = 0; i < getAllId().length; i++){
            if (verify.equals(getAllId()[i]) && getAllPosition()[i].equals("InventoryAdmin")) {
                return true;
            }
        }
        return false;
    }
}
