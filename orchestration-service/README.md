# Orchestration Service

## Description
Service d'orchestration coordonnant plusieurs services pour des workflows complexes.

## Technologie
- **Protocole**: REST
- **Framework**: Spring Boot + WebFlux
- **Port**: 8085

## Endpoints

### Planifier un Trajet
Combine les données de qualité d'air et de mobilité.

```bash
POST /orchestration/plan-journey?startLocation=Centre-ville&endLocation=Quartier%20Nord
```

**Response**:
```json
{
  "startLocation": "Centre-ville",
  "endLocation": "Quartier Nord",
  "airQualityGood": true,
  "recommendation": "Qualité d'air bonne. Tous modes recommandés.",
  "transportOptions": [
    {
      "type": "Bus",
      "line": "Ligne 72",
      "schedule": "10min",
      "duration": 15,
      "status": "Actif"
    }
  ]
}
```

### Health Check
```bash
GET /orchestration/health
```

## Workflow

1. **Vérification qualité d'air** → Service SOAP Air Quality
2. **Analyse recommandations** → Logique interne
3. **Recherche transports** → Service REST Mobility
4. **Génération plan optimal** → Réponse consolidée

## Tests

### Via curl
```bash
curl -X POST "http://localhost:8085/orchestration/plan-journey?startLocation=Centre&endLocation=Nord"
```

### Via API Gateway
```bash
curl -X POST "http://localhost:8080/api/orchestration/plan-journey?startLocation=Centre&endLocation=Nord"
```

## Communication Inter-Services

Le service utilise WebClient pour communiquer avec:
- Mobility Service (REST)
- Air Quality Service (SOAP)

Actuellement en mode simplifié avec données simulées pour garantir la stabilité.