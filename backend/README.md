# MEDDY Backend - Medicine Delivery Platform

## Architecture Overview

This backend has been completely redesigned and refactored to perfectly support all frontend requirements without requiring any frontend modifications. The architecture follows Spring Boot best practices with a clean separation of concerns.

### Architecture Layers

```
┌─────────────────────────────────────────┐
│         Controllers (REST APIs)         │
│  - Authentication & Authorization       │
│  - Request/Response Handling            │
│  - Input Validation                     │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Service Layer (Business)        │
│  - Business Logic                       │
│  - Transaction Management               │
│  - Orchestration                        │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Repository Layer (Data Access)     │
│  - JPA Repositories                     │
│  - Custom Queries                       │
│  - Database Operations                  │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Database (H2/MySQL)             │
│  - Users, Orders, Inventory             │
│  - Prescriptions, Notifications         │
└─────────────────────────────────────────┘
```

---

## Project Structure

```
backend/
├── src/main/java/com/med/delivery/
│   ├── MedDeliveryApplication.java       # Main application entry point
│   │
│   ├── controller/                       # REST API Controllers
│   │   ├── AuthController.java           # Login/Register
│   │   ├── UserController.java           # User management & addresses
│   │   ├── OrderController.java          # Order CRUD & status updates
│   │   ├── InventoryController.java      # Medicine inventory
│   │   ├── PrescriptionController.java   # Prescription upload & review
│   │   ├── NotificationController.java   # Notifications
│   │   ├── WishlistController.java       # Wishlist management
│   │   ├── DashboardController.java      # Dashboard statistics
│   │   └── HomeController.java           # Health check
│   │
│   ├── service/                          # Business Logic Layer
│   │   ├── AuthService.java              # Authentication logic
│   │   ├── OrderService.java             # Order processing
│   │   ├── NotificationService.java      # Notification management
│   │   └── DashboardService.java         # Dashboard calculations
│   │
│   ├── repository/                       # Data Access Layer
│   │   ├── UserRepository.java
│   │   ├── OrderRepository.java
│   │   ├── OrderItemRepository.java
│   │   ├── InventoryRepository.java
│   │   ├── PrescriptionRepository.java
│   │   ├── NotificationRepository.java
│   │   ├── WishlistRepository.java
│   │   └── AddressRepository.java
│   │
│   ├── model/                            # Entity Models
│   │   ├── User.java                     # User (Patient/Chemist/Delivery)
│   │   ├── Order.java                    # Order with status tracking
│   │   ├── OrderItem.java                # Individual order items
│   │   ├── Inventory.java                # Medicine inventory
│   │   ├── Prescription.java             # Uploaded prescriptions
│   │   ├── Notification.java             # System notifications
│   │   ├── Wishlist.java                 # User wishlist
│   │   └── Address.java                  # User addresses
│   │
│   ├── dto/                              # Data Transfer Objects
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── AuthResponse.java
│   │   ├── CreateOrderRequest.java
│   │   └── DashboardStats.java
│   │
│   ├── security/                         # Security Configuration
│   │   ├── JwtUtil.java                  # JWT token generation/validation
│   │   └── SecurityConfig.java           # Spring Security config
│   │
│   └── config/
│       └── CorsConfig.java               # CORS configuration
│
├── src/main/resources/
│   └── application.properties            # Configuration file
│
├── pom.xml                               # Maven dependencies
├── API_DOCUMENTATION.md                  # Complete API reference
└── README.md                             # This file
```

---

## Features Implemented

### ✅ Core Features
- **Three User Roles**: Patient, Chemist (Pharmacist), Delivery Partner
- **JWT Authentication**: Secure token-based authentication
- **Password Encryption**: BCrypt for secure password storage
- **Role-Based Access**: Different capabilities per role
- **File Upload**: Prescription image/PDF upload support

### ✅ Patient Features
- Register and login
- Browse medicines by category
- Search medicines
- View medicine details
- Add medicines to wishlist
- Upload prescriptions
- Create orders with multiple items
- Track order status with timeline
- View order history
- Save multiple delivery addresses
- Receive real-time notifications
- Dashboard with order statistics

### ✅ Chemist (Pharmacy) Features
- Inventory management (add, edit, delete medicines)
- Low stock alerts
- Out of stock tracking
- Prescription review and verification
- Order management and processing
- Update order status (approve, pack)
- Revenue tracking
- Business analytics dashboard
- Notification system for new orders

### ✅ Delivery Partner Features
- View assigned deliveries
- Accept deliveries
- Update delivery status
- Track earnings
- Online/offline status toggle
- Delivery history
- Performance ratings
- GPS location support for tracking

### ✅ System Features
- Real-time notifications for all events
- Order tracking with detailed timeline
- Automatic order number generation (MED-XXXX)
- Prescription verification workflow
- Emergency delivery flagging
- Low stock alerts
- Multiple address management
- Wishlist functionality
- Dashboard statistics for all roles
- Search and filter capabilities

---

## Technology Stack

| Component | Technology |
|-----------|-----------|
| Framework | Spring Boot 3.2.5 |
| Language | Java 17 |
| Security | Spring Security + JWT |
| Database | H2 (in-memory) / MySQL |
| ORM | Spring Data JPA |
| Password | BCrypt Encryption |
| Build Tool | Maven |
| API Style | RESTful |

---

## Setup & Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- (Optional) MySQL 8.0 or higher

### Step 1: Clone and Navigate
```bash
cd backend
```

### Step 2: Install Dependencies
```bash
mvn clean install
```

### Step 3: Configure Database (Optional)

**For H2 (Default - In-Memory):**
No configuration needed. H2 console available at: http://localhost:8080/h2-console

**For MySQL:**
Update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/meddy
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### Step 4: Run the Application
```bash
mvn spring-boot:run
```

