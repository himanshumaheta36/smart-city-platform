#!/bin/bash

echo "🚀 Démarrage de la Plateforme Ville Intelligente"
echo "================================================"

# Couleurs pour l'affichage
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Fonction pour afficher les messages
print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Vérifier que Docker est installé
if ! command -v docker &> /dev/null; then
    print_error "Docker n'est pas installé. Veuillez installer Docker d'abord."
    exit 1
fi

# Vérifier que Docker Compose est disponible
if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    print_error "Docker Compose n'est pas disponible."
    exit 1
fi

# Nettoyage complet de l'environnement précédent
print_info "Nettoyage complet de l'environnement précédent..."
docker-compose down --remove-orphans

# Nettoyage spécifique des conteneurs problématiques
print_info "Vérification des conteneurs existants..."
if docker ps -a | grep -q "api-gateway"; then
    print_warning "Suppression de l'ancien conteneur api-gateway..."
    docker rm -f api-gateway
fi

# Liste des services à vérifier
services=("api-gateway" "urban-events-service" "air-quality-service" "mobility-service" "emergency-service" "orchestration-service" "client-web")

for service in "${services[@]}"; do
    if docker ps -a | grep -q "$service"; then
        print_warning "Suppression de l'ancien conteneur $service..."
        docker rm -f "$service"
    fi
done

# Vérification des réseaux
print_info "Nettoyage des réseaux orphelins..."
docker network prune -f

print_info "Construction des images Docker..."
if docker-compose build; then
    print_success "Construction des images terminée"
else
    print_error "Échec de la construction des images"
    exit 1
fi

print_info "Démarrage des services en arrière-plan..."
if docker-compose up -d --force-recreate; then
    print_success "Services démarrés avec succès"
else
    print_error "Échec du démarrage des services"
    
    # Tentative de récupération
    print_info "Tentative de récupération..."
    docker-compose down
    sleep 5
    docker-compose up -d --force-recreate
fi

print_info "Attente du démarrage des services (60 secondes)..."
sleep 60

print_info "Vérification du statut des services..."
docker-compose ps

# Vérification supplémentaire de la santé des services
print_info "Vérification de la santé des services..."
sleep 10

# Test de l'API Gateway
if curl -s http://localhost:8080/actuator/health > /dev/null; then
    print_success "API Gateway accessible"
else
    print_warning "API Gateway non accessible, nouvelle tentative dans 30 secondes..."
    sleep 30
    if curl -s http://localhost:8080/actuator/health > /dev/null; then
        print_success "API Gateway accessible après nouvelle tentative"
    else
        print_error "API Gateway toujours inaccessible - vérifiez les logs avec: docker-compose logs api-gateway"
    fi
fi

echo ""
echo -e "${GREEN}🏙️  PLATEFORME VILLE INTELLIGENTE - DÉMARRAGE TERMINÉ${NC}"
echo "========================================================"
echo ""
echo -e "${BLUE}🌐 URLs d'accès :${NC}"
echo "   📱 Client Web:     http://localhost:3000"
echo "   🚪 API Gateway:    http://localhost:8080"
echo "   📊 Actuator:       http://localhost:8080/actuator/health"
echo ""
echo -e "${BLUE}🔧 Services :${NC}"
echo "   🚗 Mobilité (REST):       http://localhost:8081"
echo "   🌫️  Qualité Air (SOAP):   http://localhost:8082" 
echo "   🚨 Urgences (gRPC):       http://localhost:8083"
echo "   🎭 Événements (GraphQL):  http://localhost:8084"
echo "   🔄 Orchestration:         http://localhost:8085"
echo ""
echo -e "${BLUE}📚 Documentation :${NC}"
echo "   📖 API Gateway Routes:    http://localhost:8080/actuator/gateway/routes"
echo "   🔍 GraphiQL:              http://localhost:8084/graphiql"
echo "   🗄️  H2 Console:           http://localhost:8081/h2-console"
echo ""
echo -e "${YELLOW}🧪 Tests rapides :${NC}"
echo "   curl http://localhost:8080/actuator/health"
echo "   curl http://localhost:8080/api/orchestration/health"
echo "   curl -X POST \"http://localhost:8080/api/orchestration/plan-journey?startLocation=Centre&endLocation=Nord\""
echo ""
echo -e "${GREEN}✅ La plateforme est prête ! Accédez à http://localhost:3000${NC}"

# Fonction pour afficher les logs en cas de problème
show_logs_on_error() {
    echo ""
    print_warning "Problèmes détectés. Affichage des logs des services en erreur..."
    docker-compose ps | grep -v "Up" | grep -v "NAME" | while read line; do
        service=$(echo $line | awk '{print $1}')
        status=$(echo $line | awk '{print $4}')
        if [[ "$status" != "Up" ]]; then
            print_error "Logs du service $service :"
            docker-compose logs "$service"
            echo ""
        fi
    done
}

# Vérification finale du statut
if docker-compose ps | grep -q "Exit"; then
    show_logs_on_error
fi

# Attendre une entrée utilisateur pour arrêter
echo ""
read -p "Appuyez sur Entrée pour arrêter la plateforme ou Ctrl+C pour laisser tourner..."

print_info "Arrêt de la plateforme..."
docker-compose down
print_success "Plateforme arrêtée"