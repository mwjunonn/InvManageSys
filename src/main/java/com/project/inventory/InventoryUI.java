package com.project.inventory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;

public class InventoryUI extends JFrame {
    Database db = new Database("item");

    public InventoryUI(){
        super("Inventory Management System");
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
    }

    public void showInventoryGui(ArrayList<Object> table){
        setTitle("Inventory List");
        JScrollPane inventory_list_scroll;
        String[][] row;
        String[] columns;;
        if(table.get(1) instanceof Item && table.get(0) instanceof String[]) {
            row = new String[table.size()][((String[])table.get(0)).length];
            for (int i = 1; i < row.length; i++) {
                int j = 0;
                for (String str : table.get(i).toString().split("\t")) {
                    row[i - 1][j] = str;
                    j++;
                }
            }
            columns = ((String[])table.get(0));
        }else{
            return;
        }
        TableModel model = new DefaultTableModel(row, columns){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable inventory_list = new JTable(model);
        inventory_list_scroll = new JScrollPane(inventory_list);
        inventory_list.setVisible(true);
        inventory_list.setSize(100, 100);
        add(inventory_list_scroll);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}
