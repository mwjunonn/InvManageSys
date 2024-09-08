/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.inventory;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author Lee
 */
public class Test {
    static Scanner scan = new Scanner(System.in);
    static Database db = new Database("user");
    
    public static void main(String[] args){
        System.out.print("Enter Integer: ");
        int temp = scan.nextInt();
        scan.next();
        System.out.print("Enter ID: ");
        String managerID = new String();
        
        managerID = scan.nextLine();
        System.out.println(equals(managerID));
    }
    
    
    public static boolean equals(String userID){
        String[] verifyId = {"user_id"};
        db.readTable(verifyId);
        ArrayList<String> list = db.getResult();
        String[] idList = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            idList[i] = list.get(i).replaceAll(Database.spliter, "");
        }
        for(int k = 0; k < idList.length; k++){
            System.out.println(idList[k]);
            if(userID.equals(idList[k])){
                return true;
            }
        }
        return false;
    }
}
