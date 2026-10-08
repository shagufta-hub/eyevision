# Complete Setup Guide - Order Acknowledgment & Tracking

## Overview
This guide provides complete step-by-step instructions to set up and test the backend order acknowledgment and tracking system.

## Phase 1: Prerequisites Setup

### 1.1 Check Java Installation
```bash
java -version
# Should show Java 17 or higher
```

If not installed:
- Download from: https://www.oracle.com/java/technologies/downloads/
- Install Java 17 or higher

### 1.2 Check Maven Installation
```bash
mvn -version
# Should show Maven 3.6+
```

If not installed:
- Download from: https://maven.apache.org/download.cgi
- Follow installation instructions

### 1.3 Verify MySQL is Running
```bash
# On Windows
mysql -u root -p -e "SELECT 1;"

# On macOS/Linux
mysql -u root -p -e "SELECT 1;"
```

### 1.4 Create/Verify Database
```bash
mysql -u root -p
# Then in MySQL:
CREATE DATABASE IF NOT EXISTS eyevision;
USE eyevision;
SHOW TABLES;
```

---

## Phase 2: Email Setup

### 2.1 Choose Email Provider

#### Option A: Gmail (Recommended)

**Get App Password:**
1. Go to: https://myaccount.google.com/security
2. Click "2-Step Verification" and enable it
3. Create "App Password" for Mail & Windows Computer
4. Copy the 16-character password

**Note:** Don't use your regular Gmail password

#### Option B: Outlook/Office 365
```
Host: smtp-mail.outlook.com
Port: 587
Username: your-email@outlook.com
Password: your-password
```

#### Option C: SendGrid
```
Host: smtp.sendgrid.net
Port: 587
Username: apikey
Password: SG.your-api-key
```

Get API key from: https://sendgrid.com/

#### Option D: Custom SMTP Server
Use your organization's SMTP settings

---

## Phase 3: Database Migration

### 3.1 Run Migration Script

Navigate to the backend directory:
```bash
cd backend/eyevision
```

Run the migration:
```bash
# On Windows (using mysql client)
mysql -u root -p eyevision < src/main/resources/db_migration.sql

# Or using MySQL Workbench
# 1. Open MySQL Workbench
# 2. Connect to localhost
# 3. Open File → Open SQL Script
# 4. Select: src/main/resources/db_migration.sql
# 5. Execute
```

Verify tables were created:
```bash
mysql -u root -p eyevision -e "SHOW TABLES;"
# Should see: place_orders, order_tracking
```

---

## Phase 4: Configuration

### 4.1 Edit Application Properties

Open file: `backend/eyevision/src/main/resources/application.properties`

Update email settings:

**For Gmail:**
```properties
mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
mail.smtp.username=your-email@gmail.com
mail.smtp.password=your-app-password-16-chars
```

**For Outlook:**
```properties
mail.smtp.host=smtp-mail.outlook.com
mail.smtp.port=587
mail.smtp.username=your-email@outlook.com
mail.smtp.password=your-password
```

### 4.2 Verify Configuration
```properties
# These should be already set:
spring.datasource.url=jdbc:mysql://localhost:3306/eyevision?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=root

# Check these are uncommented:
mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
app.name=EyeVision
```

---

## Phase 5: Build Project

### 5.1 Build with Maven

```bash
cd backend/eyevision

# Clean and build
mvn clean install

# This will:
# ✓ Download dependencies
# ✓ Compile code
# ✓ Run tests
# ✓ Create JAR file
```

**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time:  XX.XXX s
```

**If build fails:**
- Check Java version: `java -version`
- Check Maven version: `mvn -version`
- Clear cache: `mvn clean`
- Check internet connection for dependencies

### 5.2 Alternative: Skip Tests During Build

If tests are slow:
```bash
mvn clean install -DskipTests
```

---

## Phase 6: Run Application

### 6.1 Start Spring Boot Application

```bash
# Option 1: Using Maven
mvn spring-boot:run

# Option 2: Using JAR directly
java -jar target/eyevision-0.0.1-SNAPSHOT.jar

# Option 3: With environment variables
export MAIL_USERNAME="your-email@gmail.com"
export MAIL_PASSWORD="your-app-password"
mvn spring-boot:run
```

**Expected Output:**
```
 . __  _  _ ____  __  __  _ ____
 / |  /  / /    / /  / / / /  __/
