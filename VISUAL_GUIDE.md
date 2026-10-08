# Implementation Overview - Visual Guide

## 🏗️ Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                    Angular Frontend (Phase 2)                   │
│                   (Tracking Page, Search Form)                  │
└────────────────────────┬────────────────────────────────────────┘
                         │
                  HTTP/REST API
                         │
┌────────────────────────▼────────────────────────────────────────┐
│              Spring Boot REST API Layer                         │
│                  PlaceOrderController                           │
│  ┌────────────────────────────────────────────────────────┐   │
│  │ POST /orders/place          → Create Order + Send Email   │
│  │ GET /orders/{id}            → Get Order Details          │
│  │ GET /orders/{id}/track ⭐   → Get Order + Full History   │
│  │ GET /orders/{id}/tracking   → Tracking Events Only       │
│  │ PUT /orders/{id}            → Update Status + Send Email   │
│  │ GET /orders/email/{email}   → Search Orders              │
│  └────────────────────────────────────────────────────────┘
└────────────────────────┬────────────────────────────────────────┘
                         │
        ┌────────────────┼────────────────┐
        │                │                │
        ▼                ▼                ▼
   ┌────────┐    ┌──────────────┐    ┌────────────┐
   │ Email  │    │Order Service │    │Tracking    │
   │Service │    │Implementation│    │Service     │
   └────────┘    └──────────────┘    └────────────┘
        │                │                │
        │    ┌──────────┬┴────────┬──────┘
        │    │          │         │
        ▼    ▼          ▼         ▼
   ┌───────────────────────────────────┐
   │    MySQL Database                 │
   │  ┌────────────────────────────┐  │
   │  │ place_orders Table         │  │
   │  │ - orderId, customerName    │  │
   │  │ - email, productName       │  │
   │  │ - status, totalPrice       │  │
   │  │ - createdAt, updatedAt     │  │
   │  └────────────────────────────┘  │
   │  ┌────────────────────────────┐  │
   │  │ order_tracking Table ⭐    │  │
   │  │ - trackingId, orderId      │  │
   │  │ - status, description      │  │
   │  │ - createdAt                │  │
   │  └────────────────────────────┘  │
   └───────────────────────────────────┘
        │
   ┌────▼─────────────────┐
   │   SMTP Server        │
   │  (Gmail, Outlook)    │
   └────────────────────┬─┘
                        │
                   ┌────▼──────────┐
                   │ Customer Email│
                   │ (Inbox) ✉️    │
                   └───────────────┘
```

## 🔄 Order Flow with Email Notifications

```
START: Customer at Checkout Page
       │
       ▼
[Customer Submits Order]
       │
       ▼
API: POST /api/orders/place
       │
       ├─→ Validate Input
       │
       ├─→ Save Order to Database
       │   └─ status = PENDING
       │
       ├─→ Create Initial Tracking Entry
       │   └─ tracking_id = 1, status = PENDING
       │
       ├─→ Send Confirmation Email ✉️
       │   ├─ To: customer@example.com
       │   ├─ Subject: Order Confirmation - Order ID: 1
       │   ├─ Content: Order details + tracking link
       │   └─ Status: PENDING
       │
       ▼
API Response: Order Created
  {
    "orderId": 1,
    "status": "PENDING",
    "createdAt": "2026-05-23T19:35:00"
  }
       │
       ▼
[Customer Receives Email]
  ✉️  Order Confirmation
  📌 Order ID: 1
  📍 Status: PENDING
  🔗 Tracking Link: http://localhost:4200/track?orderId=1
       │
       ▼
[Customer Clicks Tracking Link]
       │
       ▼
API: GET /api/orders/1/track
  Response:
  {
    "orderId": 1,
    "status": "PENDING",
    "trackingHistory": [
      {
        "status": "PENDING",
        "description": "Your order has been placed successfully",
        "createdAt": "2026-05-23T19:35:00"
      }
    ]
  }
       │
       ▼
[Frontend Displays Tracking Page]
  ┌─────────────────────┐
  │ Order #1 Tracking   │
  │ Status: PENDING ⏳  │
  │                     │
  │ Timeline:           │
  │ 📍 PENDING          │
  │    Placed at 19:35  │
  └─────────────────────┘
       │
       ▼
