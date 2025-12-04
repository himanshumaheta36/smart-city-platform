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
    if docker compose version &> /dev/null; then
        COMPOSE_CMD="docker compose"
    elif command -v docker-compose &> /dev/null; then
        COMPOSE_CMD="docker-compose"
    else
        print_error "Docker Compose is not installed"
        exit 1
    fi
    print_success "Docker Compose found: $($COMPOSE_CMD version)"
    
    # Check ports (cross-platform method)
    print_step "Checking required ports..."
    PORTS=(3000 8080 8081 8082 8083 8084 8085 9090)
    CONFLICTS=false
    
    for port in "${PORTS[@]}"; do
        if nc -z localhost $port 2>/dev/null; then
            print_warning "Port $port is already in use"
            CONFLICTS=true
        fi
    done
    
    if [ "$CONFLICTS" = true ]; then
        read -p "Some ports are in use. Continue anyway? (y/n) " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            exit 1
        fi
    else
        print_success "All ports are available"
    fi
}

check_circuit_breaker_dependency() {
    print_step "Checking API Gateway dependencies..."
    
    if [ -f "./api-gateway/pom.xml" ]; then
        if grep -q "circuitbreaker-reactor-resilience4j" ./api-gateway/pom.xml; then
            print_success "Circuit Breaker dependency found"
        else
            print_warning "Circuit Breaker dependency missing in API Gateway"
            print_warning "This may cause startup failures"
            read -p "Continue anyway? (y/n) " -n 1 -r
            echo
            if [[ ! $REPLY =~ ^[Yy]$ ]]; then
                exit 1
            fi
        fi
    fi
}

clean_environment() {
    print_step "Cleaning previous deployment..."
    $COMPOSE_CMD down -v --remove-orphans 2>/dev/null || true
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
    print_step "Waiting for services to be ready (up to 120 seconds)..."
    
    # Wait for services to start
    for i in {1..120}; do
        if $COMPOSE_CMD ps | grep -q "Up"; then
            break
        fi
        echo -n "."
        sleep 1
    done
    echo ""
    
    print_success "Initial startup completed"
}

check_health() {
    print_step "Checking services health..."
    
    # CORRECTED health check endpoints
    SERVICES=(
        "http://localhost:8080/actuator/health :API Gateway"
        "http://localhost:8081/mobility/actuator/health :Mobility Service" 
        "http://localhost:8082/airquality/actuator/health :Air Quality Service"
        "http://localhost:8083/api/emergencies/health :Emergency Service"
        "http://localhost:8084/actuator/health :Urban Events Service"
        "http://localhost:8085/orchestration/health :Orchestration Service"
        "http://localhost:3000 :Client Web"
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
        print_warning "Some services are not healthy. This might be expected on first run."
        print_warning "Check logs with: $COMPOSE_CMD logs"
        print_warning "Or wait a bit longer and run: $COMPOSE_CMD logs -f"
    else
        print_success "All services are healthy!"
    fi
}

show_info() {
    echo ""
    echo -e "${GREEN}╔════════════════════════════════════════════════════╗${NC}"
    echo -e "${GREEN}║         Deployment Completed!                     ║${NC}"
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
    echo -e "${BLUE}📚 Documentation & Tools:${NC}"
    echo "  🔍 GraphiQL:       http://localhost:8084/graphiql"
    echo "  📖 WSDL:           http://localhost:8082/airquality/ws/airquality.wsdl"
    echo "  🗄️  H2 Console:    http://localhost:8081/h2-console"
    echo ""
    echo -e "${BLUE}🧪 Quick Tests:${NC}"
    echo "  # Check health"
    echo "  curl http://localhost:8080/actuator/health"
    echo ""
    echo "  # Test Mobility API"
    echo "  curl http://localhost:8080/api/mobility/transport-lines"
    echo ""
    echo "  # Test Orchestration"
    echo "  curl -X POST \"http://localhost:8080/api/orchestration/plan-journey?startLocation=Centre&endLocation=Nord\""
    echo ""
    echo -e "${YELLOW}📝 Useful Commands:${NC}"
    echo "  $COMPOSE_CMD ps              # Check status"
    echo "  $COMPOSE_CMD logs -f         # View logs"
    echo "  $COMPOSE_CMD down            # Stop all services"
    echo "  $COMPOSE_CMD restart <name>  # Restart a service"
    echo ""
}

# Main execution
main() {
    print_header
    
    check_prerequisites
    check_circuit_breaker_dependency
    clean_environment
    build_services
    start_services
    wait_for_services
    check_health
    show_info
    
    echo -e "${GREEN}✅ Deployment process completed!${NC}"
    echo ""
    read -p "Press Enter to view live logs (Ctrl+C to exit)..."
    $COMPOSE_CMD logs -f
}

# Run main function
main "$@"