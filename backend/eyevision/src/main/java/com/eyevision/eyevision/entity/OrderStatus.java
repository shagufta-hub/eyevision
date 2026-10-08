package com.eyevision.eyevision.entity;

public enum OrderStatus {
    PENDING("Order Pending", "Your order is being processed"),
    CONFIRMED("Order Confirmed", "Your order has been confirmed"),
    PROCESSING("Processing", "Your order is being prepared for shipment"),
    SHIPPED("Shipped", "Your order has been shipped"),
    DELIVERED("Delivered", "Your order has been delivered"),
    CANCELLED("Cancelled", "Your order has been cancelled");
    
    private final String displayName;
    private final String description;
    
    OrderStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}
