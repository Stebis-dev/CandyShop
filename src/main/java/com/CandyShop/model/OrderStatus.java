package com.CandyShop.model;

public enum OrderStatus {
    WAITING_FOR_PAYMENT("Waiting for Payment"),
    PROCESSING("Processing"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    COMPLETE("Complete"),
    CANCELED("Canceled");

    private final String statusName;

    OrderStatus(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return statusName;
    }
}