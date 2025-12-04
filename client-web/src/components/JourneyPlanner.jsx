import React, { useState, useRef, useEffect } from 'react';
import axios from 'axios';
import '../App.css';

const JourneyPlanner = () => {
  // États pour le planificateur de trajet
  const [startLocation, setStartLocation] = useState('');
  const [endLocation, setEndLocation] = useState('');
  const [journeyPlan, setJourneyPlan] = useState(null);
  
  // États pour le planificateur de journée IA
  const [preferences, setPreferences] = useState('');
  const [dayPlan, setDayPlan] = useState(null);
  
  // États pour le chat
  const [chatMessages, setChatMessages] = useState([]);
  const [chatInput, setChatInput] = useState('');
  const [sessionId, setSessionId] = useState(null);
  
  // États généraux
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [activeTab, setActiveTab] = useState('journey');
  const [aiStatus, setAiStatus] = useState({ enabled: false, connected: false });
  
  const chatContainerRef = useRef(null);

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

  // Vérifier le statut de l'IA au chargement
  useEffect(() => {
    checkAIStatus();
    loadWelcomeMessage();
  }, []);

  // Scroll automatique du chat
  useEffect(() => {
    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
    }
  }, [chatMessages]);

  const API_BASE = 'http://localhost:8085/orchestration';

  const checkAIStatus = async () => {
    try {
      const response = await axios.get(`${API_BASE}/health`);
      if (response.data.gemini) {
        setAiStatus(response.data.gemini);
      }
    } catch (err) {
      console.error('Erreur vérification IA:', err);
    }
  };

  const loadWelcomeMessage = async () => {
    try {
      const response = await axios.get(`${API_BASE}/welcome`);
      setChatMessages([{
        role: 'assistant',
        content: response.data.message,
        timestamp: new Date()
      }]);
    } catch (err) {
      setChatMessages([{
        role: 'assistant',
        content: '👋 Bonjour! Je suis votre assistant Smart City. Comment puis-je vous aider?',
        timestamp: new Date()
      }]);
    }
  };

  // Planifier un trajet simple
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
      const response = await axios.post(
        `${API_BASE}/plan-journey`,
        null,
        { params: { startLocation, endLocation } }
      );
      setJourneyPlan(response.data);
    } catch (err) {
      setError('❌ Erreur lors de la planification du trajet');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Planifier une journée avec IA
  const handlePlanDay = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    
    try {
      const response = await axios.post(
        `${API_BASE}/plan-day`,
        null,
        { params: { preferences, location: startLocation || 'Centre-ville' } }
      );
      setDayPlan(response.data);
    } catch (err) {
      setError('❌ Erreur lors de la planification de la journée');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Envoyer un message au chat
  const handleSendMessage = async (e) => {
    e.preventDefault();
    if (!chatInput.trim()) return;

    const userMessage = chatInput;
    setChatInput('');
    
    // Ajouter le message utilisateur
    setChatMessages(prev => [...prev, {
      role: 'user',
      content: userMessage,
      timestamp: new Date()
    }]);

    setLoading(true);

    try {
      const response = await axios.post(`${API_BASE}/chat`, {
        message: userMessage,
        sessionId: sessionId,
        location: startLocation || 'Centre-ville'
      });

      if (response.data.sessionId) {
        setSessionId(response.data.sessionId);
      }

      setChatMessages(prev => [...prev, {
        role: 'assistant',
        content: response.data.message,
        timestamp: new Date(),
        plan: response.data.suggestedPlan
      }]);

      // Si un plan est suggéré, le stocker
      if (response.data.suggestedPlan) {
        setDayPlan(response.data.suggestedPlan);
      }
    } catch (err) {
      setChatMessages(prev => [...prev, {
        role: 'assistant',
        content: '❌ Désolé, une erreur s\'est produite. Veuillez réessayer.',
        timestamp: new Date()
      }]);
    } finally {
      setLoading(false);
    }
  };

  const swapLocations = () => {
    const temp = startLocation;
    setStartLocation(endLocation);
    setEndLocation(temp);
  };

  const quickPrompts = [
    "Planifie-moi une journée sportive",
    "Quels événements aujourd'hui?",
    "Comment est la qualité de l'air?",
    "Je veux visiter des musées",
    "Journée en famille avec enfants"
  ];

  return (
    <div>
      {/* Header */}
      <div className="card" style={{ 
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        color: 'white'
      }}>
        <h2 style={{ marginBottom: '0.5rem' }}>🤖 Assistant Smart City Intelligent</h2>
        <p style={{ opacity: 0.9, marginBottom: '1rem' }}>
          Planification de trajets et journées avec Intelligence Artificielle (Gemini)
        </p>
        <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
          <div style={{ 
            background: aiStatus.enabled ? 'rgba(16,185,129,0.3)' : 'rgba(239,68,68,0.3)', 
            padding: '0.5rem 1rem', 
            borderRadius: '8px',
            display: 'flex',
            alignItems: 'center',
            gap: '0.5rem'
          }}>
            <span style={{ 
              width: '10px', 
              height: '10px', 
              borderRadius: '50%', 
              background: aiStatus.enabled ? '#10b981' : '#ef4444'
            }}></span>
            <strong>Gemini AI:</strong> {aiStatus.enabled ? 'Activé' : 'Désactivé'}
          </div>
          <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '8px' }}>
            <strong>Services:</strong> Air Quality + Mobility + Events
          </div>
        </div>
      </div>

      {/* Tabs */}
      <div className="card">
        <div style={{ display: 'flex', gap: '0.5rem', borderBottom: '2px solid #e5e7eb', paddingBottom: '0.5rem', marginBottom: '1rem' }}>
          {[
            { id: 'journey', label: '🗺️ Trajet', icon: '🚗' },
            { id: 'dayplan', label: '📋 Journée IA', icon: '🤖' },
            { id: 'chat', label: '💬 Chat IA', icon: '💬' }
          ].map(tab => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              style={{
                padding: '0.75rem 1.5rem',
                background: activeTab === tab.id ? '#667eea' : 'transparent',
                color: activeTab === tab.id ? 'white' : '#666',
                border: 'none',
                borderRadius: '8px',
                cursor: 'pointer',
                fontWeight: '600',
                display: 'flex',
                alignItems: 'center',
                gap: '0.5rem'
              }}
            >
              {tab.label}
            </button>
          ))}
        </div>

        {/* Tab: Planifier un trajet */}
        {activeTab === 'journey' && (
          <div>
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
                    }}>A</span>
                    Lieu de départ
                  </label>
                  <select
                    value={startLocation}
                    onChange={(e) => setStartLocation(e.target.value)}
                    style={{ width: '100%', padding: '0.75rem', fontSize: '1rem' }}
                  >
                    <option value="">Sélectionnez un lieu...</option>
                    {locations.map(loc => (
                      <option key={loc} value={loc}>{loc}</option>
                    ))}
                  </select>
                </div>

                {/* Swap Button */}
                <div style={{ 
                  position: 'absolute',
                  right: '10px',
                  top: '50%',
                  transform: 'translateY(-50%)',
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
                      fontSize: '1.25rem'
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
                    }}>B</span>
                    Lieu d'arrivée
                  </label>
                  <select
                    value={endLocation}
                    onChange={(e) => setEndLocation(e.target.value)}
                    style={{ width: '100%', padding: '0.75rem', fontSize: '1rem' }}
                  >
                    <option value="">Sélectionnez un lieu...</option>
                    {locations.map(loc => (
                      <option key={loc} value={loc}>{loc}</option>
                    ))}
                  </select>
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

            {error && <div className="error" style={{ marginTop: '1rem' }}>{error}</div>}

            {/* Journey Results */}
            {journeyPlan && (
              <div style={{ marginTop: '2rem' }}>
                {/* Route Overview */}
                <div style={{
                  padding: '1.5rem',
                  background: 'linear-gradient(to right, #10b981, #ef4444)',
                  borderRadius: '12px',
                  color: 'white',
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  marginBottom: '1rem'
                }}>
                  <div style={{ textAlign: 'center', flex: 1 }}>
                    <div style={{ fontSize: '2rem' }}>🅰️</div>
                    <div style={{ fontWeight: '600' }}>{journeyPlan.startLocation}</div>
                  </div>
                  <div style={{ fontSize: '2rem' }}>→</div>
                  <div style={{ textAlign: 'center', flex: 1 }}>
                    <div style={{ fontSize: '2rem' }}>🅱️</div>
                    <div style={{ fontWeight: '600' }}>{journeyPlan.endLocation}</div>
                  </div>
                </div>

                {/* Air Quality */}
                <div style={{
                  padding: '1rem',
                  background: journeyPlan.airQualityGood ? '#ecfdf5' : '#fff7ed',
                  border: `2px solid ${journeyPlan.airQualityGood ? '#10b981' : '#f97316'}`,
                  borderRadius: '8px',
                  marginBottom: '1rem'
                }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                    <span style={{ fontSize: '2rem' }}>
                      {journeyPlan.airQualityGood ? '✅' : '⚠️'}
                    </span>
                    <div>
                      <div style={{ fontWeight: '600' }}>
                        Qualité de l'air: {journeyPlan.airQualityGood ? 'Bonne' : 'Médiocre'}
                      </div>
                      <div style={{ fontSize: '0.875rem', color: '#666' }}>
                        {journeyPlan.recommendation}
                      </div>
                    </div>
                  </div>
                </div>

                {/* Transport Options */}
                <h4>🚌 Options de Transport</h4>
                <div style={{ display: 'grid', gap: '0.5rem', marginTop: '0.5rem' }}>
                  {journeyPlan.transportOptions?.map((option, index) => (
                    <div key={index} style={{
                      padding: '1rem',
                      border: '1px solid #e5e7eb',
                      borderRadius: '8px',
                      display: 'flex',
                      justifyContent: 'space-between',
                      alignItems: 'center'
                    }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                        <span style={{ fontSize: '1.5rem' }}>
                          {option.type === 'BUS' ? '🚌' : 
                           option.type === 'METRO' ? '🚇' : 
                           option.type === 'TRAM' ? '🚊' : '🚗'}
                        </span>
                        <div>
                          <div style={{ fontWeight: '600' }}>
                            {option.type} - {option.lineNumber || option.line}
                          </div>
                          <div style={{ fontSize: '0.875rem', color: '#666' }}>
                            {option.status}
                          </div>
                        </div>
                      </div>
                      <div style={{ textAlign: 'right' }}>
                        <div style={{ fontWeight: '600' }}>
                          {option.duration} min
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        )}

        {/* Tab: Planifier une journée avec IA */}
        {activeTab === 'dayplan' && (
          <div>
            <h3 style={{ marginBottom: '1rem' }}>
              🤖 Planifier votre journée avec l'IA
              {aiStatus.enabled && <span style={{ fontSize: '0.75rem', color: '#10b981', marginLeft: '0.5rem' }}>● Gemini connecté</span>}
            </h3>
            
            <form onSubmit={handlePlanDay}>
              <div className="form-group">
                <label>📍 Votre localisation</label>
                <select
                  value={startLocation}
                  onChange={(e) => setStartLocation(e.target.value)}
                  style={{ width: '100%', padding: '0.75rem' }}
                >
                  <option value="">Sélectionnez...</option>
                  {locations.map(loc => (
                    <option key={loc} value={loc}>{loc}</option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label>🎯 Vos préférences et envies</label>
                <textarea
                  value={preferences}
                  onChange={(e) => setPreferences(e.target.value)}
                  placeholder="Ex: Je veux une journée sportive avec des événements culturels, j'aime la nature..."
                  style={{ width: '100%', padding: '0.75rem', minHeight: '100px', resize: 'vertical' }}
                />
              </div>

              <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.5rem', marginBottom: '1rem' }}>
                {['Sport', 'Culture', 'Nature', 'Famille', 'Détente', 'Gastronomie'].map(tag => (
                  <button
                    key={tag}
                    type="button"
                    onClick={() => setPreferences(prev => prev ? `${prev}, ${tag.toLowerCase()}` : tag.toLowerCase())}
                    style={{
                      padding: '0.5rem 1rem',
                      border: '1px solid #667eea',
                      background: preferences.toLowerCase().includes(tag.toLowerCase()) ? '#667eea' : 'white',
                      color: preferences.toLowerCase().includes(tag.toLowerCase()) ? 'white' : '#667eea',
                      borderRadius: '20px',
                      cursor: 'pointer'
                    }}
                  >
                    {tag}
                  </button>
                ))}
              </div>

              <button 
                type="submit" 
                className="btn btn-primary" 
                disabled={loading}
                style={{ width: '100%', padding: '1rem' }}
              >
                {loading ? '🤖 Génération en cours...' : '✨ Générer mon plan de journée'}
              </button>
            </form>

            {error && <div className="error" style={{ marginTop: '1rem' }}>{error}</div>}

            {/* Day Plan Results */}
            {dayPlan && (
              <div style={{ marginTop: '2rem' }}>
                {/* Warnings */}
                {dayPlan.warnings?.length > 0 && (
                  <div style={{
                    padding: '1rem',
                    background: '#fef3c7',
                    border: '2px solid #f59e0b',
                    borderRadius: '8px',
                    marginBottom: '1rem'
                  }}>
                    {dayPlan.warnings.map((warning, i) => (
                      <div key={i}>{warning}</div>
                    ))}
                  </div>
                )}

                {/* AI Summary */}
                {dayPlan.aiSummary && (
                  <div style={{
                    padding: '1.5rem',
                    background: '#f0f9ff',
                    border: '2px solid #3b82f6',
                    borderRadius: '12px',
                    marginBottom: '1rem'
                  }}>
                    <h4 style={{ marginBottom: '1rem', color: '#1e40af' }}>
                      🤖 Recommandations de l'IA
                    </h4>
                    <div style={{ 
                      whiteSpace: 'pre-wrap', 
                      lineHeight: '1.6',
                      color: '#1e3a8a'
                    }}>
                      {dayPlan.aiSummary}
                    </div>
                  </div>
                )}

                {/* Activities */}
                {dayPlan.activities?.length > 0 && (
                  <div>
                    <h4 style={{ marginBottom: '1rem' }}>📅 Activités planifiées</h4>
                    <div style={{ display: 'grid', gap: '0.5rem' }}>
                      {dayPlan.activities.map((activity, index) => (
                        <div key={index} style={{
                          padding: '1rem',
                          border: '1px solid #e5e7eb',
                          borderRadius: '8px',
                          display: 'flex',
                          gap: '1rem',
                          alignItems: 'flex-start'
                        }}>
                          <div style={{
                            background: '#667eea',
                            color: 'white',
                            padding: '0.5rem',
                            borderRadius: '8px',
                            fontWeight: '600',
                            minWidth: '60px',
                            textAlign: 'center'
                          }}>
                            {activity.time}
                          </div>
                          <div>
                            <div style={{ fontWeight: '600' }}>{activity.title}</div>
                            <div style={{ fontSize: '0.875rem', color: '#666' }}>
                              📍 {activity.location}
                            </div>
                            {activity.description && (
                              <div style={{ fontSize: '0.875rem', marginTop: '0.25rem' }}>
                                {activity.description}
                              </div>
                            )}
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                )}

                {/* Air Quality Info */}
                {dayPlan.airQuality && (
                  <div style={{
                    marginTop: '1rem',
                    padding: '1rem',
                    background: dayPlan.airQuality.aqiValue <= 50 ? '#ecfdf5' : '#fef3c7',
                    borderRadius: '8px'
                  }}>
                    <strong>🌫️ Qualité de l'air:</strong> AQI {dayPlan.airQuality.aqiValue?.toFixed(0)} 
                    ({dayPlan.airQuality.aqiCategory})
                  </div>
                )}
              </div>
            )}
          </div>
        )}

        {/* Tab: Chat IA */}
        {activeTab === 'chat' && (
          <div>
            <h3 style={{ marginBottom: '1rem' }}>
              💬 Discutez avec l'assistant IA
              {aiStatus.enabled && <span style={{ fontSize: '0.75rem', color: '#10b981', marginLeft: '0.5rem' }}>● Gemini</span>}
            </h3>

            {/* Quick prompts */}
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.5rem', marginBottom: '1rem' }}>
              {quickPrompts.map((prompt, i) => (
                <button
                  key={i}
                  onClick={() => setChatInput(prompt)}
                  style={{
                    padding: '0.5rem 1rem',
                    border: '1px solid #e5e7eb',
                    background: 'white',
                    borderRadius: '20px',
                    cursor: 'pointer',
                    fontSize: '0.875rem'
                  }}
                >
                  {prompt}
                </button>
              ))}
            </div>

            {/* Chat messages */}
            <div 
              ref={chatContainerRef}
              style={{
                height: '400px',
                overflowY: 'auto',
                border: '1px solid #e5e7eb',
                borderRadius: '12px',
                padding: '1rem',
                background: '#f9fafb',
                marginBottom: '1rem'
              }}
            >
              {chatMessages.map((msg, index) => (
                <div 
                  key={index}
                  style={{
                    display: 'flex',
                    justifyContent: msg.role === 'user' ? 'flex-end' : 'flex-start',
                    marginBottom: '1rem'
                  }}
                >
                  <div style={{
                    maxWidth: '80%',
                    padding: '1rem',
                    borderRadius: '12px',
                    background: msg.role === 'user' ? '#667eea' : 'white',
                    color: msg.role === 'user' ? 'white' : '#333',
                    boxShadow: '0 1px 3px rgba(0,0,0,0.1)'
                  }}>
                    <div style={{ whiteSpace: 'pre-wrap', lineHeight: '1.5' }}>
                      {msg.content}
                    </div>
                    <div style={{ 
                      fontSize: '0.75rem', 
                      opacity: 0.7, 
                      marginTop: '0.5rem',
                      textAlign: 'right'
                    }}>
                      {new Date(msg.timestamp).toLocaleTimeString()}
                    </div>
                  </div>
                </div>
              ))}
              {loading && (
                <div style={{ textAlign: 'center', padding: '1rem' }}>
                  <span>🤖 L'assistant réfléchit...</span>
                </div>
              )}
            </div>

            {/* Chat input */}
            <form onSubmit={handleSendMessage} style={{ display: 'flex', gap: '0.5rem' }}>
              <input
                type="text"
                value={chatInput}
                onChange={(e) => setChatInput(e.target.value)}
                placeholder="Posez votre question..."
                style={{ flex: 1, padding: '1rem', fontSize: '1rem' }}
                disabled={loading}
              />
              <button 
                type="submit" 
                className="btn btn-primary"
                disabled={loading || !chatInput.trim()}
                style={{ padding: '1rem 2rem' }}
              >
                {loading ? '...' : '📤 Envoyer'}
              </button>
            </form>
          </div>
        )}
      </div>

      {/* Info Card */}
      <div className="card" style={{ background: '#f0f9ff', border: '2px solid #3b82f6' }}>
        <h3>🔄 Comment ça fonctionne?</h3>
        <div style={{ marginTop: '1rem', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
          {[
            { icon: '🌫️', title: 'Air Quality (SOAP)', desc: 'Données de pollution' },
            { icon: '🚌', title: 'Mobility (REST)', desc: 'Options de transport' },
            { icon: '📅', title: 'Events (GraphQL)', desc: 'Événements de la ville' },
            { icon: '🤖', title: 'Gemini AI', desc: 'Intelligence artificielle' }
          ].map((item, i) => (
            <div key={i} style={{ padding: '1rem', background: 'white', borderRadius: '8px', textAlign: 'center' }}>
              <div style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>{item.icon}</div>
              <div style={{ fontWeight: '600', fontSize: '0.875rem' }}>{item.title}</div>
              <div style={{ fontSize: '0.75rem', color: '#666' }}>{item.desc}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default JourneyPlanner;