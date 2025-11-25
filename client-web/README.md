# 🏙️ Client Web - Smart City Platform

## 📋 Vue d'ensemble

Client React moderne pour interagir avec la plateforme Smart City qui démontre l'interopérabilité entre 4 protocoles différents :
- **REST** (Mobility Service)
- **SOAP** (Air Quality Service)
- **gRPC** (Emergency Service)
- **GraphQL** (Events Service)

## 🚀 Démarrage rapide

### Prérequis
- Node.js 18+ installé
- Tous les services backend en cours d'exécution
- Port 3000 disponible

### Installation

```bash
cd client-web
npm install
npm run dev
```

Le client sera accessible sur `http://localhost:3000`

## 📁 Structure des fichiers

```
client-web/
├── src/
│   ├── components/
│   │   ├── Dashboard.jsx           # Tableau de bord principal
│   │   ├── MobilityService.jsx     # Service REST
│   │   ├── AirQualityService.jsx   # Service SOAP
│   │   ├── EmergencyService.jsx    # Service gRPC
│   │   ├── EventsService.jsx       # Service GraphQL
│   │   └── JourneyPlanner.jsx      # Orchestration
│   ├── services/
│   │   └── api.js                  # Configuration API centralisée
│   ├── App.jsx                     # Composant principal
│   ├── App.css                     # Styles globaux
│   └── main.jsx                    # Point d'entrée
├── package.json
├── vite.config.js
└── Dockerfile
```

## 🔧 Fichiers modifiés

### 1. **src/services/api.js**

Ce fichier centralise toutes les communications avec l'API Gateway.

**Fonctionnalités :**
- Configuration Axios avec intercepteurs
- Logging automatique des requêtes/réponses
- Gestion des timeouts
- Support de tous les protocoles (REST, SOAP, gRPC, GraphQL)
- Health checks pour tous les services

**Points clés :**
```javascript
const API_BASE_URL = 'http://localhost:8080/api';

// Exemples d'utilisation
mobilityAPI.getTransportLines()
airQualityAPI.getAirQuality('Centre-ville')
emergencyAPI.createAlert(alertData)
eventsAPI.getAllEvents()
orchestrationAPI.planJourney(start, end)
```

### 2. **src/components/Dashboard.jsx** 

**Nouvelles fonctionnalités :**
- ✅ Health check en temps réel de tous les services
- ✅ Cards visuelles pour chaque service avec code couleur
- ✅ Informations d'architecture détaillées
- ✅ Guide de démarrage rapide
- ✅ Liens directs vers la documentation (WSDL, GraphiQL, Swagger)

**Indicateurs de santé :**
- 🟢 UP (Vert) : Service opérationnel
- 🔴 DOWN (Rouge) : Service hors ligne

### 3. **src/components/MobilityService.jsx** 

Service REST pour les transports publics.

**Fonctionnalités :**
- ✅ Système d'onglets (Lignes / Trafic)
- ✅ Liste des lignes avec filtrage par type (BUS, METRO, TRAIN)
- ✅ Affichage des horaires en tableau
- ✅ Info trafic avec niveaux de sévérité
- ✅ Code couleur par type de transport
- ✅ Indicateurs de statut en temps réel
- ✅ Documentation API intégrée avec lien Swagger

**Codes couleur :**
- 🟠 BUS
- 🔵 METRO
- 🟢 TRAIN

### 4. **src/components/AirQualityService.jsx** 

Service SOAP pour la qualité de l'air.

**Fonctionnalités :**
- ✅ 3 onglets : Vérifier / Comparer / Vue d'ensemble
- ✅ Parsing XML des réponses SOAP
- ✅ Affichage de l'indice AQI avec échelle colorée
- ✅ Détails des polluants (PM2.5, PM10, NO₂, O₃, CO, SO₂)
- ✅ Recommandations basées sur la qualité
- ✅ Comparaison de deux zones
- ✅ Vue d'ensemble de toutes les zones
- ✅ Échelle AQI de référence
- ✅ Lien vers le WSDL

**Échelle AQI :**
- 0-50 : 🟢 Bon
- 51-100 : 🟡 Modéré
- 101-150 : 🟠 Sensible
- 151-200 : 🔴 Mauvais
- 201+ : 🟤 Dangereux

### 5. **src/components/EmergencyService.jsx** 

Service gRPC (via REST adapter) pour les urgences.

