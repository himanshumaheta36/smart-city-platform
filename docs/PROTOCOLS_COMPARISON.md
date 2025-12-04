# 📊 Comparaison des Protocoles Implémentés

## Vue d'Ensemble

| Critère | REST | SOAP | gRPC | GraphQL |
|---------|------|------|------|---------|
| **Format** | JSON | XML | Protobuf | JSON |
| **Transport** | HTTP | HTTP | HTTP/2 | HTTP |
| **Style** | Resource | RPC | RPC | Query |
| **Typage** | Faible | Fort | Fort | Fort |
| **Performance** | ⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| **Simplicité** | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ |
| **Flexibilité** | ⭐⭐⭐ | ⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐⭐ |

---

## REST (Representational State Transfer)

### ✅ Avantages

- **Simplicité** : Facile à comprendre et implémenter
- **Standards** : Basé sur HTTP, largement supporté
- **Cache** : Support natif du cache HTTP
- **Sans état** : Chaque requête est indépendante
- **Lisibilité** : Format JSON lisible par humain
- **Documentation** : OpenAPI/Swagger bien établi

### ❌ Inconvénients

- **Over-fetching** : Récupération de données inutiles
- **Under-fetching** : Nécessité de multiples requêtes
- **Versioning** : Gestion de versions complexe
- **Performance** : Plus lent que gRPC pour données binaires

### 🎯 Cas d'usage dans notre projet

**Service Mobilité** (Port 3001) :
- Opérations CRUD sur les lignes de transport
- Consultation des horaires
- État du trafic
- Correspondances entre lignes

**Pourquoi REST ici ?**
- Données structurées simples
- Besoin de cache (horaires)
- Clients divers (web, mobile, etc.)
- API publique facilement explorable

### 💻 Exemple de Requête/Réponse

```bash
# Requête via API Gateway
GET http://localhost:8080/api/mobility/transport-lines

# Requête directe
GET http://localhost:8081/mobility/api/transport-lines

# Réponse
{
  "success": true,
  "data": [
    {
      "id": "BUS-101",
      "name": "Bus Central",
      "type": "BUS",
      "schedule": ["06:00", "06:15", "06:30"],
      "status": "normal"
    }
  ]
}
```

**Endpoints disponibles** :
- `GET /api/transport-lines` - Liste des lignes
- `GET /api/transport-lines/number/:number` - Ligne par numéro
- `GET /api/transport-lines/type/:type` - Lignes par type
- `GET /api/schedules/line/:lineNumber` - Horaires d'une ligne
- `GET /api/traffic-info/active` - Informations trafic actif

**Documentation** : Swagger disponible à `/swagger-ui.html`

---

## SOAP (Simple Object Access Protocol)

### ✅ Avantages

- **Contrat strict** : WSDL définit précisément le contrat
- **Sécurité** : WS-Security pour authentification et encryption
- **Transactions** : Support ACID
- **Standards** : WS-* stack complet
- **Enterprise** : Largement utilisé dans les entreprises
- **Fiabilité** : Gestion d'erreurs robuste

### ❌ Inconvénients

- **Complexité** : Verbose, difficile à débugger
- **Performance** : XML lourd à parser
- **Rigidité** : Modifications difficiles
- **Tooling** : Requiert des outils spécialisés

### 🎯 Cas d'usage dans notre projet

**Service Qualité de l'Air** (Port 3002) :
- Données environnementales critiques
- Consultation AQI (Air Quality Index) par zone
- Détails des polluants (PM2.5, PM10, NO2, CO2, O3)
- Comparaison entre zones
- Historique des mesures

**Pourquoi SOAP ici ?**
- Données sensibles (santé publique)
- Intégration avec capteurs/systèmes existants
- Besoin de validation stricte
- Conformité réglementaire
- Contrat WSDL pour clients externes

### 💻 Exemple de Requête/Réponse

