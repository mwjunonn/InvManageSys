package com.inventory;

public class Main{
    public static void main(String[] args) {
        Database db = new Database("supplier");
        db.startDatabase();
        String[][] condition = {{"SUPPLIER_NAME", "%%"}, {"SUPPLIER_ADDRESS", "%%"}};
        String[] column = {db.all};
        db.readRecord(column, condition);
        System.out.println(db.getResult());
    }
}