**Fonctionnalités :**
- ✅ Tableau de bord statistiques (24h)
- ✅ Formulaire de création d'alerte complet
- ✅ Types d'urgence : ACCIDENT, FIRE, MEDICAL, SECURITY, etc.
- ✅ Niveaux de sévérité : LOW, MEDIUM, HIGH, CRITICAL
- ✅ Liste des alertes actives avec détails
- ✅ Indicateurs visuels de statut
- ✅ Explication des avantages gRPC
- ✅ Documentation des endpoints REST

**Types d'urgence :**
- 🚗 ACCIDENT
- 🔥 FIRE
- 🏥 MEDICAL
- 👮 SECURITY
- 🌪️ NATURAL_DISASTER
- ⚡ TECHNICAL

### 6. **src/components/EventsService.jsx** 

Service GraphQL pour les événements urbains.

**Fonctionnalités :**
- ✅ 3 onglets : Tous / Recherche / Filtres
- ✅ Recherche par mots-clés
- ✅ Filtrage par type, catégorie, gratuité
- ✅ Affichage riche des événements avec images
- ✅ Inscription aux événements
- ✅ Affichage des places disponibles
- ✅ Tags et informations détaillées
- ✅ Explication des avantages GraphQL
- ✅ Lien vers GraphiQL Explorer

**Types d'événements :**
- 🎵 CONCERT
- 🎉 FESTIVAL
- ⚽ SPORTS
- 🎤 CONFERENCE
- 🖼️ EXHIBITION
- 🛠️ WORKSHOP
- 👥 COMMUNITY
- 🎭 CULTURAL

### 7. **src/components/JourneyPlanner.jsx** 

Service d'orchestration combinant Air Quality + Mobility.

**Fonctionnalités :**
- ✅ Interface de sélection départ/arrivée
- ✅ Bouton d'inversion des lieux
- ✅ Analyse de la qualité de l'air du trajet
- ✅ Recommandations basées sur la pollution
- ✅ Options de transport multiples
- ✅ Visualisation du workflow d'orchestration
- ✅ Explication de l'interopérabilité

**Workflow :**
1. 🌫️ Vérification Air (SOAP)
2. 🧠 Analyse
3. 🚗 Recherche Transport (REST)
4. ✅ Plan Optimal

### 8. **src/App.css** 

Styles CSS modernes et cohérents.

**Améliorations :**
- ✅ Design system cohérent
- ✅ Variables de couleurs
- ✅ Composants réutilisables
- ✅ Animations fluides
- ✅ Responsive design
- ✅ Scrollbar personnalisée
- ✅ États hover/focus améliorés

## 🎨 Design System

### Couleurs principales
- **Primary**: `#667eea` → `#764ba2` (Gradient violet)
- **Success**: `#10b981` (Vert)
- **Warning**: `#f59e0b` (Orange)
- **Error**: `#ef4444` (Rouge)
- **Info**: `#3b82f6` (Bleu)

### Typographie
- **Font**: Inter, Segoe UI
- **H1**: 2rem (32px), bold
- **H2**: 1.75rem (28px), semibold
- **H3**: 1.375rem (22px), semibold
- **Body**: 1rem (16px), regular

## 🔍 Tests et Vérification

### 1. Vérifier la santé des services

Dans le Dashboard, cliquez sur "🔄 Actualiser" pour vérifier l'état de tous les services.

Tous les services doivent afficher 🟢 UP.

### 2. Tester chaque service

**Mobility (REST) :**
```bash
# Via le client
1. Cliquer sur "🚗 Mobilité"
2. Voir la liste des lignes
3. Cliquer sur une ligne pour voir les horaires
4. Onglet "🚦 Info Trafic" pour les incidents
```

**Air Quality (SOAP) :**
```bash
# Via le client
1. Cliquer sur "🌫️ Qualité d'Air"
2. Onglet "🔍 Vérifier Qualité"
3. Sélectionner "Centre-ville"
4. Voir l'AQI et les polluants
```

**Emergency (gRPC) :**
```bash
# Via le client
1. Cliquer sur "🚨 Urgences"
2. Onglet "🆘 Créer Alerte"
3. Remplir le formulaire
4. Envoyer l'alerte
5. Vérifier dans "📋 Alertes Actives"
```

