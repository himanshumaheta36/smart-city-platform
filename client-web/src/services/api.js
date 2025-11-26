import axios from 'axios';

// Configuration avec fallback automatique
const API_CONFIG = {
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  }
};

// Fonction utilitaire pour essayer Gateway puis fallback direct
const tryRequest = async (gatewayRequest, directRequest) => {
  try {
    console.log('🔄 Tentative via Gateway...');
    return await gatewayRequest();
  } catch (gatewayError) {
    console.log('❌ Gateway échoué, tentative directe...');
    try {
      return await directRequest();
    } catch (directError) {
      console.error('❌ Les deux méthodes ont échoué');
      throw gatewayError; // Retourne l'erreur originale
    }
  }
};

// ==================== MOBILITY SERVICE ====================
export const mobilityAPI = {
  getTransportLines: () => tryRequest(
    () => axios.get('http://localhost:8080/api/mobility/transport-lines', API_CONFIG),
    () => axios.get('http://localhost:8081/mobility/api/transport-lines', API_CONFIG)
  ),
  
  getTransportLineByNumber: (lineNumber) => tryRequest(
    () => axios.get(`http://localhost:8080/api/mobility/transport-lines/number/${lineNumber}`, API_CONFIG),
    () => axios.get(`http://localhost:8081/mobility/api/transport-lines/number/${lineNumber}`, API_CONFIG)
  ),
  
  getTransportLinesByType: (type) => tryRequest(
    () => axios.get(`http://localhost:8080/api/mobility/transport-lines/type/${type}`, API_CONFIG),
    () => axios.get(`http://localhost:8081/mobility/api/transport-lines/type/${type}`, API_CONFIG)
  ),
  
  getSchedulesByLine: (lineNumber) => tryRequest(
    () => axios.get(`http://localhost:8080/api/mobility/schedules/line/${lineNumber}`, API_CONFIG),
    () => axios.get(`http://localhost:8081/mobility/api/schedules/line/${lineNumber}`, API_CONFIG)
  ),
  
  getTrafficInfo: () => tryRequest(
    () => axios.get('http://localhost:8080/api/mobility/traffic-info/active', API_CONFIG),
    () => axios.get('http://localhost:8081/mobility/api/traffic-info/active', API_CONFIG)
  ),
};

// ==================== AIR QUALITY SERVICE ====================
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

    return tryRequest(
      () => axios.post('http://localhost:8080/api/air-quality/ws', soapRequest, soapConfig),
      () => axios.post('http://localhost:8082/airquality/ws', soapRequest, soapConfig)
    );
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

    return tryRequest(
      () => axios.post('http://localhost:8080/api/air-quality/ws', soapRequest, soapConfig),
      () => axios.post('http://localhost:8082/airquality/ws', soapRequest, soapConfig)
    );
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

    return tryRequest(
      () => axios.post('http://localhost:8080/api/air-quality/ws', soapRequest, soapConfig),
      () => axios.post('http://localhost:8082/airquality/ws', soapRequest, soapConfig)
    );
  },
};

// ==================== EMERGENCY SERVICE ====================
export const emergencyAPI = {
  createAlert: (alertData) => tryRequest(
    () => axios.post('http://localhost:8080/api/emergency/emergencies', alertData, API_CONFIG),
    () => axios.post('http://localhost:8083/api/emergencies', alertData, API_CONFIG)
  ),
  
  getAlerts: () => tryRequest(
    () => axios.get('http://localhost:8080/api/emergency/emergencies', API_CONFIG),
    () => axios.get('http://localhost:8083/api/emergencies', API_CONFIG)
  ),
  
  getAlert: (emergencyId) => tryRequest(
    () => axios.get(`http://localhost:8080/api/emergency/emergencies/${emergencyId}`, API_CONFIG),
    () => axios.get(`http://localhost:8083/api/emergencies/${emergencyId}`, API_CONFIG)
  ),
  
  updateStatus: (emergencyId, statusData) => tryRequest(
    () => axios.put(`http://localhost:8080/api/emergency/emergencies/${emergencyId}/status`, statusData, API_CONFIG),
    () => axios.put(`http://localhost:8083/api/emergencies/${emergencyId}/status`, statusData, API_CONFIG)
  ),
  
  getStats: (hoursBack = 24) => tryRequest(
    () => axios.get(`http://localhost:8080/api/emergency/emergencies/stats?hoursBack=${hoursBack}`, API_CONFIG),
    () => axios.get(`http://localhost:8083/api/emergencies/stats?hoursBack=${hoursBack}`, API_CONFIG)
  ),
};

// ==================== EVENTS SERVICE ====================
const graphqlConfig = {
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
};

