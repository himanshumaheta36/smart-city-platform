# 🔗 URLs Fonctionnelles de la Plateforme

## ✅ Services Principaux

### 🌐 Client Web
- **URL**: http://localhost:3000
- **Status**: ✅ Fonctionnel
- **Description**: Interface utilisateur React

### 🚪 API Gateway
- **URL**: http://localhost:8080
- **Health**: http://localhost:8080/actuator/health
- **Status**: ✅ Fonctionnel
- **Description**: Point d'entrée unique

---

## 🔧 Services Backend

### 🚗 Mobility Service (REST)
**Base URL**: http://localhost:8081/mobility

✅ **Endpoints fonctionnels**:
- Health: http://localhost:8081/mobility/actuator/health
- Lignes de transport: http://localhost:8081/mobility/api/transport-lines
- Ligne spécifique: http://localhost:8081/mobility/api/transport-lines/number/BUS-101
- Par type: http://localhost:8081/mobility/api/transport-lines/type/BUS
- Horaires: http://localhost:8081/mobility/api/schedules/line/BUS-101
- Trafic actif: http://localhost:8081/mobility/api/traffic-info/active
- H2 Console: http://localhost:8081/mobility/h2-console
  - JDBC URL: `jdbc:h2:mem:mobilitydb`
  - User: `sa`
  - Password: `password`

**Via API Gateway**:
- http://localhost:8080/api/mobility/transport-lines

---

### 🌫️ Air Quality Service (SOAP)
**Base URL**: http://localhost:8082/airquality

✅ **Endpoints fonctionnels**:
- Health: http://localhost:8082/airquality/actuator/health
- WSDL: http://localhost:8082/airquality/ws/airquality.wsdl
- SOAP Endpoint: http://localhost:8082/airquality/ws
- H2 Console: http://localhost:8082/airquality/h2-console
  - JDBC URL: `jdbc:h2:mem:airqualitydb`
  - User: `sa`
  - Password: `password`

**Test SOAP**:
```bash
curl -X POST http://localhost:8082/airquality/ws \
  -H "Content-Type: text/xml" \
  -d '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
   <soapenv:Body>
      <air:GetAirQualityRequest>
         <air:zoneName>Centre-ville</air:zoneName>
      </air:GetAirQualityRequest>
   </soapenv:Body>
</soapenv:Envelope>'
```

---

### 🚨 Emergency Service (gRPC + REST)
**Base URL**: http://localhost:8083

✅ **Endpoints fonctionnels**:
- Health: http://localhost:8083/api/emergencies/health
- REST API: http://localhost:8083/api/emergencies
- Créer urgence: POST http://localhost:8083/api/emergencies
- Statistiques: http://localhost:8083/api/emergencies/stats
- gRPC Port: 9090
- H2 Console: http://localhost:8083/h2-console
  - JDBC URL: `jdbc:h2:mem:emergencydb`
  - User: `sa`
  - Password: `password`

**Test REST**:
```bash
curl http://localhost:8083/api/emergencies
```

---

### 🎭 Urban Events Service (GraphQL)
**Base URL**: http://localhost:8084

✅ **Endpoints fonctionnels**:
- Health: http://localhost:8084/actuator/health
- GraphQL: http://localhost:8084/graphql
- GraphiQL: http://localhost:8084/graphiql
- H2 Console: http://localhost:8084/h2-console
  - JDBC URL: `jdbc:h2:mem:urbaneventsdb`
  - User: `sa`
  - Password: `password`

**Test GraphQL**:
```bash
curl -X POST http://localhost:8084/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ getAllEvents { id title location } }"}'
```

**Exemple de requête dans GraphiQL**:
```graphql
query {
  getAllEvents {
    id
    title
    location
    startDateTime
    availableSpots
  }
}
```

---

### 🔄 Orchestration Service
**Base URL**: http://localhost:8085

✅ **Endpoints fonctionnels**:
- Health: http://localhost:8085/orchestration/health
- Planifier trajet: POST http://localhost:8085/orchestration/plan-journey?startLocation=Centre&endLocation=Nord

