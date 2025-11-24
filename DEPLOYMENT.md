# Guide de Déploiement Détaillé

## Processus Complet de Déploiement

### Étape 1: Préparation de l'Environnement

#### 1.1 Vérification des Prérequis
```bash
# Vérifier Docker
docker --version
# Doit être >= 20.10

# Vérifier Docker Compose
docker-compose --version
# Doit être >= 2.0

# Vérifier les ports disponibles
lsof -i :3000,8080,8081,8082,8083,8084,8085,9090
# Aucun processus ne doit utiliser ces ports
```

#### 1.2 Nettoyage de l'Environnement
```bash
# Arrêter tous les conteneurs existants
docker-compose down -v

# Supprimer les anciens conteneurs (optionnel)
docker rm -f $(docker ps -aq)

# Nettoyer les images non utilisées (optionnel)
docker image prune -a
```

### Étape 2: Construction des Services

#### 2.1 Construction Complète
```bash
# À la racine du projet
docker-compose build --no-cache
```

Cette commande va:
1. Télécharger les images de base (Maven, Node, Java)
2. Compiler chaque service Java avec Maven
3. Construire l'application React
4. Créer les images Docker finales

**Temps estimé**: 10-15 minutes (première fois)

#### 2.2 Construction Service par Service (optionnel)
```bash
# API Gateway
docker-compose build api-gateway

# Mobility Service
docker-compose build mobility-service

# Air Quality Service
docker-compose build air-quality-service

# Emergency Service
docker-compose build emergency-service

# Urban Events Service
docker-compose build urban-events-service

# Orchestration Service
docker-compose build orchestration-service

# Client Web
docker-compose build client-web
```

### Étape 3: Démarrage des Services

#### 3.1 Démarrage Orchestré
```bash
# Démarrer tous les services
docker-compose up -d

# Vérifier l'état
docker-compose ps
```