```xml
<!-- Endpoint SOAP -->
POST http://localhost:8082/airquality/ws

<!-- WSDL disponible à -->
GET http://localhost:8082/airquality/ws/airquality.wsdl

<!-- Requête -->
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:air="http://smartcity.com/airquality">
   <soapenv:Body>
      <air:GetAirQualityRequest>
         <air:zoneName>Centre-ville</air:zoneName>
      </air:GetAirQualityRequest>
   </soapenv:Body>
</soapenv:Envelope>

<!-- Réponse -->
<soapenv:Envelope>
   <soapenv:Body>
      <tns:GetAirQualityResponse>
         <zone>Centre-ville</zone>
         <aqi>85</aqi>
         <level>Modéré</level>
         <pm25>35.2</pm25>
         <pm10>45.8</pm10>
         <no2>28.5</no2>
         <lastUpdate>2025-12-04T10:30:00Z</lastUpdate>
      </tns:GetAirQualityResponse>
   </soapenv:Body>
</soapenv:Envelope>
```

**Opérations SOAP disponibles** :
- `GetAirQuality` - AQI d'une zone spécifique
- `GetAllZones` - Liste de toutes les zones
- `GetZoneDetails` - Détails complets d'une zone
- `GetPollutants` - Détails des polluants
- `CompareZones` - Comparer deux zones

**Zones de test** : Centre-ville, Quartier Nord, Zone Industrielle, Parc Central, Banlieue Sud

---

## gRPC (gRPC Remote Procedure Call)

### ✅ Avantages

- **Performance** : Format binaire (Protobuf)
- **Streaming** : Bidirectionnel
- **Latence** : HTTP/2, multiplexing
- **Typage fort** : Contrat .proto
- **Multi-language** : Code génération automatique
- **Efficacité** : Payload léger

### ❌ Inconvénients

- **Lisibilité** : Format binaire non lisible
- **Support navigateur** : Limité (gRPC-Web requis)
- **Debug** : Plus difficile
- **Firewall** : Peut être bloqué

### 🎯 Cas d'usage dans notre projet

**Service Urgences** (Ports 50051 gRPC, 3003 HTTP) :
- Création et gestion d'alertes d'urgence
- Suivi en temps réel
- Gestion des ressources (ambulances, pompiers, police)
- Stream d'alertes en temps réel
- Temps de réponse optimisé

**Pourquoi gRPC ici ?**
- Performance critique (vies en jeu)
- Communication bidirectionnelle (streaming)
- Latence minimale requise
- Communication serveur-serveur efficace
- Protocole moderne pour temps réel

### 💻 Exemple de .proto et Requête

```protobuf
// emergency.proto
syntax = "proto3";

package emergency;

message AlertRequest {
  string reporter_id = 1;
  EmergencyType type = 2;
  SeverityLevel severity = 3;
  string description = 4;
  Location location = 5;
  int32 affected_people = 6;
  repeated string tags = 7;
}

message AlertResponse {
  string alert_id = 1;
  AlertStatus status = 2;
  int32 estimated_response_time = 3;
  repeated string assigned_units = 4;
}

enum EmergencyType {
  FIRE = 0;
  ACCIDENT = 1;
  MEDICAL = 2;
  CRIME = 3;
  NATURAL_DISASTER = 4;
}

enum SeverityLevel {
  LOW = 0;
  MEDIUM = 1;
  HIGH = 2;
  CRITICAL = 3;
}

service EmergencyService {
  rpc CreateAlert (AlertRequest) returns (AlertResponse);
  rpc GetAlertStatus (AlertStatusRequest) returns (AlertStatusResponse);
  rpc UpdateAlertStatus (UpdateAlertRequest) returns (UpdateAlertResponse);
  rpc ListActiveAlerts (ListAlertsRequest) returns (ListAlertsResponse);
  rpc GetAvailableResources (ResourceRequest) returns (ResourceResponse);
  rpc StreamAlerts (stream AlertRequest) returns (stream AlertResponse);
}
```

```bash
# Appel REST (wrapper HTTP)
POST http://localhost:8083/api/emergencies
Content-Type: application/json

{
  "reporterId": "user123",
  "emergencyType": "FIRE",
  "severityLevel": "HIGH",
  "location": "Downtown",
  "latitude": 48.8566,
  "longitude": 2.3522,
  "description": "Building fire",
  "affectedPeople": 10,
  "tags": ["fire", "urgent"]
}

# Via API Gateway
POST http://localhost:8080/api/emergency
```

