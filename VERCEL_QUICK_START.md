# VERCEL FRONTEND DEPLOYMENT - QUICK GUIDE

## 📝 VERCEL FORM ME YEH FILL KARO:

| Field | Value |
|-------|-------|
| **Project Name** | `leave-management-system` |
| **Framework** | (Auto-detect - Vite) |
| **Root Directory** | `.` (dot) or empty |
| **Build Command** | `npm run build` |
| **Output Directory** | `dist` |
| **Install Command** | `npm install` |

---

## 🔑 ENVIRONMENT VARIABLES (After project created)

**Go to Settings → Environment Variables → Add:**

| Name | Value |
|------|-------|
| `VITE_API_URL` | `https://employee-leave-management-backend.onrender.com/api` |

**Replace URL with your actual Render backend URL!**

---

## ✅ DEPLOYMENT STEPS:

1. **Go to vercel.com → Sign in with GitHub**
2. **Click "Add New" → "Project"**
3. **Select repo:** roushansharma7492/Leave-Managament-System
4. **Click "Import"**
5. **Fill form values** (see table above)
6. **Add VITE_API_URL** environment variable
7. **Click "Deploy"** button
8. **Wait 2-3 minutes** for build
9. **Get Vercel URL** (like: `https://leave-management-system-abc.vercel.app`)

---

## 🔗 AFTER DEPLOYMENT:

### Update Backend CORS
1. Go to **Render Dashboard → Backend Service**
2. **Settings → Environment Variables**
3. Add your **Vercel URL** to `ALLOWED_ORIGINS`:
   ```
   https://leave-management-system-xyz.vercel.app
   ```
4. **Save** (auto-redeploys backend)

---

## 🧪 TEST IT:

1. Open your Vercel URL
2. Try to **login**
3. If error → Check **Console (F12)**
4. Common fix:
   - Update `VITE_API_URL` with correct backend URL
   - Update backend `ALLOWED_ORIGINS`
   - Redeploy both

---

## 📌 AUTO-DEPLOYMENT:

✅ Each `git push` to main:
- Vercel auto-builds
- Auto-deploys
- URL stays same
- No manual action needed

---

## 🚀 YOU'RE DONE!

Frontend deployed + Backend ready = Full-stack app live! 🎉

---

## QUICK REFERENCE

**Vercel URL Format:**
```
https://leave-management-system-xxx.vercel.app
```

**API URL for Vercel env var:**
```
https://employee-leave-management-backend.onrender.com/api
```

**Backend CORS URL:**
```
https://leave-management-system-xxx.vercel.app
```

✅ All 3 must match correctly!
