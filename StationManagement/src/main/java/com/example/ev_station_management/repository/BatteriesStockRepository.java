package com.example.ev_station_management.repository;

import com.example.ev_station_management.model.BatteriesStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatteriesStockRepository extends JpaRepository<BatteriesStock, Long> {
    // You can add custom queries here if needed
    // Custom query to find the first BatteriesStock entry by batteryId
    Optional<BatteriesStock> findFirstByStationId(Long stationId);

    Optional<BatteriesStock> findByStationIdAndBatteryId(Long stationId, String batteryId);
}