**Méthodes RPC disponibles** :
- `CreateAlert` - Créer une alerte d'urgence
- `GetAlertStatus` - Obtenir le statut d'une alerte
- `UpdateAlertStatus` - Mettre à jour le statut
- `ListActiveAlerts` - Lister les alertes actives
- `GetAvailableResources` - Ressources disponibles
- `StreamAlerts` - Stream temps réel des alertes

**Health Check** : `GET http://localhost:8083/api/emergencies/health`

---

## GraphQL

### ✅ Avantages

- **Flexibilité** : Client spécifie exactement ce qu'il veut
- **Un seul endpoint** : /graphql pour tout
- **Pas d'over-fetching** : Données précises
- **Typage** : Schema Definition Language (SDL)
- **Introspection** : Découverte du schema
- **Real-time** : Subscriptions

### ❌ Inconvénients

- **Complexité serveur** : Résolution de queries
- **Cache** : Plus difficile qu'avec REST
- **Courbe d'apprentissage** : Nouveau paradigme
- **Performance** : Queries complexes coûteuses

### 🎯 Cas d'usage dans notre projet

**Service Événements Urbains** (Port 3004) :
- Gestion des événements urbains
- Calendrier d'événements
- Catégories variées (festivals, conférences, sports)
- Inscriptions aux événements
- Recherche flexible

**Pourquoi GraphQL ici ?**
- Éviter multiples endpoints REST
- Flexibilité pour clients différents
- Réduire le nombre de requêtes
- Exploration de données facilitée
- Requêtes personnalisées selon les besoins

### 💻 Exemple de Query

```graphql
# Endpoint GraphQL
POST http://localhost:8084/graphql
# GraphiQL disponible à: http://localhost:8084/graphiql

# Query - Récupérer tous les événements
query {
  getAllEvents {
    id
    title
    location
    startDateTime
    endDateTime
    category
    availableSpots
    registeredCount
  }
}

# Query - Événement spécifique
query {
  getEvent(id: "1") {
    title
    description
    location
    startDateTime
    category
    availableSpots
  }
}

# Query - Événements par catégorie
query {
  getEventsByCategory(category: "FESTIVAL") {
    id
    title
    startDateTime
    availableSpots
  }
}

# Mutation - Créer un événement
mutation {
  createEvent(input: {
    title: "Festival de Musique"
    description: "Concert en plein air"
    location: "Parc Central"
    startDateTime: "2025-07-15T18:00:00Z"
    endDateTime: "2025-07-15T23:00:00Z"
    category: FESTIVAL
    capacity: 5000
  }) {
    id
    title
    registeredCount
  }
}

# Réponse
{
  "data": {
    "getAllEvents": [
      {
        "id": "1",
        "title": "Festival de Musique d'Été",
        "location": "Parc Central",
        "startDateTime": "2025-07-15T18:00:00Z",
        "endDateTime": "2025-07-15T23:00:00Z",
        "category": "FESTIVAL",
        "availableSpots": 4950,
        "registeredCount": 50
      }
    ]
  }
}
```

**Queries disponibles** :
- `getAllEvents` - Tous les événements
- `getEvent(id)` - Événement spécifique
- `getEventsByCategory(category)` - Par catégorie
- `getUpcomingEvents` - Événements à venir
- `searchEvents(query)` - Recherche

**Mutations disponibles** :
- `createEvent` - Créer un événement
- `updateEvent` - Modifier un événement
- `deleteEvent` - Supprimer un événement
- `registerForEvent` - S'inscrire à un événement

**Catégories** : FESTIVAL, CONFERENCE, SPORT, CULTURAL, COMMUNITY, WORKSHOP

---

## Comparaison Détaillée

### Performance

