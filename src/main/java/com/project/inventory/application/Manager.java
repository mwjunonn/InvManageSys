package com.project.inventory.application;
import java.util.ArrayList;

public class Manager extends User {
    private final static String POSITION = "Manager";
    
    //Initialize use constructor
    public Manager(String user_id, String name, String password, String email){
        super(user_id, name, password, email);
        super.position = this.POSITION;
    }

    public Manager(String name, String password, String email){
        super(name, password, email);
        super.position = this.POSITION;
        super.userCount++;
    }
    
    public Manager(String name, String password){
        super(name, password, "");
        super.position = this.POSITION;
        super.userCount++;
    }
    
    public Manager(){
        super();
    }
    
    public void setALL(){
        super.setALL(name, password, email);
        super.position = POSITION;
    }
    
    public Permission permission(){
        return Permission.FULL_CONTROL;
    }
    
    //Verify Id
    public boolean checkRoles(Object verify, ArrayList<User> userArr){
        for(int i = 0; i < userArr.size(); i++){
            if (userArr.get(i).getId().equals(verify) &&
                    userArr.get(i).getPosition().equals("Manager")) {
                arrayCounter = i;
                return true;
            }
        }
        return false;
    }
    
    public void displayAllUser(ArrayList<User> userArr){
        System.out.println();
        System.out.printf("%-6s %-10s %-20s %-30s %s\n", "RowNo.", "User ID", "User Name", "Position", "Email");
        System.out.printf("%-6s %-10s %-20s %-30s %s\n", "------", "-------", "---------", "--------", "-----");
        
        for (int i = 0; i < userArr.size(); i++) {
            System.out.printf("%-6d %-10s %-20s %-30s %s\n",(i+1), userArr.get(i).getId(), userArr.get(i).getName(), 
                    userArr.get(i).getPosition(), userArr.get(i).getEmail());
        }
    }
    
    
    public void modifyStaff(String userID, int modifyAttributes, ArrayList<User> userArr){
        String[][] modifyColumn;
        String[][] conditions = {{"user_id", userID}};
        switch(modifyAttributes){
            case 1:     //Name
                modifyColumn = new String[][]{{"name", super.name}};
                for (int i = 0; i < userArr.size(); i++) {
                    if (userArr.get(i).getName().equals(super.name)) {
                        arrayCounter = i;
                    }
                }
                db.updateTable(modifyColumn, conditions);
                break;
            case 2:     //Password
                modifyColumn = new String[][]{{"password", super.password}};
                for (int i = 0; i < userArr.size(); i++) {
                    if (userArr.get(i).getPassword().equals(super.password)) {
                        arrayCounter = i;
                    }
                }
                db.updateTable(modifyColumn, conditions);
                break;
            case 3:     //Email
                modifyColumn = new String[][]{{"email", super.email}};
                for (int i = 0; i < userArr.size(); i++) {
                    if (userArr.get(i).getEmail().equals(super.email)) {
                        arrayCounter = i;
                    }
                }
                db.updateTable(modifyColumn, conditions);
                break;
            case 4:     //None
                break;
            default:
                break;
        }
    }
    
    public void deleteStaff(String deleteID, ArrayList<User> userArr){
        for (int i = 0; i < userArr.size(); i++) {
            if (userArr.get(i).getId().equals(deleteID)) {
                arrayCounter = i;
            }
        }
        db.deleteRecord(new String[][]{{"user_id", deleteID}});
    }
    
}
