package com.smartcity.mobility.repository;

import com.smartcity.mobility.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    List<Schedule> findByTransportLineLineNumber(String lineNumber);
    
    List<Schedule> findByStationAndDayType(String station, String dayType);
    
    @Query("SELECT s FROM Schedule s WHERE s.transportLine.lineNumber = :lineNumber " +
           "AND s.station = :station AND s.dayType = :dayType " +
           "AND s.departureTime >= :startTime AND s.departureTime <= :endTime")
    List<Schedule> findSchedulesByLineAndStationAndTime(
            @Param("lineNumber") String lineNumber,
            @Param("station") String station,
            @Param("dayType") String dayType,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}