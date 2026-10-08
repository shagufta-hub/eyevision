package com.eyevision.eyevision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    
    private Long orderId;
    
    private String customerName;
    
    private String email;
    
    private String phoneNumber;
    
    private String productName;
    
    private Integer quantity;
    
    private Double totalPrice;
    
    private String shippingAddress;
    
    private String city;
    
    private String zipCode;
    
    private String paymentMethod;
    
    private String status;
    
    private String statusDescription;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private List<OrderTrackingDTO> trackingHistory;
}
