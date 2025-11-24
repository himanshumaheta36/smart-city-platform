package com.smartcity.mobility.config;

import com.smartcity.mobility.model.Schedule;
import com.smartcity.mobility.model.TrafficInfo;
import com.smartcity.mobility.model.TransportLine;
import com.smartcity.mobility.repository.ScheduleRepository;
import com.smartcity.mobility.repository.TrafficInfoRepository;
import com.smartcity.mobility.repository.TransportLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TransportLineRepository transportLineRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TrafficInfoRepository trafficInfoRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== DÉBUT DE L'INITIALISATION DES DONNÉES ===");
        
        // Nettoyer les données existantes (dans l'ordre inverse des dépendances)
        scheduleRepository.deleteAll();
        trafficInfoRepository.deleteAll();
        transportLineRepository.deleteAll();

        // Créer les lignes de transport
        TransportLine bus101 = new TransportLine();
        bus101.setLineNumber("BUS-101");
        bus101.setTransportType("BUS");
        bus101.setDescription("Central Station to Downtown");
        bus101.setStations(Arrays.asList("Central Station", "Downtown Mall", "City Park"));
        bus101.setStatus("ACTIVE");
        bus101.setDelayMinutes(0);
        bus101.setLastUpdate(LocalDateTime.now());

        TransportLine bus202 = new TransportLine();
        bus202.setLineNumber("BUS-202");
        bus202.setTransportType("BUS");
        bus202.setDescription("North District to South District");
        bus202.setStations(Arrays.asList("North Terminal", "City Center", "South Plaza"));
        bus202.setStatus("ACTIVE");
        bus202.setDelayMinutes(5);
        bus202.setLastUpdate(LocalDateTime.now());

        TransportLine metroRed = new TransportLine();
        metroRed.setLineNumber("METRO-RED");
        metroRed.setTransportType("METRO");
        metroRed.setDescription("Red Line - East-West Connection");
        metroRed.setStations(Arrays.asList("East Gate", "Central Hub", "West End"));
        metroRed.setStatus("ACTIVE");
        metroRed.setDelayMinutes(0);
        metroRed.setLastUpdate(LocalDateTime.now());

        TransportLine metroBlue = new TransportLine();
        metroBlue.setLineNumber("METRO-BLUE");
        metroBlue.setTransportType("METRO");
        metroBlue.setDescription("Blue Line - North-South Connection");
        metroBlue.setStations(Arrays.asList("North Point", "Central Square", "South Station"));
        metroBlue.setStatus("DELAYED");
        metroBlue.setDelayMinutes(15);
        metroBlue.setLastUpdate(LocalDateTime.now());

        TransportLine trainEx1 = new TransportLine();
        trainEx1.setLineNumber("TRAIN-EX1");
        trainEx1.setTransportType("TRAIN");
        trainEx1.setDescription("Express Train to Airport");
        trainEx1.setStations(Arrays.asList("Central Station", "Business District", "Airport Terminal"));
        trainEx1.setStatus("ACTIVE");
        trainEx1.setDelayMinutes(0);
        trainEx1.setLastUpdate(LocalDateTime.now());

        // Sauvegarder les lignes et récupérer les IDs
        TransportLine savedBus101 = transportLineRepository.save(bus101);
        TransportLine savedBus202 = transportLineRepository.save(bus202);
        TransportLine savedMetroRed = transportLineRepository.save(metroRed);
        TransportLine savedMetroBlue = transportLineRepository.save(metroBlue);
        TransportLine savedTrainEx1 = transportLineRepository.save(trainEx1);

        System.out.println("Lignes de transport créées avec IDs: " + 
            savedBus101.getId() + ", " + savedBus202.getId() + ", " + 
            savedMetroRed.getId() + ", " + savedMetroBlue.getId() + ", " + 
            savedTrainEx1.getId());

        // Créer les horaires
        Schedule s1 = new Schedule();
        s1.setTransportLine(savedBus101);
        s1.setStation("Central Station");
        s1.setDepartureTime(LocalTime.of(8, 0));
        s1.setArrivalTime(LocalTime.of(8, 5));
        s1.setDayType("WEEKDAY");

        Schedule s2 = new Schedule();
        s2.setTransportLine(savedBus101);
        s2.setStation("Downtown Mall");
        s2.setDepartureTime(LocalTime.of(8, 15));
        s2.setArrivalTime(LocalTime.of(8, 20));
        s2.setDayType("WEEKDAY");

        Schedule s3 = new Schedule();
        s3.setTransportLine(savedBus101);
        s3.setStation("City Park");
        s3.setDepartureTime(LocalTime.of(8, 30));
        s3.setArrivalTime(LocalTime.of(8, 35));
        s3.setDayType("WEEKDAY");

        Schedule s4 = new Schedule();
        s4.setTransportLine(savedBus202);
        s4.setStation("North Terminal");
        s4.setDepartureTime(LocalTime.of(9, 0));
        s4.setArrivalTime(LocalTime.of(9, 5));
        s4.setDayType("WEEKDAY");

        Schedule s5 = new Schedule();
        s5.setTransportLine(savedBus202);
        s5.setStation("City Center");
        s5.setDepartureTime(LocalTime.of(9, 20));
        s5.setArrivalTime(LocalTime.of(9, 25));
        s5.setDayType("WEEKDAY");

        Schedule s6 = new Schedule();
        s6.setTransportLine(savedMetroRed);
        s6.setStation("East Gate");
        s6.setDepartureTime(LocalTime.of(7, 30));
        s6.setArrivalTime(LocalTime.of(7, 35));
        s6.setDayType("WEEKDAY");

        Schedule s7 = new Schedule();
        s7.setTransportLine(savedMetroRed);
        s7.setStation("Central Hub");
        s7.setDepartureTime(LocalTime.of(7, 45));
        s7.setArrivalTime(LocalTime.of(7, 50));
        s7.setDayType("WEEKDAY");

        Schedule s8 = new Schedule();
        s8.setTransportLine(savedTrainEx1);
        s8.setStation("Central Station");
        s8.setDepartureTime(LocalTime.of(6, 0));
        s8.setArrivalTime(LocalTime.of(6, 5));
        s8.setDayType("WEEKDAY");

        // Sauvegarder les horaires
        scheduleRepository.saveAll(Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8));

        // Créer les informations trafic
        TrafficInfo t1 = new TrafficInfo();
        t1.setLocation("Central Station Area");
        t1.setSeverity("MEDIUM");
        t1.setDescription("Road construction affecting traffic");
        t1.setIncidentType("CONSTRUCTION");
        t1.setReportedAt(LocalDateTime.now());

        TrafficInfo t2 = new TrafficInfo();
        t2.setLocation("North District Highway");
        t2.setSeverity("HIGH");
        t2.setDescription("Multi-vehicle accident, expect delays");
        t2.setIncidentType("ACCIDENT");
        t2.setReportedAt(LocalDateTime.now());

        TrafficInfo t3 = new TrafficInfo();
        t3.setLocation("City Center");
        t3.setSeverity("LOW");
        t3.setDescription("Minor congestion due to events");
        t3.setIncidentType("CONGESTION");
        t3.setReportedAt(LocalDateTime.now());

        // Sauvegarder les informations trafic
        trafficInfoRepository.saveAll(Arrays.asList(t1, t2, t3));

        // Vérification finale
        long transportCount = transportLineRepository.count();
        long scheduleCount = scheduleRepository.count();
        long trafficCount = trafficInfoRepository.count();

        System.out.println("=== INITIALISATION DES DONNÉES TERMINÉE ===");
        System.out.println("Lignes de transport créées: " + transportCount);
        System.out.println("Horaires créés: " + scheduleCount);
        System.out.println("Incidents trafic créés: " + trafficCount);
        System.out.println("=== L'APPLICATION EST PRÊTE ===");
    }
}