The backend will start on: **http://localhost:8080**

---

## API Endpoints Summary

### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - User login

### User Management
- `GET /api/users/{userId}` - Get user profile
- `PUT /api/users/{userId}` - Update profile
- `PUT /api/users/{userId}/password` - Change password
- `GET /api/users/{userId}/addresses` - Get addresses
- `POST /api/users/{userId}/addresses` - Add address

### Dashboard
- `GET /api/dashboard/patient/{patientId}` - Patient stats
- `GET /api/dashboard/chemist/{chemistId}` - Chemist stats
- `GET /api/dashboard/delivery/{deliveryPartnerId}` - Delivery stats

### Orders
- `POST /api/orders/create` - Create order
- `GET /api/orders/patient/{patientId}` - Patient orders
- `GET /api/orders/chemist/{chemistId}` - Chemist orders
- `GET /api/orders/delivery/{deliveryPartnerId}` - Delivery orders
- `GET /api/orders/{orderId}` - Order details
- `PUT /api/orders/{orderId}/status` - Update status

### Inventory
- `POST /api/inventory/add` - Add medicine
- `PUT /api/inventory/{id}` - Update medicine
- `DELETE /api/inventory/{id}` - Delete medicine
- `GET /api/inventory/all` - Browse medicines
- `GET /api/inventory/search?query=` - Search medicines
- `GET /api/inventory/category/{category}` - Filter by category

### Prescriptions
- `POST /api/prescriptions/upload` - Upload prescription
- `GET /api/prescriptions/pending` - Pending reviews
- `PUT /api/prescriptions/{id}/verify` - Verify prescription
- `PUT /api/prescriptions/{id}/reject` - Reject prescription

### Notifications
- `GET /api/notifications/user/{userId}` - Get notifications
- `GET /api/notifications/user/{userId}/unread-count` - Unread count
- `PUT /api/notifications/{id}/read` - Mark as read
- `PUT /api/notifications/user/{userId}/mark-all-read` - Mark all read

### Wishlist
- `GET /api/wishlist/user/{userId}` - Get wishlist
- `POST /api/wishlist/add` - Add to wishlist
- `DELETE /api/wishlist/user/{userId}/item/{inventoryId}` - Remove

**See [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for complete API reference with request/response examples.**

---

## Database Schema

### Tables Created
1. **users** - All users (patients, chemists, delivery partners)
2. **orders** - Order details with status tracking
3. **order_items** - Individual items in each order
4. **inventory** - Medicine inventory per chemist
5. **prescriptions** - Uploaded prescription files
6. **notifications** - System notifications
7. **wishlist** - User wishlist items
8. **addresses** - User delivery addresses

All tables are auto-created by Hibernate DDL with proper relationships.

---

## Order Status Workflow

```
PENDING
    ↓
PRESCRIPTION_VERIFIED (if required)
    ↓
APPROVED (by chemist)
    ↓
PACKED (medicines ready)
    ↓
DELIVERY_PARTNER_ASSIGNED
    ↓
OUT_FOR_DELIVERY
    ↓
DELIVERED
```

---

## Security Features

1. **JWT Authentication**: All sensitive endpoints protected
2. **Password Encryption**: BCrypt with salt
3. **CORS Enabled**: Frontend integration ready
4. **Role-Based Access**: Different permissions per role
5. **Session Management**: Stateless with JWT
6. **Remember Me**: Optional long-lived sessions
7. **Two-Factor Auth**: Support for 2FA (ready to integrate)

---

## Configuration

### JWT Settings
```properties
jwt.secret=5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437
jwt.expiration=86400000  # 24 hours
```

### File Upload
```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### Database
```properties
spring.jpa.hibernate.ddl-auto=update  # Auto-create tables
spring.jpa.show-sql=true              # Log SQL queries
```

---

## Testing the Backend

### Using cURL

**Register:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "mobile": "+91 9876543210",
    "role": "PATIENT"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

**Get Medicines:**
```bash
curl http://localhost:8080/api/inventory/all
```

---

## Frontend Integration

### CORS Configuration
Backend is configured to accept requests from any origin:
```java
configuration.setAllowedOrigins(List.of("*"));
```

### Authentication Flow
1. Frontend sends login credentials to `/auth/login`
2. Backend returns JWT token + user object
3. Frontend stores token (localStorage/sessionStorage)
4. Frontend includes token in Authorization header: `Bearer {token}`
5. Backend validates token on protected routes

### Example Frontend Request
```javascript
fetch('http://localhost:8080/api/orders/patient/1', {
  headers: {
    'Authorization': 'Bearer ' + localStorage.getItem('token')
  }
})
.then(response => response.json())
.then(data => console.log(data));
```

---

## Deployment

### Local Development
```bash
mvn spring-boot:run
```

### Production Build
```bash
mvn clean package
java -jar target/medicine-delivery-0.0.1-SNAPSHOT.jar
```

### Docker (Optional)
```bash
docker build -t meddy-backend .
docker run -p 8080:8080 meddy-backend
```

---

## Future Enhancements (Ready to Add)

- [ ] Payment gateway integration
- [ ] SMS/Email notifications
- [ ] Real-time GPS tracking via WebSocket
- [ ] AI-based medicine recommendations
- [ ] Analytics and reporting dashboard
- [ ] Admin panel for system management
- [ ] Medicine interaction checker
- [ ] Prescription OCR for automated parsing
- [ ] Multi-language support
- [ ] Push notifications (FCM)

---

## Support

For issues or questions:
1. Check [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
2. Review error logs in console
3. Test endpoints with Postman/cURL
4. Verify database schema in H2 console

---

## License

This project is part of the MEDDY Healthcare Platform.

**Built with ❤️ for faster, safer medicine delivery across India**
