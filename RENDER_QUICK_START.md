# RENDER DEPLOYMENT - QUICK REFERENCE

## Files Created for Render Deployment:
✅ `system.properties` - Java 21 configuration
✅ `backend/src/main/resources/application-render.properties` - Production database config
✅ `render.yaml` - Complete Render deployment blueprint
✅ `.env.example` - Environment variables reference
✅ `RENDER_DEPLOYMENT.md` - Detailed setup guide

---

## WHAT TO ADD IN RENDER DASHBOARD:

### 1️⃣ CREATE WEB SERVICE
- **Name:** `employee-leave-management-backend`
- **GitHub Repo:** (Select your repo)
- **Branch:** `main`
- **Environment:** Java
- **Build Command:** `cd backend && mvn clean -B -DskipTests package`
- **Start Command:** `java -jar backend/target/employee-leave-management-1.0.0.jar`
- **Plan:** Free (or Starter)
- **Region:** Oregon

### 2️⃣ CREATE MYSQL DATABASE
- **Service:** MySQL
- **Name:** `employee-leave-db`
- **Database Name:** `employee_leave_db`
- **User:** `leave_user`
- **Plan:** Free
- **Region:** Oregon (same as web service)

### 3️⃣ ADD ENVIRONMENT VARIABLES TO WEB SERVICE

Copy these to your Web Service → Environment section:

```
SPRING_PROFILES_ACTIVE=render
ALLOWED_ORIGINS=https://your-frontend-url.vercel.app
JWT_SECRET=your-secure-32-character-random-string
PORT=8080
```

### 4️⃣ LINK DATABASE TO WEB SERVICE

Click "Environment" → "Add from Database"

- Select: `employee-leave-db`
- Add variable: `DATABASE_URL` → copy from database
- Add variable: `DATABASE_USER` → copy from database  
- Add variable: `DATABASE_PASSWORD` → copy from database

---

## ENVIRONMENT VARIABLES EXPLAINED:

| Variable | Value | Where to Get |
|----------|-------|-------------|
| `SPRING_PROFILES_ACTIVE` | `render` | Type this directly |
| `ALLOWED_ORIGINS` | Your Vercel frontend URL | From Vercel deployment |
| `JWT_SECRET` | Random 32+ chars | Generate with: `openssl rand -base64 32` |
| `PORT` | `8080` | Default - don't change |
| `DATABASE_URL` | Auto from MySQL | Render auto-fills from database |
| `DATABASE_USER` | Auto from MySQL | Render auto-fills from database |
| `DATABASE_PASSWORD` | Auto from MySQL | Render auto-fills from database |

---

## DEPLOYMENT STEPS:

1. Push code to GitHub:
   ```bash
   git add .
   git commit -m "Add Render deployment configuration"
   git push
   ```

2. Go to Render.com → Dashboard

3. Click "New +" → "Blueprint" → Select your GitHub repo

4. If blueprint auto-deploys: Done! ✅

5. If manual setup needed:
   - Create Web Service (see above)
   - Create MySQL Database (see above)
   - Add all environment variables (see above)
   - Click "Deploy"

---

## GENERATE JWT_SECRET:

**Windows PowerShell:**
```powershell
[Convert]::ToBase64String([System.Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
```

**Mac/Linux:**
```bash
openssl rand -base64 32
```

**Online (for testing only):**
https://www.random.org/strings/?num=1&len=32&digits=on&unique=on&format=html&rnd=new

---

## AFTER DEPLOYMENT:

### Test Health Endpoint:
```
https://employee-leave-management-backend.onrender.com/api/health
```

### Update Frontend API URL:
Change from: `http://localhost:8080`
To: `https://employee-leave-management-backend.onrender.com`

In `frontend/src/api.ts`:
```typescript
const API_BASE_URL = process.env.REACT_APP_API_URL || 
  'https://employee-leave-management-backend.onrender.com';
```

---

## IMPORTANT NOTES:

⚠️ **Free Tier:** Backend sleeps after 15 min of no requests (database stays active)

✅ **Auto Database Setup:** Tables created automatically on first run

🔒 **Security:** Never commit real JWT_SECRET to GitHub

📊 **Monitor:** Check logs in Render dashboard for errors

---

## TROUBLESHOOTING:

**Build fails?**
- Ensure `backend/pom.xml` exists
- Check Java 21 in `system.properties`

**Database connection error?**
- Verify DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD are set
- Check if database service is running

**CORS errors?**
- Update ALLOWED_ORIGINS with your frontend URL
- Include `https://` protocol

**500 errors?**
- Check Render logs: Dashboard → Web Service → Logs