**Test**:
```bash
curl -X POST "http://localhost:8085/orchestration/plan-journey?startLocation=Centre-ville&endLocation=Quartier%20Nord"
```

---

## 🧪 Tests Rapides

### Via curl
```bash
# 1. Gateway health
curl http://localhost:8080/actuator/health

# 2. Mobility via gateway
curl http://localhost:8080/api/mobility/api/transport-lines

# 3. Air Quality WSDL
curl http://localhost:8082/airquality/ws/airquality.wsdl

# 4. Emergency health
curl http://localhost:8083/api/emergencies/health

# 5. Events GraphQL
curl -X POST http://localhost:8084/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ getAllEvents { id title } }"}'

# 6. Orchestration
curl -X POST "http://localhost:8085/orchestration/plan-journey?startLocation=Centre&endLocation=Nord"
```

---

## 📊 Données de Test

### Mobility Service
- **Lignes**: BUS-101, BUS-202, METRO-RED, METRO-BLUE, TRAIN-EX1
- **Stations**: Central Station, Downtown Mall, City Park, etc.

### Air Quality Service
- **Zones**: Centre-ville, Quartier Nord, Zone Industrielle, Parc Central, Banlieue Sud

### Emergency Service
- **Urgences de test**: 3 alertes pré-créées (accident, incendie, médical)

### Events Service
- **Événements**: 8 événements (festivals, conférences, sports, etc.)

---

## ❌ URLs Non Fonctionnelles (corrigées dans les fichiers)

Ces URLs nécessitent les corrections des fichiers `application.yml` :

- ~~http://localhost:8081/h2-console~~ → Utiliser http://localhost:8081/mobility/h2-console
- ~~http://localhost:8082/h2-console~~ → Utiliser http://localhost:8082/airquality/h2-console
- ~~http://localhost:8083/h2-console~~ → Utiliser http://localhost:8083/h2-console (✅ correct)
- ~~http://localhost:8084/h2-console~~ → Utiliser http://localhost:8084/h2-console (✅ correct)

---

## 🔧 Actions Requises

### 1. Redémarrer les services après corrections
```bash
docker-compose down
docker-compose up --build
```

### 2. Attendre 60 secondes
Les services Spring Boot prennent du temps à démarrer complètement.

### 3. Vérifier les health checks
```bash
# API Gateway
curl http://localhost:8080/actuator/health

# Tous les services
for port in 8081 8082 8083 8084 8085; do
  echo "Checking port $port..."
  curl -s http://localhost:$port/actuator/health || \
  curl -s http://localhost:$port/mobility/actuator/health || \
  curl -s http://localhost:$port/airquality/actuator/health || \
  curl -s http://localhost:$port/api/emergencies/health || \
  curl -s http://localhost:$port/orchestration/health
done
```

---

## 📝 Notes Importantes

1. **Context Path**: Certains services ont un context-path :
   - Mobility: `/mobility`
   - Air Quality: `/airquality`
   - Emergency: Pas de context-path
   - Events: Pas de context-path
   - Orchestration: Pas de context-path (mais préfixe `/orchestration`)

2. **H2 Console**: Accessible uniquement avec le context-path correct

3. **GraphiQL**: Doit être accessible sur http://localhost:8084/graphiql après redémarrage

4. **API Gateway**: Tous les appels via le gateway utilisent le préfixe `/api/`

---

## 🎯 Workflow Recommandé

1. **Développement Local**: Utiliser les URLs directes (8081-8085)
2. **Production/Démo**: Utiliser l'API Gateway (8080)
3. **Debug**: Utiliser les H2 Consoles pour vérifier les données
4. **Tests GraphQL**: Utiliser GraphiQL pour explorer l'API

---

## 📞 Support

Si un service ne répond pas:
1. Vérifier les logs: `docker-compose logs [service-name]`
2. Vérifier le démarrage: `docker-compose ps`
3. Attendre 60s supplémentaires (temps de démarrage)
4. Redémarrer le service: `docker-compose restart [service-name]`