| Protocole | Taille Payload | Temps de Parse | Latence | Bande Passante |
|-----------|----------------|----------------|---------|----------------|
| REST      | JSON (~1KB)    | 5-10ms        | 20-50ms | Moyenne        |
| SOAP      | XML (~2KB)     | 10-20ms       | 30-80ms | Élevée         |
| gRPC      | Protobuf (400B)| 1-3ms         | 5-20ms  | Faible         |
| GraphQL   | JSON (~800B)   | 5-12ms        | 15-60ms | Variable       |

### Scalabilité

```
gRPC > GraphQL > REST > SOAP
```

**gRPC** :
- HTTP/2 multiplexing
- Streaming bidirectionnel
- Protobuf compact

**GraphQL** :
- Batching des requêtes
- Caching avec DataLoader
- Une seule connexion

**REST** :
- Scaling horizontal simple
- Cache HTTP standard
- Load balancing facile

**SOAP** :
- Overhead XML important
- Pas de cache natif
- State management complexe

### Sécurité

| Protocole | Auth | Encryption | Standards |
|-----------|------|------------|-----------|
| REST      | JWT, OAuth | HTTPS | Oui |
| SOAP      | WS-Security | SSL/TLS | Oui |
| gRPC      | SSL/TLS, Token | Oui | Oui |
| GraphQL   | JWT, OAuth | HTTPS | Oui |

### Ecosystème et Tooling

**REST** :
- ✅ Postman, Insomnia, curl
- ✅ OpenAPI/Swagger
- ✅ Support universel
- ✅ H2 Console : `http://localhost:8081/mobility/h2-console`

**SOAP** :
- ✅ SoapUI, curl avec XML
- ✅ WSDL : `http://localhost:8082/airquality/ws/airquality.wsdl`
- ⚠️ Tooling lourd

**gRPC** :
- ✅ grpcurl, BloomRPC
- ✅ .proto files
- ⚠️ Browser limited
- ✅ REST wrapper disponible

**GraphQL** :
- ✅ GraphiQL : `http://localhost:8084/graphiql`
- ✅ Playground intégré
- ✅ Excellent DX (Developer Experience)

---

## Quand Utiliser Chaque Protocole ?

### Utilisez REST quand :
- ✅ Vous construisez une API publique
- ✅ Simplicité et standards sont prioritaires
- ✅ Opérations CRUD sur des ressources
- ✅ Cache HTTP est important
- ✅ Clients divers (web, mobile, IoT)
- **Exemple** : Service Mobilité - transport public

### Utilisez SOAP quand :
- ✅ Intégration avec systèmes legacy
- ✅ Contrat strict requis (WSDL)
- ✅ Transactions ACID nécessaires
- ✅ Standards WS-* requis (WS-Security, etc.)
- ✅ Environnement entreprise
- **Exemple** : Service Qualité Air - données environnementales

### Utilisez gRPC quand :
- ✅ Performance est critique
- ✅ Communication serveur-serveur
- ✅ Streaming temps réel nécessaire
- ✅ Microservices internes
- ✅ Latence faible requise
- **Exemple** : Service Urgences - alertes temps réel

### Utilisez GraphQL quand :
- ✅ Clients ont besoins variables
- ✅ Éviter over/under-fetching
- ✅ Données relationnelles complexes
- ✅ Interface unique pour données variées
- ✅ Exploration de données nécessaire
- **Exemple** : Service Événements - requêtes flexibles

---

## Notre Choix Architectural

Dans ce projet, nous avons **délibérément utilisé les 4 protocoles** pour :

1. **Démontrer l'interopérabilité** entre protocoles différents
2. **Choisir le meilleur outil** pour chaque cas d'usage
3. **Simuler un environnement réel** avec systèmes hétérogènes
4. **Apprendre et comparer** les technologies

### Mapping Service ↔ Protocole

| Service | Protocole | Port(s) | Justification |
|---------|-----------|---------|---------------|
| Mobilité | REST | 3001 | API publique, CRUD, cache |
| Qualité Air | SOAP | 3002 | Données critiques, contrat strict |
| Urgences | gRPC | 3003, 50051 | Temps réel, performance, streaming |
| Événements | GraphQL | 3004 | Flexibilité, agrégation, exploration |
| Orchestrateur | REST | 3005 | Coordination de workflows |
| API Gateway | REST | 8080 | Point d'entrée unique |

