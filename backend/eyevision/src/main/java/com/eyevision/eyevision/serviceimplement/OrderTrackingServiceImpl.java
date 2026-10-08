package com.eyevision.eyevision.serviceimplement;

import com.eyevision.eyevision.entity.OrderStatus;
import com.eyevision.eyevision.entity.OrderTracking;
import com.eyevision.eyevision.entity.OrderTrackingDTO;
import com.eyevision.eyevision.repository.OrderTrackingRepository;
import com.eyevision.eyevision.service.OrderTrackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderTrackingServiceImpl implements OrderTrackingService {
    
    @Autowired
    private OrderTrackingRepository orderTrackingRepository;
    
    @Override
    public OrderTracking addTracking(Long orderId, String status, String description) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status);
            OrderTracking tracking = new OrderTracking();
            tracking.setOrderId(orderId);
            tracking.setStatus(orderStatus);
            tracking.setDescription(description);
            
            return orderTrackingRepository.save(tracking);
        } catch (IllegalArgumentException e) {
            log.error("Invalid order status: {}", status);
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
    }
    
    @Override
    public List<OrderTrackingDTO> getTrackingHistory(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("OrderId must be valid");
        }
        
        List<OrderTracking> trackingList = orderTrackingRepository.findByOrderIdOrderByCreatedAtDesc(orderId);
        return trackingList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public OrderTrackingDTO getLatestTracking(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("OrderId must be valid");
        }
        
        List<OrderTracking> trackingList = orderTrackingRepository.findByOrderIdOrderByCreatedAtDesc(orderId);
        if (trackingList.isEmpty()) {
            return null;
        }
        return convertToDTO(trackingList.get(0));
    }
    
    private OrderTrackingDTO convertToDTO(OrderTracking tracking) {
        OrderTrackingDTO dto = new OrderTrackingDTO();
        dto.setTrackingId(tracking.getTrackingId());
        dto.setOrderId(tracking.getOrderId());
        dto.setStatus(tracking.getStatus().toString());
        dto.setStatusDescription(tracking.getStatus().getDescription());
        dto.setDescription(tracking.getDescription());
        dto.setCreatedAt(tracking.getCreatedAt());
        return dto;
    }
}
