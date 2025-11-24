import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';  // Appel direct depuis le navigateur
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Services API
export const mobilityAPI = {
  getTransportOptions: () => api.get('/mobility/transport-options'),
  getSchedules: (line) => api.get(`/mobility/schedules/${line}`),
  getTrafficStatus: () => api.get('/mobility/traffic-status'),
};

export const airQualityAPI = {
  getAirQuality: (location) => api.get(`/air-quality/air-quality?location=${location}`),
  getPollutants: (location) => api.get(`/air-quality/pollutants?location=${location}`),
  compareAirQuality: (location1, location2) => 
    api.get(`/air-quality/compare?location1=${location1}&location2=${location2}`),
};

export const emergencyAPI = {
  sendAlert: (alertData) => api.post('/emergency/alert', alertData),
  getAlerts: () => api.get('/emergency/alerts'),
};

export const eventsAPI = {
  getAllEvents: () => api.get('/events/graphql?query={getAllEvents{id,title,location,startDateTime}}'),
  searchEvents: (keyword) => 
    api.get(`/events/graphql?query={searchEvents(keyword:"${keyword}"){id,title,location}}`),
};

export const orchestrationAPI = {
  planJourney: (startLocation, endLocation) => 
    api.post(`/orchestration/plan-journey?startLocation=${startLocation}&endLocation=${endLocation}`),
};

export default api;