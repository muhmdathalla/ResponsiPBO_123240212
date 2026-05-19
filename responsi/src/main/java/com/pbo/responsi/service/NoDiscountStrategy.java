/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbo.responsi.service;

/**
 *
 * @author farhannivta
 */
public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculateDiscount(double totalAmount) {
        return 0;
    }

    @Override
    public String getDiscountName() {
        return "No Discount";
    }
    
}
