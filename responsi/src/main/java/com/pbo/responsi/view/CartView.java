/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbo.responsi.view;

/**
 *
 * @author farhannivta
 */
import com.pbo.responsi.dto.CartItemDTO;
import com.pbo.responsi.model.CartRepository;
import com.pbo.responsi.service.DiscountStrategy;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener;

public class CartView extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    
    private JTextField txtName;
    private JTextField txtPrice;
    private JTextField txtQty;
    
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    
    private JLabel lblSubtotal;
    private JLabel lblDiscount;
    private JLabel lblTotalBayar;

    public CartView() {
        initComponent();
    }

    private void initComponent() {
        setTitle("Responsi PBO - E-Commerce Cart (MVP Pattern)");
        setSize(750, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PANEL INPUT (KIRI) ---
        JPanel pnlInput = new JPanel(new GridLayout(4, 2, 5, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Tambah / Ubah Item"));
        
        pnlInput.add(new JLabel("Nama Barang:"));
        txtName = new JTextField();
        pnlInput.add(txtName);

        pnlInput.add(new JLabel("Harga Satuan:"));
        txtPrice = new JTextField();
        pnlInput.add(txtPrice);

        pnlInput.add(new JLabel("Jumlah (Qty):"));
        txtQty = new JTextField();
        pnlInput.add(txtQty);

        // Komponen Button di-expose secara internal via event registerer
        btnAdd = new JButton("Tambah ke Keranjang");
        pnlInput.add(btnAdd);
        add(pnlInput, BorderLayout.WEST);

        // --- PANEL TABEL (TENGAH) ---
        String[] columns = {"Nama Barang", "Harga", "Qty", "Total"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- PANEL RINGKASAN & AKSI (BAWAH) ---
        JPanel pnlBottom = new JPanel(new BorderLayout());
        
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnUpdate = new JButton("Ubah Qty Terpilih");
        btnDelete = new JButton("Hapus Barang");
        pnlActions.add(btnUpdate);
        pnlActions.add(btnDelete);
        pnlBottom.add(pnlActions, BorderLayout.WEST);

        JPanel pnlBilling = new JPanel(new GridLayout(3, 2, 5, 2));
        pnlBilling.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
        
        pnlBilling.add(new JLabel("Subtotal:", SwingConstants.RIGHT));
        lblSubtotal = new JLabel("Rp0", SwingConstants.RIGHT);
        pnlBilling.add(lblSubtotal);

        pnlBilling.add(new JLabel("Diskon:", SwingConstants.RIGHT));
        lblDiscount = new JLabel("Rp0", SwingConstants.RIGHT);
        pnlBilling.add(lblDiscount);

        JLabel lblTotalTitle = new JLabel("TOTAL BAYAR:", SwingConstants.RIGHT);
        lblTotalTitle.setFont(new Font("Arial", Font.BOLD, 13));
        pnlBilling.add(lblTotalTitle);

        lblTotalBayar = new JLabel("Rp0", SwingConstants.RIGHT);
        lblTotalBayar.setFont(new Font("Arial", Font.BOLD, 13));
        lblTotalBayar.setForeground(Color.RED);
        pnlBilling.add(lblTotalBayar);

        pnlBottom.add(pnlBilling, BorderLayout.EAST);
        add(pnlBottom, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    // --- GETTER INPUT FORM ---
    public String getNameInput() { return txtName.getText(); }
    public String getPriceInput() { return txtPrice.getText(); }
    public String getQtyInput() { return txtQty.getText(); }

    // --- GETTER DATA TABEL ---
    public String getSelectedRowItemName() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return null;
        return tableModel.getValueAt(selectedRow, 0).toString();
    }

    // --- SETTER / UPDATE TAMPILAN (DIPANGGIL OLEH PRESENTER) ---
    public void setForm(String name, double price, int qty) {
        txtName.setText(name);
        txtPrice.setText(String.valueOf(price));
        txtQty.setText(String.valueOf(qty));
    }

    public void clearForm() {
        txtName.setText("");
        txtPrice.setText("");
        txtQty.setText("");
        table.clearSelection();
    }

    public void showCartItems(List<CartItemDTO> items, double subtotal, double discountAmount, double grandTotal, String discountName) {
        tableModel.setRowCount(0);
        for (CartItemDTO item : items) {
            double totalItemPrice = item.getPrice() * item.getQuantity();
            tableModel.addRow(new Object[]{
                    item.getName(),
                    String.format("Rp%,.2f", item.getPrice()),
                    item.getQuantity(),
                    String.format("Rp%,.2f", totalItemPrice)
            });
        }
        lblSubtotal.setText(String.format("Rp%,.2f", subtotal));
        lblDiscount.setText(String.format("-Rp%,.2f (%s)", discountAmount, discountName));
        lblTotalBayar.setText(String.format("Rp%,.2f", grandTotal));
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    // --- REGISTER EVENT LISTENERS (UNTUK DIKONTROL DI MAIN/PRESENTER) ---
    public void onAdd(ActionListener listener) { btnAdd.addActionListener(listener); }
    public void onUpdate(ActionListener listener) { btnUpdate.addActionListener(listener); }
    public void onDelete(ActionListener listener) { btnDelete.addActionListener(listener); }
    
    public void onTableSelect(ListSelectionListener listener) {
        table.getSelectionModel().addListSelectionListener(listener);
    }
}


