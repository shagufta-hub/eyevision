package com.eyevision.eyevision.serviceimplement;

import com.eyevision.eyevision.entity.PlaceOrder;
import com.eyevision.eyevision.entity.EmailRequest;
import com.eyevision.eyevision.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username:noreply@eyevision.com}")
    private String fromEmail;
    
    @Value("${app.name:EyeVision}")
    private String appName;
    
    @Override
    public void sendOrderConfirmation(PlaceOrder order) {
        try {
            String subject = "Order Confirmation - Order ID: " + order.getOrderId();
            String htmlBody = buildOrderConfirmationEmail(order);
            
            EmailRequest emailRequest = new EmailRequest(order.getEmail(), subject, htmlBody, true);
            sendEmail(emailRequest);
            
            log.info("Order confirmation email sent to: {}", order.getEmail());
        } catch (Exception e) {
            log.error("Failed to send order confirmation email to: {}", order.getEmail(), e);
        }
    }
    
    @Override
    public void sendOrderStatusUpdate(PlaceOrder order, String previousStatus) {
        try {
            String subject = "Order Status Updated - Order ID: " + order.getOrderId();
            String htmlBody = buildOrderStatusUpdateEmail(order, previousStatus);
            
            EmailRequest emailRequest = new EmailRequest(order.getEmail(), subject, htmlBody, true);
            sendEmail(emailRequest);
            
            log.info("Order status update email sent to: {} (Status: {})", order.getEmail(), order.getStatus());
        } catch (Exception e) {
            log.error("Failed to send order status update email to: {}", order.getEmail(), e);
        }
    }
    
    @Override
    public void sendEmail(EmailRequest emailRequest) {
        System.out.println("Sending email to obj: " );
                System.out.println("Sending email to: " + emailRequest.getTo());

        try {
            if (emailRequest.isHtml()) {
                sendHtmlEmail(emailRequest);
            } else {
                sendSimpleEmail(emailRequest);
            }
        } catch (Exception e) {
            log.error("Failed to send email to: {}", emailRequest.getTo(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
    
    private void sendSimpleEmail(EmailRequest emailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(emailRequest.getTo());
        message.setSubject(emailRequest.getSubject());
        message.setText(emailRequest.getBody());
        
        mailSender.send(message);
    }
    
    private void sendHtmlEmail(EmailRequest emailRequest) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom(fromEmail);
        helper.setTo(emailRequest.getTo());
        helper.setSubject(emailRequest.getSubject());
        helper.setText(emailRequest.getBody(), true);
        
        mailSender.send(message);
    }
    
    private String buildOrderConfirmationEmail(PlaceOrder order) {
        return String.format("""
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }
                    .content { padding: 20px; }
                    .order-details { background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 15px 0; }
                    .detail-row { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid #eee; }
                    .detail-row:last-child { border-bottom: none; }
                    .label { font-weight: bold; color: #666; }
                    .value { color: #333; }
                    .footer { text-align: center; font-size: 12px; color: #999; padding: 20px; border-top: 1px solid #ddd; }
                    .button { display: inline-block; background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; margin-top: 15px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Order Confirmation</h1>
                        <p>Thank you for your order!</p>
                    </div>
                    <div class="content">
                        <p>Dear <strong>%s</strong>,</p>
                        <p>Your order has been successfully placed. Below are your order details:</p>
                        
                        <div class="order-details">
                            <div class="detail-row">
                                <span class="label">Order ID:</span>
                                <span class="value">#%d</span>
                            </div>
                            <div class="detail-row">
                                <span class="label">Order Date:</span>
                                <span class="value">%s</span>
                            </div>
                            <div class="detail-row">
                                <span class="label">Product:</span>
                                <span class="value">%s</span>
                            </div>
                            <div class="detail-row">
                                <span class="label">Quantity:</span>
                                <span class="value">%d</span>
                            </div>
                            <div class="detail-row">
                                <span class="label">Total Price:</span>
                                <span class="value">₹%.2f</span>
                            </div>
                            <div class="detail-row">
                                <span class="label">Payment Method:</span>
                                <span class="value">%s</span>
                            </div>
                            <div class="detail-row">
                                <span class="label">Status:</span>
                                <span class="value" style="color: #4CAF50; font-weight: bold;">%s</span>
                            </div>
                        </div>
                        
                        <h3>Shipping Details:</h3>
                        <div class="order-details">
                            <div class="detail-row">
                                <span class="label">Address:</span>
                                <span class="value">%s</span>
                            </div>
                            <div class="detail-row">
                                <span class="label">City:</span>
                                <span class="value">%s</span>
                            </div>
                            <div class="detail-row">
                                <span class="label">Zip Code:</span>
                                <span class="value">%s</span>
                            </div>
                        </div>
                        
                        <p>You can track your order using the order ID <strong>#%d</strong> on our website.</p>
                        
                        <a href="http://localhost:4200/track?orderId=%d" class="button">Track Your Order</a>
                        
                        <p>If you have any questions, please feel free to contact us.</p>
                        <p>Thank you for shopping with us!</p>
                    </div>
                    <div class="footer">
                        <p>&copy; %s. All rights reserved.</p>
                        <p>This is an automated email. Please do not reply to this email.</p>
                    </div>
                </div>
            </body>
            </html>
            """,
            order.getCustomerName(),
            order.getOrderId(),
            order.getCreatedAt(),
            order.getProductName(),
            order.getQuantity(),
            order.getTotalPrice(),
            order.getPaymentMethod(),
            order.getStatus(),
            order.getShippingAddress(),
            order.getCity(),
            order.getZipCode(),
            order.getOrderId(),
            order.getOrderId(),
            appName
        );
    }
    
    private String buildOrderStatusUpdateEmail(PlaceOrder order, String previousStatus) {
        String statusEmoji = getStatusEmoji(order.getStatus().toString());
        
        return String.format("""
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }
                    .header { background-color: #2196F3; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }
                    .content { padding: 20px; }
                    .status-box { background-color: #e3f2fd; padding: 15px; border-radius: 5px; margin: 15px 0; border-left: 4px solid #2196F3; }
                    .footer { text-align: center; font-size: 12px; color: #999; padding: 20px; border-top: 1px solid #ddd; }
                    .button { display: inline-block; background-color: #2196F3; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; margin-top: 15px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>%s Order Status Update</h1>
                    </div>
                    <div class="content">
                        <p>Dear <strong>%s</strong>,</p>
                        <p>Great news! Your order status has been updated:</p>
                        
                        <div class="status-box">
                            <h2>Order ID: #%d</h2>
                            <p><strong>Current Status:</strong> %s %s</p>
                            <p><strong>Previous Status:</strong> %s</p>
                            <p><strong>Updated At:</strong> %s</p>
                        </div>
                        
                        <p>Your order is on its way! You can view more details and track your order below:</p>
                        
                        <a href="http://localhost:4200/track?orderId=%d" class="button">View Order Details</a>
                        
                        <p>If you have any questions or concerns, please don't hesitate to contact our support team.</p>
                    </div>
                    <div class="footer">
                        <p>&copy; %s. All rights reserved.</p>
                        <p>This is an automated email. Please do not reply to this email.</p>
                    </div>
                </div>
            </body>
            </html>
            """,
            statusEmoji,
            order.getCustomerName(),
            order.getOrderId(),
            order.getStatus().toString(),
            statusEmoji,
            previousStatus,
            order.getUpdatedAt(),
            order.getOrderId(),
            appName
        );
    }
    
    private String getStatusEmoji(String status) {
        return switch (status) {
            case "CONFIRMED" -> "✅";
            case "PROCESSING" -> "🔄";
            case "SHIPPED" -> "📦";
            case "DELIVERED" -> "✨";
            case "CANCELLED" -> "❌";
            default -> "📧";
        };
    }


}
