# 🚀 MEDDY - Quick Reference Card

## 📍 WHERE IS THE BACKEND URL?

### Option 1: Separate File (Recommended) ⭐
```
📁 File: /frontend/api-config.js
📍 Line: 11

const API_CONFIG = {
  BASE_URL: 'http://localhost:8080',  // ← CHANGE HERE
};
```

### Option 2: Inside HTML File
```
📁 File: /frontend/front.html  
📍 Line: ~2183

const API_CONFIG = {
  BASE_URL: 'http://localhost:8080',  // ← CHANGE HERE
};
```

---

## ⚡ Quick Start (30 Seconds)

### 1️⃣ Start Backend
```bash
cd backend
mvn spring-boot:run
```
**Running on:** http://localhost:8080 ✅

### 2️⃣ Update Frontend URL
```javascript
// In api-config.js OR front.html
BASE_URL: 'http://localhost:8080'
```

### 3️⃣ Open Frontend
```bash
# Double-click front.html
# OR
open frontend/front.html
```

**Done!** 🎉

---

## 🌐 Environment URLs

| Environment | URL to Use |
|-------------|-----------|
| **Local Development** | `http://localhost:8080` |
| **Production** | `https://your-domain.com` |
| **Heroku** | `https://your-app.herokuapp.com` |
| **Render** | `https://your-app.onrender.com` |
| **AWS** | `https://api.yourdomain.com` |

---

## 🔧 Backend Endpoints (Quick Reference)

| Category | Endpoint | Method |
|----------|----------|--------|
| **Login** | `/auth/login` | POST |
| **Register** | `/auth/register` | POST |
| **Get Medicines** | `/api/inventory/all` | GET |
| **Create Order** | `/api/orders/create` | POST |
| **Dashboard** | `/api/dashboard/patient/{id}` | GET |
| **Notifications** | `/api/notifications/user/{id}` | GET |
| **Wishlist** | `/api/wishlist/user/{id}` | GET |

**Full list:** See `backend/API_DOCUMENTATION.md`

---

## 💻 Code Examples

### Login
```javascript
const result = await login('user@example.com', 'password123');
if (result) {
  console.log('Logged in!', result.user);
}
```

### Get Medicines
```javascript
const medicines = await getAllMedicines();
console.log('Medicines:', medicines);
```

### Create Order
```javascript
const order = await createOrder({
  patientId: 1,
  chemistId: 2,
  deliveryAddress: "Mumbai",
  items: [{ inventoryId: 10, quantity: 2, unitPrice: 38 }]
});
```

### Check if Logged In
```javascript
const user = getCurrentUser();
if (user) {
  console.log('User is logged in:', user.name);
}
```

---

## 🧪 Test Backend (Browser Console)

Press **F12**, then paste:

```javascript
// Test connection
fetch('http://localhost:8080/')
  .then(r => r.text())
  .then(console.log);

// Test medicines API
fetch('http://localhost:8080/api/inventory/all')
  .then(r => r.json())
  .then(console.log);
```

---

## 🐛 Common Issues

### ❌ CORS Error
**Fix:** Backend already has CORS enabled. Just make sure backend is running.

### ❌ Connection Refused
**Fix:** Start backend with `mvn spring-boot:run`

### ❌ 401 Unauthorized
**Fix:** User needs to login again. Token expired.

### ❌ Can't find api-config.js
**Fix:** Use inline config in HTML instead (already added at line 2183)

---

## 📁 Project Structure

```
Meddy/
├── backend/                    ← Java Spring Boot
│   ├── src/main/java/...      ← Backend code
│   ├── API_DOCUMENTATION.md   ← All endpoints
│   ├── README.md              ← Setup guide
│   └── QUICK_START.md         ← 5-min guide
│
├── frontend/                   ← HTML/CSS/JS
│   ├── front.html             ← Main file
│   └── api-config.js          ← API config ⭐
│
└── FRONTEND_BACKEND_INTEGRATION_GUIDE.md  ← Full guide
```

---

## 🎯 Deployment Checklist

- [ ] Backend deployed & running
- [ ] Get backend production URL
- [ ] Update `BASE_URL` in api-config.js
- [ ] Test login from frontend
- [ ] Test API calls work
- [ ] Deploy frontend

---

## 📞 Need Help?

1. **Backend Setup:** Read `backend/README.md`
2. **API Docs:** Read `backend/API_DOCUMENTATION.md`
3. **Integration:** Read `FRONTEND_BACKEND_INTEGRATION_GUIDE.md`
4. **Quick Start:** Read `backend/QUICK_START.md`

---

## ✅ Success Indicators

✅ Backend: http://localhost:8080/ shows "Welcome to MEDDY API"
✅ Frontend: Can open front.html in browser
✅ Login: Can register and login successfully
✅ API: Can fetch medicines list

---

**That's all you need to know! 🚀**

**To change backend URL:**
1. Open `frontend/api-config.js`
2. Change line 11: `BASE_URL: 'your-new-url'`
3. Done! ✨
