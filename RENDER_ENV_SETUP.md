# RENDER ENVIRONMENT VARIABLES - EXACT SETUP

## 📋 COPY-PASTE READY

### Step 1: Generate JWT_SECRET (Run in terminal)

**Windows PowerShell:**
```powershell
[Convert]::ToBase64String([System.Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
```
Copy the output (it will look like: `abc123def456...`)

**Mac/Linux/Git Bash:**
```bash
openssl rand -base64 32
```

---

## Step 2: Get Frontend URL from Vercel

1. Go to https://vercel.com/dashboard
2. Select your project
3. Copy the deployment URL (looks like: `https://employee-leave-frontend-abc123.vercel.app`)

---

## Step 3: Set in Render Dashboard

Go to: **Render.com → Web Service → Environment**

### Copy these exactly (substitute the values below):

| Key | Value | Example |
|-----|-------|---------|
| `SPRING_PROFILES_ACTIVE` | `render` | `render` |
| `ALLOWED_ORIGINS` | Your Vercel URL | `https://employee-leave-frontend-abc123.vercel.app` |
| `JWT_SECRET` | JWT key from Step 1 | `abc123def456ghi789...` |
| `PORT` | `8080` | `8080` |

---

## Step 4: Link MySQL Database Variables

After creating MySQL database in Render:

Click "Environment" → "Add from Database"

Connect these variables:
- `DATABASE_URL` → select from `employee-leave-db`
- `DATABASE_USER` → select from `employee-leave-db`
- `DATABASE_PASSWORD` → select from `employee-leave-db`

---

## Final Environment Variables List:

```
# Application Profile
SPRING_PROFILES_ACTIVE=render

# Frontend URL (update with YOUR Vercel URL)
ALLOWED_ORIGINS=https://YOUR-VERCEL-URL.vercel.app

# Security (generate using commands above)
JWT_SECRET=YOUR-GENERATED-SECRET-HERE

# Port
PORT=8080

# Database (auto-filled by Render from MySQL service)
DATABASE_URL=mysql://...
DATABASE_USER=leave_user
DATABASE_PASSWORD=...
```

---

## Render.yaml Already Configured:

The `render.yaml` file has these commands already set:

```yaml
buildCommand: cd backend && mvn clean -B -DskipTests package
startCommand: java -jar backend/target/employee-leave-management-1.0.0.jar
```

Just deploy! ✅

---

## If Using Blueprint:

1. Go to Render → New → Blueprint
2. Select your GitHub repo
3. It will auto-read `render.yaml`
4. Deploy
5. Go to Web Service → Environment
6. Add the variables from "Step 3" above

Done! 🎉
