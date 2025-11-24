# Smart City Platform - Plateforme Ville Intelligente

## 📋 Description

Plateforme microservices pour la gestion de services urbains intelligents utilisant 4 protocoles de communication différents :
- **REST** (Mobilité)
- **SOAP** (Qualité de l'Air)
- **gRPC** (Urgences)
- **GraphQL** (Événements Urbains)

## 🏗️ Architecture

```
┌─────────────────┐
│   Client Web    │ (Port 3000)
│     (React)     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   API Gateway   │ (Port 8080)
└────────┬────────┘
         │
    ┌────┴────┬────────┬──────────┬────────────┐
    ▼         ▼        ▼          ▼            ▼
┌────────┐┌────────┐┌────────┐┌──────────┐┌──────────┐
│Mobility││AirQual.││Emerg.  ││Events    ││Orchestra.│
│(8081)  ││(8082)  ││(8083)  ││(8084)    ││(8085)    │
│REST    ││SOAP    ││gRPC    ││GraphQL   ││REST      │
└────────┘└────────┘└────────┘└──────────┘└──────────┘
```

## 🚀 Installation et Démarrage Rapide

### Prérequis

- **Docker** >= 20.10
- **Docker Compose** >= 2.0
- **Java** 17 (pour développement local)
- **Maven** 3.9+ (pour développement local)
- **Node.js** 18+ (pour développement client)

### Démarrage avec Docker Compose

1. **Cloner le projet**
```bash
git clone <repository-url>
cd smart-city-platform
```

2. **Construire et démarrer tous les services**
```bash
docker-compose up --build -d
```

3. **Attendre que tous les services soient prêts** (environ 2-3 minutes)
```bash
docker-compose ps
```

4. **Vérifier l'état des services**
```bash
# Santé de l'API Gateway
curl http://localhost:8080/actuator/health

# Santé des services individuels
curl http://localhost:8081/mobility/actuator/health
curl http://localhost:8082/airquality/actuator/health
curl http://localhost:8083/api/emergencies/health
curl http://localhost:8084/actuator/health
curl http://localhost:8085/orchestration/health
```

## 🌐 URLs d'Accès

### Client et Gateway
- **Client Web**: http://localhost:3000
- **API Gateway**: http://localhost:8080
- **Gateway Health**: http://localhost:8080/actuator/health
- **Gateway Routes**: http://localhost:8080/actuator/gateway/routes

### Services
- **Mobilité** (REST): http://localhost:8081/mobility
- **Qualité d'Air** (SOAP): http://localhost:8082/airquality
- **Urgences** (gRPC/REST): http://localhost:8083
- **Événements** (GraphQL): http://localhost:8084
- **Orchestration**: http://localhost:8085

### Documentation & Outils
- **GraphiQL** (Events): http://localhost:8084/graphiql
- **WSDL** (Air Quality): http://localhost:8082/airquality/ws/airquality.wsdl
- **H2 Console** (Mobility): http://localhost:8081/mobility/h2-console
- **Swagger UI** (Mobility): http://localhost:8081/mobility/swagger-ui.html

## 🧪 Tests des Services

### Via le Client Web
Accédez à http://localhost:3000 et naviguez entre les différents services.

### Via API Gateway (recommandé)

**1. Mobilité (REST)**
```bash
# Liste des lignes de transport
curl http://localhost:8080/api/mobility/api/transport-lines

# Horaires d'une ligne
curl http://localhost:8080/api/mobility/api/schedules/line/BUS-101
```

**2. Qualité d'Air (SOAP via REST)**
```bash
# Consulter la qualité d'air
curl "http://localhost:8080/api/air-quality/ws"
```

**3. Urgences (gRPC via REST)**
```bash
# Liste des urgences
curl http://localhost:8080/api/emergency

# Créer une urgence
curl -X POST http://localhost:8080/api/emergency \
  -H "Content-Type: application/json" \
  -d '{
    "reporterId": "user123",
    "emergencyType": "ACCIDENT",
    "severityLevel": "HIGH",
    "location": "Centre-ville",
    "latitude": 48.8566,
    "longitude": 2.3522,
    "description": "Accident de voiture",
    "affectedPeople": 2,
    "tags": ["accident", "urgent"]
  }'
```

**4. Événements (GraphQL)**
```bash
# Via GraphiQL: http://localhost:8084/graphiql
# Ou via curl
curl -X POST http://localhost:8080/api/events/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ getAllEvents { id title location startDateTime } }"}'
```

**5. Orchestration**
```bash
# Planifier un trajet
curl -X POST "http://localhost:8080/api/orchestration/plan-journey?startLocation=Centre-ville&endLocation=Quartier%20Nord"
```

## 🛠️ Développement

### Structure du Projet
```
smart-city-platform/
├── api-gateway/           # API Gateway (Spring Cloud Gateway)
├── mobility-service/      # Service REST
├── air-quality-service/   # Service SOAP
├── emergency-service/     # Service gRPC
├── urban-events-service/  # Service GraphQL
├── orchestration-service/ # Service d'orchestration
├── client-web/           # Client React
├── docker-compose.yml    # Configuration Docker
└── README.md            # Ce fichier
```

### Développement Local (sans Docker)

**1. Démarrer les services backend (dans cet ordre)**
```bash
# Terminal 1 - Mobility Service
cd mobility-service
mvn spring-boot:run

# Terminal 2 - Air Quality Service
cd air-quality-service
mvn spring-boot:run

# Terminal 3 - Emergency Service
cd emergency-service
mvn spring-boot:run

# Terminal 4 - Urban Events Service
cd urban-events-service
mvn spring-boot:run

# Terminal 5 - Orchestration Service
cd orchestration-service
mvn spring-boot:run

# Terminal 6 - API Gateway
cd api-gateway
mvn spring-boot:run
```

**2. Démarrer le client web**
```bash
cd client-web
npm install
npm run dev
```

### Recompiler un Service Spécifique

```bash
# Arrêter le service
docker-compose stop <service-name>

# Reconstruire et redémarrer
docker-compose up -d --build <service-name>

# Exemple pour mobility-service
docker-compose stop mobility-service
docker-compose up -d --build mobility-service
```

## 🐛 Dépannage

### Les services ne démarrent pas

1. Vérifier Docker et Docker Compose
```bash
docker --version
docker-compose --version
```

2. Nettoyer l'environnement Docker
```bash
docker-compose down -v
docker system prune -a
docker-compose up --build -d
```

### Le client web ne se connecte pas aux services

1. Vérifier que l'API Gateway est accessible
```bash
curl http://localhost:8080/actuator/health
```

2. Vérifier les logs de l'API Gateway
```bash
docker-compose logs api-gateway
```

### Un service spécifique ne répond pas

```bash
# Vérifier les logs
docker-compose logs <service-name>

# Exemple
docker-compose logs mobility-service

# Redémarrer le service
docker-compose restart <service-name>
```

### Erreurs de build Maven

```bash
# Nettoyer le cache Maven local
cd <service-directory>
mvn clean install -U

# Forcer la recompilation
docker-compose build --no-cache <service-name>
```

## 📊 Monitoring

### Logs en temps réel
```bash
# Tous les services
docker-compose logs -f

# Service spécifique
docker-compose logs -f mobility-service
```

### État des conteneurs
```bash
docker-compose ps
```

### Statistiques des ressources
```bash
docker stats
```

## 🛑 Arrêt et Nettoyage

### Arrêter tous les services
```bash
docker-compose down
```

### Arrêter et supprimer les volumes
```bash
docker-compose down -v
```

### Nettoyage complet
```bash
docker-compose down -v
docker system prune -a
```

## 📝 Notes Importantes

1. **Ordre de démarrage**: L'API Gateway attend que tous les services soient prêts
2. **Ports**: Assurez-vous que les ports 3000, 8080-8085, 9090 sont disponibles
3. **Mémoire**: Recommandé minimum 4GB RAM pour Docker
4. **Temps de démarrage**: Première construction peut prendre 5-10 minutes
5. **Healthchecks**: Les services ont des healthchecks automatiques

## 🔧 Configuration

### Variables d'Environnement

Modifiable dans `docker-compose.yml`:
- `SPRING_PROFILES_ACTIVE=docker`
- Ports des services
- Configuration réseau

### Personnalisation

Voir les fichiers README.md individuels dans chaque service pour plus de détails.

## 📚 Documentation des Services

- [API Gateway](./api-gateway/README.md)
- [Mobility Service](./mobility-service/README.md)
- [Air Quality Service](./air-quality-service/README.md)
- [Emergency Service](./emergency-service/README.md)
- [Urban Events Service](./urban-events-service/README.md)
- [Orchestration Service](./orchestration-service/README.md)
- [Client Web](./client-web/README.md)

## 🤝 Support

Pour tout problème:
1. Vérifier les logs: `docker-compose logs <service>`
2. Vérifier la section Dépannage ci-dessus
3. Reconstruire: `docker-compose up --build -d`

## 📄 Licence

Ce projet est un exemple éducatif pour démontrer l'interopérabilité des microservices avec différents protocoles de communication.