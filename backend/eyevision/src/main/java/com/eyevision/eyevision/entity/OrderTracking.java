package com.eyevision.eyevision.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_tracking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTracking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackingId;
    
    @Column(nullable = false)
    private Long orderId;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
