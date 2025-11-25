import React, { useState, useEffect } from 'react';
import { eventsAPI } from '../services/api';
import '../App.css';

const EventsService = () => {
  const [events, setEvents] = useState([]);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('all');
  const [filters, setFilters] = useState({
    type: '',
    category: '',
    freeOnly: false
  });

  useEffect(() => {
    loadEvents();
  }, []);

  const loadEvents = async () => {
    setLoading(true);
    try {
      const response = await eventsAPI.getAllEvents();
      const eventsData = response.data?.data?.getAllEvents || [];
      setEvents(eventsData);
    } catch (error) {
      console.error('Erreur chargement événements:', error);
      setEvents([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!searchKeyword.trim()) {
      loadEvents();
      return;
    }

    setLoading(true);
    try {
      const response = await eventsAPI.searchEvents(searchKeyword);
      const eventsData = response.data?.data?.searchEvents || [];
      setEvents(eventsData);
    } catch (error) {
      console.error('Erreur recherche:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleFilter = async () => {
    if (!filters.type && !filters.category && !filters.freeOnly) {
      loadEvents();
      return;
    }

    setLoading(true);
    try {
      const response = await eventsAPI.filterEvents(filters);
      const eventsData = response.data?.data?.filterEvents || [];
      setEvents(eventsData);
    } catch (error) {
      console.error('Erreur filtrage:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadUpcoming = async () => {
    setLoading(true);
    try {
      const response = await eventsAPI.getUpcomingEvents();
      const eventsData = response.data?.data?.getUpcomingEvents || [];
      setEvents(eventsData);
    } catch (error) {
      console.error('Erreur:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (eventId) => {
    try {
      await eventsAPI.registerToEvent(eventId);
      loadEvents(); // Recharger pour voir les places mises à jour
    } catch (error) {
      console.error('Erreur inscription:', error);
    }
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('fr-FR', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const getTypeColor = (type) => {
    const colors = {
      CONCERT: '#8b5cf6',
      FESTIVAL: '#ec4899',
      SPORTS: '#10b981',
      CONFERENCE: '#3b82f6',
      EXHIBITION: '#f59e0b',
      WORKSHOP: '#6366f1',
      COMMUNITY: '#14b8a6',
      CULTURAL: '#f97316'
    };
    return colors[type] || '#6b7280';
  };

  const getTypeIcon = (type) => {
    const icons = {
      CONCERT: '🎵',
      FESTIVAL: '🎉',
      SPORTS: '⚽',
      CONFERENCE: '🎤',
      EXHIBITION: '🖼️',
      WORKSHOP: '🛠️',
      COMMUNITY: '👥',
      CULTURAL: '🎭'
    };
    return icons[type] || '📅';
  };

  return (
    <div>
      {/* Header */}
      <div className="card" style={{ 
        background: 'linear-gradient(135deg, #30cfd0 0%, #330867 100%)',
        color: 'white'
      }}>
        <h2 style={{ marginBottom: '0.5rem' }}>🎭 Service Événements Urbains</h2>
        <p style={{ opacity: 0.9, marginBottom: '1rem' }}>
          GraphQL - Agenda culturel et communautaire avec requêtes flexibles
        </p>
        <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
          <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '8px' }}>
            <strong>Protocole:</strong> GraphQL
          </div>
          <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '8px' }}>
            <strong>Port:</strong> 8084
          </div>
          <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '8px' }}>
            <strong>Endpoint:</strong> /api/events/graphql
          </div>
        </div>
      </div>

      {/* Search and Filters */}
      <div className="card">
        <div style={{ display: 'flex', gap: '0.5rem', borderBottom: '2px solid #e5e7eb', paddingBottom: '0.5rem', marginBottom: '1rem' }}>
          <button
            onClick={() => setActiveTab('all')}
            style={{
              padding: '0.5rem 1rem',
              background: activeTab === 'all' ? '#30cfd0' : 'transparent',
              color: activeTab === 'all' ? 'white' : '#666',
              border: 'none',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: '500'
            }}
          >
            📅 Tous les événements
          </button>
          <button
            onClick={() => { setActiveTab('search'); }}
            style={{
              padding: '0.5rem 1rem',
              background: activeTab === 'search' ? '#30cfd0' : 'transparent',
              color: activeTab === 'search' ? 'white' : '#666',
              border: 'none',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: '500'
            }}
          >
            🔍 Rechercher
          </button>
          <button
            onClick={() => { setActiveTab('filter'); }}
            style={{
              padding: '0.5rem 1rem',
              background: activeTab === 'filter' ? '#30cfd0' : 'transparent',
              color: activeTab === 'filter' ? 'white' : '#666',
              border: 'none',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: '500'
            }}
          >
            🎯 Filtrer
          </button>
        </div>

        {/* All Events Tab */}
        {activeTab === 'all' && (
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '1rem' }}>
              <h3>Tous les événements ({events.length})</h3>
              <div style={{ display: 'flex', gap: '0.5rem' }}>
                <button onClick={loadEvents} className="btn btn-secondary" disabled={loading}>
                  {loading ? '🔄' : '🔄 Actualiser'}
                </button>
                <button onClick={loadUpcoming} className="btn btn-primary" disabled={loading}>
                  📅 À venir
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Search Tab */}
        {activeTab === 'search' && (
          <div>
            <form onSubmit={handleSearch}>
              <div className="form-group">
                <label>Rechercher par mot-clé:</label>
                <div style={{ display: 'flex', gap: '0.5rem' }}>
                  <input
                    type="text"
                    value={searchKeyword}
                    onChange={(e) => setSearchKeyword(e.target.value)}
                    placeholder="Titre, lieu, description, tags..."
                    style={{ flex: 1 }}
                  />
                  <button type="submit" className="btn btn-primary" disabled={loading}>
                    {loading ? '🔄' : '🔍 Rechercher'}
                  </button>
                  <button 
                    type="button" 
                    className="btn btn-secondary"
                    onClick={() => {
                      setSearchKeyword('');
                      loadEvents();
                    }}
                  >
                    ✖️ Effacer
                  </button>
                </div>
              </div>
            </form>
          </div>
        )}

        {/* Filter Tab */}
        {activeTab === 'filter' && (
          <div>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem', marginBottom: '1rem' }}>
              <div className="form-group">
                <label>Type d'événement:</label>
                <select 
                  value={filters.type}
                  onChange={(e) => setFilters({...filters, type: e.target.value})}
                >
                  <option value="">Tous</option>
                  <option value="CONCERT">🎵 Concert</option>
                  <option value="FESTIVAL">🎉 Festival</option>
                  <option value="SPORTS">⚽ Sports</option>
                  <option value="CONFERENCE">🎤 Conférence</option>
                  <option value="EXHIBITION">🖼️ Exposition</option>
                  <option value="WORKSHOP">🛠️ Atelier</option>
                  <option value="COMMUNITY">👥 Communauté</option>
                  <option value="CULTURAL">🎭 Culturel</option>
                </select>
              </div>

              <div className="form-group">
                <label>Catégorie:</label>
                <select 
                  value={filters.category}
                  onChange={(e) => setFilters({...filters, category: e.target.value})}
                >
                  <option value="">Toutes</option>
                  <option value="FREE">🆓 Gratuit</option>
                  <option value="PAID">💳 Payant</option>
                  <option value="DONATION">🎁 Don</option>
                  <option value="INVITATION">📧 Invitation</option>
                </select>
              </div>

              <div className="form-group">
                <label>Options:</label>
                <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginTop: '0.5rem' }}>
                  <input
                    type="checkbox"
                    checked={filters.freeOnly}
                    onChange={(e) => setFilters({...filters, freeOnly: e.target.checked})}
                  />
                  <span>Gratuits uniquement</span>
                </label>
              </div>
            </div>

            <div style={{ display: 'flex', gap: '0.5rem' }}>
              <button onClick={handleFilter} className="btn btn-primary" disabled={loading}>
                {loading ? '🔄 Filtrage...' : '🎯 Appliquer les filtres'}
              </button>
              <button 
                onClick={() => {
                  setFilters({ type: '', category: '', freeOnly: false });
                  loadEvents();
                }}
                className="btn btn-secondary"
              >
                ✖️ Réinitialiser
              </button>
            </div>
          </div>
        )}
      </div>

      {/* Events List */}
      <div className="card">
        {loading ? (
          <div style={{ textAlign: 'center', padding: '3rem' }}>
            <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>🔄</div>
            <div>Chargement des événements...</div>
          </div>
        ) : events.length > 0 ? (
          <div style={{ display: 'grid', gap: '1.5rem' }}>
            {events.map(event => (
              <div 
                key={event.id}
                style={{
                  display: 'flex',
                  gap: '1.5rem',
                  padding: '1.5rem',
                  background: 'white',
                  border: '1px solid #e5e7eb',
                  borderRadius: '12px',
                  boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
                  transition: 'all 0.3s ease'
                }}
                onMouseEnter={(e) => {
                  e.currentTarget.style.boxShadow = '0 8px 16px rgba(0,0,0,0.15)';
                  e.currentTarget.style.transform = 'translateY(-2px)';
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.boxShadow = '0 2px 4px rgba(0,0,0,0.1)';
                  e.currentTarget.style.transform = 'translateY(0)';
                }}
              >
                {/* Event Image/Icon */}
                <div style={{
                  width: '120px',
                  height: '120px',
                  borderRadius: '12px',
                  background: getTypeColor(event.eventType),
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontSize: '3rem',
                  flexShrink: 0
                }}>
                  {getTypeIcon(event.eventType)}
                </div>

                {/* Event Details */}
                <div style={{ flex: 1 }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', marginBottom: '0.75rem' }}>
                    <div>
                      <h3 style={{ margin: '0 0 0.5rem 0', fontSize: '1.25rem' }}>
                        {event.title}
                        {event.isFree && (
                          <span style={{
                            marginLeft: '0.5rem',
                            background: '#10b981',
                            color: 'white',
                            padding: '0.25rem 0.5rem',
                            borderRadius: '12px',
                            fontSize: '0.75rem',
                            fontWeight: '600'
                          }}>
                            GRATUIT
                          </span>
                        )}
                      </h3>
                      <div style={{ display: 'flex', gap: '1rem', fontSize: '0.875rem', color: '#666' }}>
                        <span style={{
                          background: getTypeColor(event.eventType),
                          color: 'white',
                          padding: '0.25rem 0.75rem',
                          borderRadius: '12px',
                          fontSize: '0.75rem'
                        }}>
                          {event.eventType}
                        </span>
                        <span>📍 {event.location}</span>
                      </div>
                    </div>
                    
                    {event.availableSpots !== undefined && (
                      <div style={{ textAlign: 'right' }}>
                        <div style={{ 
                          fontSize: '1.5rem', 
                          fontWeight: 'bold',
                          color: event.availableSpots > 50 ? '#10b981' : event.availableSpots > 10 ? '#f59e0b' : '#ef4444'
                        }}>
                          {event.availableSpots}
                        </div>
                        <div style={{ fontSize: '0.75rem', color: '#666' }}>places restantes</div>
                      </div>
                    )}
                  </div>

                  <p style={{ color: '#666', marginBottom: '0.75rem', lineHeight: '1.5' }}>
                    {event.description}
                  </p>

                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '1rem' }}>
                    <div style={{ fontSize: '0.875rem', color: '#666' }}>
                      <div>🗓️ {formatDate(event.startDateTime)}</div>
                      {event.organizer && (
                        <div style={{ marginTop: '0.25rem' }}>👤 {event.organizer}</div>
                      )}
                    </div>

                    {event.availableSpots > 0 && (
                      <button 
                        onClick={() => handleRegister(event.id)}
                        className="btn btn-primary"
                      >
                        📝 S'inscrire
                      </button>
                    )}
                  </div>

                  {event.tags && event.tags.length > 0 && (
                    <div style={{ marginTop: '0.75rem', display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                      {event.tags.map((tag, idx) => (
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
                          #{tag}
                        </span>
                      ))}
                    </div>
                  )}
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div style={{ textAlign: 'center', padding: '3rem' }}>
            <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>📅</div>
            <div style={{ fontSize: '1.125rem', color: '#666' }}>Aucun événement trouvé</div>
          </div>
        )}
      </div>

      {/* GraphQL Advantages */}
      <div className="card" style={{ background: '#f0f9ff', border: '2px solid #3b82f6' }}>
        <h3>🔄 Avantages de GraphQL</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '1rem', marginTop: '1rem' }}>
          <div style={{ padding: '1rem', background: 'white', borderRadius: '8px' }}>
            <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem' }}>🎯</div>
            <div style={{ fontWeight: '600', marginBottom: '0.25rem' }}>Requêtes personnalisées</div>
            <div style={{ fontSize: '0.875rem', color: '#666' }}>
              Demandez exactement les données dont vous avez besoin, rien de plus
            </div>
          </div>
          
          <div style={{ padding: '1rem', background: 'white', borderRadius: '8px' }}>
            <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem' }}>⚡</div>
            <div style={{ fontWeight: '600', marginBottom: '0.25rem' }}>Évite l'over-fetching</div>
            <div style={{ fontSize: '0.875rem', color: '#666' }}>
              Pas de données inutiles transmises sur le réseau
            </div>
          </div>
          
          <div style={{ padding: '1rem', background: 'white', borderRadius: '8px' }}>
            <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem' }}>🔍</div>
            <div style={{ fontWeight: '600', marginBottom: '0.25rem' }}>Recherche flexible</div>
            <div style={{ fontSize: '0.875rem', color: '#666' }}>
              Filtrage, recherche et tri côté serveur
            </div>
          </div>
          
          <div style={{ padding: '1rem', background: 'white', borderRadius: '8px' }}>
            <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem' }}>📘</div>
            <div style={{ fontWeight: '600', marginBottom: '0.25rem' }}>Fortement typé</div>
            <div style={{ fontSize: '0.875rem', color: '#666' }}>
              Schéma auto-documenté avec introspection
            </div>
          </div>
        </div>
      </div>

      {/* Documentation */}
      <div className="card" style={{ background: '#fffbeb', border: '2px solid #fcd34d' }}>
        <h3>📚 Documentation GraphQL</h3>
        <div style={{ marginTop: '1rem' }}>
          <div style={{ marginBottom: '1rem' }}>
            <strong>Requêtes GraphQL disponibles:</strong>
          </div>
          <code style={{ 
            display: 'block', 
            background: 'white', 
            padding: '1rem', 
            borderRadius: '6px',
            fontSize: '0.875rem',
            whiteSpace: 'pre-wrap'
          }}>
{`query {
  getAllEvents { id title location ... }
  searchEvents(keyword: "jazz") { ... }
  filterEvents(type: CONCERT, freeOnly: true) { ... }
  getUpcomingEvents { ... }
}

mutation {
  registerAttendee(eventId: 1) { ... }
}`}
          </code>
          <a 
            href="http://localhost:8084/graphiql" 
            target="_blank" 
            rel="noopener noreferrer"
            className="btn btn-primary"
            style={{ marginTop: '1rem', display: 'inline-block' }}
          >
            🔍 Ouvrir GraphiQL Explorer
          </a>
        </div>
      </div>
    </div>
  );
};

export default EventsService;