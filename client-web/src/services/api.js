import axios from 'axios';

// Configuration de base de l'API Gateway
const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

// Intercepteur pour logger les requêtes
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
  getTransportLines: () => api.get('/mobility/api/transport-lines'),
  getTransportLineByNumber: (lineNumber) => api.get(`/mobility/api/transport-lines/number/${lineNumber}`),
  getTransportLinesByType: (type) => api.get(`/mobility/api/transport-lines/type/${type}`),
  getSchedulesByLine: (lineNumber) => api.get(`/mobility/api/schedules/line/${lineNumber}`),
  getTrafficInfo: () => api.get('/mobility/api/traffic-info/active'),
};

// ==================== AIR QUALITY SERVICE (SOAP) ====================
export const airQualityAPI = {
  // Toutes les zones - appel direct au service SOAP
  getAllZones: () => axios.post(
    'http://localhost:8082/airquality/ws',
    `<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
  <soapenv:Header/>
  <soapenv:Body>
    <air:GetAllZonesRequest/>
  </soapenv:Body>
</soapenv:Envelope>`,
    { 
      headers: { 
        'Content-Type': 'text/xml; charset=utf-8',
        'SOAPAction': ''
      },
      timeout: 10000
    }
  ),
  
  // Qualité d'air par zone
  getAirQuality: (zoneName) => axios.post(
    'http://localhost:8082/airquality/ws',
    `<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
  <soapenv:Header/>
  <soapenv:Body>
    <air:GetAirQualityRequest>
      <air:zoneName>${zoneName}</air:zoneName>
    </air:GetAirQualityRequest>
  </soapenv:Body>
</soapenv:Envelope>`,
    { 
      headers: { 
        'Content-Type': 'text/xml; charset=utf-8',
        'SOAPAction': ''
      },
      timeout: 10000
    }
  ),
  
  // Comparer deux zones
  compareZones: (zone1, zone2) => axios.post(
    'http://localhost:8082/airquality/ws',
    `<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
  <soapenv:Header/>
  <soapenv:Body>
    <air:CompareZonesRequest>
      <air:zone1>${zone1}</air:zone1>
      <air:zone2>${zone2}</air:zone2>
    </air:CompareZonesRequest>
  </soapenv:Body>
</soapenv:Envelope>`,
    { 
      headers: { 
        'Content-Type': 'text/xml; charset=utf-8',
        'SOAPAction': ''
      },
      timeout: 10000
    }
  ),
};

// ==================== EMERGENCY SERVICE (gRPC via REST) ====================
export const emergencyAPI = {
  createAlert: (alertData) => axios.post(
    'http://localhost:8083/api/emergencies',
    alertData,
    { timeout: 10000 }
  ),
  getAlerts: () => axios.get('http://localhost:8083/api/emergencies', { timeout: 10000 }),
  getAlert: (emergencyId) => axios.get(`http://localhost:8083/api/emergencies/${emergencyId}`, { timeout: 10000 }),
  updateStatus: (emergencyId, statusData) => axios.put(
    `http://localhost:8083/api/emergencies/${emergencyId}/status`,
    statusData,
    { timeout: 10000 }
  ),
  getStats: (hoursBack = 24) => axios.get(
    `http://localhost:8083/api/emergencies/stats?hoursBack=${hoursBack}`,
    { timeout: 10000 }
  ),
};

// ==================== EVENTS SERVICE (GraphQL) ====================
export const eventsAPI = {
  getAllEvents: () => axios.post(
    'http://localhost:8084/graphql',
    {
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
    },
    { timeout: 10000 }
  ),
  
  searchEvents: (keyword) => axios.post(
    'http://localhost:8084/graphql',
    {
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
            availableSpots
            organizer
            tags
          }
        }
      `
    },
    { timeout: 10000 }
  ),
  
  getUpcomingEvents: () => axios.post(
    'http://localhost:8084/graphql',
    {
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
            organizer
          }
        }
      `
    },
    { timeout: 10000 }
  ),
  
  filterEvents: (filters) => {
    const { type, category, freeOnly } = filters;
    let filterArgs = [];
    if (type) filterArgs.push(`type: ${type}`);
    if (category) filterArgs.push(`category: ${category}`);
    if (freeOnly !== undefined) filterArgs.push(`freeOnly: ${freeOnly}`);
    
    return axios.post(
      'http://localhost:8084/graphql',
      {
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
              organizer
              tags
            }
          }
        `
      },
      { timeout: 10000 }
    );
  },
  
  registerToEvent: (eventId) => axios.post(
    'http://localhost:8084/graphql',
    {
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
    },
    { timeout: 10000 }
  ),
};

// ==================== ORCHESTRATION SERVICE ====================
export const orchestrationAPI = {
  planJourney: (startLocation, endLocation) => axios.post(
    'http://localhost:8085/orchestration/plan-journey',
    null,
    {
      params: {
        startLocation: startLocation,
        endLocation: endLocation
      },
      timeout: 10000
    }
  ),
  
  health: () => axios.get('http://localhost:8085/orchestration/health', { timeout: 5000 }),
};

// ==================== HEALTH CHECKS ====================
export const healthAPI = {
  gateway: () => axios.get('http://localhost:8080/actuator/health', { timeout: 5000 }),
  mobility: () => axios.get('http://localhost:8081/mobility/actuator/health', { timeout: 5000 }),
  airQuality: () => axios.get('http://localhost:8082/airquality/actuator/health', { timeout: 5000 }),
  emergency: () => axios.get('http://localhost:8083/actuator/health', { timeout: 5000 }),
  events: () => axios.get('http://localhost:8084/actuator/health', { timeout: 5000 }),
  orchestration: () => axios.get('http://localhost:8085/orchestration/health', { timeout: 5000 }),
};

export default api;