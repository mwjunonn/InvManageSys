package com.project.inventory.application;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class InventoryFileIO extends Thread{

        public InventoryFileIO() {
            super();
            createCurrentStockListFile();
        }

    public void createCurrentStockListFile() {
        try {
            LocalDateTime now = LocalDateTime.now();
            File file = new File("./CSR" + now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss")) + ".txt");
            if (!file.createNewFile()) {
                JOptionPane.showMessageDialog(null, "Failed to create file");
                return;
            }
            if (!file.setWritable(true)) {
                JOptionPane.showMessageDialog(null, "Failed to create file");
                return;
            }
            FileWriter fw = new FileWriter(file);
            fw.write(String.format("%30s\n\n", "Current Stock Report as at " + now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
            ArrayList<Object> itemListWithColumns = Inventory.getInstance().getItemListWithColumns();
            for (String s : (String[]) itemListWithColumns.getFirst()) {
                fw.write(String.format("%-15s | ", s));
            }
            fw.write("\n");
            for (int i = 1; i < itemListWithColumns.size(); i++) {
                for (int j = 0; j < 54; j++) {
                    fw.write("-");
                }
                fw.write("\n");

                fw.write(String.format("%-15s | %-15s | %-15s | \n", ((Item) itemListWithColumns.get(i)).getItemName(), ((Item) itemListWithColumns.get(i)).getItemType(), String.format("%.2f/%s",  ((Item) itemListWithColumns.get(i)).getQuantity(),  ((Item) itemListWithColumns.get(i)).getItemUnit())));
            }
            for (int j = 0; j < 54; j++) {
                fw.write("-");
            }
            fw.close();
            if (!file.exists())
                JOptionPane.showMessageDialog(null, "Failed to create file");
            else
                JOptionPane.showMessageDialog(null, "Successfully created file of " + file.getName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void run() {
    }
}
