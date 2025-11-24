# Emergency Service - Service gRPC

## Description
Service haute performance pour la gestion des urgences en temps réel.

## Technologie
- **Protocole**: gRPC (avec adaptateur REST)
- **Framework**: Spring Boot + gRPC Java
- **Base de données**: H2 (en mémoire)
- **Ports**: 8083 (REST), 9090 (gRPC)

## Endpoints REST (Adaptateur)

### Liste des urgences
```bash
GET /api/emergencies
```

### Créer une urgence
```bash
POST /api/emergencies
Content-Type: application/json

{
  "reporterId": "user123",
  "emergencyType": "ACCIDENT",
  "severityLevel": "HIGH",
  "location": "Centre-ville",
  "latitude": 48.8566,
  "longitude": 2.3522,
  "description": "Accident de voiture avec blessés",
  "affectedPeople": 3,
  "tags": ["accident", "urgent"]
}
```

### Statistiques
```bash
GET /api/emergencies/stats?hoursBack=24
```

## Types d'Urgence
- ACCIDENT
- FIRE
- MEDICAL
- SECURITY
- NATURAL_DISASTER
- TECHNICAL

## Niveaux de Sévérité
- LOW
- MEDIUM
- HIGH
- CRITICAL

## Tests

### Via curl
```bash
# Liste
curl http://localhost:8083/api/emergencies

# Créer
curl -X POST http://localhost:8083/api/emergencies \
  -H "Content-Type: application/json" \
  -d '{
    "reporterId": "user123",
    "emergencyType": "FIRE",
    "severityLevel": "CRITICAL",
    "location": "Immeuble A",
    "latitude": 48.86,
    "longitude": 2.35,
    "description": "Incendie",
    "affectedPeople": 15,
    "tags": ["fire", "evacuation"]
  }'
```

### Via API Gateway
```bash
curl http://localhost:8080/api/emergency
```

## gRPC Proto
Fichier: `src/main/proto/emergency.proto`

Service: `EmergencyService`
- CreateEmergencyAlert
- GetEmergencyAlert
- UpdateEmergencyStatus
- ListActiveEmergencies
- StreamEmergencies (bidirectionnel)
- AssignResources
- GetEmergencyStats

## Données de Test
- 3 urgences de test (accident, incendie, urgence médicale)
- Ressources assignées (ambulances, pompiers, police)

## H2 Console
- URL: http://localhost:8083/h2-console
- JDBC URL: `jdbc:h2:mem:emergencydb`
- User: `sa`
- Password: `password`