package com.eyevision.eyevision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTrackingDTO {
    
    private Long trackingId;
    
    private Long orderId;
    
    private String status;
    
    private String statusDescription;
    
    private String description;
    
    private LocalDateTime createdAt;
}
