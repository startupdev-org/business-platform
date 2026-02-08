# Setup Guide - Beauty & Barbershop Booking Platform

## Quick Start (5 minutes)

### Step 1: Prerequisites Check
```bash
java -version          # Should be 17+
mvn -version          # Should be 3.8+
psql --version        # Should be 12+
```

### Step 2: Database Setup
```bash
# Create database
createdb beauty_booking

# Or if using remote PostgreSQL, update secrets.properties with connection details
```

### Step 3: Configure Environment
```bash
cp secrets.properties.dev secrets.properties

# Edit secrets.properties with your database credentials:
# DB_HOST=localhost
# DB_PORT=5432
# DB_NAME=beauty_booking
# DB_USER=postgres
# DB_PASSWORD=postgres
```

### Step 4: Build & Run
```bash
mvn clean package
mvn spring-boot:run
```

Application runs on: `http://localhost:8080`

## Database Setup (PostgreSQL)

### Using PostgreSQL CLI
```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE beauty_booking;

# Connect to the database
\c beauty_booking

# Exit
\q
```

### Using Docker (Optional)
```bash
docker run --name beauty-db \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=beauty_booking \
  -p 5432:5432 \
  -d postgres:15
```

## Configuration

### Database Connection (.env)
```
DB_HOST=localhost           # PostgreSQL host
DB_PORT=5432                # PostgreSQL port
DB_NAME=beauty_booking      # Database name
DB_USER=postgres            # Database user
DB_PASSWORD=postgres        # Database password
```

### JWT Configuration (.env)
```
JWT_SECRET=MyVerySecureJwtSecretKeyThatIsAtLeast32CharactersLongForHS256Algorithm
JWT_EXPIRATION=86400000     # 24 hours in milliseconds
```

### Server Configuration (.env)
```
SERVER_PORT=8080            # Server port
SPRING_PROFILES_ACTIVE=dev  # Environment (dev, prod)
```

## Initial Data Setup (Optional)

### Create Test Data via API

**1. Register Business Admin**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@salon.com",
    "password": "SecurePassword123"
  }'
```

Response:
```json
{
  "id": "uuid-here",
  "email": "admin@salon.com",
  "role": "BUSINESS_ADMIN",
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer"
}
```

**2. Create Business**
```bash
curl -X POST http://localhost:8080/api/business \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_token>" \
  -d '{
    "name": "The Beauty Salon",
    "description": "Premium beauty and hair salon",
    "address": "123 Main Street",
    "city": "New York",
    "phone": "+1-555-0100",
    "website": "https://thebeautysalon.com"
  }'
```

**3. Add Employees**
```bash
curl -X POST http://localhost:8080/api/business/{businessId}/employee \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_token>" \
  -d '{
    "name": "Sarah Johnson",
    "photoUrl": "https://example.com/photo.jpg",
    "active": true
  }'
```

**4. Add Services**
```bash
curl -X POST http://localhost:8080/api/business/{businessId}/providedService \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_token>" \
  -d '{
    "name": "Haircut",
    "description": "Professional haircut",
    "price": 35.00,
    "durationMinutes": 45,
    "active": true
  }'
```

**5. Create Booking**
```bash
curl -X POST http://localhost:8080/api/booking \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "customerPhone": "+1-555-0101",
    "customerEmail": "john@example.com",
    "employeeId": "{employeeId}",
    "serviceId": "{serviceId}",
    "startTime": "2024-02-15T10:00:00"
  }'
```

## IDE Setup

### IntelliJ IDEA
1. Open project: `File → Open → Select project root`
2. Configure JDK: `File → Project Structure → SDK → Java 17`
3. Enable Lombok: `File → Settings → Plugins → Search "Lombok" → Install`
4. Run: `Right-click BeautyBookingPlatformApplication.java → Run`

### Eclipse
1. Import project: `File → Import → Maven → Existing Maven Projects`
2. Configure JDK: `Project → Properties → Java Build Path → JRE System Library → 17`
3. Install Lombok: `Help → Install New Software → Add Repository → lombok.org`
4. Run: `Right-click project → Run As → Spring Boot App`

### VS Code
1. Install Extensions: Java Extension Pack, Spring Boot Extension Pack
2. Open project folder
3. Press Ctrl+F5 to run

## Verification

### Check Application is Running
```bash
curl http://localhost:8080/swagger-ui.html
```

### View API Documentation
```
http://localhost:8080/swagger-ui.html
```

### Check Logs
```bash
# In same terminal where app is running
# Look for: "Started BeautyBookingPlatformApplication"
```

## Common Issues & Solutions

### Issue: "Database connection refused"
**Solution:**
```bash
# Check PostgreSQL is running
pg_isready -h localhost -p 5432

# Or start PostgreSQL
sudo providedService postgresql start  # Linux
brew services start postgresql  # macOS
```

### Issue: "Port 8080 already in use"
**Solution:**
```bash
# Find process using port 8080
lsof -i :8080

# Kill process (macOS/Linux)
kill -9 <PID>

# Or change port in secrets.properties
SERVER_PORT=8081
```

### Issue: "JWT_SECRET too short"
**Solution:**
```bash
# Generate secure secret (min 32 chars)
openssl rand -base64 32

# Update secrets.properties
JWT_SECRET=<generated_secret>
```

### Issue: "Hibernate can't create tables"
**Solution:**
```bash
# Check ddl-auto setting in application.yml
# For development, ensure it's set to:
# hibernate:
#   ddl-auto: create-drop  (dev profile)
#   ddl-auto: validate     (prod profile)
```

## Development Workflow

### 1. Daily Development
```bash
# Start application in dev mode
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# In another terminal, test API
curl http://localhost:8080/api/business
```

### 2. Making Changes
- Edit code in `src/main/java`
- Spring Boot Dev Tools auto-reloads changes
- Refresh browser/API calls to see changes

### 3. Testing
```bash
mvn test
```

### 4. Build for Production
```bash
mvn clean package -DskipTests
```

## Database Migrations

### For Development (Auto-managed by Hibernate)
```yaml
# application-dev.yml
jpa:
  hibernate:
    ddl-auto: create-drop  # Creates tables on startup, drops on shutdown
```

### For Production (Use SQL Scripts)
```yaml
# application.yml
jpa:
  hibernate:
    ddl-auto: validate     # Only validates schema
```

Create migration scripts in `src/main/resources/db/migration/`:
```sql
-- V1__initial_schema.sql
CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  -- ... other columns
);
```

## Next Steps

1. **Explore API Documentation**: Visit `http://localhost:8080/swagger-ui.html`
2. **Create Test Data**: Use provided curl commands above
3. **Integrate Frontend**: Configure CORS in `CorsConfig.java`
4. **Deploy**: Follow deployment guide in README.md

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Docs](https://spring.io/projects/spring-security)
- [PostgreSQL Docs](https://www.postgresql.org/docs)
- [JWT Introduction](https://jwt.io/introduction)

## Support

For issues, check:
1. Log files for errors
2. Database connectivity
3. Environment variables
4. Port availability
5. Java version compatibility
