package com.example.ev_station_management.repository;

import com.example.ev_station_management.model.BatterySwapLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatterySwapLogRepository extends JpaRepository<BatterySwapLog, Long> {

    // You can add custom queries or methods if needed

    // Example of a custom query method to find BatterySwapLogs by stationId
    List<BatterySwapLog> findByStationId(Long stationId);

}
