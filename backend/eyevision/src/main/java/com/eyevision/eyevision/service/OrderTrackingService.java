package com.eyevision.eyevision.service;

import com.eyevision.eyevision.entity.OrderTracking;
import com.eyevision.eyevision.entity.OrderTrackingDTO;
import java.util.List;

public interface OrderTrackingService {
    
    OrderTracking addTracking(Long orderId, String status, String description);
    
    List<OrderTrackingDTO> getTrackingHistory(Long orderId);
    
    OrderTrackingDTO getLatestTracking(Long orderId);
}
