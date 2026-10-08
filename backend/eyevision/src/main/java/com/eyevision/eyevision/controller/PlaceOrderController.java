package com.eyevision.eyevision.controller;

import com.eyevision.eyevision.entity.PlaceOrder;
import com.eyevision.eyevision.entity.OrderResponseDTO;
import com.eyevision.eyevision.entity.OrderTrackingDTO;
import com.eyevision.eyevision.service.PlaceOrderService;
import com.eyevision.eyevision.service.OrderTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = {"http://localhost:4200", "https://evoptical.in"})
public class PlaceOrderController {
    
    @Autowired
    private PlaceOrderService placeOrderService;
    
    @Autowired
    private OrderTrackingService orderTrackingService;
    
    @PostMapping("/place")
    public ResponseEntity<PlaceOrder> placeOrder(@RequestBody PlaceOrder placeOrder) {
        try {
            PlaceOrder createdOrder = placeOrderService.placeOrder(placeOrder);
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<PlaceOrder> getOrderById(@PathVariable Long orderId) {
        try {
            Optional<PlaceOrder> order = placeOrderService.getOrderById(orderId);
            if (order.isPresent()) {
                return new ResponseEntity<>(order.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{orderId}/track")
    public ResponseEntity<OrderResponseDTO> getOrderWithTracking(@PathVariable Long orderId) {
        try {
            OrderResponseDTO order = placeOrderService.getOrderWithTracking(orderId);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<PlaceOrder>> getAllOrders() {
        try {
            List<PlaceOrder> orders = placeOrderService.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<PlaceOrder>> getOrdersByCustomerName(@PathVariable String customerName) {
        try {
            List<PlaceOrder> orders = placeOrderService.getOrdersByCustomerName(customerName);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<List<PlaceOrder>> getOrdersByEmail(@PathVariable String email) {
        try {
            List<PlaceOrder> orders = placeOrderService.getOrdersByEmail(email);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<List<PlaceOrder>> getOrdersByPhoneNumber(@PathVariable String phoneNumber) {
        try {
            List<PlaceOrder> orders = placeOrderService.getOrdersByPhoneNumber(phoneNumber);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{orderId}")
    public ResponseEntity<PlaceOrder> updateOrder(@PathVariable Long orderId, @RequestBody PlaceOrder placeOrder) {
        try {
            PlaceOrder updatedOrder = placeOrderService.updateOrder(orderId, placeOrder);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        try {
            boolean deleted = placeOrderService.deleteOrder(orderId);
            if (deleted) {
                return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
            }
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalOrderCount() {
        try {
            long count = placeOrderService.getTotalOrderCount();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{orderId}/tracking-history")
    public ResponseEntity<List<OrderTrackingDTO>> getTrackingHistory(@PathVariable Long orderId) {
        try {
            List<OrderTrackingDTO> trackingHistory = orderTrackingService.getTrackingHistory(orderId);
            return new ResponseEntity<>(trackingHistory, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
