import React, { useState } from 'react';
import { orchestrationAPI } from '../services/api';
import '../App.css';

const JourneyPlanner = () => {
  const [startLocation, setStartLocation] = useState('');
  const [endLocation, setEndLocation] = useState('');
  const [journeyPlan, setJourneyPlan] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  // Suggestions de lieux
  const locations = [
    'Centre-ville',
    'Quartier Nord',
    'Zone Industrielle',
    'Parc Central',
    'Banlieue Sud',
    'Quartier Est',
    'Zone Résidentielle',
    'District des Affaires'
  ];

  const handlePlanJourney = async (e) => {
    e.preventDefault();
    if (!startLocation || !endLocation) {
      setError('⚠️ Veuillez saisir les lieux de départ et d\'arrivée');
      return;
    }

    if (startLocation === endLocation) {
      setError('⚠️ Le lieu de départ et d\'arrivée doivent être différents');
      return;
    }

    setLoading(true);
    setError('');
    
    try {
      const response = await orchestrationAPI.planJourney(startLocation, endLocation);
      setJourneyPlan(response.data);
    } catch (err) {
      setError('❌ Erreur lors de la planification du trajet');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const swapLocations = () => {
    const temp = startLocation;
    setStartLocation(endLocation);
    setEndLocation(temp);
  };

  return (
    <div>
      {/* Header */}
      <div className="card" style={{ 
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        color: 'white'
      }}>
        <h2 style={{ marginBottom: '0.5rem' }}>🗺️ Planificateur de Trajet Intelligent</h2>
        <p style={{ opacity: 0.9, marginBottom: '1rem' }}>
          Service d'orchestration - Combine qualité d'air et options de transport
        </p>
        <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
          <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '8px' }}>
            <strong>Services utilisés:</strong> Air Quality + Mobility
          </div>
          <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '8px' }}>
            <strong>Port:</strong> 8085
          </div>
          <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '8px' }}>
            <strong>Endpoint:</strong> /api/orchestration
          </div>
        </div>
      </div>

      {/* Journey Form */}
      <div className="card">
        <h3 style={{ marginBottom: '1.5rem' }}>🚀 Planifier votre trajet</h3>
        
        <form onSubmit={handlePlanJourney}>
          <div style={{ position: 'relative', marginBottom: '2rem' }}>
            {/* Start Location */}
            <div style={{ marginBottom: '1rem' }}>
              <label style={{ 
                display: 'flex', 
                alignItems: 'center', 
                gap: '0.5rem',
                marginBottom: '0.5rem',
                fontWeight: '600'
              }}>
                <span style={{ 
                  background: '#10b981',
                  color: 'white',
                  width: '24px',
                  height: '24px',
                  borderRadius: '50%',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontSize: '0.75rem'
                }}>
                  A
                </span>
                Lieu de départ
              </label>
              <input
                type="text"
                list="start-locations"
                value={startLocation}
                onChange={(e) => setStartLocation(e.target.value)}
                placeholder="Sélectionnez ou tapez un lieu..."
                style={{ width: '100%', padding: '0.75rem', fontSize: '1rem' }}
              />
              <datalist id="start-locations">
                {locations.map(loc => (
                  <option key={loc} value={loc} />
                ))}
              </datalist>
            </div>

            {/* Swap Button */}
            <div style={{ 
              position: 'absolute',
              left: '50%',
              top: '50%',
              transform: 'translate(-50%, -50%)',
              zIndex: 10
            }}>
              <button
                type="button"
                onClick={swapLocations}
                style={{
                  width: '40px',
                  height: '40px',
                  borderRadius: '50%',
                  border: '2px solid #e5e7eb',
                  background: 'white',
                  cursor: 'pointer',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontSize: '1.25rem',
                  boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
                  transition: 'all 0.2s ease'
                }}
                onMouseEnter={(e) => {
                  e.currentTarget.style.transform = 'rotate(180deg)';
                  e.currentTarget.style.background = '#667eea';
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.transform = 'rotate(0deg)';
                  e.currentTarget.style.background = 'white';
                }}
              >
                🔄
              </button>
            </div>

            {/* End Location */}
            <div>
              <label style={{ 
                display: 'flex', 
                alignItems: 'center', 
                gap: '0.5rem',
                marginBottom: '0.5rem',
                fontWeight: '600'
              }}>
                <span style={{ 
                  background: '#ef4444',
                  color: 'white',
                  width: '24px',
                  height: '24px',
                  borderRadius: '50%',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontSize: '0.75rem'
                }}>
                  B
                </span>
                Lieu d'arrivée
              </label>
              <input
                type="text"
                list="end-locations"
                value={endLocation}
                onChange={(e) => setEndLocation(e.target.value)}
                placeholder="Sélectionnez ou tapez un lieu..."
                style={{ width: '100%', padding: '0.75rem', fontSize: '1rem' }}
              />
              <datalist id="end-locations">
                {locations.map(loc => (
                  <option key={loc} value={loc} />
                ))}
              </datalist>
            </div>
          </div>

          <button 
            type="submit" 
            className="btn btn-primary" 
            disabled={loading}
            style={{ width: '100%', padding: '1rem', fontSize: '1.125rem' }}
          >
            {loading ? '🔄 Planification en cours...' : '🗺️ Planifier le trajet'}
          </button>
        </form>

        {error && (
          <div className="error" style={{ marginTop: '1rem' }}>
            {error}
          </div>
        )}
      </div>

      {/* Journey Plan Results */}
      {journeyPlan && (
        <>
          {/* Route Overview */}
          <div className="card">
            <h3 style={{ marginBottom: '1rem' }}>📍 Votre Trajet</h3>
            <div style={{
              padding: '1.5rem',
              background: 'linear-gradient(to right, #10b981, #ef4444)',
              borderRadius: '12px',
              color: 'white',
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center'
            }}>
              <div style={{ textAlign: 'center', flex: 1 }}>
                <div style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>🅰️</div>
                <div style={{ fontSize: '1.125rem', fontWeight: '600' }}>
                  {journeyPlan.startLocation}
                </div>
              </div>
              
              <div style={{ padding: '0 2rem' }}>
                <div style={{ fontSize: '2rem' }}>→</div>
              </div>
              
              <div style={{ textAlign: 'center', flex: 1 }}>
                <div style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>🅱️</div>
                <div style={{ fontSize: '1.125rem', fontWeight: '600' }}>
                  {journeyPlan.endLocation}
                </div>
              </div>
            </div>
          </div>

          {/* Air Quality Analysis */}
          <div className="card">
            <h3 style={{ marginBottom: '1rem' }}>🌫️ Analyse de la Qualité de l'Air</h3>
            <div style={{
              padding: '1.5rem',
              background: journeyPlan.airQualityGood ? '#ecfdf5' : '#fff7ed',
              border: `3px solid ${journeyPlan.airQualityGood ? '#10b981' : '#f97316'}`,
              borderRadius: '12px'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '1rem' }}>
                <div style={{ 
                  fontSize: '3rem',
                  background: journeyPlan.airQualityGood ? '#10b981' : '#f97316',
                  width: '80px',
                  height: '80px',
                  borderRadius: '12px',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center'
                }}>
                  {journeyPlan.airQualityGood ? '✅' : '⚠️'}
                </div>
                <div style={{ flex: 1 }}>
                  <div style={{ 
                    fontSize: '1.25rem', 
                    fontWeight: '600',
                    color: journeyPlan.airQualityGood ? '#047857' : '#c2410c',
                    marginBottom: '0.5rem'
                  }}>
                    Qualité de l'air: {journeyPlan.airQualityGood ? 'Bonne' : 'Médiocre'}
                  </div>
                  <p style={{ 
                    color: journeyPlan.airQualityGood ? '#065f46' : '#9a3412',
                    lineHeight: '1.5'
                  }}>
                    {journeyPlan.recommendation}
                  </p>
                </div>
              </div>

              {!journeyPlan.airQualityGood && (
                <div style={{
                  marginTop: '1rem',
                  padding: '1rem',
                  background: 'white',
                  borderRadius: '8px',
                  border: '2px solid #fed7aa'
                }}>
                  <div style={{ fontWeight: '600', marginBottom: '0.5rem', color: '#c2410c' }}>
                    💡 Recommandations
                  </div>
                  <ul style={{ marginLeft: '1.5rem', color: '#9a3412' }}>
                    <li>Privilégiez les transports en commun fermés (métro, train)</li>
                    <li>Évitez les activités physiques intenses pendant le trajet</li>
                    <li>Portez un masque si nécessaire</li>
                  </ul>
                </div>
              )}
            </div>
          </div>

          {/* Transport Options */}
          <div className="card">
            <h3 style={{ marginBottom: '1rem' }}>🚌 Options de Transport</h3>
            {journeyPlan.transportOptions && journeyPlan.transportOptions.length > 0 ? (
              <div style={{ display: 'grid', gap: '1rem' }}>
                {journeyPlan.transportOptions.map((option, index) => (
                  <div 
                    key={index}
                    style={{
                      padding: '1.5rem',
                      border: '2px solid #e5e7eb',
                      borderRadius: '12px',
                      background: 'white',
                      transition: 'all 0.3s ease'
                    }}
                    onMouseEnter={(e) => {
                      e.currentTarget.style.borderColor = '#667eea';
                      e.currentTarget.style.boxShadow = '0 4px 6px rgba(0,0,0,0.1)';
                    }}
                    onMouseLeave={(e) => {
                      e.currentTarget.style.borderColor = '#e5e7eb';
                      e.currentTarget.style.boxShadow = 'none';
                    }}
                  >
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                      <div style={{ flex: 1 }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '0.75rem' }}>
                          <div style={{ fontSize: '2rem' }}>
                            {option.type === 'Bus' ? '🚌' : 
                             option.type === 'Métro' ? '🚇' : 
                             option.type === 'Vélo' ? '🚲' : 
                             option.type === 'Train' ? '🚆' : '🚗'}
                          </div>
                          <div>
                            <div style={{ fontSize: '1.25rem', fontWeight: '600' }}>
                              {option.type} - {option.line}
                            </div>
                            <div style={{ fontSize: '0.875rem', color: '#666' }}>
                              {option.status}
                            </div>
                          </div>
                        </div>
                        
                        <div style={{ display: 'flex', gap: '2rem', fontSize: '0.875rem' }}>
                          <div>
                            <span style={{ color: '#666' }}>Prochain départ:</span>
                            <span style={{ fontWeight: '600', marginLeft: '0.5rem' }}>
                              {option.schedule}
                            </span>
                          </div>
                          <div>
                            <span style={{ color: '#666' }}>Durée:</span>
                            <span style={{ fontWeight: '600', marginLeft: '0.5rem' }}>
                              ~{option.duration} min
                            </span>
                          </div>
                        </div>
                      </div>
                      
                      <button 
                        className="btn btn-primary"
                        style={{ marginLeft: '1rem' }}
                      >
                        Choisir
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div style={{ textAlign: 'center', padding: '2rem', color: '#666' }}>
                Aucune option de transport disponible
              </div>
            )}
          </div>
        </>
      )}

      {/* Workflow Explanation */}
      <div className="card" style={{ background: '#f0f9ff', border: '2px solid #3b82f6' }}>
        <h3>🔄 Workflow d'Orchestration</h3>
        <div style={{ marginTop: '1rem' }}>
          <div style={{ 
            display: 'grid', 
            gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', 
            gap: '1rem'
          }}>
            {[
              { step: '1', icon: '🌫️', title: 'Vérification Air', desc: 'Appel SOAP Air Quality' },
              { step: '2', icon: '🧠', title: 'Analyse', desc: 'Traitement des données' },
              { step: '3', icon: '🚗', title: 'Recherche Transport', desc: 'Appel REST Mobility' },
              { step: '4', icon: '✅', title: 'Plan Optimal', desc: 'Génération résultat' }
            ].map((item) => (
              <div 
                key={item.step}
                style={{
                  padding: '1rem',
                  background: 'white',
                  borderRadius: '8px',
                  textAlign: 'center'
                }}
              >
                <div style={{
                  background: '#3b82f6',
                  color: 'white',
                  width: '32px',
                  height: '32px',
                  borderRadius: '50%',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  margin: '0 auto 0.5rem',
                  fontWeight: '600'
                }}>
                  {item.step}
                </div>
                <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem' }}>
                  {item.icon}
                </div>
                <div style={{ fontWeight: '600', marginBottom: '0.25rem', fontSize: '0.875rem' }}>
                  {item.title}
                </div>
                <div style={{ fontSize: '0.75rem', color: '#666' }}>
                  {item.desc}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Interoperability Info */}
      <div className="card" style={{ background: '#fef3c7', border: '2px solid #f59e0b' }}>
        <h3>🌐 Interopérabilité des Protocoles</h3>
        <p style={{ marginTop: '1rem', marginBottom: '1rem', color: '#78350f' }}>
          Ce service démontre comment différents protocoles peuvent travailler ensemble de manière transparente:
        </p>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '1rem' }}>
          <div style={{ padding: '1rem', background: 'white', borderRadius: '8px' }}>
            <div style={{ fontWeight: '600', marginBottom: '0.5rem', color: '#92400e' }}>
              📡 SOAP (Air Quality)
            </div>
            <div style={{ fontSize: '0.875rem', color: '#78350f' }}>
              Requêtes XML pour obtenir les données de pollution
            </div>
          </div>
          
          <div style={{ padding: '1rem', background: 'white', borderRadius: '8px' }}>
            <div style={{ fontWeight: '600', marginBottom: '0.5rem', color: '#92400e' }}>
              🔗 REST (Mobility)
            </div>
            <div style={{ fontSize: '0.875rem', color: '#78350f' }}>
              API JSON pour les options de transport
            </div>
          </div>
          
          <div style={{ padding: '1rem', background: 'white', borderRadius: '8px' }}>
            <div style={{ fontWeight: '600', marginBottom: '0.5rem', color: '#92400e' }}>
              ⚙️ Orchestration
            </div>
            <div style={{ fontSize: '0.875rem', color: '#78350f' }}>
              Coordination et agrégation des réponses
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default JourneyPlanner;