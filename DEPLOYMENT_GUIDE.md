# 🚀 MEDDY - Deployment Guide

## ❌ Issue: 404 NOT_FOUND on Vercel

**Problem:** Your frontend has `front.html` but Vercel expects `index.html`

**Solution:** ✅ I've fixed this for you!

---

## ✅ What I Fixed:

1. **Created `index.html`** - Copy of `front.html` (Vercel's default entry point)
2. **Created `vercel.json`** - Configuration file for Vercel deployment
3. **Added routing rules** - Ensures proper file serving

---

## 🌐 Frontend Deployment (Vercel)

### Option 1: Deploy via Vercel Dashboard (Easiest)

1. **Go to:** https://vercel.com/
2. **Click:** "Add New Project"
3. **Import:** Your GitHub repository (or upload folder)
4. **Root Directory:** Set to `frontend`
5. **Framework Preset:** Select "Other"
6. **Click:** "Deploy"

### Option 2: Deploy via Vercel CLI

```bash
# Install Vercel CLI
npm install -g vercel

# Navigate to frontend
cd frontend

# Deploy
vercel

# Follow the prompts:
# - Set up and deploy? Yes
# - Which scope? Your account
# - Link to existing project? No
# - What's your project's name? meddy-frontend
# - In which directory is your code located? ./
# - Want to override the settings? No
```

---

## 🖥️ Backend Deployment

### Option 1: Deploy to Render (Recommended for Spring Boot)

1. **Go to:** https://render.com/
2. **Click:** "New +"
3. **Select:** "Web Service"
4. **Connect:** Your GitHub repository
5. **Settings:**
   - **Name:** `meddy-backend`
   - **Root Directory:** `backend`
   - **Environment:** `Java`
   - **Build Command:** `mvn clean install -DskipTests`
   - **Start Command:** `java -jar target/medicine-delivery-0.0.1-SNAPSHOT.jar`
   - **Instance Type:** Free
6. **Click:** "Create Web Service"

**Your backend URL will be:** `https://meddy-backend.onrender.com`

### Option 2: Deploy to Heroku

```bash
# Install Heroku CLI
brew tap heroku/brew && brew install heroku

# Login
heroku login

# Create app
cd backend
heroku create meddy-backend

# Add Java buildpack
heroku buildpacks:set heroku/java

# Deploy
git push heroku main

# Your backend URL: https://meddy-backend.herokuapp.com
```

### Option 3: Deploy to Railway

1. **Go to:** https://railway.app/
2. **Click:** "New Project"
3. **Select:** "Deploy from GitHub repo"
4. **Choose:** Your repository
5. **Settings:**
   - **Root Directory:** `/backend`
   - **Build Command:** `mvn clean package -DskipTests`
   - **Start Command:** `java -jar target/medicine-delivery-0.0.1-SNAPSHOT.jar`

---

## 🔗 Connect Frontend to Backend

### Step 1: Get Your Backend URL

After deploying backend, you'll get a URL like:
- Render: `https://meddy-backend.onrender.com`
- Heroku: `https://meddy-backend.herokuapp.com`
- Railway: `https://meddy-backend-production.up.railway.app`

### Step 2: Update Frontend Configuration

**Edit:** `frontend/api-config.js`

**Change line 11:**
```javascript
const API_CONFIG = {
  BASE_URL: 'https://meddy-backend.onrender.com',  // ← Your backend URL
  ENDPOINTS: { ... }
};
```

**OR edit:** `frontend/index.html`

**Find this section (around line 2183):**
```javascript
const API_CONFIG = {
  BASE_URL: 'https://meddy-backend.onrender.com',  // ← Your backend URL
  ENDPOINTS: { ... }
};
```

### Step 3: Redeploy Frontend

```bash
cd frontend
vercel --prod
```

**Done!** ✅

---

## 📋 Complete Deployment Checklist

### Backend Deployment ✅
- [ ] Choose deployment platform (Render/Heroku/Railway)
- [ ] Connect GitHub repository
- [ ] Configure build settings
- [ ] Deploy backend
- [ ] Test backend URL: `https://your-backend-url.com/`
- [ ] Copy backend URL

### Frontend Deployment ✅
- [ ] Update `api-config.js` with backend URL
- [ ] Verify `index.html` exists
- [ ] Verify `vercel.json` exists
- [ ] Deploy to Vercel
- [ ] Test frontend URL
- [ ] Test login/register functionality

---

## 🧪 Testing After Deployment

### 1. Test Backend Health
```bash
curl https://meddy-backend.onrender.com/
# Should return: "Welcome to MEDDY API"
```

### 2. Test Backend API
```bash
curl https://meddy-backend.onrender.com/api/inventory/all
# Should return: Array of medicines (or empty array)
```

### 3. Test Frontend
Open your Vercel URL in browser:
```
https://meddy-frontend.vercel.app
```

Should see the MEDDY homepage (no 404 error)

### 4. Test Integration
1. Open frontend in browser
2. Click "Register"
3. Fill form and submit
4. Check browser console (F12) for any errors
5. Should successfully create account

---

## 🐛 Common Deployment Issues

### Issue 1: 404 NOT_FOUND on Vercel
**Cause:** Missing `index.html` or wrong `vercel.json`
**Fix:** ✅ Already fixed! Files created for you.

### Issue 2: CORS Error
**Cause:** Backend URL not configured
**Fix:** Update `BASE_URL` in `api-config.js`

### Issue 3: Backend Build Failed
**Cause:** Java version mismatch
**Fix:** In Render/Heroku, set environment variable:
```
JAVA_VERSION=17
```

### Issue 4: Backend 503 Service Unavailable
**Cause:** Backend crashed or not started
**Fix:** Check logs on your deployment platform

### Issue 5: Cannot Connect to Backend
**Cause:** Backend URL is wrong
**Fix:** Verify backend URL is accessible:
```bash
curl https://your-backend-url.com/
```

---

## 📁 Required Files in Frontend Folder

```
frontend/
├── index.html          ✅ Created (copy of front.html)
├── front.html          ✅ Exists (original file)
├── api-config.js       ✅ Created (API configuration)
└── vercel.json         ✅ Created (Vercel config)
```

---

## 🔧 Environment Variables

### Backend (Render/Heroku)

Add these environment variables in your deployment platform:

```bash
PORT=8080
JWT_SECRET=5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437
JWT_EXPIRATION=86400000

# For MySQL (if not using H2):
SPRING_DATASOURCE_URL=jdbc:mysql://host:port/database
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
```

### Frontend (Vercel)

No environment variables needed if you hardcode the backend URL in `api-config.js`.

**Optional:** Use Vercel environment variables:
```bash
VITE_API_URL=https://meddy-backend.onrender.com
```

---

## 🎯 Step-by-Step Deployment (Complete)

### Step 1: Prepare Backend
```bash
cd backend
mvn clean package -DskipTests
# Verify JAR file created in target/
```

### Step 2: Deploy Backend
1. Go to Render.com
2. Create new Web Service
3. Connect GitHub repo
4. Set root directory: `backend`
5. Build: `mvn clean install -DskipTests`
6. Start: `java -jar target/medicine-delivery-0.0.1-SNAPSHOT.jar`
7. Deploy ✅

**Copy your backend URL:** `https://meddy-backend.onrender.com`

### Step 3: Update Frontend
```bash
cd frontend

# Edit api-config.js
# Change: BASE_URL: 'https://meddy-backend.onrender.com'
```

### Step 4: Deploy Frontend
```bash
cd frontend
vercel

# Or via Vercel dashboard:
# - Upload frontend folder
# - Auto-deploys
```

**Copy your frontend URL:** `https://meddy.vercel.app`

### Step 5: Test Everything
1. Open frontend URL
2. Should see homepage (no 404)
3. Click Register
4. Create account
5. Login
6. Browse medicines
7. ✅ Success!

---

## 📊 Deployment Status Check

After deployment, verify:

| Check | URL | Expected Result |
|-------|-----|----------------|
| Backend Health | `https://your-backend.com/` | "Welcome to MEDDY API" |
| Backend API | `https://your-backend.com/api/inventory/all` | JSON array |
| Frontend | `https://your-frontend.vercel.app` | MEDDY homepage |
| H2 Console | `https://your-backend.com/h2-console` | H2 login page |

---

## 🎉 Production URLs (After Deployment)

### Example:
- **Frontend:** https://meddy-frontend.vercel.app
- **Backend:** https://meddy-backend.onrender.com
- **API Docs:** https://meddy-backend.onrender.com/swagger-ui.html (if added)

---

## 💡 Pro Tips

1. **Use MySQL in Production:** H2 is in-memory and resets on restart
2. **Add HTTPS:** Render/Vercel provide free SSL certificates
3. **Monitor Logs:** Check logs on deployment platforms
4. **Add Analytics:** Google Analytics or similar
5. **Add Error Tracking:** Sentry or similar
6. **Enable Caching:** Add cache headers
7. **Compress Assets:** Enable gzip compression
8. **Add CDN:** Cloudflare for faster delivery

---

## 🆘 Need Help?

### Backend Issues
- Check logs on Render/Heroku dashboard
- Verify JAR file builds locally: `mvn clean package`
- Test locally first: `mvn spring-boot:run`

### Frontend Issues
- Check browser console (F12)
- Verify backend URL is correct
- Test backend URL with curl
- Check Network tab for failed requests

### CORS Issues
- Backend already has CORS enabled
- Verify `BASE_URL` matches exactly
- Check for trailing slashes

---

## ✅ Success!

Your MEDDY application is now deployed! 🎉

**Frontend:** https://your-frontend.vercel.app
**Backend:** https://your-backend.onrender.com

**To update:**
- Frontend: Push to GitHub or run `vercel --prod`
- Backend: Push to GitHub (auto-deploys)

---

**Happy Deploying! 🚀**
