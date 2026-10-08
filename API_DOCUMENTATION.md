# Order Acknowledgment & Tracking API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication
Currently no authentication required. Add JWT/OAuth as needed.

## Content-Type
All requests and responses use `application/json`

---

## Endpoints

### 1. Place Order
**Sends order confirmation email automatically**

```
POST /orders/place
```

**Request Body:**
```json
{
  "customerName": "John Doe",
  "email": "john@example.com",
  "phoneNumber": "9876543210",
  "productName": "Premium Sunglasses",
  "quantity": 1,
  "totalPrice": 2999.00,
  "shippingAddress": "123 Main Street",
  "city": "Mumbai",
  "zipCode": "400001",
  "paymentMethod": "Credit Card"
}
```

**Response (201 Created):**
```json
{
  "orderId": 1,
  "customerName": "John Doe",
  "email": "john@example.com",
  "phoneNumber": "9876543210",
  "productName": "Premium Sunglasses",
  "quantity": 1,
  "totalPrice": 2999.00,
  "shippingAddress": "123 Main Street",
  "city": "Mumbai",
  "zipCode": "400001",
  "paymentMethod": "Credit Card",
  "status": "PENDING",
  "createdAt": "2026-05-23T19:35:00",
  "updatedAt": "2026-05-23T19:35:00"
}
```

**Actions:**
- ✓ Creates order in database
- ✓ Sets status to PENDING
- ✓ Creates initial tracking entry
- ✓ Sends confirmation email to customer

---

### 2. Get Order Details
```
GET /orders/{orderId}
```

**Path Parameters:**
- `orderId` (Long) - Order ID

**Response (200 OK):**
```json
{
  "orderId": 1,
  "customerName": "John Doe",
  "email": "john@example.com",
  "phoneNumber": "9876543210",
  "productName": "Premium Sunglasses",
  "quantity": 1,
  "totalPrice": 2999.00,
  "shippingAddress": "123 Main Street",
  "city": "Mumbai",
  "zipCode": "400001",
  "paymentMethod": "Credit Card",
  "status": "PENDING",
  "createdAt": "2026-05-23T19:35:00",
  "updatedAt": "2026-05-23T19:35:00"
}
```

**Error (404 Not Found):**
```json
null
```

---

### 3. Get Order with Tracking History ⭐
**Most comprehensive endpoint**

```
GET /orders/{orderId}/track
```

**Path Parameters:**
- `orderId` (Long) - Order ID

**Response (200 OK):**
```json
{
  "orderId": 1,
  "customerName": "John Doe",
  "email": "john@example.com",
  "phoneNumber": "9876543210",
  "productName": "Premium Sunglasses",
  "quantity": 1,
  "totalPrice": 2999.00,
  "shippingAddress": "123 Main Street",
  "city": "Mumbai",
  "zipCode": "400001",
  "paymentMethod": "Credit Card",
  "status": "SHIPPED",
  "statusDescription": "Your order has been shipped",
  "createdAt": "2026-05-23T19:35:00",
  "updatedAt": "2026-05-23T20:15:00",
  "trackingHistory": [
    {
      "trackingId": 3,
      "orderId": 1,
      "status": "SHIPPED",
      "statusDescription": "Your order has been shipped",
      "description": "Order status updated to Shipped",
      "createdAt": "2026-05-23T20:15:00"
    },
    {
      "trackingId": 2,
      "orderId": 1,
      "status": "CONFIRMED",
      "statusDescription": "Your order has been confirmed",
      "description": "Order confirmed by admin",
      "createdAt": "2026-05-23T19:45:00"
    },
    {
      "trackingId": 1,
      "orderId": 1,
      "status": "PENDING",
      "statusDescription": "Your order is being processed",
      "description": "Your order has been placed successfully",
      "createdAt": "2026-05-23T19:35:00"
    }
  ]
}
```

---

### 4. Get Tracking History Only
```
GET /orders/{orderId}/tracking-history
```

**Path Parameters:**
- `orderId` (Long) - Order ID

