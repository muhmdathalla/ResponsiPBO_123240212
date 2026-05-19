/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbo.responsi.model;

import com.pbo.responsi.dto.CartItemDTO;
import java.util.List;

/**
 *
 * @author farhannivta
 */
public interface CartRepository {
    List<CartItemDTO> findAll();
    void save(CartItemDTO item);
    void updateQuantity(String name, int newQty);
    void delete (String name);
}
