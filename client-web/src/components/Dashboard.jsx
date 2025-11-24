import React from 'react';
import '../App.css';

const Dashboard = () => {
  const services = [
    {
      id: 'mobility',
      title: '🚗 Mobilité Intelligente',
      description: 'Transports publics, horaires et état du trafic',
      endpoint: '/api/mobility'
    },
    {
      id: 'air-quality',
      title: '🌫️ Qualité de l\'Air',
      description: 'Indices AQI et polluants par zone',
      endpoint: '/api/air-quality'
    },
    {
      id: 'emergency',
      title: '🚨 Service d\'Urgences',
      description: 'Alertes en temps réel et gestion des crises',
      endpoint: '/api/emergency'
    },
    {
      id: 'events',
      title: '🎭 Événements Urbains',
      description: 'Agenda des événements culturels et communautaires',
      endpoint: '/api/events'
    }
  ];

  const protocols = [
    { service: 'Mobilité', protocol: 'REST', port: '8081' },
    { service: 'Qualité d\'Air', protocol: 'SOAP', port: '8082' },
    { service: 'Urgences', protocol: 'gRPC', port: '8083' },
    { service: 'Événements', protocol: 'GraphQL', port: '8084' }
  ];

  return (
    <div>
      <div className="card">
        <h2>🏙️ Tableau de Bord - Ville Intelligente</h2>
        <p>Plateforme de services urbains interopérables utilisant 4 protocoles différents.</p>
        
        <div className="service-grid">
          {services.map(service => (
            <div key={service.id} className="service-card">
              <h3>{service.title}</h3>
              <p>{service.description}</p>
              <small>Endpoint: {service.endpoint}</small>
            </div>
          ))}
        </div>
      </div>

      <div className="card">
        <h2>📊 Architecture Microservices</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '1rem' }}>
          {protocols.map((item, index) => (
            <div key={index} style={{ 
              padding: '1rem', 
              background: '#f8f9fa', 
              borderRadius: '8px',
              borderLeft: '4px solid #667eea'
            }}>
              <h4>{item.service}</h4>
              <p><strong>Protocole:</strong> {item.protocol}</p>
              <p><strong>Port:</strong> {item.port}</p>
            </div>
          ))}
        </div>
      </div>

      <div className="card">
        <h2>🚀 Comment démarrer</h2>
        <ol style={{ lineHeight: '2', marginLeft: '1.5rem' }}>
          <li>Lancer tous les services: <code>docker-compose up -d</code></li>
          <li>API Gateway disponible sur: <code>http://localhost:8080</code></li>
          <li>Client web sur: <code>http://localhost:3000</code></li>
          <li>Naviguer entre les différents services via le menu</li>
        </ol>
      </div>
    </div>
  );
};

export default Dashboard;