**Response (200 OK):**
```json
[
  {
    "trackingId": 3,
    "orderId": 1,
    "status": "SHIPPED",
    "statusDescription": "Your order has been shipped",
    "description": "Order status updated to Shipped",
    "createdAt": "2026-05-23T20:15:00"
  },
  {
    "trackingId": 2,
    "orderId": 1,
    "status": "CONFIRMED",
    "statusDescription": "Your order has been confirmed",
    "description": "Order confirmed by admin",
    "createdAt": "2026-05-23T19:45:00"
  },
  {
    "trackingId": 1,
    "orderId": 1,
    "status": "PENDING",
    "statusDescription": "Your order is being processed",
    "description": "Your order has been placed successfully",
    "createdAt": "2026-05-23T19:35:00"
  }
]
```

---

### 5. Update Order Status
**Automatically sends status update email**

```
PUT /orders/{orderId}
```

**Path Parameters:**
- `orderId` (Long) - Order ID

**Request Body:**
```json
{
  "status": "SHIPPED"
}
```

**Allowed Status Values:**
- PENDING
- CONFIRMED
- PROCESSING
- SHIPPED
- DELIVERED
- CANCELLED

**Response (200 OK):**
```json
{
  "orderId": 1,
  "customerName": "John Doe",
  "email": "john@example.com",
  "phoneNumber": "9876543210",
  "productName": "Premium Sunglasses",
  "quantity": 1,
  "totalPrice": 2999.00,
  "shippingAddress": "123 Main Street",
  "city": "Mumbai",
  "zipCode": "400001",
  "paymentMethod": "Credit Card",
  "status": "SHIPPED",
  "createdAt": "2026-05-23T19:35:00",
  "updatedAt": "2026-05-23T20:15:00"
}
```

**Actions:**
- ✓ Updates order status
- ✓ Creates tracking entry for the status change
- ✓ Sends status update email if status changed

**Error (400 Bad Request):**
```json
null
```

---

### 6. Get Orders by Customer Name
```
GET /orders/customer/{customerName}
```

**Path Parameters:**
- `customerName` (String) - Customer name (exact match or partial)

**Response (200 OK):**
```json
[
  {
    "orderId": 1,
    "customerName": "John Doe",
    "email": "john@example.com",
    ...
  },
  {
    "orderId": 5,
    "customerName": "John Doe",
    "email": "johndoe@gmail.com",
    ...
  }
]
```

---

### 7. Get Orders by Email
```
GET /orders/email/{email}
```

**Path Parameters:**
- `email` (String) - Customer email address

**Response (200 OK):**
```json
[
  {
    "orderId": 1,
    "customerName": "John Doe",
    "email": "john@example.com",
    ...
  }
]
```

---

### 8. Get Orders by Phone Number
```
GET /orders/phone/{phoneNumber}
```

**Path Parameters:**
- `phoneNumber` (String) - Customer phone number

**Response (200 OK):**
```json
[
  {
    "orderId": 1,
    "customerName": "John Doe",
    "phoneNumber": "9876543210",
    ...
  }
]
```

---

### 9. Get All Orders
```
GET /orders/all
```

**Response (200 OK):**
```json
[
  {
    "orderId": 1,
    "customerName": "John Doe",
    ...
  },
  {
    "orderId": 2,
    "customerName": "Jane Smith",
    ...
  }
]
```

---

### 10. Get Total Order Count
```
GET /orders/count
```

**Response (200 OK):**
```json
5
```

---

### 11. Delete Order
```
DELETE /orders/{orderId}
```

**Path Parameters:**
- `orderId` (Long) - Order ID

**Response (200 OK):**
```json
"Order deleted successfully"
```

**Response (404 Not Found):**
```json
"Order not found"
```

---

## Order Status Reference

| Status | Display | Description |
|--------|---------|-------------|
| PENDING | Order Pending | Your order is being processed |
| CONFIRMED | Order Confirmed | Your order has been confirmed |
| PROCESSING | Processing | Your order is being prepared for shipment |
| SHIPPED | Shipped | Your order has been shipped |
| DELIVERED | Delivered | Your order has been delivered |
| CANCELLED | Cancelled | Your order has been cancelled |

