# 🤖 Orchestration Service + IA Gemini

## Description

Service d'orchestration intelligent coordonnant plusieurs services pour des workflows complexes et intégrant **Google Gemini AI** pour :
- 💬 **Assistant conversationnel intelligent**
- 📋 **Planification automatique de journées**
- 🧠 **Recommandations contextuelles**
- 🔄 **Coordination multi-services**

## Technologie

- **Protocole Principal** : REST
- **Framework** : Spring Boot 3.2.0 + WebFlux
- **IA** : Google Gemini 2.0 Flash
- **Port** : 8085
- **Communication** : WebClient réactif

## 🆕 Fonctionnalités IA Gemini

### 🤖 Assistant Conversationnel

Discutez en langage naturel avec l'IA pour :
- Obtenir des informations sur la ville
- Poser des questions sur les services
- Recevoir des recommandations personnalisées
- Planifier vos activités

**Exemple** :
```
Vous : "Je veux faire du sport mais il y a de la pollution ?"
IA : "🌫️ La qualité de l'air est actuellement bonne (AQI: 45).
      C'est parfait pour le sport ! Je recommande le Parc Central
      accessible via Metro ligne B. Voulez-vous que je vous
      prépare un plan complet ?"
```

### 📋 Planification Intelligente

L'IA génère automatiquement des plans de journée basés sur :
- Vos préférences (sport, culture, détente...)
- La qualité de l'air en temps réel
- Les transports disponibles
- Les événements du jour
- Vos contraintes horaires

### 🧠 Analyse Contextuelle

L'IA prend en compte :
- ✅ Qualité d'air (SOAP)
- ✅ Transports (REST)
- ✅ Événements (GraphQL)
- ✅ Historique de conversation
- ✅ Localisation

---

## 🚀 Configuration

### Variables d'Environnement

```yaml
# application.yml
gemini:
  api-key: ${GEMINI_API_KEY:}           # Votre clé API Gemini
  model: ${GEMINI_MODEL:gemini-2.0-flash}  # Modèle à utiliser
  enabled: ${GEMINI_ENABLED:true}       # Activer/Désactiver l'IA
  url: https://generativelanguage.googleapis.com/v1beta
```

### Obtenir une Clé API Gemini

