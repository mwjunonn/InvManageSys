package com.project.inventory.ui;

import com.project.inventory.application.InventoryUIController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

public class InventoryUI extends JFrame {
    InventoryUIController controller;
    JScrollPane scrollPane;
    Container pane = new Container();
    JTable inventory_list;
    JDialog dialog;

    public InventoryUI() {
        super("Inventory Management System");
        pane.setLayout(new BorderLayout());
        setContentPane(pane);
        //Initialised Panel
    }

    public InventoryUI(InventoryUIController controller){
        this();
        this.controller = controller;
        dialog= new JDialog(this, Dialog.ModalityType.APPLICATION_MODAL);
    }

    public InventoryUIController getController() {
        return controller;
    }

    public void setController(InventoryUIController controller) {
        this.controller = controller;
    }

    private void initDialog(){
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setAlwaysOnTop(true);
        dialog.pack();
        dialog.setVisible(true);
        dialog.setLocationRelativeTo(null);
    }

    public void showInventoryListGui() {
        JTextField inventory_search = new JTextField(16);
        inventory_search.setEditable(true);
        inventory_search.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        inventory_search.setToolTipText("Search Item");
        inventory_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search = e.getActionCommand();
                inventory_list.setModel(controller.searchResult(search));
            }
        });

        inventory_search.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String search = inventory_search.getText().trim();
                inventory_list.setModel(controller.searchResult(search));
            }
        });

        inventory_search.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                inventory_search.setText("");
                inventory_search.setForeground(Color.BLACK);
                inventory_search.getInputContext().selectInputMethod(Locale.getDefault());
            }

            @Override
            public void focusLost(FocusEvent e) {
                inventory_search.setText("Search Item");
                inventory_search.setForeground(Color.GRAY);
            }
        });
        pane.add(inventory_search, BorderLayout.NORTH);
        tableInit();
        pane.add(scrollPane, BorderLayout.CENTER);
        dialog.setContentPane(pane);
        initDialog();
    }


    private void tableInit() {
        inventory_list = controller.getTable();
        inventory_list.setFont(new Font("MiSans", Font.PLAIN, 12));
        inventory_list.setRowHeight(32);
        inventory_list.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                int rowSelected = inventory_list.getSelectedRow();
                controller.setRowSelectedIndex(rowSelected);
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        inventory_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.scrollPane = new JScrollPane(inventory_list);
    }
}

