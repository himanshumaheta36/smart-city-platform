# Client Web - React Application

## Description
Interface web pour accéder à tous les services de la plateforme.

## Technologie
- **Framework**: React 18 + Vite
- **HTTP Client**: Axios
- **Port**: 3000

## Pages

1. **Dashboard** - Vue d'ensemble
2. **Mobilité** - Transports publics
3. **Qualité d'Air** - Pollution et AQI
4. **Urgences** - Gestion des alertes
5. **Événements** - Calendrier culturel
6. **Planificateur** - Optimisation de trajets

## Configuration API

Fichier: `src/services/api.js`

```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

Tous les appels passent par l'API Gateway.

## Installation Locale

```bash
cd client-web
npm install
npm run dev
```

## Build Production

```bash
npm run build
```

Les fichiers sont générés dans `dist/`

## Docker

Le client utilise nginx en production:
- Build multi-stage
- Fichiers statiques optimisés
- Configuration nginx incluse

## Tests

### Accès
```
http://localhost:3000
```

### Vérifier API Gateway
Ouvrir la console du navigateur et vérifier les requêtes vers `http://localhost:8080/api/*`

## Structure

```
src/
├── components/          # Composants React
│   ├── Dashboard.jsx
│   ├── MobilityService.jsx
│   ├── AirQualityService.jsx
│   ├── EmergencyService.jsx
│   ├── EventsService.jsx
│   └── JourneyPlanner.jsx
├── services/
│   └── api.js          # Configuration API
├── App.jsx             # Composant principal
└── main.jsx           # Point d'entrée
```

## Dépannage

### Les requêtes échouent
1. Vérifier que l'API Gateway est accessible
2. Vérifier CORS dans la console
3. Vérifier la configuration API_BASE_URL

### Erreur de build
```bash
rm -rf node_modules package-lock.json
npm install
npm run build
```