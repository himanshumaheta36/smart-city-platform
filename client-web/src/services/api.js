import axios from 'axios';

// Configuration de base
const API_CONFIG = {
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  }
};

// Fonction utilitaire pour essayer Gateway puis fallback direct
const tryRequest = async (gatewayRequest, directRequest) => {
  try {
    const result = await gatewayRequest();
    return result;
  } catch (gatewayError) {
    console.log('Gateway failed, trying direct...', gatewayError.message);
    try {
      return await directRequest();
    } catch (directError) {
      throw directError;
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
    () => axios.post('http://localhost:8080/api/emergency', alertData, API_CONFIG),
    () => axios.post('http://localhost:8083/api/emergencies', alertData, API_CONFIG)
  ),
  
  getAlerts: () => tryRequest(
    () => axios.get('http://localhost:8080/api/emergency', API_CONFIG),
    () => axios.get('http://localhost:8083/api/emergencies', API_CONFIG)
  ),
  
  getAlert: (emergencyId) => tryRequest(
    () => axios.get(`http://localhost:8080/api/emergency/${emergencyId}`, API_CONFIG),
    () => axios.get(`http://localhost:8083/api/emergencies/${emergencyId}`, API_CONFIG)
  ),
  
  updateStatus: (emergencyId, statusData) => tryRequest(
    () => axios.put(`http://localhost:8080/api/emergency/${emergencyId}/status`, statusData, API_CONFIG),
    () => axios.put(`http://localhost:8083/api/emergencies/${emergencyId}/status`, statusData, API_CONFIG)
  ),
  
  getStats: (hoursBack = 24) => tryRequest(
    () => axios.get(`http://localhost:8080/api/emergency/stats?hoursBack=${hoursBack}`, API_CONFIG),
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
// Dans client-web/src/services/api.js

export const healthAPI = {
  gateway: async () => {
    try {
      const response = await axios.get('http://localhost:8080/actuator/health', { timeout: 5000 });
      return { data: { status: response.data?.status === 'UP' ? 'UP' : 'DOWN' } };
    } catch (error) {
      return { data: { status: 'DOWN' } };
    }
  },
  
  mobility: async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/mobility/transport-lines', { timeout: 5000 });
      if (response.status === 200) {
        return { data: { status: 'UP' } };
      }
    } catch (e) {}
    
    try {
      const response = await axios.get('http://localhost:8081/mobility/api/transport-lines', { timeout: 5000 });
      if (response.status === 200) {
        return { data: { status: 'UP' } };
      }
    } catch (e) {}
    
    return { data: { status: 'DOWN' } };
  },
  
  // CORRIGÉ - Air Quality utilise WSDL pour vérifier
  airQuality: async () => {
    try {
      // Vérifier si le WSDL est accessible via Gateway
      const response = await axios.get('http://localhost:8080/api/air-quality/ws/airquality.wsdl', { 
        timeout: 5000,
        responseType: 'text'
      });
      if (response.status === 200 && response.data.includes('wsdl')) {
        return { data: { status: 'UP' } };
      }
    } catch (e) {
      console.log('Gateway air quality check failed:', e.message);
    }
    
    try {
      // Vérifier directement
      const response = await axios.get('http://localhost:8082/airquality/ws/airquality.wsdl', { 
        timeout: 5000,
        responseType: 'text'
      });
      if (response.status === 200 && response.data.includes('wsdl')) {
        return { data: { status: 'UP' } };
      }
    } catch (e) {
      console.log('Direct air quality check failed:', e.message);
    }
    
    try {
      // Essayer l'actuator
      const response = await axios.get('http://localhost:8082/airquality/actuator/health', { timeout: 5000 });
      if (response.status === 200) {
        return { data: { status: 'UP' } };
      }
    } catch (e) {}
    
    return { data: { status: 'DOWN' } };
  },
  
  emergency: async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/emergency/health', { timeout: 5000 });
      if (response.data && response.data.status === 'UP') {
        return { data: { status: 'UP' } };
      }
      if (response.status === 200) {
        return { data: { status: 'UP' } };
      }
    } catch (e) {}
    
    try {
      const response = await axios.get('http://localhost:8083/api/emergencies/health', { timeout: 5000 });
      if (response.data && response.data.status === 'UP') {
        return { data: { status: 'UP' } };
      }
    } catch (e) {}
    
    try {
      const response = await axios.get('http://localhost:8083/api/emergencies', { timeout: 5000 });
      if (response.status === 200) {
        return { data: { status: 'UP' } };
      }
    } catch (e) {}
    
    return { data: { status: 'DOWN' } };
  },
  
  // CORRIGÉ - Events utilise GraphQL query
  events: async () => {
    try {
      // Essayer une requête GraphQL simple
      const query = '{"query": "{ getAllEvents { id } }"}';
      const response = await axios.post('http://localhost:8084/graphql', 
        JSON.parse(query),
        { 
          timeout: 5000,
          headers: { 'Content-Type': 'application/json' }
        }
      );
      if (response.status === 200 && response.data) {
        return { data: { status: 'UP' } };
      }
    } catch (e) {
      console.log('GraphQL check failed:', e.message);
    }
    
    try {
      const response = await axios.get('http://localhost:8084/actuator/health', { timeout: 5000 });
      if (response.status === 200) {
        return { data: { status: 'UP' } };
      }
    } catch (e) {}
    
    return { data: { status: 'DOWN' } };
  },
  
  orchestration: async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/orchestration/health', { timeout: 5000 });
      if (response.data && response.data.status === 'UP') {
        return { data: { status: 'UP' } };
      }
    } catch (e) {}
    
    try {
      const response = await axios.get('http://localhost:8085/orchestration/health', { timeout: 5000 });
      if (response.data && response.data.status === 'UP') {
        return { data: { status: 'UP' } };
      }
    } catch (e) {}
    
    return { data: { status: 'DOWN' } };
  },
};

export default axios;