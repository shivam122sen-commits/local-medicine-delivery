# MEDDY - Frontend & Backend Integration Guide

## 🔗 Where is the Backend URL?

The backend URL configuration is located in **TWO places**:

### 1. **Separate Config File** (Recommended)
📁 **File:** `/frontend/api-config.js`

```javascript
const API_CONFIG = {
  BASE_URL: 'http://localhost:8080',  // ← CHANGE THIS
  ENDPOINTS: { ... }
};
```

### 2. **Inside HTML File**
📁 **File:** `/frontend/front.html`

Look for this section at line ~2182:
```javascript
<script>
/* BACKEND API CONFIGURATION */
const API_CONFIG = {
  BASE_URL: 'http://localhost:8080',  // ← CHANGE THIS
  ENDPOINTS: { ... }
};
```

---

## 🚀 Quick Setup (3 Steps)

### Step 1: Start the Backend
```bash
cd backend
mvn spring-boot:run
```
✅ Backend runs on: **http://localhost:8080**

### Step 2: Update Frontend Configuration

**Option A: If using api-config.js (Recommended)**
```javascript
// In frontend/api-config.js, line 11
const API_CONFIG = {
  BASE_URL: 'http://localhost:8080',  // Local development
  // BASE_URL: 'https://your-domain.com',  // Production
};
```

**Option B: If using inline config in HTML**
```javascript
// In frontend/front.html, around line 2183
const API_CONFIG = {
  BASE_URL: 'http://localhost:8080',  // Change this URL
};
```

### Step 3: Open Frontend
```bash
# Simply open the HTML file in your browser
open frontend/front.html

# Or use a local server (recommended)
cd frontend
python -m http.server 3000
# Then open: http://localhost:3000/front.html
```

---

## 📝 How to Use the API Configuration

### Method 1: Include the Config File (Recommended)

Add this line in your HTML `<head>` section:
```html
<head>
  <!-- ... other tags ... -->
  <script src="api-config.js"></script>
</head>
```

Then use the functions in your code:
```javascript
// Login example
async function handleLogin() {
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;
  
  const result = await login(email, password);
  if (result) {
    console.log('Login successful!', result.user);
    // Redirect to dashboard
  }
}

// Get medicines example
async function loadMedicines() {
  const medicines = await getAllMedicines();
  console.log('Medicines:', medicines);
  // Display medicines on page
}

// Create order example
async function placeOrder() {
  const orderData = {
    patientId: 1,
    chemistId: 2,
    deliveryAddress: "123 MG Road, Mumbai",
    items: [
      {
        inventoryId: 10,
        medicineName: "Paracetamol 500mg",
        quantity: 2,
        unitPrice: 38.0
      }
    ]
  };
  
  const order = await createOrder(orderData);
  console.log('Order created:', order);
}
```

### Method 2: Use Inline (Already in HTML)

The configuration is already embedded in `front.html`. Just update the `BASE_URL`:

```javascript
// Around line 2183 in front.html
const API_CONFIG = {
  BASE_URL: 'http://localhost:8080',  // ← Change this for production
  ENDPOINTS: { ... }
};
```

Then use the `apiRequest` function:
```javascript
// Example: Get patient dashboard
async function loadDashboard() {
  const user = getCurrentUser();
  if (!user) return;
  
  const stats = await apiRequest(
    `/api/dashboard/patient/${user.id}`,
    { method: 'GET' }
  );
  
  console.log('Dashboard stats:', stats);
}
```

---

## 🔑 Authentication Flow

### 1. Register a User
```javascript
async function handleRegister() {
  const userData = {
    name: "Rahul Sharma",
    email: "rahul@example.com",
    password: "password123",
    mobile: "+91 9876543210",
    address: "123 MG Road, Mumbai",
    role: "PATIENT"  // or "CHEMIST" or "DELIVERY"
  };
  
  const result = await register(userData);
  if (result) {
    // Automatically saves token and user to localStorage
    console.log('Registered successfully!');
    // Redirect to dashboard
  }
}
```