[Admin Confirms Order]
       │
       ▼
API: PUT /api/orders/1
  {
    "status": "CONFIRMED"
  }
       │
       ├─→ Update status in database
       │   └─ status = CONFIRMED
       │
       ├─→ Create Tracking Entry
       │   └─ tracking_id = 2, status = CONFIRMED
       │
       ├─→ Send Status Update Email ✉️
       │   ├─ To: customer@example.com
       │   ├─ Subject: Order Status Updated - Order ID: 1
       │   ├─ Current Status: ✅ CONFIRMED
       │   ├─ Previous Status: PENDING
       │   └─ Tracking Link
       │
       ▼
[Customer Receives Update Email]
  ✉️  Order Status Updated
  ✅ Confirmed at 19:45
       │
       ▼
[Customer Tracks Again]
       │
       ▼
API: GET /api/orders/1/track
  Response:
  {
    "orderId": 1,
    "status": "CONFIRMED",
    "trackingHistory": [
      {
        "status": "CONFIRMED",
        "description": "Order confirmed by admin",
        "createdAt": "2026-05-23T19:45:00"
      },
      {
        "status": "PENDING",
        "description": "Your order has been placed successfully",
        "createdAt": "2026-05-23T19:35:00"
      }
    ]
  }
       │
       ▼
[Frontend Shows Updated Timeline]
  ┌─────────────────────┐
  │ Order #1 Tracking   │
  │ Status: CONFIRMED ✅│
  │                     │
  │ Timeline:           │
  │ ✅ CONFIRMED        │
  │    Confirmed at 19:45
  │ 📍 PENDING          │
  │    Placed at 19:35  │
  └─────────────────────┘

[Process Continues: PROCESSING → SHIPPED → DELIVERED]
[Each step sends email notification]

END: Order Delivered
```

## 📦 Order Status Timeline

```
┌──────────────────────────────────────────────────────────────┐
│                    ORDER LIFECYCLE                          │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  ⏳ PENDING              ✅ CONFIRMED                        │
│  (Initial State)        (Admin Approved)                    │
│       │                      │                              │
│       └──────────────────────┤                              │
│                              │                              │
│                         🔄 PROCESSING                        │
│                         (Preparing)                          │
│                              │                              │
│                         📦 SHIPPED                           │
│                         (In Transit)                         │
│                              │                              │
│                         ✨ DELIVERED                         │
│                         (Completed)                          │
│                                                              │
│                   OPTIONAL: ❌ CANCELLED                     │
│                   (At any point)                             │
│                                                              │
└──────────────────────────────────────────────────────────────┘

Actions at Each Stage:
═════════════════════════════════════════════════════════════

⏳ PENDING
  • Order saved to database
  • Confirmation email sent
  • Awaiting admin approval

✅ CONFIRMED
  • Email notification sent
  • Order confirmed by admin
  • Ready for processing

🔄 PROCESSING
  • Email notification sent
  • Being prepared for shipment
  • Picking & packing items

📦 SHIPPED
  • Email notification sent
  • Left warehouse
  • Tracking number shared
  • In transit to customer

✨ DELIVERED
  • Email notification sent
  • Arrived at destination
  • Order complete
  • Ready for feedback

❌ CANCELLED
  • Email notification sent
  • Refund initiated
  • Can occur at any stage
```

## 📊 Database Relationship

```
place_orders (1) ──────◄── (Many) order_tracking
═══════════════════════════════════════════════════════

place_orders Table:
┌─────────────────────────────────────────┐
│ orderId (PK)              : 1           │
│ customerName              : John Doe    │
│ email                     : john@...    │
│ phoneNumber               : 9876543210  │
│ productName               : Sunglasses  │
│ quantity                  : 1           │
│ totalPrice                : 2999.00     │
│ shippingAddress           : 123 Main St │
│ city                      : Mumbai      │
│ zipCode                   : 400001      │
│ paymentMethod             : Credit Card │
│ status                    : CONFIRMED   │◄──┐
│ createdAt                 : 2026-05-23  │   │
│ updatedAt                 : 2026-05-23  │   │
└─────────────────────────────────────────┘   │
                                              │
