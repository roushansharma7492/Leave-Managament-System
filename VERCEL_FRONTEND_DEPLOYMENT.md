# Vercel Frontend Deployment Guide

## Prerequisites
- GitHub account (repository already linked)
- Vercel account (free tier available)
- Backend deployed on Render (with API URL)

---

## VERCEL DEPLOYMENT FORM - FILL YEH VALUES:

### Repository Information
- **Importing from GitHub:** roushansharma7492/Leave-Managament-System ✅
- **Branch:** main ✅
- **Project Name:** leave-management-system (or any name you want)
- **Vercel Team:** Select your team (Hobby is free)

### Build and Output Settings
- **Framework Preset:** (Skip - let Vercel auto-detect)
- **Build Command:** `npm run build`
- **Output Directory:** `dist`
- **Install Command:** `npm install`

### Root Directory
- Leave **EMPTY** (Frontend is at root level)
- OR set to `.` (dot)

---

## ENVIRONMENT VARIABLES IN VERCEL DASHBOARD

After creating project, go to **Settings → Environment Variables**

Add this variable:

| Name | Value | 
|------|-------|
| `VITE_API_URL` | `https://employee-leave-management-backend.onrender.com/api` |

**Note:** Replace with your actual Render backend URL!

---

## Step-by-Step Deployment

### 1. Go to Vercel.com
- Sign in with GitHub
- Click "Add New..." → "Project"

### 2. Import from GitHub
- Select repository: `roushansharma7492/Leave-Managament-System`
- Click "Import"

### 3. Configure Project
| Field | Value |
|-------|-------|
| Project Name | `leave-management-system` |
| Framework | Vite (auto-detected) |
| Root Directory | `.` or leave empty |
| Build Command | `npm run build` |
| Output Directory | `dist` |

### 4. Add Environment Variable
- **Name:** `VITE_API_URL`
- **Value:** `https://your-backend-url.onrender.com/api`
- Click "Add"

### 5. Deploy!
- Click **"Deploy"** button
- Wait for build to complete (usually 2-3 min)
- You'll get a deployment URL like: `https://leave-management-system-xyz.vercel.app`

---

## After Deployment

### Update Backend CORS Settings (in Render)
1. Go to Render Dashboard → Backend Web Service
2. Settings → Environment Variables
3. Update `ALLOWED_ORIGINS` to include your Vercel URL:
   ```
   https://leave-management-system-xyz.vercel.app
   ```
4. Click "Save" → Service will redeploy

### Test the Deployment
1. Open your Vercel URL
2. Test login with backend
3. Check browser console (F12) for any CORS errors

---

## Troubleshooting

### Build Fails
- Check `npm run build` works locally first:
  ```bash
  cd frontend
  npm install
  npm run build
  ```
- Ensure all dependencies are in package.json

### API Connection Error
- Verify `VITE_API_URL` environment variable is set
- Check backend is running on Render
- Check CORS settings on backend

### CORS Error
- Add Vercel frontend URL to backend `ALLOWED_ORIGINS`:
  ```
  ALLOWED_ORIGINS=https://your-vercel-url.vercel.app
  ```

### Blank Page
- Check browser console for JavaScript errors (F12)
- Ensure API responses are correct
- Check if JWT token is being sent

---

## Environment Variables Summary

### For Vercel (Production)
```
VITE_API_URL=https://employee-leave-management-backend.onrender.com/api
```

### For Local Development
```
VITE_API_URL=http://localhost:8080/api
```

The app automatically uses:
- Production values when deployed on Vercel
- Local values when running `npm run dev`

---

## Files Used for Deployment

- `vercel.json` - Vercel configuration
- `package.json` - Build scripts and dependencies
- `vite.config.ts` - Build configuration
- `.env.production` - Production environment
- `frontend/src/api.ts` - API client using VITE_API_URL

---

## Important Notes

✅ Vercel provides free tier:
- 100GB bandwidth/month
- Always-on deployment
- Automatic HTTPS
- Automatic deployments on git push

⚠️ Each git push to main will trigger:
- Automatic rebuild
- Automatic deployment
- New URL remains the same

🔄 To redeploy:
- Push to GitHub
- Vercel auto-redeploys
- No manual action needed

---

## Frontend API Integration

The app uses Vite environment variables:
- `VITE_API_URL` → Backend API base URL
- Configured in `frontend/src/api.ts`
- Automatically injected during build

Example usage:
```typescript
const baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
```

---

## Testing on Vercel

After deployment:
1. Visit your Vercel URL
2. Try to login
3. If API error: Check console (F12)
4. Common issues:
   - Backend not accessible (Render service down)
   - Wrong API URL in env variable
   - CORS not configured on backend

---

## Support

Check these files for help:
- [Vercel Docs](https://vercel.com/docs)
- `vercel.json` for build config
- `package.json` for scripts
- `frontend/src/api.ts` for API integration