### 2. Login
```javascript
async function handleLogin() {
  const result = await login('rahul@example.com', 'password123', true);
  if (result) {
    console.log('Logged in!', result.user);
    // Token is automatically stored
    // Redirect based on role
    if (result.user.role === 'PATIENT') {
      showPage('patient-dashboard');
    }
  }
}
```

### 3. Check if User is Logged In
```javascript
function checkAuth() {
  const user = getCurrentUser();
  const token = getAuthToken();
  
  if (!user || !token) {
    // Not logged in, show login modal
    return false;
  }
  
  console.log('Current user:', user);
  return true;
}
```

### 4. Logout
```javascript
function handleLogout() {
  logout(); // Clears localStorage
  showPage('landing');
}
```

---

## 📋 Common API Examples

### Get All Medicines
```javascript
async function loadMedicines() {
  const medicines = await getAllMedicines();
  // medicines is an array of medicine objects
  displayMedicines(medicines);
}
```

### Search Medicines
```javascript
async function searchMeds(query) {
  const results = await searchMedicines(query);
  displayMedicines(results);
}
```

### Create Order
```javascript
async function placeOrder(cartItems) {
  const user = getCurrentUser();
  
  const orderData = {
    patientId: user.id,
    chemistId: 2, // Selected pharmacy
    deliveryAddress: user.address,
    prescriptionRequired: false,
    isEmergency: false,
    items: cartItems.map(item => ({
      inventoryId: item.id,
      medicineName: item.name,
      quantity: item.quantity,
      unitPrice: item.price
    }))
  };
  
  const order = await createOrder(orderData);
  if (order) {
    alert('Order placed! Order number: ' + order.orderNumber);
  }
}
```

### Get Patient Dashboard
```javascript
async function initPatientDashboard() {
  const user = getCurrentUser();
  const stats = await getPatientDashboard(user.id);
  
  if (stats) {
    document.getElementById('totalOrders').textContent = stats.totalOrders;
    document.getElementById('activeOrders').textContent = stats.activeOrders;
    document.getElementById('deliveredOrders').textContent = stats.deliveredOrders;
  }
}
```

### Upload Prescription
```javascript
async function handlePrescriptionUpload(fileInput) {
  const user = getCurrentUser();
  const file = fileInput.files[0];
  
  if (!file) return;
  
  const result = await uploadPrescription(file, user.id);
  if (result) {
    alert('Prescription uploaded successfully!');
  }
}

// In HTML:
// <input type="file" onchange="handlePrescriptionUpload(this)">
```

### Get Notifications
```javascript
async function loadNotifications() {
  const user = getCurrentUser();
  const notifications = await getUserNotifications(user.id);
  displayNotifications(notifications);
}

async function showUnreadCount() {
  const user = getCurrentUser();
  const count = await getUnreadCount(user.id);
  document.getElementById('notifBadge').textContent = count;
}
```

### Add to Wishlist
```javascript
async function addMedicineToWishlist(medicineId) {
  const user = getCurrentUser();
  const result = await addToWishlist(user.id, medicineId);
  
  if (result) {
    alert('Added to wishlist!');
  }
}
```

---

## 🌐 Environment Configuration

### Local Development
```javascript
BASE_URL: 'http://localhost:8080'
```

### Production (Update before deploying)
```javascript
BASE_URL: 'https://api.meddy.com'  // Your production URL
// Or
BASE_URL: 'https://your-backend-url.onrender.com'
// Or
BASE_URL: 'https://your-backend-url.herokuapp.com'
```

### Using Environment Variables (Advanced)
```javascript
const API_CONFIG = {
  BASE_URL: process.env.API_URL || 'http://localhost:8080'
};
```

---

## 🎯 Integration Checklist