order_tracking Table:                         │
┌────────────────────────────────────────┐   │
│ trackingId (PK)      : 1               │   │
│ orderId (FK)         : 1 ─────────────────┤
│ status               : CONFIRMED       │   │
│ description          : Order confirmed │   │
│ createdAt            : 2026-05-23      │   │
└────────────────────────────────────────┘   │
                                              │
┌────────────────────────────────────────┐   │
│ trackingId (PK)      : 2               │   │
│ orderId (FK)         : 1 ─────────────────┤
│ status               : PENDING         │   │
│ description          : Order placed    │   │
│ createdAt            : 2026-05-23      │   │
└────────────────────────────────────────┘◄──┘

Relationship: 1 Order → Multiple Tracking Records
```

## 📧 Email Service Flow

```
API Request
   │
   ├─ POST /orders/place (on order creation)
   │
   └─ PUT /orders/{id} (on status change)
         │
         ▼
    Service Layer
    PlaceOrderService
         │
         ├─→ Save order to database
         │
         ├─→ Create tracking entry
         │
         └─→ Call EmailService
              │
              ▼
         EmailServiceImpl
         │
         ├─→ Get order details
         │
         ├─→ Build HTML email template
         │   ├─ Order confirmation OR
         │   └─ Status update email
         │
         ├─→ Create email request
         │
         └─→ Configure SMTP
              │
              ├─→ Set host (smtp.gmail.com)
              ├─→ Set port (587)
              ├─→ Set credentials
              └─→ Configure TLS
                   │
                   ▼
              SMTP Connection
                   │
                   ├─→ Connect to mail server
                   ├─→ Authenticate
                   ├─→ Send email
                   └─→ Close connection
                        │
                        ▼
                   ✉️ Email Sent Successfully
                        │
                        ▼
                   Customer Inbox
```

## 🎯 File Dependencies

```
Controller Layer
    │
    ├─→ PlaceOrderController
         │
         └─→ (depends on)
              │
              ├─→ PlaceOrderService (Interface)
              │    └─→ PlaceOrderServiceImpl (Implementation)
              │         │
              │         ├─→ PlaceOrderRepository
              │         │
              │         ├─→ EmailService (Interface)
              │         │    └─→ EmailServiceImpl (Implementation)
              │         │
              │         └─→ OrderTrackingService (Interface)
              │              └─→ OrderTrackingServiceImpl (Implementation)
              │                  └─→ OrderTrackingRepository
              │
              └─→ OrderTrackingService (Interface)
                   └─→ OrderTrackingServiceImpl (Implementation)
                        └─→ OrderTrackingRepository

Database
    │
    ├─→ PlaceOrderRepository → place_orders table
    │
    └─→ OrderTrackingRepository → order_tracking table

Configuration
    │
    └─→ EmailConfig (Spring Configuration)
         └─→ JavaMailSender (Spring Bean)
