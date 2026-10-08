package com.eyevision.eyevision.service;

import com.eyevision.eyevision.entity.PlaceOrder;
import com.eyevision.eyevision.entity.OrderResponseDTO;
import java.util.List;
import java.util.Optional;

public interface PlaceOrderService {
    
    PlaceOrder placeOrder(PlaceOrder placeOrder);
    
    Optional<PlaceOrder> getOrderById(Long orderId);
    
    OrderResponseDTO getOrderWithTracking(Long orderId);
    
    List<PlaceOrder> getAllOrders();
    
    List<PlaceOrder> getOrdersByCustomerName(String customerName);
    
    List<PlaceOrder> getOrdersByEmail(String email);
    
    List<PlaceOrder> getOrdersByPhoneNumber(String phoneNumber);
    
    PlaceOrder updateOrder(Long orderId, PlaceOrder placeOrder);
    
    boolean deleteOrder(Long orderId);
    
    long getTotalOrderCount();
}
