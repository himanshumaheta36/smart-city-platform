import axios from 'axios';

// CONFIGURATION CORRIGÉE - URLs DIRECTES AVEC BONS CHEMINS
const API_CONFIG = {
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  }
};

// ==================== MOBILITY SERVICE (URLs CORRIGÉES) ====================
export const mobilityAPI = {
  getTransportLines: () => axios.get('http://localhost:8081/mobility/api/transport-lines', API_CONFIG),
  getTransportLineByNumber: (lineNumber) => axios.get(`http://localhost:8081/mobility/api/transport-lines/number/${lineNumber}`, API_CONFIG),
  getTransportLinesByType: (type) => axios.get(`http://localhost:8081/mobility/api/transport-lines/type/${type}`, API_CONFIG),
  getSchedulesByLine: (lineNumber) => axios.get(`http://localhost:8081/mobility/api/schedules/line/${lineNumber}`, API_CONFIG),
  getTrafficInfo: () => axios.get('http://localhost:8081/mobility/api/traffic-info/active', API_CONFIG),
};

// ==================== AIR QUALITY SERVICE (URLs CORRIGÉES) ====================
const soapConfig = {
  timeout: 20000,
  headers: { 
    'Content-Type': 'text/xml; charset=utf-8',
    'SOAPAction': ''
  }
};

export const airQualityAPI = {
  getAllZones: async () => {
    const soapRequest = `<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
  <soapenv:Header/>
  <soapenv:Body>
    <air:GetAllZonesRequest/>
  </soapenv:Body>
</soapenv:Envelope>`;
    
    return axios.post('http://localhost:8082/airquality/ws', soapRequest, soapConfig);
  },
  
  getAirQuality: async (zoneName) => {
    const soapRequest = `<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
  <soapenv:Header/>
  <soapenv:Body>
    <air:GetAirQualityRequest>
      <air:zoneName>${zoneName}</air:zoneName>
    </air:GetAirQualityRequest>
  </soapenv:Body>
</soapenv:Envelope>`;
    
    return axios.post('http://localhost:8082/airquality/ws', soapRequest, soapConfig);
  },
  
  compareZones: async (zone1, zone2) => {
    const soapRequest = `<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
  <soapenv:Header/>
  <soapenv:Body>
    <air:CompareZonesRequest>
      <air:zone1>${zone1}</air:zone1>
      <air:zone2>${zone2}</air:zone2>
    </air:CompareZonesRequest>
  </soapenv:Body>
</soapenv:Envelope>`;
    
    return axios.post('http://localhost:8082/airquality/ws', soapRequest, soapConfig);
  },
};

// ==================== EMERGENCY SERVICE (URLs CORRIGÉES) ====================
export const emergencyAPI = {
  createAlert: (alertData) => axios.post('http://localhost:8083/api/emergencies', alertData, API_CONFIG),
  getAlerts: () => axios.get('http://localhost:8083/api/emergencies', API_CONFIG),
  getAlert: (emergencyId) => axios.get(`http://localhost:8083/api/emergencies/${emergencyId}`, API_CONFIG),
  updateStatus: (emergencyId, statusData) => axios.put(`http://localhost:8083/api/emergencies/${emergencyId}/status`, statusData, API_CONFIG),
  getStats: (hoursBack = 24) => axios.get(`http://localhost:8083/api/emergencies/stats?hoursBack=${hoursBack}`, API_CONFIG),
};

// ==================== EVENTS SERVICE (URLs CORRIGÉES) ====================
const graphqlConfig = {
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
};

export const eventsAPI = {
  getAllEvents: async () => {
    return axios.post('http://localhost:8084/graphql', {
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
    }, graphqlConfig);
  },
  
  searchEvents: async (keyword) => {
    return axios.post('http://localhost:8084/graphql', {
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
    }, graphqlConfig);
  },
  
  getUpcomingEvents: async () => {
    return axios.post('http://localhost:8084/graphql', {
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
    }, graphqlConfig);
  },
  
  filterEvents: async (filters) => {
    const { type, category, freeOnly } = filters;
    let filterArgs = [];
    if (type) filterArgs.push(`type: "${type}"`);
    if (category) filterArgs.push(`category: "${category}"`);
    if (freeOnly !== undefined) filterArgs.push(`freeOnly: ${freeOnly}`);
    
    return axios.post('http://localhost:8084/graphql', {
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
    }, graphqlConfig);
  },
  
  registerToEvent: async (eventId) => {
    return axios.post('http://localhost:8084/graphql', {
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
    }, graphqlConfig);
  },
};

// ==================== ORCHESTRATION SERVICE (URLs CORRIGÉES) ====================
export const orchestrationAPI = {
  planJourney: (startLocation, endLocation) => axios.post(
    'http://localhost:8085/orchestration/plan-journey',
    null,
    {
      params: {
        startLocation: startLocation,
        endLocation: endLocation
      },
      timeout: 20000
    }
  ),
  
  health: () => axios.get('http://localhost:8085/orchestration/health', { timeout: 5000 }),
};

// ==================== HEALTH CHECKS (URLs CORRIGÉES) ====================
export const healthAPI = {
  gateway: () => axios.get('http://localhost:8080/actuator/health', { timeout: 5000 }),
  mobility: () => axios.get('http://localhost:8081/mobility/actuator/health', { timeout: 5000 }),
  airQuality: () => axios.get('http://localhost:8082/airquality/actuator/health', { timeout: 5000 }),
  emergency: () => axios.get('http://localhost:8083/actuator/health', { timeout: 5000 }),
  events: () => axios.get('http://localhost:8084/actuator/health', { timeout: 5000 }),
  orchestration: () => axios.get('http://localhost:8085/orchestration/health', { timeout: 5000 }),
};

export default axios;