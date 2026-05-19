/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.pbo.responsi;

import com.pbo.responsi.database.DatabaseConnection;
import com.pbo.responsi.dto.CartItemDTO;
import com.pbo.responsi.model.CartRepository;
import com.pbo.responsi.repository.MySQLCartRepository;
import com.pbo.responsi.service.DiscountStrategy;
import com.pbo.responsi.service.EventDiscountStrategy;
import com.pbo.responsi.view.CartView;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author farhannivta
 */
public class Responsi {

    public static void main(String[] args) {

        // Inisialisasi database (buat DB & tabel jika belum ada)
        try {
            DatabaseConnection.initializeDatabase();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                null,
                "Gagal terhubung ke database MySQL!\n\n"
                + e.getMessage() + "\n\n"
                + "Pastikan:\n"
                + "1. MySQL server sudah berjalan\n"
                + "2. Konfigurasi di DatabaseConnection.java sudah benar\n"
                + "   (DB_USER, DB_PASS, DB_HOST, DB_PORT)",
                "Koneksi Database Gagal",
                JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }

        CartRepository repository = new MySQLCartRepository();
        DiscountStrategy discountStrategy = new EventDiscountStrategy();

        CartView view = new CartView();

        // Runnable untuk Sinkronisasi Data Model ke View (Refresh)
        Runnable refreshView = () -> {
            List<CartItemDTO> items = repository.findAll();
            
            // Hitung logika billing
            double subtotal = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
            double discountAmount = discountStrategy.calculateDiscount(subtotal);
            double grandTotal = subtotal - discountAmount;
            
            
            view.showCartItems(items, subtotal, discountAmount, grandTotal, discountStrategy.getDiscountName());
        };

        // --- BINDING EVENT LISTENERS (LOGIKA MVP) ---

      
        view.onAdd(e -> {
            String name = view.getNameInput();
            String priceStr = view.getPriceInput();
            String qtyStr = view.getQtyInput();

            if (name.isEmpty() || priceStr.isEmpty() || qtyStr.isEmpty()) {
                view.showMessage("Semua field input wajib diisi!");
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                int qty = Integer.parseInt(qtyStr);

                repository.save(new CartItemDTO(name, price, qty));
                refreshView.run();
                view.clearForm();
            } catch (NumberFormatException ex) {
                view.showMessage("Harga dan Qty harus berupa angka!");
            }
        });

      
        view.onUpdate(e -> {
            String selectedName = view.getSelectedRowItemName();
            if (selectedName == null) {
                view.showMessage("Pilih barang di tabel terlebih dahulu!");
                return;
            }

            String inputStr = JOptionPane.showInputDialog(view, "Masukkan Jumlah (Qty) Baru untuk " + selectedName + ":");
            if (inputStr != null) {
                try {
                    int newQty = Integer.parseInt(inputStr);
                    repository.updateQuantity(selectedName, newQty);
                    refreshView.run();
                    view.clearForm();
                } catch (NumberFormatException ex) {
                    view.showMessage("Qty harus diisi angka!");
                }
            }
        });

       
        view.onDelete(e -> {
            String selectedName = view.getSelectedRowItemName();
            if (selectedName == null) {
                view.showMessage("Pilih barang di tabel terlebih dahulu!");
                return;
            }

            repository.delete(selectedName);
            refreshView.run();
            view.clearForm();
        });

        
        view.onTableSelect(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedName = view.getSelectedRowItemName();
                if (selectedName == null) return;

                
                CartItemDTO targetItem = repository.findAll().stream()
                        .filter(item -> item.getName().equals(selectedName))
                        .findFirst()
                        .orElse(null);

                if (targetItem != null) {
                    view.setForm(targetItem.getName(), targetItem.getPrice(), targetItem.getQuantity());
                }
            }
        });

        // Render awal tabel saat program pertama kali dibuka
        SwingUtilities.invokeLater(refreshView::run);
    }
}