### Architecture de Communication

```
Client Web (80)
    ↓
API Gateway (8080) - Rate Limiting, CORS, Security
    ↓
Orchestrateur (3005) - Coordination de workflows
    ↓
    ├─→ Mobilité REST (3001)
    ├─→ Qualité Air SOAP (3002)
    ├─→ Urgences gRPC (3003/50051)
    └─→ Événements GraphQL (3004)
```

---

## URLs et Endpoints Principaux

### Client et Gateway
- **Client Web** : `http://localhost:3000`
- **API Gateway** : `http://localhost:8080`
- **Health Check Global** : `http://localhost:8080/actuator/health`

### Services Backend

#### Mobilité (REST)
- **Base** : `http://localhost:8081/mobility`
- **Health** : `http://localhost:8081/mobility/actuator/health`
- **Transport Lines** : `http://localhost:8081/mobility/api/transport-lines`
- **H2 Console** : `http://localhost:8081/mobility/h2-console`
  - JDBC URL: `jdbc:h2:mem:mobilitydb`
  - User: `sa` / Password: `password`

#### Qualité de l'Air (SOAP)
- **Base** : `http://localhost:8082/airquality`
- **Health** : `http://localhost:8082/airquality/actuator/health`
- **SOAP Endpoint** : `http://localhost:8082/airquality/ws`
- **WSDL** : `http://localhost:8082/airquality/ws/airquality.wsdl`
- **H2 Console** : `http://localhost:8082/airquality/h2-console`
  - JDBC URL: `jdbc:h2:mem:airqualitydb`

#### Urgences (gRPC)
- **Base HTTP** : `http://localhost:8083`
- **gRPC Port** : `50051`
- **Health** : `http://localhost:8083/api/emergencies/health`
- **REST API** : `http://localhost:8083/api/emergencies`
- **H2 Console** : `http://localhost:8083/h2-console`
  - JDBC URL: `jdbc:h2:mem:emergencydb`

#### Événements (GraphQL)
- **Base** : `http://localhost:8084`
- **Health** : `http://localhost:8084/actuator/health`
- **GraphQL** : `http://localhost:8084/graphql`
- **GraphiQL** : `http://localhost:8084/graphiql`
- **H2 Console** : `http://localhost:8084/h2-console`
  - JDBC URL: `jdbc:h2:mem:urbaneventsdb`

#### Orchestration
- **Base** : `http://localhost:8085`
- **Health** : `http://localhost:8085/orchestration/health`
- **Plan Journey** : `POST http://localhost:8085/orchestration/plan-journey`

---

## Conclusion

Il n'y a **pas de protocole parfait**. Le choix dépend de :

- 📊 Nature des données
- ⚡ Exigences de performance
- 🔒 Besoins de sécurité
- 👥 Clients cibles
- 🏢 Contraintes legacy
- 📱 Plateformes supportées

Notre projet démontre que **plusieurs protocoles peuvent coexister** dans une même architecture, chacun servant son objectif spécifique, orchestrés intelligemment via une API Gateway (port 8080) et un orchestrateur (port 3005).

**L'avenir tend vers** :
- **gRPC** pour communications internes haute performance
- **GraphQL** pour APIs client-facing flexibles
- **REST** reste un standard solide et universel
- **SOAP** pour legacy et environnements entreprise

**La vraie compétence : savoir quand utiliser chacun ! 🎯**

---

## Commandes de Test Rapides

```bash
# REST - Mobilité
curl http://localhost:8080/api/mobility/transport-lines | jq

# SOAP - Qualité Air (via wrapper)
curl http://localhost:8082/airquality/ws/airquality.wsdl

# gRPC - Urgences (via REST wrapper)
curl http://localhost:8083/api/emergencies | jq

# GraphQL - Événements
curl -X POST http://localhost:8084/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ getAllEvents { id title } }"}' | jq

# Orchestration - Plan Journey
curl -X POST "http://localhost:8085/orchestration/plan-journey?startLocation=Centre&endLocation=Nord" | jq
```