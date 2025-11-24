# API Gateway

## Description
Point d'entrée unique pour tous les services de la plateforme.

## Technologie
- **Framework**: Spring Cloud Gateway
- **Port**: 8080

## Routes

### Mobility Service
```
/api/mobility/** → http://mobility-service:8081/mobility/**
```

### Air Quality Service
```
/api/air-quality/** → http://air-quality-service:8082/airquality/**
```

### Emergency Service
```
/api/emergency/** → http://emergency-service:8083/api/emergencies/**
```

### Urban Events Service
```
/api/events/** → http://urban-events-service:8084/**
```

### Orchestration Service
```
/api/orchestration/** → http://orchestration-service:8085/orchestration/**
```

## CORS Configuration

Autorise les requêtes depuis:
- http://localhost:3000 (Client Web)
- Tous headers
- Méthodes: GET, POST, PUT, DELETE, OPTIONS

## Circuit Breaker

Chaque route a un circuit breaker pour la résilience:
- Timeout automatique
- Fallback sur erreur
- Protection contre les cascades d'échecs

## Monitoring

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Liste des Routes
```bash
curl http://localhost:8080/actuator/gateway/routes
```

## Tests

### Vérifier une route
```bash
# Via Gateway
curl http://localhost:8080/api/mobility/api/transport-lines

# Direct (comparaison)
curl http://localhost:8081/mobility/api/transport-lines
```

## Configuration

Fichier: `src/main/resources/application.yml`

Personnaliser:
- URLs des services
- CORS origins
- Circuit breaker settings
- Timeouts