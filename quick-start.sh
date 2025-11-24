#!/bin/bash

# Smart City Platform - Quick Start Script
# This script automates the deployment of all services

set -e  # Exit on error

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Functions
print_header() {
    echo -e "${BLUE}╔════════════════════════════════════════════════════╗${NC}"
    echo -e "${BLUE}║    Smart City Platform - Automated Deployment    ║${NC}"
    echo -e "${BLUE}╚════════════════════════════════════════════════════╝${NC}"
    echo ""
}

print_step() {
    echo -e "${BLUE}▶ $1${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

check_prerequisites() {
    print_step "Checking prerequisites..."
    
    # Check Docker
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed"
        exit 1
    fi
    print_success "Docker found: $(docker --version)"
    
    # Check Docker Compose
    if ! docker compose version &> /dev/null; then
        if ! command -v docker-compose &> /dev/null; then
            print_error "Docker Compose is not installed"
            exit 1
        fi
        COMPOSE_CMD="docker-compose"
    else
        COMPOSE_CMD="docker compose"
    fi
    print_success "Docker Compose found"
    
    # Check ports
    print_step "Checking required ports..."
    PORTS=(3000 8080 8081 8082 8083 8084 8085 9090)
    for port in "${PORTS[@]}"; do
        if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
            print_warning "Port $port is already in use"
            read -p "Continue anyway? (y/n) " -n 1 -r
            echo
            if [[ ! $REPLY =~ ^[Yy]$ ]]; then
                exit 1
            fi
            break
        fi
    done
    print_success "All ports are available"
}

clean_environment() {
    print_step "Cleaning previous deployment..."
    $COMPOSE_CMD down -v 2>/dev/null || true
    print_success "Environment cleaned"
}

build_services() {
    print_step "Building services (this may take 5-10 minutes)..."
    
    if $COMPOSE_CMD build --no-cache; then
        print_success "All services built successfully"
    else
        print_error "Build failed"
        exit 1
    fi
}

start_services() {
    print_step "Starting services..."
    
    if $COMPOSE_CMD up -d; then
        print_success "Services started"
    else
        print_error "Failed to start services"
        exit 1
    fi
}

wait_for_services() {
    print_step "Waiting for services to be ready (60 seconds)..."
    
    for i in {1..60}; do
        echo -n "."
        sleep 1
    done
    echo ""
    
    print_success "Wait period completed"
}

check_health() {
    print_step "Checking services health..."
    
    SERVICES=(
        "http://localhost:8080/actuator/health:API Gateway"
        "http://localhost:8081/mobility/actuator/health:Mobility Service"
        "http://localhost:8082/airquality/actuator/health:Air Quality Service"
        "http://localhost:8083/api/emergencies/health:Emergency Service"
        "http://localhost:8084/actuator/health:Urban Events Service"
        "http://localhost:8085/orchestration/health:Orchestration Service"
        "http://localhost:3000:Client Web"
    )
    
    ALL_HEALTHY=true
    for service in "${SERVICES[@]}"; do
        IFS=':' read -r url name <<< "$service"
        if curl -s -f "$url" > /dev/null 2>&1; then
            print_success "$name is healthy"
        else
            print_error "$name is not responding"
            ALL_HEALTHY=false
        fi
    done
    
    if [ "$ALL_HEALTHY" = false ]; then
        print_warning "Some services are not healthy. Check logs with: docker-compose logs"
    fi
}

show_info() {
    echo ""
    echo -e "${GREEN}╔════════════════════════════════════════════════════╗${NC}"
    echo -e "${GREEN}║         Deployment Successful!                    ║${NC}"
    echo -e "${GREEN}╚════════════════════════════════════════════════════╝${NC}"
    echo ""
    echo -e "${BLUE}🌐 Access URLs:${NC}"
    echo "  📱 Client Web:     http://localhost:3000"
    echo "  🚪 API Gateway:    http://localhost:8080"
    echo "  📊 Gateway Health: http://localhost:8080/actuator/health"
    echo ""
    echo -e "${BLUE}🔧 Services:${NC}"
    echo "  🚗 Mobility:       http://localhost:8081/mobility"
    echo "  🌫️  Air Quality:   http://localhost:8082/airquality"
    echo "  🚨 Emergency:      http://localhost:8083"
    echo "  🎭 Events:         http://localhost:8084"
    echo "  🔄 Orchestration:  http://localhost:8085"
    echo ""
    echo -e "${BLUE}📚 Documentation:${NC}"
    echo "  🔍 GraphiQL:       http://localhost:8084/graphiql"
    echo "  📖 WSDL:           http://localhost:8082/airquality/ws/airquality.wsdl"
    echo "  🗄️  H2 Console:    http://localhost:8081/mobility/h2-console"
    echo ""
    echo -e "${BLUE}🧪 Quick Tests:${NC}"
    echo "  curl http://localhost:8080/actuator/health"
    echo "  curl http://localhost:8080/api/mobility/api/transport-lines"
    echo "  curl -X POST \"http://localhost:8080/api/orchestration/plan-journey?startLocation=Centre&endLocation=Nord\""
    echo ""
    echo -e "${YELLOW}📝 Useful Commands:${NC}"
    echo "  docker-compose ps              # Check status"
    echo "  docker-compose logs -f         # View logs"
    echo "  docker-compose down            # Stop all services"
    echo "  docker-compose restart <name>  # Restart a service"
    echo ""
}

# Main execution
main() {
    print_header
    
    check_prerequisites
    clean_environment
    build_services
    start_services
    wait_for_services
    check_health
    show_info
    
    echo -e "${GREEN}✅ Deployment completed successfully!${NC}"
    echo ""
    read -p "Press Enter to view live logs (Ctrl+C to exit)..."
    $COMPOSE_CMD logs -f
}

# Run main function
main