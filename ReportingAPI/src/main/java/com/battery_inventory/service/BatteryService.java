package com.battery_inventory.service;

import com.battery_inventory.dto.BatteryDto;

import java.util.List;
import java.util.Optional;

public interface BatteryService {
    // Get a list of all batteries or a specific battery by ID
    List<BatteryDto> getBatteryDetails(Optional<String> batteryId);

    // Add a new battery
    BatteryDto addNewBattery(BatteryDto batteryDto);

    // Update battery status or location
    BatteryDto updateBatteryStatus(String batteryId, BatteryDto updateDto);

    // Mark a battery for scrapping
    boolean markBatteryForScrapping(String batteryId);
}
