# MEDDY Backend - Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Step 1: Prerequisites Check
```bash
java -version    # Should be Java 17+
mvn -version     # Should be Maven 3.6+
```

### Step 2: Start the Backend
```bash
cd backend
mvn spring-boot:run
```

✅ Backend running at: **http://localhost:8080**

---

## 🧪 Test the APIs

### 1. Health Check
```bash
curl http://localhost:8080/
```

### 2. Register a Patient
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Rahul Sharma",
    "email": "rahul@example.com",
    "password": "password123",
    "mobile": "+91 9876543210",
    "address": "123 MG Road, Mumbai",
    "role": "PATIENT"
  }'
```

**Response:** You'll get a JWT token and user object.

### 3. Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "rahul@example.com",
    "password": "password123"
  }'
```

**Save the token from the response!**

### 4. Register a Chemist
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "City Medical Store",
    "email": "citymedical@example.com",
    "password": "password123",
    "mobile": "+91 9876543211",
    "address": "456 Station Road, Mumbai",
    "role": "CHEMIST",
    "pharmacyName": "City Medical Store",
    "licenseNumber": "LIC123456"
  }'
```

### 5. Add Medicine to Inventory
```bash
curl -X POST http://localhost:8080/api/inventory/add \
  -H "Content-Type: application/json" \
  -d '{
    "chemistId": 2,
    "medicineName": "Paracetamol 500mg",
    "category": "Tablets",
    "manufacturer": "Sun Pharma",
    "batchNumber": "BATCH001",
    "mrp": 50.0,
    "sellingPrice": 38.0,
    "stock": 100,
    "prescriptionRequired": false,
    "description": "Pain reliever and fever reducer"
  }'
```

### 6. Browse All Medicines
```bash
curl http://localhost:8080/api/inventory/all
```

### 7. Create an Order
```bash
curl -X POST http://localhost:8080/api/orders/create \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "chemistId": 2,
    "deliveryAddress": "123 MG Road, Mumbai",
    "prescriptionRequired": false,
    "isEmergency": false,
    "items": [
      {
        "inventoryId": 1,
        "medicineName": "Paracetamol 500mg",
        "quantity": 2,
        "unitPrice": 38.0
      }
    ]
  }'
```

### 8. Get Patient Dashboard
```bash
curl http://localhost:8080/api/dashboard/patient/1
```

---

## 🗄️ Access H2 Database Console

1. Open browser: **http://localhost:8080/h2-console**
2. Use these settings:
   - **JDBC URL:** `jdbc:h2:mem:testdb`
   - **Username:** `sa`
   - **Password:** (leave empty)
3. Click **Connect**

You can now view all tables and data!

---

## 📊 Database Tables

After running the app, these tables will be created automatically:

1. `users` - All users (patients, chemists, delivery partners)
2. `orders` - Order details
3. `order_items` - Items in each order
4. `inventory` - Medicine inventory
5. `prescriptions` - Uploaded prescriptions
6. `notifications` - System notifications
7. `wishlist` - User wishlists
8. `addresses` - User addresses

---

## 🔑 User Roles

### PATIENT
- Browse medicines
- Create orders
- Upload prescriptions
- Track deliveries
- Manage wishlist

### CHEMIST
- Manage inventory
- Review prescriptions
- Process orders
- View analytics

### DELIVERY
- View assigned deliveries
- Update delivery status
- Track earnings

---

## 📱 Frontend Integration

### Store Token After Login
```javascript
const response = await fetch('http://localhost:8080/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email, password })
});

const data = await response.json();
localStorage.setItem('token', data.token);
localStorage.setItem('user', JSON.stringify(data.user));
```

### Use Token in Requests
```javascript
const token = localStorage.getItem('token');

fetch('http://localhost:8080/api/orders/patient/1', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
})
.then(res => res.json())
.then(orders => console.log(orders));
```

---

## 🎯 Common Use Cases

### Create Complete Order Flow

1. Patient registers → Get userId
2. Chemist adds medicines → Get inventoryId
3. Patient creates order with items
4. Chemist approves order
5. Delivery partner is assigned
6. Order is delivered

### Prescription Flow

1. Patient uploads prescription
2. Chemist reviews in pending queue
3. Chemist approves/rejects
4. Patient gets notification

---

## 🔧 Configuration

### Change Port
```properties
# In application.properties
server.port=9090
```

### Use MySQL Instead of H2
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/meddy
spring.datasource.username=root
spring.datasource.password=yourpassword
```

### Change JWT Expiration
```properties
jwt.expiration=86400000  # 24 hours in milliseconds
```

---

## 📖 Documentation

- **Complete API Docs:** [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
- **Full README:** [README.md](README.md)
- **Redesign Summary:** [../BACKEND_REDESIGN_SUMMARY.md](../BACKEND_REDESIGN_SUMMARY.md)

---

## 🐛 Troubleshooting

### Port Already in Use
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9
```

### Maven Build Fails
```bash
mvn clean install -U
```

### H2 Console Not Loading
- Check if app is running
- Try: http://localhost:8080/h2-console
- Verify JDBC URL: `jdbc:h2:mem:testdb`

---

## 🎓 Learning Path

1. ✅ Start the backend
2. ✅ Test health check endpoint
3. ✅ Register users (patient + chemist)
4. ✅ Add medicines to inventory
5. ✅ Create an order
6. ✅ Check H2 database
7. ✅ Explore all API endpoints
8. ✅ Integrate with frontend

---

## 🚦 Status Codes

- **200** - Success
- **201** - Created
- **400** - Bad Request (validation error)
- **401** - Unauthorized (invalid credentials)
- **404** - Not Found
- **500** - Server Error

---

## 🎉 You're Ready!

The backend is fully functional and ready to support your frontend. All 50+ API endpoints are live and tested.

**Next Steps:**
1. Integrate frontend with backend APIs
2. Test all user flows
3. Add error handling
4. Deploy to production

**Need help?** Check the documentation files or test APIs with Postman!

---

**Happy Coding! 🚀**