/ / / / / __  / /  / /_/ /  /   
/ /  / / / / / / / / _  / / /   
/.  / /  / / / /__ / / / / /    
.    / /    /  ____/ / /

2026-05-23 19:35:10.000  INFO ... Tomcat initialized with port(s): 8080 (http)
2026-05-23 19:35:10.500  INFO ... Tomcat started on port(s): 8080 (http) with context path ''
2026-05-23 19:35:10.600  INFO ... Started EyevisionApplication in 5.234 seconds
```

### 6.2 Verify Server is Running

```bash
# In another terminal
curl http://localhost:8080/api/orders/all

# Should return an empty array: []
```

---

## Phase 7: Test Email Configuration

### 7.1 Place a Test Order

```bash
curl -X POST http://localhost:8080/api/orders/place \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Test User",
    "email": "your-test-email@gmail.com",
    "phoneNumber": "9876543210",
    "productName": "Test Sunglasses",
    "quantity": 1,
    "totalPrice": 999.00,
    "shippingAddress": "123 Test Street",
    "city": "Test City",
    "zipCode": "400001",
    "paymentMethod": "Credit Card"
  }'
```

**Expected Response:**
```json
{
  "orderId": 1,
  "customerName": "Test User",
  "email": "your-test-email@gmail.com",
  "status": "PENDING",
  "createdAt": "2026-05-23T19:35:00",
  ...
}
```

**Check your email:**
- ✓ Go to your inbox
- ✓ Look for email from: `your-email@gmail.com`
- ✓ Subject: "Order Confirmation - Order ID: 1"
- ✓ Click the tracking link

### 7.2 Test Tracking Endpoint

```bash
curl http://localhost:8080/api/orders/1/track
```

**Expected Response:**
```json
{
  "orderId": 1,
  "customerName": "Test User",
  "status": "PENDING",
  "statusDescription": "Your order is being processed",
  "trackingHistory": [
    {
      "trackingId": 1,
      "status": "PENDING",
      "description": "Your order has been placed successfully",
      "createdAt": "2026-05-23T19:35:00"
    }
  ],
  ...
}
```

### 7.3 Test Status Update (Sends Email)

```bash
curl -X PUT http://localhost:8080/api/orders/1 \
  -H "Content-Type: application/json" \
  -d '{"status": "CONFIRMED"}'
```

**Check your email again:**
- ✓ New email should arrive (status update)
- ✓ Subject: "Order Status Updated - Order ID: 1"

---

## Phase 8: Verify Database

### 8.1 Check Orders Table

```bash
mysql -u root -p eyevision

# In MySQL:
SELECT * FROM place_orders;

# Should see your test order
```

### 8.2 Check Tracking History

```bash
# In MySQL:
SELECT * FROM order_tracking WHERE order_id = 1 ORDER BY created_at DESC;

# Should show:
# - First entry: CONFIRMED (from the update)
# - Second entry: PENDING (from the initial order)
```

---

## Phase 9: Test Multiple Scenarios

### Scenario 1: Test with Different Email Providers

```bash
# Test with different email
curl -X POST http://localhost:8080/api/orders/place \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Another User",
    "email": "another-email@outlook.com",
    "phoneNumber": "9988776655",
    "productName": "Premium Glasses",
    "quantity": 2,
    "totalPrice": 1999.00,
    "shippingAddress": "456 Another St",
    "city": "Another City",
    "zipCode": "400002",
    "paymentMethod": "UPI"
  }'
```

### Scenario 2: Test Complete Order Lifecycle

```bash
# 1. Place order
curl -X POST http://localhost:8080/api/orders/place ... # Order 2

# 2. Confirm order
curl -X PUT http://localhost:8080/api/orders/2 \
  -H "Content-Type: application/json" \
  -d '{"status": "CONFIRMED"}'

# 3. Process order
curl -X PUT http://localhost:8080/api/orders/2 \
  -H "Content-Type: application/json" \
  -d '{"status": "PROCESSING"}'

# 4. Ship order
curl -X PUT http://localhost:8080/api/orders/2 \
  -H "Content-Type: application/json" \
  -d '{"status": "SHIPPED"}'

# 5. Deliver order
curl -X PUT http://localhost:8080/api/orders/2 \
  -H "Content-Type: application/json" \
  -d '{"status": "DELIVERED"}'

