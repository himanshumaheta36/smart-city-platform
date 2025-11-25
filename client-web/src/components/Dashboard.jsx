import React, { useState, useEffect } from 'react';
import { healthAPI } from '../services/api';
import '../App.css';

const Dashboard = () => {
  const [servicesHealth, setServicesHealth] = useState({});
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    checkServicesHealth();
  }, []);

  const checkServicesHealth = async () => {
    setLoading(true);
    const healthChecks = {
      'API Gateway': healthAPI.gateway,
      'Mobility Service': healthAPI.mobility,
      'Air Quality Service': healthAPI.airQuality,
      'Emergency Service': healthAPI.emergency,
      'Events Service': healthAPI.events,
      'Orchestration Service': healthAPI.orchestration,
    };

    const results = {};
    for (const [name, checkFn] of Object.entries(healthChecks)) {
      try {
        const response = await checkFn();
        results[name] = {
          status: response.data.status === 'UP' ? 'UP' : 'DOWN',
          color: response.data.status === 'UP' ? '#10b981' : '#ef4444'
        };
      } catch (error) {
        results[name] = {
          status: 'DOWN',
          color: '#ef4444'
        };
      }
    }
    setServicesHealth(results);
    setLoading(false);
  };

  const services = [
    {
      id: 'mobility',
      icon: '🚗',
      title: 'Mobilité Intelligente',
      description: 'Gestion des transports publics urbains',
      protocol: 'REST',
      port: '8081',
      endpoint: '/api/mobility',
      features: ['Lignes de transport', 'Horaires en temps réel', 'Info trafic'],
      color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
    },
    {
      id: 'air-quality',
      icon: '🌫️',
      title: 'Qualité de l\'Air',
      description: 'Surveillance de la pollution atmosphérique',
      protocol: 'SOAP',
      port: '8082',
      endpoint: '/api/air-quality',
      features: ['Indice AQI', 'Polluants', 'Comparaison zones'],
      color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
    },
    {
      id: 'emergency',
      icon: '🚨',
      title: 'Service d\'Urgences',
      description: 'Alertes et gestion de crises en temps réel',
      protocol: 'gRPC',
      port: '8083',
      endpoint: '/api/emergency',
      features: ['Alertes rapides', 'Streaming temps réel', 'Ressources'],
      color: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)'
    },
    {
      id: 'events',
      icon: '🎭',
      title: 'Événements Urbains',
      description: 'Agenda culturel et communautaire',
      protocol: 'GraphQL',
      port: '8084',
      endpoint: '/api/events',
      features: ['Requêtes flexibles', 'Filtres avancés', 'Inscriptions'],
      color: 'linear-gradient(135deg, #30cfd0 0%, #330867 100%)'
    }
  ];

  const architectureInfo = [
    {
      title: 'API Gateway',
      icon: '🚪',
      description: 'Point d\'entrée unique pour tous les services',
      tech: 'Spring Cloud Gateway',
      features: ['Routing', 'Load Balancing', 'Circuit Breaker']
    },
    {
      title: 'Service Discovery',
      icon: '🔍',
      description: 'Services communiquent via le gateway',
      tech: 'Docker Networking',
      features: ['Service Resolution', 'Health Checks', 'Auto-scaling']
    },
    {
      title: 'Interopérabilité',
      icon: '🔄',
      description: '4 protocoles différents, 1 plateforme',
      tech: 'Multi-Protocol',
      features: ['REST', 'SOAP', 'gRPC', 'GraphQL']
    }
  ];

  return (
    <div>
      {/* Hero Section */}
      <div className="card" style={{
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        color: 'white',
        padding: '3rem 2rem',
        marginBottom: '2rem'
      }}>
        <div style={{ textAlign: 'center' }}>
          <h1 style={{ fontSize: '2.5rem', marginBottom: '1rem', fontWeight: 'bold' }}>
            🏙️ Plateforme Ville Intelligente
          </h1>
          <p style={{ fontSize: '1.2rem', opacity: 0.9, maxWidth: '800px', margin: '0 auto' }}>
            Une architecture microservices démontrant l'interopérabilité entre REST, SOAP, gRPC et GraphQL
          </p>
          <div style={{ marginTop: '2rem', display: 'flex', gap: '1rem', justifyContent: 'center', flexWrap: 'wrap' }}>
            <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '20px' }}>
              ⚡ Temps réel
            </div>
            <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '20px' }}>
              🔒 Sécurisé
            </div>
            <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '20px' }}>
              📈 Scalable
            </div>
            <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '20px' }}>
              🌐 Interopérable
            </div>
          </div>
        </div>
      </div>

      {/* Services Status */}
      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
          <h2>📊 État des Services</h2>
          <button 
            onClick={checkServicesHealth} 
            className="btn btn-secondary"
            disabled={loading}
          >
            {loading ? '🔄 Vérification...' : '🔄 Actualiser'}
          </button>
        </div>
        
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '1rem' }}>
          {Object.entries(servicesHealth).map(([name, health]) => (
            <div 
              key={name}
              style={{
                padding: '1rem',
                background: health.status === 'UP' ? '#ecfdf5' : '#fef2f2',
                borderLeft: `4px solid ${health.color}`,
                borderRadius: '8px',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center'
              }}
            >
              <div>
                <div style={{ fontWeight: '600', marginBottom: '0.25rem' }}>{name}</div>
                <div style={{ fontSize: '0.875rem', color: '#666' }}>
                  {health.status}
                </div>
              </div>
              <div style={{ 
                width: '12px', 
                height: '12px', 
                borderRadius: '50%', 
                background: health.color,
                boxShadow: `0 0 8px ${health.color}`
              }} />
            </div>
          ))}
        </div>
      </div>

      {/* Services Cards */}
      <div className="card">
        <h2>🎯 Services Disponibles</h2>
        <div style={{ 
          display: 'grid', 
          gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', 
          gap: '1.5rem',
          marginTop: '1.5rem'
        }}>
          {services.map(service => (
            <div 
              key={service.id}
              style={{
                background: 'white',
                borderRadius: '12px',
                overflow: 'hidden',
                boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
                transition: 'transform 0.3s ease, box-shadow 0.3s ease',
                cursor: 'pointer'
              }}
              onMouseEnter={(e) => {
                e.currentTarget.style.transform = 'translateY(-5px)';
                e.currentTarget.style.boxShadow = '0 10px 20px rgba(0,0,0,0.15)';
              }}
              onMouseLeave={(e) => {
                e.currentTarget.style.transform = 'translateY(0)';
                e.currentTarget.style.boxShadow = '0 4px 6px rgba(0,0,0,0.1)';
              }}
            >
              <div style={{ 
                background: service.color,
                padding: '2rem',
                textAlign: 'center',
                color: 'white'
              }}>
                <div style={{ fontSize: '3rem', marginBottom: '0.5rem' }}>{service.icon}</div>
                <h3 style={{ margin: '0.5rem 0', fontSize: '1.25rem' }}>{service.title}</h3>
                <div style={{ 
                  background: 'rgba(255,255,255,0.2)',
                  display: 'inline-block',
                  padding: '0.25rem 0.75rem',
                  borderRadius: '12px',
                  fontSize: '0.875rem',
                  marginTop: '0.5rem'
                }}>
                  {service.protocol} • Port {service.port}
                </div>
              </div>
              
              <div style={{ padding: '1.5rem' }}>
                <p style={{ color: '#666', marginBottom: '1rem', lineHeight: '1.5' }}>
                  {service.description}
                </p>
                
                <div style={{ marginBottom: '1rem' }}>
                  <div style={{ fontSize: '0.75rem', color: '#999', marginBottom: '0.5rem' }}>
                    ENDPOINT
                  </div>
                  <code style={{ 
                    background: '#f3f4f6',
                    padding: '0.5rem',
                    borderRadius: '4px',
                    fontSize: '0.875rem',
                    display: 'block'
                  }}>
                    {service.endpoint}
                  </code>
                </div>
                
                <div>
                  <div style={{ fontSize: '0.75rem', color: '#999', marginBottom: '0.5rem' }}>
                    FONCTIONNALITÉS
                  </div>
                  <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.5rem' }}>
                    {service.features.map((feature, idx) => (
                      <span 
                        key={idx}
                        style={{
                          background: '#f3f4f6',
                          padding: '0.25rem 0.5rem',
                          borderRadius: '12px',
                          fontSize: '0.75rem',
                          color: '#666'
                        }}
                      >
                        {feature}
                      </span>
                    ))}
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Architecture */}
      <div className="card">
        <h2>🏗️ Architecture de la Plateforme</h2>
        <div style={{ 
          display: 'grid', 
          gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', 
          gap: '1rem',
          marginTop: '1.5rem'
        }}>
          {architectureInfo.map((item, idx) => (
            <div 
              key={idx}
              style={{
                padding: '1.5rem',
                background: '#f9fafb',
                borderRadius: '8px',
                border: '2px solid #e5e7eb'
              }}
            >
              <div style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>{item.icon}</div>
              <h3 style={{ marginBottom: '0.5rem', fontSize: '1.125rem' }}>{item.title}</h3>
              <p style={{ color: '#666', fontSize: '0.875rem', marginBottom: '0.75rem' }}>
                {item.description}
              </p>
              <div style={{ 
                background: 'white',
                padding: '0.5rem',
                borderRadius: '4px',
                marginBottom: '0.5rem'
              }}>
                <div style={{ fontSize: '0.75rem', color: '#999' }}>TECHNOLOGIE</div>
                <div style={{ fontWeight: '600', fontSize: '0.875rem' }}>{item.tech}</div>
              </div>
              <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.25rem' }}>
                {item.features.map((feature, i) => (
                  <span 
                    key={i}
                    style={{
                      fontSize: '0.75rem',
                      background: '#667eea',
                      color: 'white',
                      padding: '0.25rem 0.5rem',
                      borderRadius: '8px'
                    }}
                  >
                    {feature}
                  </span>
                ))}
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Quick Start */}
      <div className="card" style={{ background: '#fffbeb', border: '2px solid #fcd34d' }}>
        <h2>🚀 Guide de Démarrage Rapide</h2>
        <div style={{ marginTop: '1rem' }}>
          <div style={{ marginBottom: '1.5rem' }}>
            <h3 style={{ fontSize: '1rem', marginBottom: '0.5rem' }}>1️⃣ Vérifier les services</h3>
            <p style={{ color: '#666', fontSize: '0.875rem' }}>
              Assurez-vous que tous les services sont en état "UP" dans le panneau ci-dessus.
            </p>
          </div>
          
          <div style={{ marginBottom: '1.5rem' }}>
            <h3 style={{ fontSize: '1rem', marginBottom: '0.5rem' }}>2️⃣ Explorer les services</h3>
            <p style={{ color: '#666', fontSize: '0.875rem' }}>
              Utilisez le menu de navigation pour tester chaque service individuellement.
            </p>
          </div>
          
          <div style={{ marginBottom: '1.5rem' }}>
            <h3 style={{ fontSize: '1rem', marginBottom: '0.5rem' }}>3️⃣ Tester l'orchestration</h3>
            <p style={{ color: '#666', fontSize: '0.875rem' }}>
              Utilisez le "Planificateur de Trajet" pour voir comment les services communiquent entre eux.
            </p>
          </div>
          
          <div>
            <h3 style={{ fontSize: '1rem', marginBottom: '0.5rem' }}>📚 Documentation</h3>
            <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
              <a 
                href="http://localhost:8082/airquality/ws/airquality.wsdl" 
                target="_blank" 
                rel="noopener noreferrer"
                style={{ 
                  background: '#667eea',
                  color: 'white',
                  padding: '0.5rem 1rem',
                  borderRadius: '6px',
                  textDecoration: 'none',
                  fontSize: '0.875rem'
                }}
              >
                📄 WSDL (SOAP)
              </a>
              <a 
                href="http://localhost:8084/graphiql" 
                target="_blank" 
                rel="noopener noreferrer"
                style={{ 
                  background: '#667eea',
                  color: 'white',
                  padding: '0.5rem 1rem',
                  borderRadius: '6px',
                  textDecoration: 'none',
                  fontSize: '0.875rem'
                }}
              >
                🔍 GraphiQL
              </a>
              <a 
                href="http://localhost:8081/mobility/swagger-ui.html" 
                target="_blank" 
                rel="noopener noreferrer"
                style={{ 
                  background: '#667eea',
                  color: 'white',
                  padding: '0.5rem 1rem',
                  borderRadius: '6px',
                  textDecoration: 'none',
                  fontSize: '0.875rem'
                }}
              >
                📖 Swagger (REST)
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;