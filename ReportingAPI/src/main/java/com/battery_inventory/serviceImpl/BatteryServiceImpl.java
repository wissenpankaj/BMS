package com.battery_inventory.serviceImpl;

import com.battery_inventory.dto.BatteryDto;
import com.battery_inventory.service.BatteryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BatteryServiceImpl implements BatteryService {

    // In-memory list of batteries (can be replaced with database)
    private List<BatteryDto> batteryStore = new ArrayList<>();

    @Override
    public List<BatteryDto> getBatteryDetails(Optional<String> batteryId) {
        if (batteryId.isPresent()) {
            // Return a specific battery if the ID is provided
            return batteryStore.stream()
                    .filter(battery -> batteryId.get().equals(battery.serialNumber))
                    .toList();
        }
        // Return all batteries if no ID is specified
        return batteryStore;
    }

    @Override
    public BatteryDto addNewBattery(BatteryDto batteryDto) {
        // Check if a battery with the same serial number already exists
        for (BatteryDto battery : batteryStore) {
            if (battery.serialNumber.equals(batteryDto.serialNumber)) {
                throw new IllegalArgumentException("Battery with this serial number already exists.");
            }
        }

        // Add new battery to the store
        batteryStore.add(batteryDto);
        return batteryDto;
    }

    @Override
    public BatteryDto updateBatteryStatus(String batteryId, BatteryDto updateDto) {
        // Find and update the battery with the given ID
        for (BatteryDto battery : batteryStore) {
            if (battery.serialNumber.equals(batteryId)) {
                battery.type = updateDto.type != null ? updateDto.type : battery.type;
                battery.location = updateDto.location != null ? updateDto.location : battery.location;
                battery.status = updateDto.status != null ? updateDto.status : battery.status;
                return battery;
            }
        }
        throw new IllegalArgumentException("Battery with ID " + batteryId + " not found.");
    }

    @Override
    public boolean markBatteryForScrapping(String batteryId) {
        // Mark the battery as "scrapped"
        for (BatteryDto battery : batteryStore) {
            if (battery.serialNumber.equals(batteryId)) {
                battery.status = "Scrapped";
                return true;
            }
        }
        throw new IllegalArgumentException("Battery with ID " + batteryId + " not found.");
    }
}
