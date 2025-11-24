# Urban Events Service - Service GraphQL

## Description
Service GraphQL pour la gestion des événements urbains et culturels.

## Technologie
- **Protocole**: GraphQL
- **Framework**: Spring Boot + Spring GraphQL
- **Base de données**: H2 (en mémoire)
- **Port**: 8084

## GraphQL Queries

### Tous les événements
```graphql
query {
  getAllEvents {
    id
    title
    description
    location
    startDateTime
    eventType
    category
    availableSpots
  }
}
```

### Rechercher
```graphql
query {
  searchEvents(keyword: "jazz") {
    id
    title
    location
  }
}
```

### Filtrer
```graphql
query {
  filterEvents(type: CONCERT, freeOnly: true) {
    id
    title
    price
    isFree
  }
}
```

### Événements à venir
```graphql
query {
  getUpcomingEvents {
    id
    title
    startDateTime
    availableSpots
  }
}
```

## GraphQL Mutations

### Créer un événement
```graphql
mutation {
  createEvent(input: {
    title: "Concert de Jazz"
    description: "Concert en plein air"
    eventType: CONCERT
    category: FREE
    location: "Parc Central"
    latitude: 48.8566
    longitude: 2.3522
    startDateTime: "2025-07-15T18:00:00"
    endDateTime: "2025-07-15T23:00:00"
    capacity: 500
    organizer: "Mairie"
    tags: ["jazz", "musique", "gratuit"]
  }) {
    id
    title
  }
}
```

### S'inscrire
```graphql
mutation {
  registerAttendee(eventId: 1) {
    id
    registeredAttendees
    availableSpots
  }
}
```

## Types d'Événements
- CONCERT
- FESTIVAL
- SPORTS
- CONFERENCE
- EXHIBITION
- WORKSHOP
- COMMUNITY
- CULTURAL

## Tests

### Via GraphiQL
Accéder à: http://localhost:8084/graphiql

### Via curl
```bash
curl -X POST http://localhost:8084/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ getAllEvents { id title location } }"}'
```

### Via API Gateway
```bash
curl -X POST http://localhost:8080/api/events/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ getAllEvents { id title } }"}'
```

## Événements de Test
- Festival de Jazz Urbain
- Marathon de la Ville
- Conférence IA
- Marché Artisanal
- Exposition d'Art
- Atelier de Recyclage
- Concert Symphonique
- Festival de Cuisine

## H2 Console
- URL: http://localhost:8084/h2-console
- JDBC URL: `jdbc:h2:mem:urbaneventsdb`
- User: `sa`
- Password: `password`

## GraphQL Schema
Fichier: `src/main/resources/graphql/urban-events.graphqls`