1. Visitez [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Connectez-vous avec votre compte Google
3. Cliquez sur "Create API Key"
4. Copiez la clé générée

### Démarrage avec IA

**Docker** :
```bash
docker-compose up -d
# La clé API peut être définie dans docker-compose.yml
```

**Local** :
```bash
export GEMINI_API_KEY="votre-clé-api"
cd orchestration-service
mvn spring-boot:run
```

**Sans IA** (Mode Fallback) :
```bash
export GEMINI_ENABLED=false
mvn spring-boot:run
```

---

## 📡 Endpoints API

### 🤖 IA - Chat Conversationnel

```bash
POST /orchestration/chat
Content-Type: application/json

{
  "message": "Planifie-moi une journée sportive",
  "location": "Centre-ville",
  "sessionId": "optional-session-id"
}
```

**Réponse** :
```json
{
  "message": "🏃‍♂️ Voici votre journée sportive idéale...",
  "suggestedPlan": { ... },
  "sessionId": "abc-123",
  "success": true,
  "timestamp": "2025-12-04T10:30:00"
}
```

**Exemples de questions** :
- "Quelle est la qualité de l'air ?"
- "Je veux faire du vélo, où aller ?"
- "Planifie-moi une journée détente"
- "Quels événements aujourd'hui ?"
- "Comment aller au Parc Central ?"

### 📋 IA - Planification de Journée

```bash
POST /orchestration/plan-day?preferences=sport,culture&location=Centre-ville
```

**Réponse** :
```json
{
  "date": "2025-12-04",
  "userPreferences": "sport, culture",
  "aiSummary": "✨ JOURNÉE SPORTIVE ET CULTURELLE ✨\n\n[Plan détaillé généré par l'IA]",
  "activities": [
    {
      "time": "08:00",
      "title": "Petit-déjeuner énergétique",
      "location": "Café Bio Centre-ville",
      "type": "MEAL",
      "transport": { ... }
    },
    {
      "time": "09:00",
      "title": "Jogging au Parc Central",
      "location": "Parc Central",
      "type": "SPORT",
      "description": "2km de piste, qualité d'air excellente",
      "transport": {
        "type": "METRO",
        "lineNumber": "B",
        "duration": 12
      }
    }
  ],
  "airQuality": {
    "zoneName": "Centre-ville",
    "aqiValue": 45,
    "aqiCategory": "Good",
    "recommendation": "Parfait pour activités extérieures"
  },
  "transportOptions": [ ... ],
  "warnings": [],
  "aiRecommendations": "🌳 Qualité d'air excellente - Profitez des activités en plein air!",
  "generatedAt": "2025-12-04T08:00:00"
}
```

### 🗺️ Planifier un Trajet (Classique)

Combine qualité d'air et mobilité (sans IA).

```bash
POST /orchestration/plan-journey?startLocation=Centre-ville&endLocation=Quartier%20Nord
```

**Réponse** :
```json
{
  "startLocation": "Centre-ville",
  "endLocation": "Quartier Nord",
  "airQuality": {
    "zoneName": "Quartier Nord",
    "aqiValue": 65,
    "aqiCategory": "Moderate"
  },
  "airQualityGood": true,
  "recommendation": "✅ Qualité d'air acceptable. Tous modes recommandés.",
  "transportOptions": [
    {
      "type": "BUS",
      "lineNumber": "72",
      "duration": 15,
      "status": "ACTIVE"
    },
    {
      "type": "METRO",
      "lineNumber": "B",
      "duration": 8,
      "status": "ACTIVE"
    }
  ]
}
```

### 🏙️ Données Complètes de la Ville

Agrège toutes les données en temps réel.

```bash
GET /orchestration/city-data?location=Centre-ville
```

**Réponse** :
```json
{
  "airQuality": { ... },
  "transports": [ ... ],
  "events": [ ... ],
  "location": "Centre-ville",
  "timestamp": "2025-12-04T10:30:00",
  "aiEnabled": true
}
```

### 🌫️ Qualité d'Air

```bash
# Une zone spécifique
GET /orchestration/air-quality/Centre-ville

# Toutes les zones
GET /orchestration/air-quality
```

### 🚌 Transports

```bash
GET /orchestration/transports
```

### 📅 Événements

```bash
# Tous les événements
GET /orchestration/events

# Recherche par mot-clé
GET /orchestration/events/search?keyword=concert
```

### ❤️ Health Check

```bash
GET /orchestration/health
```

**Réponse** :
```json
{
  "status": "UP",
  "service": "Orchestration Service",
  "timestamp": "2025-12-04T10:30:00",
  "gemini": {
    "enabled": true,
    "connected": true
  },
  "features": [
    "day-planning",
    "chat",
    "real-time-data",
    "gemini-ai"
  ]
}
```

### 👋 Welcome

```bash
GET /orchestration/welcome
```

---

## 🔄 Workflow Intelligent

### Avec IA Gemini

```
┌────────────────┐
│   Utilisateur  │
│  (Question NL) │
└───────┬────────┘
        │
        ▼
┌───────────────────┐
│   IA Gemini       │ ← Comprend la question
│   (NLP)           │ ← Analyse le contexte
└───────┬───────────┘
        │
        ├──────────────┬───────────────┬──────────────┐
        ▼              ▼               ▼              ▼
┌──────────────┐ ┌───────────┐ ┌──────────┐ ┌──────────────┐
│Air Quality   │ │ Mobility  │ │Emergency │ │   Events     │
│Service (SOAP)│ │(REST)     │ │(gRPC)    │ │  (GraphQL)   │
└──────┬───────┘ └─────┬─────┘ └────┬─────┘ └──────┬───────┘
       │               │             │              │
       └───────────────┴─────────────┴──────────────┘
                       │
                       ▼
               ┌───────────────────┐
               │   IA Gemini       │ ← Synthétise
               │   (Génération)    │ ← Recommande
               └───────────────────┘
                       │
                       ▼
               ┌───────────────────┐
               │  Réponse          │
               │  Intelligente     │
               └───────────────────┘
```

### Sans IA (Mode Fallback)

```
┌────────────────┐
│   Requête API  │
└───────┬────────┘
        │
        ▼
┌───────────────────┐
│  Orchestrator     │ ← Logique prédéfinie
│  (Règles)         │
└───────┬───────────┘
        │
        ├──────────────┬───────────────┐
        ▼              ▼               ▼
   Air Quality     Mobility        Events
        │              │               │
        └──────────────┴───────────────┘
                       │
                       ▼
               ┌───────────────────┐
               │  Réponse Simple   │
               └───────────────────┘
```

---

## 🧪 Tests

### Test IA - Chat

```bash
# Via curl
curl -X POST http://localhost:8085/orchestration/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Bonjour! Je veux explorer la ville",
    "location": "Centre-ville"
  }'
```

**Via API Gateway** :
```bash
curl -X POST http://localhost:8080/api/orchestration/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Planifie ma journée"}'
```

### Test IA - Planification

```bash
curl -X POST "http://localhost:8085/orchestration/plan-day?preferences=sport,nature&location=Parc%20Central"
```

### Test Trajet Classique

```bash
curl -X POST "http://localhost:8085/orchestration/plan-journey?startLocation=Centre&endLocation=Nord"
```

### Test Health avec IA

```bash
curl http://localhost:8085/orchestration/health

# Devrait afficher:
{
  "status": "UP",
  "gemini": {
    "enabled": true,
    "connected": true
  }
}
```

---

## 🏗️ Architecture du Code

```
orchestration-service/
├── src/main/java/com/smartcity/orchestration/
│   ├── controller/
│   │   ├── OrchestrationController.java     # API REST
│   │   └── ChatController.java               # API Chat
│   ├── service/
│   │   ├── GeminiService.java                # 🤖 Intégration IA
│   │   ├── DayPlannerService.java            # 📋 Planification
│   │   ├── AirQualityClient.java             # SOAP client
│   │   ├── MobilityClient.java               # REST client
│   │   └── EventsClient.java                 # GraphQL client
│   ├── model/
│   │   ├── ChatRequest.java
│   │   ├── ChatResponse.java
│   │   ├── DayPlan.java
│   │   ├── AirQualityInfo.java
│   │   └── ...
│   └── config/
│       └── WebClientConfig.java
├── src/main/resources/
│   └── application.yml                       # Config (+ Gemini)
├── pom.xml
└── Dockerfile
```

---

## 💡 Exemples d'Utilisation

### Cas 1 : Journée Sportive

**Requête** :
```json
POST /orchestration/plan-day?preferences=sport,plein air&location=Centre-ville
```

**Réponse IA** :
```
✨ JOURNÉE SPORTIVE EN PLEIN AIR ✨

Bonjour! Voici votre journée sportive optimale :

🌅 MATIN (8h-12h)
08:00 - Petit-déjeuner protéiné chez Healthy Corner
09:00 - Jogging au Parc Central (5km)
  🌫️ AQI: 42 - Excellent pour le sport!
  🚇 Metro B depuis Centre-ville (8 min)
10:30 - Yoga en plein air (groupe gratuit)

🍽️ MIDI (12h-14h)
12:30 - Brunch végétarien

☀️ APRÈS-MIDI (14h-18h)
14:30 - Vélo le long de la rivière
  🚲 Station Vélib à 200m
16:00 - Escalade indoor (salle ClimbUp)
  📅 Réservation recommandée

💡 RECOMMANDATIONS:
- Qualité d'air excellente toute la journée
- Hydratez-vous bien
- Crème solaire recommandée
- Pensez à réserver l'escalade
```

### Cas 2 : Question Simple

**Requête** :
```json
POST /orchestration/chat
{
  "message": "Est-ce que je peux courir dehors ?"
}
```

**Réponse IA** :
```
🏃‍♂️ Oui, absolument!

La qualité de l'air est actuellement excellente (AQI: 45).
C'est le moment parfait pour courir!

Je recommande:
🌳 Parc Central - piste de 3km, ombragée
⏰ Meilleurs créneaux: 7h-9h ou 18h-20h
🚇 Accès: Metro ligne B, arrêt Parc

Voulez-vous que je vous prépare un itinéraire détaillé?
```

### Cas 3 : Pollution Élevée

**Requête** :
```json
POST /orchestration/plan-day?preferences=sport&location=Zone Industrielle
```

**Réponse IA** :
```
⚠️ ATTENTION: Qualité de l'air dégradée

L'AQI dans la Zone Industrielle est de 145 (Mauvais).
Je vous déconseille les activités extérieures.

🏋️ ALTERNATIVE - JOURNÉE SPORT EN INTÉRIEUR:

09:00 - Salle de sport FitZone (climatisée)
11:00 - Piscine municipale couverte
  🚌 Bus 72 toutes les 10min
14:00 - Cours de danse Studio Move
16:00 - Escalade indoor

💡 La qualité devrait s'améliorer ce soir.
   Vérifiez vers 19h pour une sortie en soirée.

Souhaitez-vous être notifié quand l'AQI s'améliore?
```

---

## 🔧 Développement

### Ajouter une Nouvelle Fonctionnalité IA

1. **Modifier `GeminiService.java`** :
```java
public Mono<String> nouvelleFeature(String input) {
    String prompt = buildPrompt(input);
    return callGemini(prompt);
}
```

2. **Créer l'endpoint dans `OrchestrationController.java`** :
```java
@PostMapping("/nouvelle-feature")
public Mono<Response> nouvelleFeature(@RequestParam String param) {
    return geminiService.nouvelleFeature(param)
        .map(Response::success);
}
```

### Personnaliser les Prompts

Les prompts sont dans `GeminiService.java` :
```java
private String buildDayPlanPrompt(...) {
    return """
        Tu es un assistant de ville intelligente.
        [Instructions personnalisées]
        """;
}
```

### Mode Debug

```bash
# Activer logs détaillés
export LOGGING_LEVEL_COM_SMARTCITY_ORCHESTRATION=DEBUG
mvn spring-boot:run
```

**Logs IA** :
```
🤖 Appel Gemini API...
📊 Contexte: {airQuality: ..., transports: ...}
✅ Réponse Gemini reçue (1234 caractères)
```

---

## 🐛 Dépannage

### L'IA ne répond pas

**Vérifier** :
```bash
curl http://localhost:8085/orchestration/health
```

**Si `gemini.enabled: false`** :
- Vérifier `GEMINI_API_KEY` dans l'environnement
- Redémarrer le service

**Si `gemini.connected: false`** :
- Vérifier la connexion internet
- Vérifier les quotas API Gemini
- Consulter les logs : `docker-compose logs orchestration-service`

### Erreur "API Key Invalid"

1. Vérifier la clé : https://makersuite.google.com/app/apikey
2. Régénérer si nécessaire
3. Mettre à jour la variable d'environnement
4. Redémarrer : `docker-compose restart orchestration-service`

### Mode Fallback Activé

Si l'IA est indisponible, le service fonctionne en mode fallback avec :
- Réponses basées sur des règles prédéfinies
- Planifications simplifiées
- Pas de compréhension du langage naturel

**Pour forcer le mode fallback** :
```bash
export GEMINI_ENABLED=false
```

---

## 📊 Monitoring

### Métriques IA

```bash
# Logs Gemini
docker-compose logs -f orchestration-service | grep "Gemini"

# Statistiques
curl http://localhost:8085/actuator/metrics
```

### Performance

- **Latence IA moyenne** : 500-2000ms
- **Timeout** : 30 secondes
- **Retry** : Automatique avec fallback
- **Cache** : Recommandé pour réponses fréquentes

---

## 🚀 Production

### Recommandations

1. **Utiliser un cache** pour les réponses IA fréquentes
2. **Rate limiting** sur les endpoints IA
3. **Monitoring** des quotas Gemini
4. **Fallback activé** en cas de panne
5. **Logs centralisés** (ELK, Splunk)

### Sécurité

```yaml
# Ne jamais commit la clé API
gemini:
  api-key: ${GEMINI_API_KEY}  # Variable d'environnement

# Utiliser des secrets managers
# - AWS Secrets Manager
# - Azure Key Vault
# - HashiCorp Vault
```

---

## 📚 Resources

**Documentation** :
- [Google Gemini API](https://ai.google.dev/tutorials/rest_quickstart)
- [Gemini Models](https://ai.google.dev/models/gemini)
- [Prompt Engineering](https://ai.google.dev/docs/prompt_best_practices)

**Notre Code** :
- `GeminiService.java` - Intégration IA
- `DayPlannerService.java` - Logique planification
- `application.yml` - Configuration

---

## 📝 Notes

- **Quota Gratuit Gemini** : 60 requêtes/minute
- **Modèle** : gemini-2.0-flash (rapide et efficace)
- **Contexte** : ~100K tokens
- **Langues** : Français, Anglais, et plus
- **Fallback** : Toujours actif si IA indisponible

---

**🤖 Smart City Platform - Orchestration Service**  
**Propulsé par Google Gemini 2.0 Flash**  
**Version 2.0 - Édition Intelligence Artificielle** 🚀