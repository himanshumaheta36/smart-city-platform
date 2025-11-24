# Getting Started - Smart City Platform

## 📋 Overview

This guide will help you deploy the complete Smart City Platform with all 4 communication protocols (REST, SOAP, gRPC, GraphQL).

## ⚡ Quick Start (3 Steps)

### Option 1: Automated Script (Recommended)

```bash
# 1. Make the script executable
chmod +x quick-start.sh

# 2. Run it
./quick-start.sh

# 3. Access the application
open http://localhost:3000
```

### Option 2: Manual Deployment

```bash
# 1. Build all services
docker-compose build

# 2. Start all services
docker-compose up -d

# 3. Wait 2-3 minutes and access
open http://localhost:3000
```

## 🔍 Detailed Setup Process

### Step 1: Prerequisites Check

```bash
# Check Docker
docker --version
# Expected: Docker version 20.10 or higher

# Check Docker Compose
docker-compose --version
# Expected: docker-compose version 2.0 or higher

# Check available ports
lsof -i :3000,8080,8081,8082,8083,8084,8085,9090
# Expected: No output (ports are free)
```

### Step 2: Clone and Prepare

```bash
# Navigate to project directory
cd smart-city-platform

# Verify structure
ls -la
# Should see: api-gateway, mobility-service, air-quality-service, etc.
```

### Step 3: Build Services

```bash
# Clean previous deployments
docker-compose down -v

# Build all services (10-15 minutes first time)
docker-compose build --no-cache

# Expected output: Successfully built messages for each service
```

### Step 4: Start Services

```bash
# Start in detached mode
docker-compose up -d

# Check status
docker-compose ps
# All services should show "Up"
```

### Step 5: Verify Health

Wait 2-3 minutes after starting, then:

```bash
# API Gateway
curl http://localhost:8080/actuator/health
# Expected: {"status":"UP"}

# Mobility
curl http://localhost:8081/mobility/actuator/health

# Air Quality
curl http://localhost:8082/airquality/actuator/health

# Emergency
curl http://localhost:8083/api/emergencies/health

# Events
curl http://localhost:8084/actuator/health

# Orchestration
curl http://localhost:8085/orchestration/health
```

### Step 6: Test Functionality

```bash
# Test via API Gateway
curl http://localhost:8080/api/mobility/api/transport-lines

# Should return JSON with transport lines
```

## 🎯 Access Points

### Main Application

```
http://localhost:3000
```

### Service Endpoints

| Service       | Direct URL                       | Via Gateway                             |
| ------------- | -------------------------------- | --------------------------------------- |
| Mobility      | http://localhost:8081/mobility   | http://localhost:8080/api/mobility      |
| Air Quality   | http://localhost:8082/airquality | http://localhost:8080/api/air-quality   |
| Emergency     | http://localhost:8083            | http://localhost:8080/api/emergency     |
| Events        | http://localhost:8084            | http://localhost:8080/api/events        |
| Orchestration | http://localhost:8085            | http://localhost:8080/api/orchestration |

## 🧪 Testing Each Service

### 1. Mobility Service (REST)

```bash
# Get all transport lines
curl http://localhost:8080/api/mobility/api/transport-lines | jq

# Get specific line
curl http://localhost:8080/api/mobility/api/transport-lines/number/BUS-101 | jq
```

### 2. Air Quality Service (SOAP)

```bash
# Via SOAP (requires SOAP client or curl with XML)
curl -X POST http://localhost:8082/airquality/ws \
  -H "Content-Type: text/xml" \
  -d '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
   <soapenv:Body>
      <air:GetAllZonesRequest/>
   </soapenv:Body>
</soapenv:Envelope>'
```

### 3. Emergency Service (gRPC via REST)

```bash
# List emergencies
curl http://localhost:8080/api/emergency | jq

# Create emergency
curl -X POST http://localhost:8080/api/emergency \
  -H "Content-Type: application/json" \
  -d '{
    "reporterId": "user123",
    "emergencyType": "FIRE",
    "severityLevel": "HIGH",
    "location": "Downtown",
    "latitude": 48.8566,
    "longitude": 2.3522,
    "description": "Building fire",
    "affectedPeople": 10,
    "tags": ["fire", "urgent"]
  }' | jq
```

### 4. Events Service (GraphQL)

```bash
# Via GraphiQL: http://localhost:8084/graphiql

# Or via curl
curl -X POST http://localhost:8080/api/events/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ getAllEvents { id title location startDateTime } }"}' | jq
```

### 5. Orchestration Service

```bash
# Plan a journey
curl -X POST "http://localhost:8080/api/orchestration/plan-journey?startLocation=Downtown&endLocation=North" | jq
```

## 🔧 Common Commands

### View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f mobility-service

# Last 100 lines
docker-compose logs --tail=100 api-gateway
```

### Service Management

```bash
# Check status
docker-compose ps

# Restart a service
docker-compose restart mobility-service

# Rebuild a service
docker-compose up -d --build mobility-service

# Stop all
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

## ❗ Troubleshooting

### Services not starting

```bash
# Check logs
docker-compose logs <service-name>

# Rebuild from scratch
docker-compose down -v
docker-compose build --no-cache
docker-compose up -d
```

### Port conflicts

```bash
# Find process using port
lsof -i :8080

# Kill process
kill -9 <PID>
```

### Services can't communicate

```bash
# Check Docker network
docker network ls
docker network inspect smart-city-network

# Recreate network
docker-compose down
docker network prune
docker-compose up -d
```

### Client web not loading

```bash
# Check if gateway is accessible
curl http://localhost:8080/actuator/health

# Check client logs
docker-compose logs client-web

# Rebuild client
docker-compose up -d --build client-web
```

## 📊 Monitoring

### Health Checks

```bash
# Gateway routes
curl http://localhost:8080/actuator/gateway/routes | jq

# Service health
curl http://localhost:8080/actuator/health | jq
```

### Resource Usage

```bash
# Real-time stats
docker stats

# Disk usage
docker system df
```

## 🧹 Cleanup

### Stop Everything

```bash
docker-compose down
```

### Complete Cleanup

```bash
# Remove everything including volumes and images
docker-compose down -v --rmi all

# Clean Docker system
docker system prune -a --volumes
```

## 📖 Next Steps

1. **Explore the Web Interface**: http://localhost:3000
2. **Read Service Documentation**: See README files in each service directory
3. **Try API Calls**: Use the examples above
4. **Customize**: Modify configuration in `docker-compose.yml` and service configs

## 🆘 Need Help?

1. Check logs: `docker-compose logs -f`
2. Verify health checks
3. Review [DEPLOYMENT.md](./DEPLOYMENT.md) for advanced troubleshooting
4. Ensure all prerequisites are met
5. Try a complete rebuild: `docker-compose down -v && docker-compose up --build -d`

## ✅ Success Checklist

- [ ] All prerequisites installed
- [ ] Ports available
- [ ] Build completed successfully
- [ ] All services show "Up" status
- [ ] Health checks pass
- [ ] Web client accessible at http://localhost:3000
- [ ] API calls return expected data

## 🎓 Learning Resources

- **REST**: Mobility Service uses standard REST principles
- **SOAP**: Air Quality Service demonstrates WSDL and SOAP operations
- **gRPC**: Emergency Service shows Protocol Buffers and high-performance RPC
- **GraphQL**: Events Service demonstrates flexible querying

Enjoy exploring the Smart City Platform! 🏙️
