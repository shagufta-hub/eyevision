package com.eyevision.eyevision.serviceimplement;

import com.eyevision.eyevision.entity.PlaceOrder;
import com.eyevision.eyevision.entity.OrderResponseDTO;
import com.eyevision.eyevision.entity.OrderTrackingDTO;
import com.eyevision.eyevision.repository.PlaceOrderRepository;
import com.eyevision.eyevision.service.PlaceOrderService;
import com.eyevision.eyevision.service.EmailService;
import com.eyevision.eyevision.service.OrderTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlaceOrderServiceImpl implements PlaceOrderService {
    
    @Autowired
    private PlaceOrderRepository placeOrderRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private OrderTrackingService orderTrackingService;
    
    @Override
    public PlaceOrder placeOrder(PlaceOrder placeOrder) {
        if (placeOrder == null) {
            throw new IllegalArgumentException("PlaceOrder cannot be null");
        }
        placeOrder.setCreatedAt(LocalDateTime.now());
        placeOrder.setUpdatedAt(LocalDateTime.now());
        
        // Save order
        PlaceOrder savedOrder = placeOrderRepository.save(placeOrder);
        
        // Add initial tracking entry
        orderTrackingService.addTracking(
            savedOrder.getOrderId(),
            "PENDING",
            "Your order has been placed successfully"
        );
        
        // Send confirmation email
        emailService.sendOrderConfirmation(savedOrder);
        
        return savedOrder;
    }
    
    @Override
    public Optional<PlaceOrder> getOrderById(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("OrderId must be valid");
        }
        return placeOrderRepository.findByOrderId(orderId);
    }
    
    @Override
    public OrderResponseDTO getOrderWithTracking(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("OrderId must be valid");
        }
        
        Optional<PlaceOrder> order = placeOrderRepository.findByOrderId(orderId);
        if (order.isEmpty()) {
            throw new IllegalArgumentException("Order not found with id: " + orderId);
        }
        
        PlaceOrder placeOrder = order.get();
        OrderResponseDTO response = convertToDTO(placeOrder);
        
        // Get tracking history
        List<OrderTrackingDTO> trackingHistory = orderTrackingService.getTrackingHistory(orderId);
        response.setTrackingHistory(trackingHistory);
        
        return response;
    }
    
    @Override
    public List<PlaceOrder> getAllOrders() {
        return placeOrderRepository.findAll();
    }
    
    @Override
    public List<PlaceOrder> getOrdersByCustomerName(String customerName) {
        if (customerName == null || customerName.isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }
        return placeOrderRepository.findByCustomerName(customerName);
    }
    
    @Override
    public List<PlaceOrder> getOrdersByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        return placeOrderRepository.findByEmail(email);
    }
    
    @Override
    public List<PlaceOrder> getOrdersByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        return placeOrderRepository.findByPhoneNumber(phoneNumber);
    }
    
    @Override
    public PlaceOrder updateOrder(Long orderId, PlaceOrder placeOrder) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("OrderId must be valid");
        }
        
        Optional<PlaceOrder> existingOrder = placeOrderRepository.findByOrderId(orderId);
        if (existingOrder.isPresent()) {
            PlaceOrder order = existingOrder.get();
            String previousStatus = order.getStatus().toString();
            
            if (placeOrder.getCustomerName() != null) {
                order.setCustomerName(placeOrder.getCustomerName());
            }
            if (placeOrder.getEmail() != null) {
                order.setEmail(placeOrder.getEmail());
            }
            if (placeOrder.getPhoneNumber() != null) {
                order.setPhoneNumber(placeOrder.getPhoneNumber());
            }
            if (placeOrder.getProductName() != null) {
                order.setProductName(placeOrder.getProductName());
            }
            if (placeOrder.getQuantity() != null) {
                order.setQuantity(placeOrder.getQuantity());
            }
            if (placeOrder.getTotalPrice() != null) {
                order.setTotalPrice(placeOrder.getTotalPrice());
            }
            if (placeOrder.getShippingAddress() != null) {
                order.setShippingAddress(placeOrder.getShippingAddress());
            }
            if (placeOrder.getCity() != null) {
                order.setCity(placeOrder.getCity());
            }
            if (placeOrder.getZipCode() != null) {
                order.setZipCode(placeOrder.getZipCode());
            }
            if (placeOrder.getPaymentMethod() != null) {
                order.setPaymentMethod(placeOrder.getPaymentMethod());
            }
            if (placeOrder.getStatus() != null) {
                order.setStatus(placeOrder.getStatus());
                
                // Add tracking entry if status changed
                if (!previousStatus.equals(placeOrder.getStatus().toString())) {
                    orderTrackingService.addTracking(
                        orderId,
                        placeOrder.getStatus().toString(),
                        "Order status updated to " + placeOrder.getStatus().getDisplayName()
                    );
                    
                    // Send status update email
                    PlaceOrder updatedOrder = placeOrderRepository.save(order);
                    emailService.sendOrderStatusUpdate(updatedOrder, previousStatus);
                    return updatedOrder;
                }
            }
            
            order.setUpdatedAt(LocalDateTime.now());
            return placeOrderRepository.save(order);
        }
        throw new IllegalArgumentException("Order not found with id: " + orderId);
    }
    
    @Override
    public boolean deleteOrder(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("OrderId must be valid");
        }
        
        if (placeOrderRepository.existsById(orderId)) {
            placeOrderRepository.deleteById(orderId);
            return true;
        }
        return false;
    }
    
    @Override
    public long getTotalOrderCount() {
        return placeOrderRepository.count();
    }
    
    private OrderResponseDTO convertToDTO(PlaceOrder placeOrder) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(placeOrder.getOrderId());
        dto.setCustomerName(placeOrder.getCustomerName());
        dto.setEmail(placeOrder.getEmail());
        dto.setPhoneNumber(placeOrder.getPhoneNumber());
        dto.setProductName(placeOrder.getProductName());
        dto.setQuantity(placeOrder.getQuantity());
        dto.setTotalPrice(placeOrder.getTotalPrice());
        dto.setShippingAddress(placeOrder.getShippingAddress());
        dto.setCity(placeOrder.getCity());
        dto.setZipCode(placeOrder.getZipCode());
        dto.setPaymentMethod(placeOrder.getPaymentMethod());
        dto.setStatus(placeOrder.getStatus().toString());
        dto.setStatusDescription(placeOrder.getStatus().getDescription());
        dto.setCreatedAt(placeOrder.getCreatedAt());
        dto.setUpdatedAt(placeOrder.getUpdatedAt());
        return dto;
    }

    
}
