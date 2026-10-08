package com.eyevision.eyevision.service;

import com.eyevision.eyevision.entity.PlaceOrder;

public interface EmailService {
    
    void sendOrderConfirmation(PlaceOrder order);
    
    void sendOrderStatusUpdate(PlaceOrder order, String previousStatus);
    
    void sendEmail(com.eyevision.eyevision.entity.EmailRequest emailRequest);
}
