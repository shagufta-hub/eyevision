# PlaceOrder API - Implementation Guide

## Overview
Complete PlaceOrder functionality has been implemented with the following components:

## Project Structure

```
backend/eyevision/src/main/java/com/eyevision/eyevision/
├── entity/
│   └── PlaceOrder.java          # JPA Entity for orders
├── repository/
│   └── PlaceOrderRepository.java # Data access layer
├── service/
│   └── PlaceOrderService.java    # Service interface
├── serviceimplement/
│   └── PlaceOrderServiceImpl.java # Service implementation
└── controller/
    └── PlaceOrderController.java # REST API endpoints
```

## Database Schema

The `place_orders` table includes:
- `order_id`: Primary key (auto-generated)
- `customer_name`: Customer name
- `email`: Customer email
- `phone_number`: Contact number
- `product_name`: Product ordered
- `quantity`: Number of items
- `total_price`: Total order amount
- `shipping_address`: Delivery address
- `city`: City for delivery
- `zip_code`: Postal code
- `payment_method`: Payment type (Card, Cash, etc.)
- `status`: Order status (PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
- `created_at`: Order creation timestamp
- `updated_at`: Last modification timestamp

## API Endpoints

### 1. Place an Order
```
POST /api/orders/place
Content-Type: application/json

{
  "customerName": "John Doe",
  "email": "john@example.com",
  "phoneNumber": "1234567890",
  "productName": "Product A",
  "quantity": 2,
  "totalPrice": 5000.0,
  "shippingAddress": "123 Main St",
  "city": "New York",
  "zipCode": "10001",
  "paymentMethod": "Credit Card",
  "status": "PENDING"
}
```

### 2. Get Order by ID
```
GET /api/orders/{orderId}
```
Response: Returns the order details

### 3. Get All Orders
```
GET /api/orders/all
```
Response: Returns list of all orders

### 4. Get Orders by Customer Name
```
GET /api/orders/customer/{customerName}
```

### 5. Get Orders by Email
```
GET /api/orders/email/{email}
```

### 6. Get Orders by Phone Number
```
GET /api/orders/phone/{phoneNumber}
```

### 7. Update Order
```
PUT /api/orders/{orderId}
Content-Type: application/json

{
  "status": "CONFIRMED",
  "paymentMethod": "Updated Payment Method"
}
```

### 8. Delete Order
```
DELETE /api/orders/{orderId}
```

### 9. Get Total Order Count
```
GET /api/orders/count
```

## Usage with Angular Frontend

Connect your Angular frontend to these endpoints:

```typescript
// Example service call
placeOrder(order: any) {
  return this.http.post('http://localhost:8080/api/orders/place', order);
}

getOrder(orderId: number) {
  return this.http.get(`http://localhost:8080/api/orders/${orderId}`);
}

updateOrder(orderId: number, order: any) {
  return this.http.put(`http://localhost:8080/api/orders/${orderId}`, order);
}
```

## Database Configuration

Update `application.properties` with your database details:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/eyevision
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Build and Run

```bash
# Navigate to backend directory
cd backend/eyevision

# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run
```

## Technologies Used
- **Spring Boot 3.3.5**: Web framework
- **Spring Data JPA**: Database access
- **MySQL**: Database
- **Lombok**: Reduce boilerplate code
- **Java 17**: Programming language

## Features
✅ Create orders  
✅ Retrieve orders (by ID, customer name, email, phone)  
✅ Update orders  
✅ Delete orders  
✅ Get order count  
✅ Automatic timestamp management  
✅ Input validation  
✅ CORS enabled for frontend integration  
✅ RESTful API design  
