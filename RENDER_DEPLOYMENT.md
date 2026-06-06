# Render Deployment Guide for Employee Leave Management Backend

## Prerequisites
- GitHub account with repository pushed
- Render.com account
- Frontend deployed (or ready to deploy)

## Step-by-Step Setup Instructions

### 1. Push Your Repository to GitHub
```bash
git add .
git commit -m "Add render configuration"
git push origin main
```

### 2. Create Render Account & Connect GitHub
- Go to https://render.com
- Sign up with GitHub
- Authorize Render to access your repositories

### 3. Deploy Backend Using render.yaml

**Option A: Using render.yaml (Automatic)**
1. In Render dashboard, click "New +"
2. Select "Blueprint"
3. Select "Connect repository" and choose your repository
4. Select the `render.yaml` file
5. Click "Deploy"

**Option B: Manual Setup**
If Option A doesn't work, create manually:

### 4. Create Web Service Manually

1. **Create Web Service**
   - Click "New +" → "Web Service"
   - Connect your GitHub repository
   - Name: `employee-leave-management-backend`
   - Environment: `Java`
   - Build Command: `cd backend && mvn clean -B -DskipTests package`
   - Start Command: `java -jar backend/target/employee-leave-management-1.0.0.jar`

2. **Set Environment Variables**
   In the "Environment" section, add:
   ```
   DATABASE_URL = Provided by MySQL database service
   DATABASE_USER = leave_user
   DATABASE_PASSWORD = Provided by MySQL database service
   SPRING_PROFILES_ACTIVE = render
   ALLOWED_ORIGINS = https://your-frontend-domain.vercel.app
   JWT_SECRET = your-very-secure-32-character-jwt-secret
   PORT = 8080
   ```

### 5. Create MySQL Database

1. Click "New +" → "MySQL"
2. Configure:
   - Name: `employee-leave-db`
   - Database Name: `employee_leave_db`
   - User: `leave_user`
   - Plan: Free (or Starter)
   - Region: Same as backend (oregon)

3. The database will automatically provide:
   - `DATABASE_URL`
   - `DATABASE_USER`
   - `DATABASE_PASSWORD`

### 6. Link Database to Web Service

1. Go to Web Service settings
2. In "Environment" section:
   - Click "Add from Database" for each variable
   - Select the MySQL database you created
   - It will auto-populate the connection details

### 7. Configure Environment Variables in Render Dashboard

| Variable | Value | Notes |
|----------|-------|-------|
| DATABASE_URL | Auto from DB | Don't modify |
| DATABASE_USER | leave_user | Auto from DB |
| DATABASE_PASSWORD | Auto from DB | Auto from DB |
| SPRING_PROFILES_ACTIVE | render | Activates render profile |
| ALLOWED_ORIGINS | https://yourfrontend.vercel.app | Your frontend URL |
| JWT_SECRET | (min 32 chars) | Generate secure random string |
| PORT | 8080 | Default, keep as is |

### 8. Generate JWT Secret
```bash
# In terminal (bash/zsh)
openssl rand -base64 32

# Or use this Python command
python3 -c "import secrets; print(secrets.token_urlsafe(32))"
```

### 9. Update Frontend Configuration
Update your frontend API endpoint to point to Render backend:
- Change: `http://localhost:8080` 
- To: `https://employee-leave-management-backend.onrender.com`

### 10. Deploy & Monitor

1. Click "Deploy"
2. Monitor logs in Render dashboard
3. Check health endpoint: `https://your-backend-url.onrender.com/api/health`

## Troubleshooting

### Build Fails
- Check Java version in `system.properties` (should be 21)
- Verify Maven build locally: `cd backend && mvn clean package`

### Database Connection Error
- Verify DATABASE_URL format is correct
- Check database credentials match
- Ensure database is in same region

### CORS Errors
- Update ALLOWED_ORIGINS with correct frontend URL
- Include protocol: `https://` not just domain

### Health Check Timeout
- Increase timeout in render.yaml (currently 30s)
- Check if database is initializing

## Important Files

- `render.yaml` - Render configuration
- `system.properties` - Java version specification
- `backend/src/main/resources/application-render.properties` - Production configuration
- `backend/pom.xml` - Maven dependencies

## Database Initialization

The application uses `spring.jpa.hibernate.ddl-auto=update`, which will:
- Create tables automatically on first run
- Run `schema.sql` if present in resources

## Backend API Endpoints

After deployment:
- Health: `https://your-url.onrender.com/api/health`
- Auth: `https://your-url.onrender.com/api/auth/*`
- Employees: `https://your-url.onrender.com/api/employees/*`
- Leave: `https://your-url.onrender.com/api/leave/*`

## Notes

- Free tier on Render: Services spin down after 15 min of inactivity
- Database remains active
- Add an uptime monitor to keep it active
- Plan upgrade for production reliability

## Support

For issues, check:
1. Render logs (real-time in dashboard)
2. MySQL connection status
3. Java application logs
4. Frontend console for API errors
