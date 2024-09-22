package com.project.inventory.application;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.*;

public class InventoryUIController{
    private int selectedIndex = -1;
    private final ArrayList<Object> items;
    private HashMap<Integer, Integer> indexOfRow;
    private TableMode tableMode;

    public InventoryUIController(TableMode mode) {
        if (mode.equals(TableMode.INVENTORY)) {
            items = Inventory.getInstance().getItemListWithColumns();
        } else if(mode.equals(TableMode.INVENTORY_WITH_SUPPLIER)){
            items = new SupplyItem().getSupplyItemWithColumn();
        }else{
            items = null;
        }
        tableMode = mode;
        initIndexOfItem();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public JTable getTable() {
        return new JTable(getTableModel(items));
    }

    public void setRowSelectedIndex(int index) {
        this.selectedIndex = indexOfRow.get(index);
    }

    public TableModel searchResult(String search) {
        ArrayList<Object> rowSearched = new ArrayList<>();
        rowSearched.add(items.getFirst());
        indexOfRow.clear();
        int j = 0;
        for (int i = 1; i < items.size(); i++) {
            if (((Item) items.get(i)).getItemName().toLowerCase().contains(search.toLowerCase())) {
                rowSearched.add(items.get(i));
                indexOfRow.put(j, i - 1);
                j++;
            }
        }
        return getTableModel(rowSearched);
    }

    private void initIndexOfItem(){
            indexOfRow = new HashMap<>();
            for (int i = 0; i < items.size()-1; i++) {
                indexOfRow.put(i, i);
            }
        }

    public TableModel getTableModel(ArrayList<Object> table) {
        Object[][] row;
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
            } else if(table.getFirst() instanceof String[] && table.getLast() instanceof Object[]) {
                ArrayList<Object> tempTable = (ArrayList<Object>) table.clone();
                columns = ((String[]) tempTable.getFirst());
                tempTable.removeFirst();
                Object[] rows = tempTable.toArray();
                row = new Object[rows.length][columns.length];
                for (int i = 0; i < rows.length; i++) {
                    for (int j = 0; j < columns.length; j++) {
                        row[i][j] = ((Object[])rows[i])[j];
                    }
                }
                return new DefaultTableModel(row, columns) {
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
            }
            else {
                return new DefaultTableModel(0, 0);
            }
        } else {
            columns = ((String[]) table.getFirst());
            return new DefaultTableModel(columns, 0);
        }
    }

    public enum TableMode{
        INVENTORY,
        INVENTORY_WITH_SUPPLIER
    }
}
