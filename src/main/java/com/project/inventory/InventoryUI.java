package com.project.inventory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

public class InventoryUI extends JFrame {
    Database db = new Database("item");
    JScrollPane table;
    Container pane = new Container();
    DefaultTableModel model;
    JTable inventory_list;
    int inventorySelectedIndex = -1;
    JDialog dialog = new JDialog(this, Dialog.ModalityType.DOCUMENT_MODAL);
    ArrayList<Object> items;

    public InventoryUI() {
        super("Inventory Management System");
        System.setProperty("javax.imageio.plugins.png.convertToRGB", "true");
        pane.setLayout(new BorderLayout());
        setContentPane(pane);
        //Initialised Panel
    }
    public InventoryUI(ArrayList<Object> item){
        this();
        this.items = item;
    }

    public void initDialog(){
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setAlwaysOnTop(true);
        dialog.pack();
        dialog.setVisible(true);
        dialog.setLocationRelativeTo(null);
    }

    public void inventoryListGui() {
        JTextField inventory_search = new JTextField(16);
        inventory_search.setEditable(true);
        inventory_search.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        inventory_search.setToolTipText("Search Item");
        inventory_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search = e.getActionCommand();
                updateTableModel(searchResult(search));
                inventory_list.setModel(model);
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
                updateTableModel(searchResult(search));
                inventory_list.setModel(model);
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
        addTable();
        pane.add(table, BorderLayout.CENTER);
        dialog.setContentPane(pane);
        initDialog();
    }

    private ArrayList<Object> searchResult(String search) {
        ArrayList<Object> rowSearched = new ArrayList<>();
        rowSearched.add(items.getFirst());
        for (int i = 1; i < items.size(); i++) {
            if (((Item) items.get(i)).getItemName().toLowerCase().contains(search.toLowerCase())) {
                rowSearched.add(items.get(i));
            }
        }
        return rowSearched;
    }

    public void addTable() {
        JScrollPane inventory_list_scroll;
        updateTableModel(items);
        inventory_list = new JTable(model);
        inventory_list.setFont(new Font("MiSans", Font.PLAIN, 12));
        inventory_list.setRowHeight(32);
        inventory_list.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                inventorySelectedIndex = inventory_list.getSelectedRow();
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        inventory_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table = new JScrollPane(inventory_list);
    }

    public int getItemSelectedIndex(){
        return inventorySelectedIndex;
    }

    private void updateTableModel(ArrayList<Object> table) {
        String[][] row;
        String[] columns;
        if (table.size() > 1) {
            if (table.get(1) instanceof Item && table.get(0) instanceof String[]) {
                row = new String[table.size() - 1][((String[]) table.get(0)).length];
                for (int i = 1; i < row.length + 1; i++) {
                    int j = 0;
                    for (String str : table.get(i).toString().split("\t")) {
                        row[i - 1][j] = str;
                        j++;
                    }
                }
                columns = ((String[]) table.get(0));
            } else {
                return;
            }
            model = new DefaultTableModel(row, columns) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        } else {
            columns = ((String[]) table.getFirst());
            model = new DefaultTableModel(columns, 0);
        }
    }
}