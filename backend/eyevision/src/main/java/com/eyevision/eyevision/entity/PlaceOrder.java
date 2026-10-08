package com.eyevision.eyevision.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "place_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    
    @Column(nullable = false)
    private String customerName;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false)
    private String productName;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private Double totalPrice;
    
    @Column(nullable = false)
    private String shippingAddress;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String zipCode;
    
    @Column(nullable = false)
    private String paymentMethod;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
