package com.pbo.responsi.repository;

import com.pbo.responsi.database.DatabaseConnection;
import com.pbo.responsi.dto.CartItemDTO;
import com.pbo.responsi.model.CartRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLCartRepository implements CartRepository {

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<CartItemDTO> findAll() {
        List<CartItemDTO> result = new ArrayList<>();
        String sql = "SELECT name, price, quantity FROM cart_items ORDER BY id ASC";

        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(new CartItemDTO(
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error findAll: " + e.getMessage());
        }

        return result;
    }

    @Override
    public void save(CartItemDTO item) {
        String sql = "INSERT INTO cart_items (name, price, quantity) VALUES (?, ?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());
            ps.setInt(3, item.getQuantity());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error save: " + e.getMessage());
        }
    }

    @Override
    public void updateQuantity(String name, int newQty) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE name = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, newQty);
            ps.setString(2, name);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updateQuantity: " + e.getMessage());
        }
    }

    @Override
    public void delete(String name) {
        String sql = "DELETE FROM cart_items WHERE name = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error delete: " + e.getMessage());
        }
    }
}
