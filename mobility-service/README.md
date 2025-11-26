# Mobility Service - Service de Mobilité REST

## Description
Service REST pour la gestion des transports publics urbains (bus, métro, train).

## Technologie
- **Protocole**: REST API
- **Framework**: Spring Boot 3.2.0
- **Base de données**: H2 (en mémoire)
- **Port**: 8081

## Endpoints Principaux

### Transport Lines
```bash
# Toutes les lignes
GET /mobility/api/transport-lines

# Ligne spécifique
GET /mobility/api/transport-lines/number/{lineNumber}

# Par type (BUS, METRO, TRAIN)
GET /mobility/api/transport-lines/type/{type}

# Par station
GET /mobility/api/transport-lines/station/{station}
```

### Schedules
```bash
# Horaires d'une ligne
GET /mobility/api/schedules/line/{lineNumber}

# Horaires d'une station
GET /mobility/api/schedules/station/{station}?dayType=WEEKDAY
```

### Traffic Info
```bash
# Incidents actifs
GET /mobility/api/traffic-info/active

# Par sévérité
GET /mobility/api/traffic-info/severity/{HIGH|MEDIUM|LOW}
```

## Tests

### Via curl
```bash
# Liste des lignes
curl http://localhost:8081/mobility/api/transport-lines

# Ligne BUS-101
curl http://localhost:8081/mobility/api/transport-lines/number/BUS-101
```

### Via API Gateway
```bash
curl http://localhost:8080/api/mobility/transport-lines
```

## Données de Test
- 5 lignes de transport (BUS-101, BUS-202, METRO-RED, METRO-BLUE, TRAIN-EX1)
- Horaires pour chaque ligne
- Incidents de trafic simulés

## Swagger UI
http://localhost:8081/mobility/swagger-ui.html

## H2 Console
- URL: http://localhost:8081/mobility/h2-console
- JDBC URL: `jdbc:h2:mem:mobilitydb`
- User: `sa`
- Password: `password`