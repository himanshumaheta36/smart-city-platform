-- Insert sample urban events
INSERT INTO urban_events (id, title, description, event_type, category, status, location, latitude, longitude, start_date_time, end_date_time, capacity, registered_attendees, price, organizer, image_url, created_at, updated_at) VALUES
(1, 'Festival de Jazz Urbain', 'Un festival de jazz en plein air avec des artistes locaux et internationaux', 'CONCERT', 'FREE', 'SCHEDULED', 'Parc Central', 48.8566, 2.3522, '2025-07-15T18:00:00', '2025-07-15T23:00:00', 500, 250, 0.0, 'Mairie de Paris', 'https://example.com/jazz-festival.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2, 'Marathon de la Ville', 'Course à pied à travers les principaux monuments de la ville', 'SPORTS', 'PAID', 'SCHEDULED', 'Départ: Place de la République', 48.8674, 2.3639, '2025-09-10T08:00:00', '2025-09-10T14:00:00', 10000, 7500, 45.0, 'Association Sportive Urbaine', 'https://example.com/marathon.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(3, 'Conférence IA et Ville Intelligente', 'Conférence sur l''utilisation de l''IA dans le développement urbain', 'CONFERENCE', 'PAID', 'SCHEDULED', 'Centre de Convention', 48.8589, 2.2944, '2025-06-20T09:00:00', '2025-06-20T17:00:00', 300, 180, 120.0, 'TechCity Association', 'https://example.com/ai-conference.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(4, 'Marché Artisanal Local', 'Marché mettant en avant les artisans et producteurs locaux', 'COMMUNITY', 'FREE', 'ONGOING', 'Place du Marché', 48.8530, 2.3499, '2025-05-01T08:00:00', '2025-12-31T19:00:00', 1000, 450, 0.0, 'Chambre de Commerce', 'https://example.com/artisan-market.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(5, 'Exposition d''Art Contemporain', 'Exposition des œuvres d''artistes urbains contemporains', 'EXHIBITION', 'DONATION', 'SCHEDULED', 'Musée d''Art Moderne', 48.8606, 2.3376, '2025-08-01T10:00:00', '2025-10-31T18:00:00', 200, 75, 0.0, 'Musée d''Art Moderne', 'https://example.com/art-exhibition.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(6, 'Atelier de Recyclage Créatif', 'Atelier pour apprendre à recycler et réutiliser les matériaux', 'WORKSHOP', 'FREE', 'SCHEDULED', 'Espace Écologique', 48.8350, 2.3650, '2025-07-08T14:00:00', '2025-07-08T17:00:00', 30, 15, 0.0, 'Association Éco-Citoyenne', 'https://example.com/recycling-workshop.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(7, 'Concert Symphonique en Plein Air', 'Orchestre symphonique interprétant des classiques', 'CONCERT', 'PAID', 'COMPLETED', 'Jardin des Tuileries', 48.8637, 2.3272, '2025-04-15T20:00:00', '2025-04-15T22:30:00', 800, 800, 25.0, 'Orchestre National', 'https://example.com/symphony-concert.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(8, 'Festival de Cuisine de Rue', 'Découverte des cuisines du monde avec des food trucks', 'FESTIVAL', 'PAID', 'SCHEDULED', 'Quai de Seine', 48.8510, 2.3520, '2025-08-25T11:00:00', '2025-08-25T23:00:00', 2000, 1200, 0.0, 'Association Culinaire Urbaine', 'https://example.com/food-festival.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert tags for events
INSERT INTO event_tags (event_id, tag) VALUES
(1, 'musique'), (1, 'jazz'), (1, 'plein-air'), (1, 'culture'),
(2, 'sport'), (2, 'course'), (2, 'santé'), (2, 'compétition'),
(3, 'technologie'), (3, 'IA'), (3, 'innovation'), (3, 'conférence'),
(4, 'artisanat'), (4, 'local'), (4, 'commerce'), (4, 'communauté'),
(5, 'art'), (5, 'contemporain'), (5, 'exposition'), (5, 'culture'),
(6, 'écologie'), (6, 'recyclage'), (6, 'atelier'), (6, 'éducation'),
(7, 'musique'), (7, 'classique'), (7, 'orchestre'), (7, 'culture'),
(8, 'nourriture'), (8, 'cuisine'), (8, 'festival'), (8, 'gastronomie');