**Ordre de démarrage automatique**:
1. Services backend (mobility, air-quality, emergency, urban-events)
2. Orchestration service (dépend des services backend)
3. API Gateway (dépend de tous les services)
4. Client Web (dépend de l'API Gateway)

#### 3.2 Surveillance du Démarrage
```bash
# Logs en temps réel de tous les services
docker-compose logs -f

# Logs d'un service spécifique
docker-compose logs -f api-gateway
```

### Étape 4: Vérification des Services

#### 4.1 Health Checks
```bash
# Attendre 2-3 minutes après le démarrage, puis:

# API Gateway
curl http://localhost:8080/actuator/health
# Doit retourner: {"status":"UP"}

# Mobility Service
curl http://localhost:8081/mobility/actuator/health

# Air Quality Service
curl http://localhost:8082/airquality/actuator/health

# Emergency Service
curl http://localhost:8083/api/emergencies/health

# Urban Events Service
curl http://localhost:8084/actuator/health

# Orchestration Service
curl http://localhost:8085/orchestration/health
```

#### 4.2 Test Fonctionnel Rapide
```bash
# Test via API Gateway
curl http://localhost:8080/api/mobility/api/transport-lines

# Doit retourner une liste de lignes de transport
```

#### 4.3 Vérification Client Web
```bash
# Dans un navigateur
open http://localhost:3000

# Ou
curl http://localhost:3000
```

### Étape 5: Tests d'Intégration

#### 5.1 Test Mobilité (REST)
```bash
curl http://localhost:8080/api/mobility/api/transport-lines | jq
```

#### 5.2 Test Qualité d'Air (SOAP)
```bash
curl -X POST http://localhost:8082/airquality/ws \
  -H "Content-Type: text/xml" \
  -d '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
   <soapenv:Body>
      <air:GetAllZonesRequest/>
   </soapenv:Body>
</soapenv:Envelope>'
```

#### 5.3 Test Urgences (gRPC via REST)
```bash
curl http://localhost:8080/api/emergency | jq
```

#### 5.4 Test Événements (GraphQL)
```bash
curl -X POST http://localhost:8080/api/events/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ getAllEvents { id title location } }"}' | jq
```

#### 5.5 Test Orchestration
```bash
curl -X POST "http://localhost:8080/api/orchestration/plan-journey?startLocation=Centre&endLocation=Nord" | jq
```

## Dépannage Avancé

### Problème 1: Service ne démarre pas

**Diagnostic**:
```bash
# Voir les logs du service
docker-compose logs <service-name>

# Vérifier l'état
docker-compose ps <service-name>

# Inspecter le conteneur
docker inspect <container-name>
```

**Solutions**:
```bash
# Redémarrer le service
docker-compose restart <service-name>

# Reconstruire et redémarrer
docker-compose up -d --build <service-name>

# Forcer la recréation
docker-compose up -d --force-recreate <service-name>
```

### Problème 2: Port déjà utilisé

**Diagnostic**:
```bash
# Trouver le processus utilisant le port
lsof -i :8080
# ou
netstat -tulpn | grep 8080
```

**Solutions**:
```bash
# Tuer le processus
kill -9 <PID>

# Ou modifier le port dans docker-compose.yml
ports:
  - "8180:8080"  # Utiliser 8180 au lieu de 8080
```

### Problème 3: Services ne communiquent pas

**Diagnostic**:
```bash
# Vérifier le réseau Docker
docker network ls
docker network inspect smart-city-network

# Tester la connectivité entre conteneurs
docker exec api-gateway ping mobility-service
```

**Solutions**:
```bash
# Recréer le réseau
docker-compose down
docker network prune
docker-compose up -d
```

### Problème 4: Base de données vide

**Diagnostic**:
```bash
# Vérifier les logs d'initialisation
docker-compose logs mobility-service | grep "INITIALISATION"
```

**Solutions**:
```bash
# Vérifier que data.sql existe
ls -la mobility-service/src/main/resources/data.sql

# Forcer la réinitialisation
docker-compose down -v
docker-compose up -d
```

### Problème 5: Client Web - 404 ou pages blanches

**Diagnostic**:
```bash
# Vérifier les logs nginx
docker-compose logs client-web

# Vérifier le build
docker exec client-web ls -la /usr/share/nginx/html
```

**Solutions**:
```bash
# Reconstruire le client
docker-compose build --no-cache client-web
docker-compose up -d client-web

# Vérifier la configuration API
docker exec client-web cat /usr/share/nginx/html/index.html
```

## Optimisation des Performances

### 1. Allouer Plus de Mémoire à Docker
```bash
# Dans Docker Desktop: Settings → Resources
# RAM recommandée: 6-8 GB
# CPUs: 4+
```

### 2. Utiliser un Cache Maven Local
```dockerfile
# Dans chaque Dockerfile service
VOLUME /root/.m2
```

### 3. Build Incrémental
```bash
# Ne reconstruire que les services modifiés
docker-compose up -d --build mobility-service
```

## Commandes Utiles

### Logs et Debugging
```bash
# Tous les logs depuis le début
docker-compose logs

# Logs en temps réel
docker-compose logs -f

# Logs d'un service avec timestamp
docker-compose logs -f --timestamps mobility-service

# Dernières 100 lignes
docker-compose logs --tail=100 mobility-service
```

### Gestion des Conteneurs
```bash
# État détaillé
docker-compose ps -a

# Statistiques en temps réel
docker stats

# Arrêter un service
docker-compose stop mobility-service

# Démarrer un service
docker-compose start mobility-service

# Redémarrer un service
docker-compose restart mobility-service
```

### Nettoyage
```bash
# Arrêter et supprimer les conteneurs
docker-compose down

# Arrêter et supprimer volumes
docker-compose down -v

# Supprimer les images
docker-compose down --rmi all

# Nettoyage complet Docker
docker system prune -a --volumes
```

## Checklist de Déploiement

- [ ] Ports 3000, 8080-8085, 9090 disponibles
- [ ] Docker et Docker Compose installés
- [ ] 6-8 GB RAM disponible
- [ ] Espace disque: 5 GB minimum
- [ ] `docker-compose build` réussi
- [ ] `docker-compose up -d` réussi
- [ ] Tous les health checks passent
- [ ] Client web accessible
- [ ] Tests d'intégration réussis

## Support

En cas de problème persistant:
1. Collecter les logs: `docker-compose logs > logs.txt`
2. Vérifier la checklist ci-dessus
3. Consulter la documentation de chaque service
4. Nettoyer et redémarrer: `docker-compose down -v && docker-compose up --build -d`