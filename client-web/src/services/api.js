import axios from 'axios';

// Configuration de base de l'API Gateway
const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10 secondes de timeout
});

// Intercepteur pour logger les requêtes (utile pour le debug)
api.interceptors.request.use(
  (config) => {
    console.log(`🔵 ${config.method.toUpperCase()} ${config.url}`);
    return config;
  },
  (error) => {
    console.error('❌ Erreur de requête:', error);
    return Promise.reject(error);
  }
);

// Intercepteur pour logger les réponses
api.interceptors.response.use(
  (response) => {
    console.log(`✅ ${response.config.method.toUpperCase()} ${response.config.url} - ${response.status}`);
    return response;
  },
  (error) => {
    console.error(`❌ ${error.config?.method?.toUpperCase()} ${error.config?.url} - ${error.response?.status || 'Network Error'}`);
    return Promise.reject(error);
  }
);

// ==================== MOBILITY SERVICE (REST) ====================
export const mobilityAPI = {
  // Lignes de transport
  getTransportLines: () => api.get('/mobility/api/transport-lines'),
  getTransportLineByNumber: (lineNumber) => api.get(`/mobility/api/transport-lines/number/${lineNumber}`),
  getTransportLinesByType: (type) => api.get(`/mobility/api/transport-lines/type/${type}`),
  getTransportLinesByStation: (station) => api.get(`/mobility/api/transport-lines/station/${station}`),
  
  // Horaires
  getSchedulesByLine: (lineNumber) => api.get(`/mobility/api/schedules/line/${lineNumber}`),
  getSchedulesByStation: (station, dayType = 'WEEKDAY') => 
    api.get(`/mobility/api/schedules/station/${station}?dayType=${dayType}`),
  
  // Trafic
  getTrafficInfo: () => api.get('/mobility/api/traffic-info/active'),
  getTrafficBySeverity: (severity) => api.get(`/mobility/api/traffic-info/severity/${severity}`),
};

// ==================== AIR QUALITY SERVICE (SOAP) ====================
export const airQualityAPI = {
  // Qualité de l'air par zone
  getAirQuality: (zoneName) => 
    api.post('/air-quality/ws', {
      headers: { 'Content-Type': 'text/xml' },
      data: `
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
          <soapenv:Header/>
          <soapenv:Body>
            <air:GetAirQualityRequest>
              <air:zoneName>${zoneName}</air:zoneName>
            </air:GetAirQualityRequest>
          </soapenv:Body>
        </soapenv:Envelope>
      `
    }),
  
  // Toutes les zones
  getAllZones: () => 
    api.post('/air-quality/ws', {
      headers: { 'Content-Type': 'text/xml' },
      data: `
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
          <soapenv:Header/>
          <soapenv:Body>
            <air:GetAllZonesRequest/>
          </soapenv:Body>
        </soapenv:Envelope>
      `
    }),
  
  // Comparer deux zones
  compareZones: (zone1, zone2) => 
    api.post('/air-quality/ws', {
      headers: { 'Content-Type': 'text/xml' },
      data: `
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
          <soapenv:Header/>
          <soapenv:Body>
            <air:CompareZonesRequest>
              <air:zone1>${zone1}</air:zone1>
              <air:zone2>${zone2}</air:zone2>
            </air:CompareZonesRequest>
          </soapenv:Body>
        </soapenv:Envelope>
      `
    }),
};

// ==================== EMERGENCY SERVICE (gRPC via REST) ====================
export const emergencyAPI = {
  // Créer une alerte
  createAlert: (alertData) => api.post('/emergency', alertData),
  
  // Obtenir toutes les alertes
  getAlerts: () => api.get('/emergency'),
  
  // Obtenir une alerte spécifique
  getAlert: (emergencyId) => api.get(`/emergency/${emergencyId}`),
  
  // Mettre à jour le statut
  updateStatus: (emergencyId, statusData) => 
    api.put(`/emergency/${emergencyId}/status`, statusData),
  
  // Statistiques
  getStats: (hoursBack = 24) => api.get(`/emergency/stats?hoursBack=${hoursBack}`),
};

// ==================== EVENTS SERVICE (GraphQL) ====================
export const eventsAPI = {
  // Tous les événements
  getAllEvents: () => 
    api.post('/events/graphql', {
      query: `
        query {
          getAllEvents {
            id
            title
            description
            location
            startDateTime
            endDateTime
            eventType
            category
            capacity
            registeredAttendees
            availableSpots
            isFree
            price
            organizer
            tags
            imageUrl
          }
        }
      `
    }),
  
  // Rechercher des événements
  searchEvents: (keyword) => 
    api.post('/events/graphql', {
      query: `
        query {
          searchEvents(keyword: "${keyword}") {
            id
            title
            description
            location
            startDateTime
            eventType
            category
            isFree
          }
        }
      `
    }),
  
  // Événements à venir
  getUpcomingEvents: () => 
    api.post('/events/graphql', {
      query: `
        query {
          getUpcomingEvents {
            id
            title
            location
            startDateTime
            endDateTime
            availableSpots
            category
            eventType
          }
        }
      `
    }),
  
  // Filtrer les événements
  filterEvents: (filters) => {
    const { type, category, freeOnly } = filters;
    let filterArgs = [];
    if (type) filterArgs.push(`type: ${type}`);
    if (category) filterArgs.push(`category: ${category}`);
    if (freeOnly !== undefined) filterArgs.push(`freeOnly: ${freeOnly}`);
    
    return api.post('/events/graphql', {
      query: `
        query {
          filterEvents(${filterArgs.join(', ')}) {
            id
            title
            location
            startDateTime
            eventType
            category
            isFree
            availableSpots
          }
        }
      `
    });
  },
  
  // S'inscrire à un événement
  registerToEvent: (eventId) => 
    api.post('/events/graphql', {
      query: `
        mutation {
          registerAttendee(eventId: ${eventId}) {
            id
            title
            registeredAttendees
            availableSpots
          }
        }
      `
    }),
};

// ==================== ORCHESTRATION SERVICE ====================
export const orchestrationAPI = {
  // Planifier un trajet
  planJourney: (startLocation, endLocation) => 
    api.post(`/orchestration/plan-journey?startLocation=${encodeURIComponent(startLocation)}&endLocation=${encodeURIComponent(endLocation)}`),
  
  // Health check
  health: () => api.get('/orchestration/health'),
};

// ==================== HEALTH CHECKS ====================
export const healthAPI = {
  gateway: () => api.get('/actuator/health', { baseURL: 'http://localhost:8080' }),
  mobility: () => axios.get('http://localhost:8081/mobility/actuator/health'),
  airQuality: () => axios.get('http://localhost:8082/airquality/actuator/health'),
  emergency: () => axios.get('http://localhost:8083/actuator/health'),
  events: () => axios.get('http://localhost:8084/actuator/health'),
  orchestration: () => axios.get('http://localhost:8085/actuator/health'),
};

export default api;