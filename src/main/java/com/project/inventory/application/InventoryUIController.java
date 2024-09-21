package com.project.inventory.application;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.HashMap;

public class InventoryUIController{
    private int inventorySelectedIndex = -1;
    private final ArrayList<Object> items;
    private HashMap<Integer, Integer> indexOfItem;

    public InventoryUIController(ArrayList<Object> items) {
        this.items = items;
        initIndexOfItem();
    }

    public int getInventorySelectedIndex() {
        return inventorySelectedIndex;
    }

    public TableModel searchResult(String search) {
        ArrayList<Object> rowSearched = new ArrayList<>();
        rowSearched.add(items.getFirst());
        indexOfItem.clear();
        int j = 0;
        for (int i = 1; i < items.size(); i++) {
            if (((Item) items.get(i)).getItemName().toLowerCase().contains(search.toLowerCase())) {
                rowSearched.add(items.get(i));
                indexOfItem.put(j, i - 1);
                j++;
            }
        }
        return getTableModel(rowSearched);
    }

    public JTable getTable() {
        return new JTable(getTableModel(items));
    }

    private void initIndexOfItem(){
        indexOfItem = new HashMap<>();
        for (int i = 0; i < items.size()-1; i++) {
            indexOfItem.put(i, i);
        }
    }

    public TableModel getTableModel(ArrayList<Object> table) {
        String[][] row;
        String[] columns;
        if (table.size() > 1) {
            if (table.get(1) instanceof Item && table.get(0) instanceof String[]) {
                row = new String[table.size() - 1][((String[]) table.getFirst()).length];
                for (int i = 1; i < row.length + 1; i++) {
                    int j = 0;
                    for (String str : table.get(i).toString().split("\t")) {
                        row[i - 1][j] = str;
                        j++;
                    }
                }
                columns = ((String[]) table.getFirst());
                return new DefaultTableModel(row, columns) {
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
            } else {
                return new DefaultTableModel(0, 0);
            }
        } else {
            columns = ((String[]) table.getFirst());
            return new DefaultTableModel(columns, 0);
        }
    }

    public void setRowSelectedIndex(int index) {
        this.inventorySelectedIndex = indexOfItem.get(index);
    }
}