---

## Error Responses

### 400 Bad Request
Invalid input or validation error

### 404 Not Found
Resource not found

### 500 Internal Server Error
Server error - check logs

---

## Example Workflows

### Workflow 1: Complete Order Lifecycle

```bash
# Step 1: Customer places order
curl -X POST http://localhost:8080/api/orders/place \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "email": "john@example.com",
    ...
  }'
# Response: {"orderId": 1, "status": "PENDING", ...}
# Action: Confirmation email sent to john@example.com

# Step 2: Admin confirms order
curl -X PUT http://localhost:8080/api/orders/1 \
  -H "Content-Type: application/json" \
  -d '{"status": "CONFIRMED"}'
# Action: Status update email sent

# Step 3: Prepare for shipping
curl -X PUT http://localhost:8080/api/orders/1 \
  -H "Content-Type: application/json" \
  -d '{"status": "PROCESSING"}'
# Action: Status update email sent

# Step 4: Mark as shipped
curl -X PUT http://localhost:8080/api/orders/1 \
  -H "Content-Type: application/json" \
  -d '{"status": "SHIPPED"}'
# Action: Shipping notification email sent

# Step 5: Mark as delivered
curl -X PUT http://localhost:8080/api/orders/1 \
  -H "Content-Type: application/json" \
  -d '{"status": "DELIVERED"}'
# Action: Delivery confirmation email sent
```

### Workflow 2: Customer Tracks Order

```bash
# Customer wants to track their order
curl -X GET http://localhost:8080/api/orders/1/track

# Response includes:
# - Current order details
# - Complete tracking history
# - Current status with description
# - Timeline of all status changes
```

### Workflow 3: Search Orders by Email

```bash
# Admin searches for customer orders
curl -X GET http://localhost:8080/api/orders/email/john@example.com

# Returns all orders placed by this customer
```

---

## Integration with Frontend

### For Order Confirmation Page (After Checkout)
```
GET /api/orders/{orderId}/track
```
Display:
- Order ID for reference
- Current status
- Tracking link
- Estimated delivery

### For Order Tracking Page
```
GET /api/orders/{orderId}/track
```
Display:
- Complete order details
- Timeline of status updates
- Current location/status

### For Customer Dashboard
```
GET /api/orders/email/{email}
```
Display:
- List of all customer orders
- Quick status overview
- Links to tracking

---

## Email Notifications Sent By API

### On Order Placement
- **Trigger:** POST /orders/place
- **Recipient:** Customer email
- **Subject:** Order Confirmation - Order ID: {id}
- **Content:** Order details, tracking link

### On Status Update
- **Trigger:** PUT /orders/{id} with status change
- **Recipient:** Customer email
- **Subject:** Order Status Updated - Order ID: {id}
- **Content:** New status, timestamp, tracking link
- **Note:** Only sent if status actually changed

---

## Rate Limiting
Currently not implemented. Add as needed for production.

---

## Pagination
Currently not implemented for list endpoints. Add as needed.

---

## Filtering
Supported filters:
- By customer name
- By email
- By phone
- By order ID
- By status (update the endpoints to add this)

---

## Performance Notes

- Tracking history is ordered by created_at DESC (newest first)
- Indexes on order_id, email, phone for fast queries
- Consider pagination for large datasets
- Email sending is synchronous - consider async for high volume

---

## Security Recommendations

1. Add authentication/authorization
2. Validate email format before sending
3. Rate limit API endpoints
4. Add CORS configuration
5. Use HTTPS in production
6. Don't expose sensitive data in responses
7. Add input validation on all endpoints
8. Log all operations for audit trail

---

## Monitoring & Debugging

### Check Email Logs
```bash
tail -f logs/application.log | grep -i email
```

### Check Database
```sql
SELECT * FROM place_orders ORDER BY created_at DESC LIMIT 1;
SELECT * FROM order_tracking WHERE order_id = 1 ORDER BY created_at DESC;
```

### Test Email Configuration
- Send test email from EmailService
- Check SMTP logs
- Verify credentials in application.properties
