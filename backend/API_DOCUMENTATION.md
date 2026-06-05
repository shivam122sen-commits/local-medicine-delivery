# MEDDY Backend API Documentation

## Complete API Redesign to Support Frontend

This backend has been completely redesigned to support all frontend workflows, dashboards, and features without requiring any frontend changes.

---

## Authentication APIs

### POST /auth/register
Register a new user (Patient, Chemist, or Delivery Partner)

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "mobile": "+91 9876543210",
  "address": "123 Street, City",
  "role": "PATIENT",
  "pharmacyName": "City Pharmacy",
  "licenseNumber": "LIC12345",
  "vehicleType": "Honda Activa",
  "vehicleNumber": "MH-04-AB-1234"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": { /* User object */ },
  "message": "Registration successful!"
}
```

### POST /auth/login
Login with email and password

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "password123",
  "rememberMe": true
}
```

---

## User Management APIs

### GET /api/users/{userId}
Get user profile by ID

### PUT /api/users/{userId}
Update user profile

### PUT /api/users/{userId}/password
Change user password

### PUT /api/users/{userId}/two-factor
Enable/disable two-factor authentication

### PUT /api/users/{userId}/online-status
Update online status (for delivery partners)

### GET /api/users/delivery/online
Get all online delivery partners

### GET /api/users/{userId}/addresses
Get all saved addresses for a user

### POST /api/users/{userId}/addresses
Add a new address

### DELETE /api/users/addresses/{addressId}
Delete an address

---

## Dashboard APIs

### GET /api/dashboard/patient/{patientId}
Get patient dashboard statistics
- Total orders
- Active orders
- Pending orders
- Delivered orders

### GET /api/dashboard/chemist/{chemistId}
Get chemist dashboard statistics
- Total orders
- Today's revenue
- Low stock items
- Pending prescriptions

### GET /api/dashboard/delivery/{deliveryPartnerId}
Get delivery partner dashboard statistics
- Total deliveries
- Today's deliveries
- Today's earnings
- Rating

---

## Order Management APIs

### POST /api/orders/create
Create a new order

**Request Body:**
```json
{
  "patientId": 1,
  "chemistId": 2,
  "deliveryAddress": "123 MG Road, Mumbai",
  "prescriptionRequired": true,
  "prescriptionUrl": "/uploads/prescription.jpg",
  "isEmergency": false,
  "items": [
    {
      "inventoryId": 10,
      "medicineName": "Paracetamol 500mg",
      "quantity": 2,
      "unitPrice": 38.0,
      "imageUrl": "/images/paracetamol.jpg"
    }
  ]
}
```

### GET /api/orders/patient/{patientId}
Get all orders for a patient

### GET /api/orders/chemist/{chemistId}
Get all orders for a chemist

### GET /api/orders/delivery/{deliveryPartnerId}
Get all orders for a delivery partner

### GET /api/orders/{orderId}
Get order details by ID

### GET /api/orders/number/{orderNumber}
Get order details by order number (e.g., MED-2047)

### PUT /api/orders/{orderId}/status
Update order status

**Query Parameters:**
- status: PENDING | PRESCRIPTION_VERIFIED | APPROVED | PACKED | DELIVERY_PARTNER_ASSIGNED | OUT_FOR_DELIVERY | DELIVERED | CANCELLED
- deliveryPartnerId: (optional) ID of delivery partner

### GET /api/orders/status/{status}
Get all orders with a specific status

### GET /api/orders/available-for-pickup
Get orders ready for delivery pickup

---

## Inventory Management APIs

### POST /api/inventory/add
Add a new medicine to inventory

**Request Body:**
```json
{
  "chemistId": 2,
  "medicineName": "Paracetamol 500mg",
  "category": "Tablets",
  "manufacturer": "Sun Pharma",
  "batchNumber": "BATCH123",
  "mrp": 50.0,
  "sellingPrice": 38.0,
  "stock": 100,
  "expiryDate": "2025-12-31T00:00:00",
  "prescriptionRequired": false,
  "description": "Pain reliever and fever reducer",
  "imageUrl": "/images/paracetamol.jpg",
  "lowStockThreshold": 10
}
```

### PUT /api/inventory/{id}
Update medicine details

### DELETE /api/inventory/{id}
Delete a medicine from inventory

### GET /api/inventory/chemist/{chemistId}
Get all inventory for a chemist

### GET /api/inventory/chemist/{chemistId}/low-stock
Get low stock items for a chemist

### GET /api/inventory/chemist/{chemistId}/out-of-stock
Get out of stock items for a chemist

### GET /api/inventory/all
Get all medicines (for patients to browse)

### GET /api/inventory/search?query={medicineName}
Search medicines by name

### GET /api/inventory/category/{category}
Get medicines by category (Tablets, Capsules, Syrups, etc.)

### GET /api/inventory/{id}
Get medicine details by ID

---

## Prescription Management APIs

### POST /api/prescriptions/upload
Upload a prescription (multipart/form-data)

**Form Data:**
- file: prescription image/PDF (max 10MB)
- patientId: Patient ID

