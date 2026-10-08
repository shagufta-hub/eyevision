# Backend - Order Acknowledgment & Tracking System

## Overview
This backend implementation provides email confirmation notifications and order tracking capabilities for the EyeVision e-commerce platform.

## Features Implemented

### 1. **Email Service**
- ✅ Order confirmation emails on successful order placement
- ✅ HTML-formatted emails with order details
- ✅ Status update notifications with tracking link
- ✅ Configurable SMTP settings (Gmail, custom SMTP)

### 2. **Order Tracking**
- ✅ Complete order history with status changes
- ✅ Chronological tracking timeline
- ✅ Status enum with display names and descriptions
- ✅ Persistent tracking records in database

### 3. **API Endpoints**

#### Order Management
```
POST   /api/orders/place              - Place new order (sends confirmation email)
GET    /api/orders/{orderId}           - Get order details
GET    /api/orders/{orderId}/track     - Get order with full tracking history
PUT    /api/orders/{orderId}           - Update order (sends status notification if status changed)
DELETE /api/orders/{orderId}           - Delete order
GET    /api/orders/all                 - Get all orders
GET    /api/orders/count               - Get total order count
```

#### Order Tracking
```
GET    /api/orders/{orderId}/tracking-history  - Get complete tracking history
GET    /api/orders/customer/{name}             - Get orders by customer name
GET    /api/orders/email/{email}               - Get orders by email
GET    /api/orders/phone/{phone}               - Get orders by phone number
```

## Setup Instructions

### 1. Database Migration
Run the migration script to set up the `order_tracking` table:

```bash
# Option A: Using MySQL client
mysql -u root -p eyevision < src/main/resources/db_migration.sql

# Option B: Manual execution in MySQL Workbench
# Copy and paste the contents of db_migration.sql and execute
```

### 2. Email Configuration
Configure email settings in `application.properties`:

#### Using Gmail
```properties
mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
mail.smtp.username=your-email@gmail.com
mail.smtp.password=your-app-password  # Use App Password, not regular password
```

**How to get Gmail App Password:**
1. Go to https://myaccount.google.com/security
2. Enable 2-Step Verification
3. Create App Password for "Mail" and "Windows Computer"
4. Use the generated 16-character password

#### Using Other SMTP Services
```properties
# Outlook
mail.smtp.host=smtp-mail.outlook.com
mail.smtp.port=587

# SendGrid
mail.smtp.host=smtp.sendgrid.net
mail.smtp.port=587
mail.smtp.username=apikey
mail.smtp.password=your-sendgrid-api-key
```

### 3. Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Or run the JAR directly
java -jar target/eyevision-0.0.1-SNAPSHOT.jar
```

### 4. Environment Variables (Optional)
Instead of hardcoding credentials, use environment variables:

```bash
# Linux/Mac
export MAIL_USERNAME="your-email@gmail.com"
export MAIL_PASSWORD="your-app-password"
mvn spring-boot:run

# Windows CMD
set MAIL_USERNAME=your-email@gmail.com
set MAIL_PASSWORD=your-app-password
mvn spring-boot:run

# Windows PowerShell
$env:MAIL_USERNAME="your-email@gmail.com"
$env:MAIL_PASSWORD="your-app-password"
mvn spring-boot:run
```

## Database Schema

### place_orders Table
```sql
CREATE TABLE place_orders (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    total_price DOUBLE NOT NULL,
    shipping_address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    payment_method VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_phone (phone_number),
    INDEX idx_status (status)
);
```

### order_tracking Table
```sql
CREATE TABLE order_tracking (
    tracking_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES place_orders(order_id) ON DELETE CASCADE,
    INDEX idx_order_id (order_id),
    INDEX idx_created_at (created_at)
);
```

## Order Status Lifecycle

| Status | Display Name | Description |
|--------|-------------|-------------|
| PENDING | Order Pending | Your order is being processed |
| CONFIRMED | Order Confirmed | Your order has been confirmed |
| PROCESSING | Processing | Your order is being prepared for shipment |
| SHIPPED | Shipped | Your order has been shipped |
| DELIVERED | Delivered | Your order has been delivered |
| CANCELLED | Cancelled | Your order has been cancelled |

## API Usage Examples

### 1. Place an Order
```bash
curl -X POST http://localhost:8080/api/orders/place \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "email": "john@example.com",
    "phoneNumber": "9876543210",
    "productName": "Premium Sunglasses",
    "quantity": 1,
    "totalPrice": 2999.00,
    "shippingAddress": "123 Main St",
    "city": "Mumbai",
    "zipCode": "400001",
    "paymentMethod": "Credit Card"
  }'

