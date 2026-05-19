/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbo.responsi.model;

import com.pbo.responsi.dto.CartItemDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author farhannivta
 */
public class FakeCartRepository implements CartRepository {
    private final List<CartItemDTO> cartList = new ArrayList<>();

    public FakeCartRepository() {
        // Data awal untuk testing fungsionalitas GUI
        cartList.add(new CartItemDTO("Laptop ROG", 15000000, 1));
        cartList.add(new CartItemDTO("Mouse Wireless", 250000, 2));
    }

    @Override
    public List<CartItemDTO> findAll() {
        return new ArrayList<>(cartList);
    }

    @Override
    public void save(CartItemDTO item) {
        cartList.add(item);
    }

    @Override
    public void updateQuantity(String name, int newQty) {
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getName().equalsIgnoreCase(name)) {
                CartItemDTO old = cartList.get(i);
                cartList.set(i, new CartItemDTO(old.getName(), old.getPrice(), newQty));
                return;
            }
        }
    }

    @Override
    public void delete(String name) {
        cartList.removeIf(item -> item.getName().equalsIgnoreCase(name));
    }
}
