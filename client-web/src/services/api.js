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
  getAllZones: async () => {
    try {
      const response = await axios.post(
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
            'SOAPAction': 'http://smartcity.com/airquality/getAllZones'
          },
          timeout: 15000
        }
      );
      console.log('✅ SOAP getAllZones response received');
      return response;
    } catch (error) {
      console.error('❌ SOAP getAllZones error:', error.message);
      throw error;
    }
  },
  
  // Qualité d'air par zone
  getAirQuality: async (zoneName) => {
    try {
      const response = await axios.post(
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
            'SOAPAction': 'http://smartcity.com/airquality/getAirQuality'
          },
          timeout: 15000
        }
      );
      console.log('✅ SOAP getAirQuality response received');
      return response;
    } catch (error) {
      console.error('❌ SOAP getAirQuality error:', error.message);
      throw error;
    }
  },
  
  // Comparer deux zones
  compareZones: async (zone1, zone2) => {
    try {
      const response = await axios.post(
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
            'SOAPAction': 'http://smartcity.com/airquality/compareZones'
          },
          timeout: 15000
        }
      );
      console.log('✅ SOAP compareZones response received');
      return response;
    } catch (error) {
      console.error('❌ SOAP compareZones error:', error.message);
      throw error;
    }
  },
};

// ==================== EMERGENCY SERVICE (gRPC via REST) ====================
export const emergencyAPI = {
  createAlert: (alertData) => axios.post(
    'http://localhost:8083/api/emergencies',
    alertData,
    { 
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json'
      }
    }
  ),
  getAlerts: () => axios.get('http://localhost:8083/api/emergencies', { timeout: 10000 }),
  getAlert: (emergencyId) => axios.get(`http://localhost:8083/api/emergencies/${emergencyId}`, { timeout: 10000 }),
  updateStatus: (emergencyId, statusData) => axios.put(
    `http://localhost:8083/api/emergencies/${emergencyId}/status`,
    statusData,
    { 
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json'
      }
    }
  ),
  getStats: (hoursBack = 24) => axios.get(
    `http://localhost:8083/api/emergencies/stats?hoursBack=${hoursBack}`,
    { timeout: 10000 }
  ),
};

// ==================== EVENTS SERVICE (GraphQL) ====================
export const eventsAPI = {
  getAllEvents: async () => {
    try {
      const response = await axios.post(
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
              }
            }
          `
        },
        { 
          timeout: 10000,
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );
      console.log('✅ GraphQL getAllEvents response:', response.data);
      return response;
    } catch (error) {
      console.error('❌ GraphQL getAllEvents error:', error.message);
      throw error;
    }
  },
  
  searchEvents: async (keyword) => {
    try {
      const response = await axios.post(
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
        { 
          timeout: 10000,
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );
      return response;
    } catch (error) {
      console.error('❌ GraphQL searchEvents error:', error.message);
      throw error;
    }
  },
  
  getUpcomingEvents: async () => {
    try {
      const response = await axios.post(
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
                tags
              }
            }
          `
        },
        { 
          timeout: 10000,
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );
      return response;
    } catch (error) {
      console.error('❌ GraphQL getUpcomingEvents error:', error.message);
      throw error;
    }
  },
  
  filterEvents: async (filters) => {
    const { type, category, freeOnly } = filters;
    let filterArgs = [];
    if (type) filterArgs.push(`type: ${type}`);
    if (category) filterArgs.push(`category: ${category}`);
    if (freeOnly !== undefined) filterArgs.push(`freeOnly: ${freeOnly}`);
    
    try {
      const response = await axios.post(
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
        { 
          timeout: 10000,
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );
      return response;
    } catch (error) {
      console.error('❌ GraphQL filterEvents error:', error.message);
      throw error;
    }
  },
  
  registerToEvent: async (eventId) => {
    try {
      const response = await axios.post(
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
        { 
          timeout: 10000,
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );
      return response;
    } catch (error) {
      console.error('❌ GraphQL registerToEvent error:', error.message);
      throw error;
    }
  },
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
  gateway: async () => {
    try {
      const response = await axios.get('http://localhost:8080/actuator/health', { timeout: 5000 });
      return response;
    } catch (error) {
      console.error('Gateway health check failed:', error.message);
      throw error;
    }
  },
  
  mobility: async () => {
    try {
      const response = await axios.get('http://localhost:8081/mobility/actuator/health', { 
        timeout: 5000,
        validateStatus: () => true // Accepter tous les status codes
      });
      if (response.status === 200 && response.data?.status === 'UP') {
        return response;
      }
      throw new Error('Service not healthy');
    } catch (error) {
      console.error('Mobility health check failed:', error.message);
      throw error;
    }
  },
  
  airQuality: async () => {
    try {
      // Essayer d'abord l'actuator
      const response = await axios.get('http://localhost:8082/airquality/actuator/health', { timeout: 5000 });
      return response;
    } catch (error) {
      console.error('Air Quality health check failed:', error.message);
      // Si l'actuator échoue, essayer le WSDL comme fallback
      try {
        await axios.get('http://localhost:8082/airquality/ws/airquality.wsdl', { timeout: 5000 });
        return { data: { status: 'UP' } };
      } catch (wsdlError) {
        throw error;
      }
    }
  },
  
  emergency: async () => {
    try {
      const response = await axios.get('http://localhost:8083/actuator/health', { 
        timeout: 5000,
        validateStatus: () => true
      });
      if (response.status === 200 && response.data?.status === 'UP') {
        return response;
      }
      throw new Error('Service not healthy');
    } catch (error) {
      console.error('Emergency health check failed:', error.message);
      throw error;
    }
  },
  
  events: async () => {
    try {
      const response = await axios.get('http://localhost:8084/actuator/health', { timeout: 5000 });
      return response;
    } catch (error) {
      console.error('Events health check failed:', error.message);
      // Essayer GraphQL comme fallback
      try {
        await axios.post('http://localhost:8084/graphql', {
          query: '{ __typename }'
        }, { timeout: 5000 });
        return { data: { status: 'UP' } };
      } catch (graphqlError) {
        throw error;
      }
    }
  },
  
  orchestration: async () => {
    try {
      const response = await axios.get('http://localhost:8085/orchestration/health', { timeout: 5000 });
      return response;
    } catch (error) {
      console.error('Orchestration health check failed:', error.message);
      throw error;
    }
  },
};

export default api;