### GET /api/prescriptions/patient/{patientId}
Get all prescriptions for a patient

### GET /api/prescriptions/chemist/{chemistId}
Get all prescriptions reviewed by a chemist

### GET /api/prescriptions/pending
Get all pending prescriptions for review

### PUT /api/prescriptions/{prescriptionId}/verify
Verify/approve a prescription

**Query Parameters:**
- chemistId: ID of chemist verifying
- verifiedBy: Name of pharmacist

### PUT /api/prescriptions/{prescriptionId}/reject
Reject a prescription

**Query Parameters:**
- chemistId: ID of chemist
- reason: Rejection reason

---

## Notification APIs

### GET /api/notifications/user/{userId}
Get all notifications for a user

### GET /api/notifications/user/{userId}/unread
Get unread notifications

### GET /api/notifications/user/{userId}/unread-count
Get count of unread notifications

### PUT /api/notifications/{notificationId}/read
Mark a notification as read

### PUT /api/notifications/user/{userId}/mark-all-read
Mark all notifications as read

---

## Wishlist APIs

### GET /api/wishlist/user/{userId}
Get user's wishlist

### GET /api/wishlist/user/{userId}/count
Get wishlist item count

### POST /api/wishlist/add
Add medicine to wishlist

**Request Body:**
```json
{
  "userId": 1,
  "inventoryId": 10
}
```

### DELETE /api/wishlist/user/{userId}/item/{inventoryId}
Remove medicine from wishlist

---

## Order Status Flow

1. **PENDING** - Order placed by patient
2. **PRESCRIPTION_VERIFIED** - Prescription approved by pharmacist
3. **APPROVED** - Order confirmed by chemist
4. **PACKED** - Medicines packed and ready
5. **DELIVERY_PARTNER_ASSIGNED** - Delivery partner assigned
6. **OUT_FOR_DELIVERY** - Delivery in progress
7. **DELIVERED** - Order delivered to patient
8. **CANCELLED** - Order cancelled

---

## Key Features Implemented

### Authentication & Security
- JWT token-based authentication
- BCrypt password encryption
- Role-based access (PATIENT, CHEMIST, DELIVERY)
- Remember me functionality
- Two-factor authentication support

### Patient Features
- Browse medicines by category
- Search medicines
- Upload prescriptions
- Create orders with multiple items
- Track order status in real-time
- View order history
- Wishlist management
- Save multiple addresses
- Receive notifications
- Dashboard with order statistics

### Chemist Features
- Manage inventory (add, update, delete medicines)
- View low stock alerts
- Review and verify prescriptions
- Manage incoming orders
- Update order status
- View revenue statistics
- Dashboard with business metrics

### Delivery Partner Features
- View assigned deliveries
- Update delivery status
- Track earnings
- Online/offline status
- GPS location tracking support
- Delivery history
- Performance ratings

### System Features
- Real-time notifications
- Order tracking with timeline
- Automatic order number generation
- Low stock alerts
- Emergency delivery flagging
- Free delivery fee calculation
- Estimated delivery time calculation

---

## Database Models

### User
- Basic info (name, email, password, mobile, address)
- Role (PATIENT, CHEMIST, DELIVERY)
- Rating (for chemists and delivery partners)
- Vehicle info (for delivery partners)
- Pharmacy info (for chemists)
- Online status, two-factor auth

### Order
- Order number (MED-XXXX)
- Patient, chemist, delivery partner IDs
- Status tracking with timestamps
- Prescription info
- Delivery address
- GPS coordinates
- Pricing (subtotal, delivery fee, total)
- Rating and review

### OrderItem
- Medicine details
- Quantity and pricing
- Linked to order

### Inventory
- Medicine details
- Category, manufacturer, batch number
- Pricing (MRP, selling price)
- Stock management
- Expiry date
- Prescription requirement
- Low stock threshold

### Prescription
- File upload URL
- Patient and chemist IDs
- Status (PENDING, VERIFIED, REJECTED)
- Verification timestamps
- Rejection reason

### Notification
- User ID
- Title and message
- Type (order, prescription, stock, etc.)
- Read status
- Timestamps

### Wishlist
- User and inventory mapping
- Save for later functionality

### Address
- User addresses
- Default address support
- GPS coordinates

---

## Tech Stack

- **Framework:** Spring Boot 3.2.5
- **Language:** Java 17
- **Database:** H2 (in-memory) / MySQL
- **Security:** Spring Security + JWT
- **Password:** BCrypt encryption
- **ORM:** Spring Data JPA
- **Build:** Maven

---

## Configuration

All configurations are in `application.properties`:
- JWT secret and expiration
- File upload settings (max 10MB)
- Database configuration
- CORS enabled for all origins

---

## Notes

1. All API endpoints return proper HTTP status codes
2. Error handling with meaningful messages
3. Transaction management for data consistency
4. Automatic notification generation for key events
5. Support for file uploads (prescriptions)
6. Pagination ready (can be added to list endpoints)
7. Search and filtering capabilities
8. Real-time data updates support

This backend is production-ready and fully aligned with the frontend requirements!