**Events (GraphQL) :**
```bash
# Via le client
1. Cliquer sur "🎭 Événements"
2. Voir tous les événements
3. Onglet "🔍 Rechercher" : taper "jazz"
4. Onglet "🎯 Filtrer" : sélectionner "CONCERT" + "Gratuit"
```

**Orchestration :**
```bash
# Via le client
1. Cliquer sur "🗺️ Planificateur"
2. Départ : "Centre-ville"
3. Arrivée : "Quartier Nord"
4. Cliquer "Planifier le trajet"
5. Voir analyse air + options transport
```

## 📊 Architecture de Communication

```
┌─────────────┐
│ Client React│
│ Port 3000   │
└──────┬──────┘
       │ HTTP/HTTPS
       ▼
┌──────────────────┐
│   API Gateway    │
│   Port 8080      │
└──────┬───────────┘
       │
       ├─────────────────────────────┐
       │                             │
       ▼                             ▼
┌─────────────┐           ┌──────────────────┐
│  Mobility   │           │  Air Quality     │
│  REST:8081  │           │  SOAP:8082       │
└─────────────┘           └──────────────────┘
       │                             │
       ▼                             ▼
┌─────────────┐           ┌──────────────────┐
│  Emergency  │           │  Events          │
│  gRPC:9090  │           │  GraphQL:8084    │
│  REST:8083  │           │                  │
└─────────────┘           └──────────────────┘
       │
       ▼
┌─────────────────┐
│ Orchestration   │
│ REST:8085       │
└─────────────────┘
```

## 🐛 Dépannage

### Problème : Services en "DOWN"

**Solution :**
```bash
# Vérifier si les services sont démarrés
docker-compose ps

# Redémarrer les services
docker-compose restart

# Vérifier les logs
docker-compose logs -f [service-name]
```

### Problème : Erreurs CORS

**Solution :**
Les CORS sont configurés dans l'API Gateway. Vérifier `api-gateway/src/main/resources/application.yml` :
```yaml
spring:
  cloud:
    gateway:
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins: 
              - "http://localhost:3000"
```

### Problème : "Cannot read property of undefined"

**Solution :**
Vérifier que les services retournent bien des données :
```bash
# Tester directement l'API Gateway
curl http://localhost:8080/actuator/health

# Tester un service spécifique
curl http://localhost:8081/mobility/actuator/health
```

### Problème : Parsing SOAP échoue

**Solution :**
Le service Air Quality utilise XML. Vérifier le format de la réponse :
```bash
curl -X POST http://localhost:8082/airquality/ws \
  -H "Content-Type: text/xml" \
  -d '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
   <soapenv:Header/>
   <soapenv:Body>
      <air:GetAllZonesRequest/>
   </soapenv:Body>
</soapenv:Envelope>'
```

## 📚 Ressources additionnelles

### Documentation API
- **REST API (Swagger)**: http://localhost:8081/mobility/swagger-ui.html
- **SOAP (WSDL)**: http://localhost:8082/airquality/ws/airquality.wsdl
- **GraphQL (GraphiQL)**: http://localhost:8084/graphiql

### Consoles de base de données
- **Mobility H2**: http://localhost:8081/mobility/h2-console
- **Air Quality H2**: http://localhost:8082/airquality/h2-console
- **Events H2**: http://localhost:8084/h2-console

**Credentials :**
- JDBC URL: `jdbc:h2:mem:[service]db`
- User: `sa`
- Password: `password`

## 🎯 Bonnes pratiques

1. **Toujours vérifier la santé des services** avant de les utiliser
2. **Utiliser le Dashboard** comme point de départ
3. **Consulter les logs du navigateur** (F12) pour le debug
4. **Tester un service à la fois** pour isoler les problèmes
5. **Recharger la page** en cas de comportement bizarre


## 📝 Notes importantes

- Le client **NE stocke RIEN** en local (pas de localStorage)
- Toutes les communications passent par l'**API Gateway** (port 8080)
- Les **parsers SOAP/XML** sont côté client pour compatibilité web
- Les **requêtes GraphQL** sont envoyées en POST avec query dans le body
- L'**orchestration** est un exemple d'interopérabilité entre protocoles

## 👥 Support

Pour toute question ou problème :
1. Vérifier les logs : `docker-compose logs -f`
2. Consulter la documentation des services
3. Vérifier que tous les ports sont disponibles

---

**Version**: 1.0.0  
**Dernière mise à jour**: 2025  
**Auteur**: ING 3INF - Service Oriented Computing