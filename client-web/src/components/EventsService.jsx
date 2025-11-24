import React, { useState, useEffect } from 'react';
import { eventsAPI } from '../services/api';
import '../App.css';

const EventsService = () => {
  const [events, setEvents] = useState([]);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [loading, setLoading] = useState(false);
  const [filteredEvents, setFilteredEvents] = useState([]);

  useEffect(() => {
    loadEvents();
  }, []);

  useEffect(() => {
    if (searchKeyword) {
      const filtered = events.filter(event =>
        event.title.toLowerCase().includes(searchKeyword.toLowerCase()) ||
        event.location.toLowerCase().includes(searchKeyword.toLowerCase()) ||
        event.description.toLowerCase().includes(searchKeyword.toLowerCase())
      );
      setFilteredEvents(filtered);
    } else {
      setFilteredEvents(events);
    }
  }, [searchKeyword, events]);

  const loadEvents = async () => {
    setLoading(true);
    try {
      // Simuler des données d'événements (remplacer par l'appel GraphQL réel)
      const mockEvents = [
        {
          id: 1,
          title: "Festival de Jazz Urbain",
          description: "Un festival de jazz en plein air avec des artistes locaux et internationaux",
          location: "Parc Central",
          startDateTime: "2025-07-15T18:00:00",
          availableSpots: 250,
          isFree: true
        },
        {
          id: 2,
          title: "Marathon de la Ville",
          description: "Course à pied à travers les principaux monuments de la ville",
          location: "Place de la République",
          startDateTime: "2025-09-10T08:00:00",
          availableSpots: 2500,
          isFree: false
        },
        {
          id: 3,
          title: "Conférence IA et Ville Intelligente",
          description: "Conférence sur l'utilisation de l'IA dans le développement urbain",
          location: "Centre de Convention",
          startDateTime: "2025-06-20T09:00:00",
          availableSpots: 120,
          isFree: false
        },
        {
          id: 4,
          title: "Marché Artisanal Local",
          description: "Marché mettant en avant les artisans et producteurs locaux",
          location: "Place du Marché",
          startDateTime: "2025-05-01T08:00:00",
          availableSpots: 550,
          isFree: true
        }
      ];
      setEvents(mockEvents);
      setFilteredEvents(mockEvents);
    } catch (error) {
      console.error('Error loading events:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!searchKeyword) {
      setFilteredEvents(events);
      return;
    }

    setLoading(true);
    try {
      // Ici vous utiliseriez eventsAPI.searchEvents(searchKeyword) en production
      const filtered = events.filter(event =>
        event.title.toLowerCase().includes(searchKeyword.toLowerCase()) ||
        event.location.toLowerCase().includes(searchKeyword.toLowerCase())
      );
      setFilteredEvents(filtered);
    } catch (error) {
      console.error('Error searching events:', error);
    } finally {
      setLoading(false);
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

  return (
    <div>
      <div className="card">
        <h2>🎭 Événements Urbains (GraphQL)</h2>
        <p>Découvrez et recherchez des événements culturels et communautaires avec des requêtes personnalisées.</p>
      </div>

      <div className="card">
        <h3>🔍 Recherche d'Événements</h3>
        <form onSubmit={handleSearch}>
          <div className="form-group">
            <input
              type="text"
              value={searchKeyword}
              onChange={(e) => setSearchKeyword(e.target.value)}
              placeholder="Rechercher par titre, lieu, description..."
            />
          </div>
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Recherche...' : '🔍 Rechercher'}
          </button>
          <button 
            type="button" 
            className="btn btn-secondary" 
            onClick={() => {
              setSearchKeyword('');
              setFilteredEvents(events);
            }}
            style={{ marginLeft: '0.5rem' }}
          >
            🔄 Réinitialiser
          </button>
        </form>
      </div>

      {loading && <div className="loading">Chargement des événements...</div>}

      <div className="card">
        <h3>📅 Événements à Venir ({filteredEvents.length})</h3>
        
        {filteredEvents.length > 0 ? (
          <div style={{ display: 'grid', gap: '1rem' }}>
            {filteredEvents.map(event => (
              <div 
                key={event.id}
                style={{
                  padding: '1.5rem',
                  border: '1px solid #e1e5e9',
                  borderRadius: '8px',
                  background: 'white'
                }}
              >
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                  <div style={{ flex: 1 }}>
                    <h4 style={{ margin: '0 0 0.5rem 0', color: '#333' }}>
                      {event.title} 
                      {event.isFree && <span style={{ 
                        background: '#28a745', 
                        color: 'white', 
                        padding: '0.25rem 0.5rem', 
                        borderRadius: '12px', 
                        fontSize: '0.8rem',
                        marginLeft: '0.5rem'
                      }}>GRATUIT</span>}
                    </h4>
                    <p style={{ margin: '0.5rem 0', color: '#666' }}>{event.description}</p>
                    <div style={{ display: 'flex', gap: '1rem', marginTop: '1rem' }}>
                      <span>📍 {event.location}</span>
                      <span>🕐 {formatDate(event.startDateTime)}</span>
                      <span>👥 {event.availableSpots} places</span>
                    </div>
                  </div>
                  <button className="btn btn-primary">
                    S'inscrire
                  </button>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p>Aucun événement trouvé</p>
        )}
      </div>

      <div className="card">
        <h3>🔄 Avantages GraphQL</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
          <div style={{ padding: '1rem', background: '#e7f3ff', borderRadius: '8px' }}>
            <h4>✅ Requêtes Personnalisées</h4>
            <p>Obtenez exactement les données dont vous avez besoin</p>
          </div>
          <div style={{ padding: '1rem', background: '#fff3cd', borderRadius: '8px' }}>
            <h4>⚡ Évite le Over-fetching</h4>
            <p>Pas de données inutiles transmises</p>
          </div>
          <div style={{ padding: '1rem', background: '#d4edda', borderRadius: '8px' }}>
            <h4>🔍 Recherche Flexible</h4>
            <p>Filtrage et recherche avancés</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EventsService;