# 6. Check complete tracking
curl http://localhost:8080/api/orders/2/track
```

### Scenario 3: Search Orders

```bash
# By email
curl http://localhost:8080/api/orders/email/test@gmail.com

# By customer name
curl http://localhost:8080/api/orders/customer/Test\ User

# By phone
curl http://localhost:8080/api/orders/phone/9876543210

# Get total count
curl http://localhost:8080/api/orders/count
```

---

## Phase 10: Troubleshooting

### Problem: Emails Not Sending

**Symptoms:** Order created but no email received

**Solutions:**
1. Check credentials in `application.properties`
2. For Gmail, verify you're using 16-char App Password (not regular password)
3. Check email spam folder
4. Check application logs for SMTP errors:
   ```bash
   # Look for lines like:
   # "SMTP connect failed"
   # "Invalid credentials"
   # "554 Message rejected"
   ```
5. Test email credentials separately:
   ```bash
   # Use a mail testing tool
   # Or check SMTP settings in email client
   ```

### Problem: Database Connection Error

**Symptoms:** "Connection refused" or "Unknown database"

**Solutions:**
1. Verify MySQL is running
2. Check database exists: `mysql -u root -p -e "SHOW DATABASES;"`
3. Check username/password in `application.properties`
4. Run migration script again
5. Check MySQL port (should be 3306)

### Problem: Build Fails

**Symptoms:** "BUILD FAILURE"

**Solutions:**
1. Check Java version: `java -version` (need 17+)
2. Check Maven version: `mvn -version`
3. Clear Maven cache: `mvn clean`
4. Check internet connection (downloading dependencies)
5. Check for syntax errors in code

### Problem: Port 8080 Already in Use

**Symptoms:** "Address already in use"

**Solutions:**
1. Find what's using port 8080:
   ```bash
   # On Windows
   netstat -ano | findstr :8080
   
   # On Mac/Linux
   lsof -i :8080
   ```
2. Kill the process or use different port:
   ```bash
   # In application.properties
   server.port=8081
   ```

### Problem: 404 Errors on Endpoints

**Symptoms:** "404 Not Found" when calling API

**Solutions:**
1. Verify server is running: `curl http://localhost:8080`
2. Check endpoint URL spelling
3. Use correct HTTP method (GET, POST, PUT, DELETE)
4. Check request/response format is JSON

---

## Phase 11: Production Deployment

When ready for production:

1. **Security:**
   - Add authentication (JWT/OAuth)
   - Use HTTPS instead of HTTP
   - Store credentials in environment variables
   - Add CORS configuration

2. **Email:**
   - Use SendGrid or AWS SES for reliability
   - Add email templates versioning
   - Monitor email delivery rates

3. **Database:**
   - Use managed database service
   - Set up automated backups
   - Add proper indexes

4. **Monitoring:**
   - Add logging framework (SLF4J + Logback)
   - Monitor email delivery
   - Track API response times

5. **Scaling:**
   - Make email sending asynchronous
   - Add message queue (RabbitMQ/Kafka)
   - Implement caching

---

## Next Steps

Once backend is verified:

1. **Start Frontend Development:**
   - Create Order Tracking page
   - Add search functionality
   - Display tracking timeline

2. **Add More Features:**
   - SMS notifications
   - Admin dashboard
   - Advanced reporting

3. **Performance Optimization:**
   - Add pagination
   - Implement caching
   - Async email sending

---

## Support & Documentation

- **API Details:** See `API_DOCUMENTATION.md`
- **Setup Guide:** See `BACKEND_SETUP.md`
- **Quick Start:** See `QUICK_START.md`
- **Summary:** See `BACKEND_IMPLEMENTATION_SUMMARY.md`

---

## Checklist

Use this checklist to verify everything is working:

- [ ] Java 17+ installed
- [ ] Maven installed
- [ ] MySQL running
- [ ] Database created
- [ ] Migration script executed
- [ ] Email credentials configured
- [ ] Project builds successfully
- [ ] Server starts on port 8080
- [ ] Test order places successfully
- [ ] Confirmation email received
- [ ] Tracking endpoint works
- [ ] Status update email sent
- [ ] Database shows order and tracking
- [ ] All scenarios tested
- [ ] Production deployment planned

---

**Congratulations!** Your backend is now ready for frontend integration and testing.
