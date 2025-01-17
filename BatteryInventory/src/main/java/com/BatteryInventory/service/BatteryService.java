package com.BatteryInventory.service;


import com.BatteryInventory.model.Battery;
import com.BatteryInventory.repository.BatteryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BatteryService {

    private final BatteryRepository batteryRepository;

    public BatteryService(BatteryRepository batteryRepository) {
        this.batteryRepository = batteryRepository;
    }

    /**
     * 1) Faulty Batteries Endpoint (POST /api/v1/batteries/faulty)
     *    - For each battery in the request, mark or create it with "status = faulty".
     */
    public List<Battery> saveFaultyBatteries(String stationId, List<Battery> faultyList) {
        List<Battery> savedBatteries = new ArrayList<>();

        for (Battery incoming : faultyList) {
            // Check if battery already exists
            Battery existing = batteryRepository.findByBatteryId(incoming.getBatteryId());
            if (existing == null) {
                // Create a new one
                Battery newBattery = new Battery(
                        incoming.getBatteryId(),
                        incoming.getType(),
                        incoming.getSerialNumber(),
                        "faulty"  // Mark as faulty
                );
                savedBatteries.add(batteryRepository.save(newBattery));
            } else {
                // Update existing as faulty
                existing.setStatus("faulty");
                existing.setType(incoming.getType());
                existing.setSerialNumber(incoming.getSerialNumber());
                savedBatteries.add(batteryRepository.save(existing));
            }
        }
        return savedBatteries;
    }

    /**
     * 2) GET /api/v1/batteries/availability
     *    Summarize by type, status, count.
     */
    public List<BatteryAvailabilityDto> getBatteryAvailability() {
        var projections = batteryRepository.findBatteryAvailability();
        List<BatteryAvailabilityDto> results = new ArrayList<>();
        for (var p : projections) {
            results.add(new BatteryAvailabilityDto(p.getType(), p.getStatus(), p.getTotalCount()));
        }
        return results;
    }

    /**
     * Simple DTO for availability
     */
    public static class BatteryAvailabilityDto {
        private String type;
        private String status;
        private long availableStock;

        public BatteryAvailabilityDto(String type, String status, long availableStock) {
            this.type = type;
            this.status = status;
            this.availableStock = availableStock;
        }
        // getters
        public String getType() {
            return type;
        }
        public String getStatus() {
            return status;
        }
        public long getAvailableStock() {
            return availableStock;
        }
    }
}

