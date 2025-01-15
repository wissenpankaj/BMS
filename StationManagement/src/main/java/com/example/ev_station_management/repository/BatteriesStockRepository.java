package com.example.ev_station_management.repository;

import com.example.ev_station_management.model.BatteriesStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatteriesStockRepository extends JpaRepository<BatteriesStock, Long> {
    // You can add custom queries here if needed
    // Custom query to find the first BatteriesStock entry by batteryId
    BatteriesStock findFirstByBatteryId(Long batteryId);
}
