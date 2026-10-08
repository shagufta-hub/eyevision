# Quick Start - Backend Setup

## Prerequisites
- Java 17+
- Maven 3.6+
- MySQL Server running
- Email account (Gmail recommended)

## Step 1: Database Setup

```bash
# Run migration in MySQL
mysql -u root -p eyevision < backend/eyevision/src/main/resources/db_migration.sql
```

## Step 2: Email Configuration

Edit `backend/eyevision/src/main/resources/application.properties`

### For Gmail:
```properties
mail.smtp.username=your-email@gmail.com
mail.smtp.password=your-16-char-app-password
```

**Get Gmail App Password:**
1. Go to myaccount.google.com/security
2. Enable 2-Step Verification
3. Generate App Password for Mail

## Step 3: Build

```bash
cd backend/eyevision
mvn clean install
```

## Step 4: Run

```bash
mvn spring-boot:run
```

Server will start at: http://localhost:8080

## Step 5: Test

### Place Order (sends email)
```bash
curl -X POST http://localhost:8080/api/orders/place \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Test User",
    "email": "test@gmail.com",
    "phoneNumber": "9876543210",
    "productName": "Sunglasses",
    "quantity": 1,
    "totalPrice": 999,
    "shippingAddress": "123 Main St",
    "city": "Mumbai",
    "zipCode": "400001",
    "paymentMethod": "Credit Card"
  }'
```

### Get Order with Tracking
```bash
curl http://localhost:8080/api/orders/1/track
```

### Update Status (sends email)
```bash
curl -X PUT http://localhost:8080/api/orders/1 \
  -H "Content-Type: application/json" \
  -d '{"status": "SHIPPED"}'
```

## What's Included

✅ Order confirmation emails  
✅ Status update notifications  
✅ Tracking history in database  
✅ Complete API endpoints  
✅ HTML email templates  

## Troubleshooting

**Emails not sending?**
- Check email credentials
- For Gmail, use 16-char App Password
- Check logs for SMTP errors

**Database errors?**
- Run migration script first
- Check MySQL is running
- Verify credentials in application.properties

## Next Step

Frontend implementation: Create order tracking page in Angular