const graphqlQueries = {
  getAllEvents: `
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
  `,
  searchEvents: (keyword) => `
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
  `,
  getUpcomingEvents: `
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
};

export const eventsAPI = {
  getAllEvents: () => tryRequest(
    () => axios.post('http://localhost:8080/api/events/graphql', { query: graphqlQueries.getAllEvents }, graphqlConfig),
    () => axios.post('http://localhost:8084/graphql', { query: graphqlQueries.getAllEvents }, graphqlConfig)
  ),
  
  searchEvents: (keyword) => tryRequest(
    () => axios.post('http://localhost:8080/api/events/graphql', { query: graphqlQueries.searchEvents(keyword) }, graphqlConfig),
    () => axios.post('http://localhost:8084/graphql', { query: graphqlQueries.searchEvents(keyword) }, graphqlConfig)
  ),
  
  getUpcomingEvents: () => tryRequest(
    () => axios.post('http://localhost:8080/api/events/graphql', { query: graphqlQueries.getUpcomingEvents }, graphqlConfig),
    () => axios.post('http://localhost:8084/graphql', { query: graphqlQueries.getUpcomingEvents }, graphqlConfig)
  ),
  
  filterEvents: async (filters) => {
    const { type, category, freeOnly } = filters;
    let filterArgs = [];
    if (type) filterArgs.push(`type: "${type}"`);
    if (category) filterArgs.push(`category: "${category}"`);
    if (freeOnly !== undefined) filterArgs.push(`freeOnly: ${freeOnly}`);
    
    const query = `
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
    `;

    return tryRequest(
      () => axios.post('http://localhost:8080/api/events/graphql', { query }, graphqlConfig),
      () => axios.post('http://localhost:8084/graphql', { query }, graphqlConfig)
    );
  },
  
  registerToEvent: async (eventId) => {
    const query = `
      mutation {
        registerAttendee(eventId: ${eventId}) {
          id
          title
          registeredAttendees
          availableSpots
        }
      }
    `;

    return tryRequest(
      () => axios.post('http://localhost:8080/api/events/graphql', { query }, graphqlConfig),
      () => axios.post('http://localhost:8084/graphql', { query }, graphqlConfig)
    );
  },
};

// ==================== ORCHESTRATION SERVICE ====================
export const orchestrationAPI = {
  planJourney: (startLocation, endLocation) => tryRequest(
    () => axios.post(
      'http://localhost:8080/api/orchestration/plan-journey',
      null,
      {
        params: { startLocation, endLocation },
        timeout: 20000
      }
    ),
    () => axios.post(
      'http://localhost:8085/orchestration/plan-journey',
      null,
      {
        params: { startLocation, endLocation },
        timeout: 20000
      }
    )
  ),
  
  health: () => tryRequest(
    () => axios.get('http://localhost:8080/api/orchestration/health', { timeout: 5000 }),
    () => axios.get('http://localhost:8085/orchestration/health', { timeout: 5000 })
  ),
};

// ==================== HEALTH CHECKS ====================
const healthCheck = async (url) => {
  try {
    const response = await axios.get(url, { 
      timeout: 5000,
      validateStatus: () => true
    });
    return { data: { status: response.status === 200 ? 'UP' : 'DOWN' } };
  } catch (error) {
    return { data: { status: 'DOWN' } };
  }
};

export const healthAPI = {
  gateway: () => healthCheck('http://localhost:8080/actuator/health'),
  
  mobility: async () => {
    const gatewayResult = await healthCheck('http://localhost:8080/api/mobility/actuator/health');
    if (gatewayResult.data.status === 'UP') return gatewayResult;
    return await healthCheck('http://localhost:8081/mobility/actuator/health');
  },
  
  airQuality: async () => {
    const gatewayResult = await healthCheck('http://localhost:8080/api/air-quality/actuator/health');
    if (gatewayResult.data.status === 'UP') return gatewayResult;
    return await healthCheck('http://localhost:8082/airquality/actuator/health');
  },
  
  emergency: async () => {
    const gatewayResult = await healthCheck('http://localhost:8080/api/emergency/actuator/health');
    if (gatewayResult.data.status === 'UP') return gatewayResult;
    return await healthCheck('http://localhost:8083/actuator/health');
  },
  
  events: async () => {
    const gatewayResult = await healthCheck('http://localhost:8080/api/events/actuator/health');
    if (gatewayResult.data.status === 'UP') return gatewayResult;
    return await healthCheck('http://localhost:8084/actuator/health');
  },
  
  orchestration: async () => {
    const gatewayResult = await healthCheck('http://localhost:8080/api/orchestration/health');
    if (gatewayResult.data.status === 'UP') return gatewayResult;
    return await healthCheck('http://localhost:8085/orchestration/health');
  },
};

export default axios;