package com.pbo.responsi.service;

public class EventDiscountStrategy implements DiscountStrategy {

    private static final double DISCOUNT_RATE = 0.12;

    @Override
    public double calculateDiscount(double totalAmount) {
        return totalAmount * DISCOUNT_RATE;
    }

    @Override
    public String getDiscountName() {
        return "Event 12.12 (12%)";
    }
}