# Response:
# {
#   "orderId": 1,
#   "customerName": "John Doe",
#   "status": "PENDING",
#   "createdAt": "2026-05-23T19:35:00",
#   ...
# }
```

### 2. Get Order with Tracking History
```bash
curl -X GET http://localhost:8080/api/orders/1/track

# Response:
# {
#   "orderId": 1,
#   "customerName": "John Doe",
#   "status": "PENDING",
#   "statusDescription": "Your order is being processed",
#   "trackingHistory": [
#     {
#       "trackingId": 1,
#       "status": "PENDING",
#       "description": "Your order has been placed successfully",
#       "createdAt": "2026-05-23T19:35:00"
#     }
#   ],
#   ...
# }
```

### 3. Update Order Status
```bash
curl -X PUT http://localhost:8080/api/orders/1 \
  -H "Content-Type: application/json" \
  -d '{
    "status": "SHIPPED"
  }'

# This will:
# - Update the order status to SHIPPED
# - Create a tracking record
# - Send status update email to customer
```

### 4. Get Tracking History
```bash
curl -X GET http://localhost:8080/api/orders/1/tracking-history

# Response:
# [
#   {
#     "trackingId": 1,
#     "orderId": 1,
#     "status": "SHIPPED",
#     "statusDescription": "Your order has been shipped",
#     "description": "Order status updated to Shipped",
#     "createdAt": "2026-05-23T20:00:00"
#   },
#   {
#     "trackingId": 0,
#     "orderId": 1,
#     "status": "PENDING",
#     "statusDescription": "Your order is being processed",
#     "description": "Your order has been placed successfully",
#     "createdAt": "2026-05-23T19:35:00"
#   }
# ]
```

## New Classes and Files

### Entities
- `OrderStatus.java` - Enum for order status with display info
- `OrderTracking.java` - Tracks order status changes
- `OrderTrackingDTO.java` - Data transfer object for tracking
- `OrderResponseDTO.java` - Complete order response with tracking

### Services
- `EmailService.java` - Interface for email operations
- `EmailServiceImpl.java` - Implementation with HTML templates
- `OrderTrackingService.java` - Interface for tracking operations
- `OrderTrackingServiceImpl.java` - Implementation for tracking

### Repositories
- `OrderTrackingRepository.java` - Database access for tracking

### Controllers
- Updated `PlaceOrderController.java` - Added tracking endpoints

### Configuration
- `EmailConfig.java` - Spring email configuration (when created)

## Email Templates

### Order Confirmation Email
- Order ID and date
- Product details (name, quantity, price)
- Shipping address
- Payment method
- Tracking link
- Professional HTML layout with colors

### Order Status Update Email
- Current status with emoji
- Previous status
- Update timestamp
- Link to order tracking page

## Testing

### Test Order Placement with Email
1. Set up correct email credentials in `application.properties`
2. Place an order via the API
3. Check the customer's email inbox for confirmation
4. Update order status to SHIPPED
5. Verify status update email is received

### Check Database
```sql
-- View all orders
SELECT * FROM place_orders;

-- View tracking history
SELECT * FROM order_tracking ORDER BY order_id, created_at DESC;

-- View specific order tracking
SELECT * FROM order_tracking WHERE order_id = 1 ORDER BY created_at DESC;
```

## Troubleshooting

### Emails Not Sending
- ✓ Check email credentials in `application.properties`
- ✓ For Gmail, use App Password (not regular password)
- ✓ Check logs for SMTP errors
- ✓ Verify internet connection
- ✓ Check firewall/antivirus blocking SMTP port 587

### Database Errors
- ✓ Run the migration script first: `db_migration.sql`
- ✓ Check MySQL is running on `localhost:3306`
- ✓ Verify database name is `eyevision`
- ✓ Check username/password in `application.properties`

### Order Not Creating
- ✓ Check all required fields are provided
- ✓ Verify email format is valid
- ✓ Check database connection

## Next Steps

1. **Frontend Integration** - Create order tracking page in Angular
2. **Admin Dashboard** - Add admin panel for order management
3. **SMS Notifications** - Integrate Twilio for SMS alerts
4. **Email Scheduling** - Add scheduled email reminders
5. **Payment Integration** - Connect payment gateway APIs