```

## 📱 Request-Response Flow

```
Frontend                Backend                  Database        Email Server
   │                       │                         │                │
   ├─ POST /orders/place ─→│                         │                │
   │ {order data}          │                         │                │
   │                       ├─ Save order ──────────→ │                │
   │                       │ {INSERT}                │                │
   │                       │ ◀─────────────────────  │                │
   │                       │ orderId: 1              │                │
   │                       │                         │                │
   │                       ├─ Add tracking ────────→ │                │
   │                       │ {INSERT}                │                │
   │                       │ ◀─────────────────────  │                │
   │                       │ trackingId: 1           │                │
   │                       │                         │                │
   │                       ├──────────── Send Email ────────────────→ │
   │                       │ {from, to, subject,     │                │
   │                       │  htmlBody}              │                │
   │                       │                         │              ◀─┤─ SMTP Auth
   │                       │                         │              ◀─┤─ Send
   │                       │                         │                │
   │ ◀─ 201 Created ─────  │                         │                │
   │ {orderId: 1,          │                         │                │
   │  status: PENDING}     │                         │                │
   │                       │                         │                │
   │ [Customer opens email]│                         │                │
   │                       │                         │                │
   ├─ GET /orders/1/track ─→│                        │                │
   │                       ├─ Query order ─────────→ │                │
   │                       │ {SELECT * WHERE id=1}   │                │
   │                       │ ◀─────────────────────  │                │
   │                       │ {order data}            │                │
   │                       │                         │                │
   │                       ├─ Query tracking ──────→ │                │
   │                       │ {SELECT * WHERE        │                │
   │                       │  orderId=1}             │                │
   │                       │ ◀─────────────────────  │                │
   │                       │ [{tracking records}]    │                │
   │                       │                         │                │
   │ ◀─ 200 OK ──────────  │                         │                │
   │ {order, history}      │                         │                │
   │                       │                         │                │
   │ [Admin updates status]│                         │                │
   │                       │                         │                │
   ├─ PUT /orders/1 ──────→ │                        │                │
   │ {status: SHIPPED}     │                         │                │
   │                       ├─ Update order ────────→ │                │
   │                       │ {UPDATE status=SHIPPED} │                │
   │                       │ ◀─────────────────────  │                │
   │                       │                         │                │
   │                       ├─ Add tracking ────────→ │                │
   │                       │ {INSERT new tracking}   │                │
   │                       │ ◀─────────────────────  │                │
   │                       │                         │                │
   │                       ├──────────── Send Email ────────────────→ │
   │                       │ {status update}         │                │
   │                       │                         │              ◀─┤─ SMTP
   │                       │                         │                │
   │ ◀─ 200 OK ──────────  │                         │                │
   │ {updated order}       │                         │                │
   │                       │                         │                │
```

## ✅ Testing Scenarios Visual

```
Scenario 1: Happy Path (Successful Order)
═════════════════════════════════════════════════════════════

Customer
  ↓ [Submits checkout form]
  ↓
Backend: Order Created
  ├─ ✓ Saved to database
  ├─ ✓ Status: PENDING
  ├─ ✓ Created tracking entry
  └─ ✓ Email sent
  ↓
Customer Email
  └─ ✓ Confirmation email received
  ↓
Admin Panel
  ├─ ✓ Sees pending order
  ├─ ✓ Updates to CONFIRMED
  └─ ✓ Email sent to customer
  ↓
Customer
  ├─ ✓ Receives update email
  ├─ ✓ Clicks tracking link
  └─ ✓ Sees tracking page with CONFIRMED status
  ↓
Order Fulfillment
  ├─ ✓ Admin marks PROCESSING
  ├─ ✓ Email sent
  ├─ ✓ Admin marks SHIPPED
  ├─ ✓ Email sent
  ├─ ✓ Admin marks DELIVERED
  └─ ✓ Email sent
  ↓
✅ Order Complete


Scenario 2: Email Configuration Error
═════════════════════════════════════════════════════════════

Admin Sets Wrong Password
  ↓
Customer Places Order
  ↓
Backend: Order Created
  ├─ ✓ Saved to database
  ├─ ✓ Status: PENDING
  ├─ ✓ Created tracking entry
  └─ ✗ Email FAILED (log error)
  ↓
Customer
  └─ ✗ No confirmation email
  ↓
Fix: Update application.properties with correct password
  ↓
Order still in database (status PENDING)
  ↓
Resend email manually (future enhancement)
  ↓
✅ Fixed


Scenario 3: Order Search
═════════════════════════════════════════════════════════════

Customer Calls Support
  ↓ "Where is my order?"
  ↓
Support Agent
  ├─ Asks: "What's your email?"
  ├─ Enters: john@example.com
  └─ Calls API: GET /api/orders/email/john@example.com
  ↓
Backend
  ├─ Query database for orders by email
  └─ Returns: [Order #1, Order #5, Order #12]
  ↓
Support Agent
  ├─ Shows customer all orders
  └─ Selects Order #5
  ↓
Agent Calls: GET /api/orders/5/track
  ↓
Backend Returns
  ├─ Order details
  └─ Complete tracking history
  ↓
Support Agent
  ├─ Sees order is SHIPPED
  ├─ Shows customer tracking history
  └─ "Your order left warehouse 2 hours ago"
  ↓
✅ Customer Happy
```

---

This visual guide complements the detailed documentation.
For step-by-step instructions, see: COMPLETE_SETUP_GUIDE.md
For API details, see: API_DOCUMENTATION.md
