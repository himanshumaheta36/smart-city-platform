-- Données initiales pour la qualité de l'air
INSERT INTO air_quality_data (zone_name, aqi_value, aqi_category, pm25, pm10, no2, o3, co, so2, measurement_date, latitude, longitude) VALUES
('Centre-ville', 45.2, 'Good', 10.5, 22.1, 18.3, 42.7, 0.8, 3.2, '2024-01-23 08:00:00', 48.8566, 2.3522),
('Quartier Nord', 68.7, 'Moderate', 15.8, 35.2, 25.6, 38.9, 1.2, 4.8, '2024-01-23 08:00:00', 48.8900, 2.3500),
('Zone Industrielle', 125.3, 'Unhealthy for Sensitive Groups', 35.6, 68.9, 45.2, 28.4, 2.5, 12.7, '2024-01-23 08:00:00', 48.8300, 2.3700),
('Parc Central', 32.1, 'Good', 8.2, 18.7, 12.4, 48.3, 0.6, 2.1, '2024-01-23 08:00:00', 48.8600, 2.3300),
('Banlieue Sud', 55.8, 'Moderate', 12.9, 28.4, 22.1, 35.6, 1.1, 3.9, '2024-01-23 08:00:00', 48.8200, 2.2900),

-- Données historiques pour Centre-ville
('Centre-ville', 42.8, 'Good', 9.8, 20.5, 17.2, 44.1, 0.7, 2.9, '2024-01-22 08:00:00', 48.8566, 2.3522),
('Centre-ville', 48.3, 'Good', 11.2, 23.8, 19.5, 41.2, 0.9, 3.4, '2024-01-21 08:00:00', 48.8566, 2.3522),
('Centre-ville', 52.7, 'Moderate', 13.5, 26.1, 21.8, 39.7, 1.0, 3.8, '2024-01-20 08:00:00', 48.8566, 2.3522),

-- Données historiques pour Zone Industrielle
('Zone Industrielle', 118.6, 'Unhealthy for Sensitive Groups', 32.8, 65.4, 42.7, 29.3, 2.3, 11.9, '2024-01-22 08:00:00', 48.8300, 2.3700),
('Zone Industrielle', 132.4, 'Unhealthy', 38.2, 72.1, 47.9, 26.8, 2.7, 13.5, '2024-01-21 08:00:00', 48.8300, 2.3700),
('Zone Industrielle', 142.8, 'Unhealthy', 41.5, 76.3, 51.2, 24.6, 3.0, 14.8, '2024-01-20 08:00:00', 48.8300, 2.3700);