- [ ] **Step 1:** Backend running on `http://localhost:8080`
- [ ] **Step 2:** Frontend `BASE_URL` set to `http://localhost:8080`
- [ ] **Step 3:** Test health check: `curl http://localhost:8080/`
- [ ] **Step 4:** Test register API from frontend
- [ ] **Step 5:** Test login API from frontend
- [ ] **Step 6:** Test browsing medicines
- [ ] **Step 7:** Test creating orders
- [ ] **Step 8:** Test dashboard loading

---

## 🐛 Troubleshooting

### Issue: CORS Error
```
Access to fetch at 'http://localhost:8080/api/...' from origin 'http://localhost:3000' 
has been blocked by CORS policy
```

**Solution:** Backend already has CORS enabled. Make sure backend is running.

### Issue: 401 Unauthorized
**Solution:** Token expired or invalid. User needs to login again.

### Issue: Connection Refused
**Solution:** Backend is not running. Start it with `mvn spring-boot:run`

### Issue: Cannot find api-config.js
**Solution:** Either use the inline config in HTML or include the script:
```html
<script src="api-config.js"></script>
```

---

## 📱 Testing APIs with Browser Console

Open browser console (F12) and try:

```javascript
// Test connection
fetch('http://localhost:8080/')
  .then(r => r.text())
  .then(console.log);

// Test register
fetch('http://localhost:8080/auth/register', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    name: 'Test User',
    email: 'test@example.com',
    password: 'password123',
    role: 'PATIENT'
  })
})
.then(r => r.json())
.then(console.log);

// Test medicines
fetch('http://localhost:8080/api/inventory/all')
  .then(r => r.json())
  .then(console.log);
```

---

## 🎨 Complete Example: Login Button

```html
<!-- In your HTML -->
<button onclick="handleLoginClick()">Login</button>

<script src="api-config.js"></script>
<script>
async function handleLoginClick() {
  const email = prompt('Enter email:');
  const password = prompt('Enter password:');
  
  try {
    const result = await login(email, password);
    
    if (result) {
      alert('Welcome ' + result.user.name + '!');
      console.log('User:', result.user);
      console.log('Token:', result.token);
      
      // Redirect based on role
      switch(result.user.role) {
        case 'PATIENT':
          showPage('patient-dashboard');
          break;
        case 'CHEMIST':
          showPage('chemist-dashboard');
          break;
        case 'DELIVERY':
          showPage('delivery-dashboard');
          break;
      }
    } else {
      alert('Login failed!');
    }
  } catch (error) {
    alert('Error: ' + error.message);
  }
}
</script>
```

---

## 📚 Available API Functions (from api-config.js)

```javascript
// Authentication
login(email, password, rememberMe)
register(userData)
logout()

// Utilities
getAuthToken()
getCurrentUser()
saveAuth(token, user)
clearAuth()

// Dashboard
getPatientDashboard(patientId)

// Medicines
getAllMedicines()
searchMedicines(query)

// Orders
createOrder(orderData)
getPatientOrders(patientId)

// Prescriptions
uploadPrescription(file, patientId)

// Notifications
getUserNotifications(userId)
getUnreadCount(userId)

// Wishlist
addToWishlist(userId, inventoryId)

// Generic
apiRequest(endpoint, options, pathParams)
```

---

## 🎯 Summary

**To change the backend URL, edit ONE of these:**

1. **api-config.js** (line 11): `BASE_URL: 'http://localhost:8080'`
2. **front.html** (line ~2183): `BASE_URL: 'http://localhost:8080'`

**For production:**
- Change `http://localhost:8080` to your production backend URL
- Example: `https://meddy-backend.onrender.com`
- Example: `https://api.meddy.com`

**That's it!** The frontend will now communicate with your backend. 🎉

---

**Need Help?**
- Check `backend/API_DOCUMENTATION.md` for all endpoints
- Check `backend/QUICK_START.md` for backend setup
- Check browser console (F12) for errors

---

**Happy Coding! 🚀**
