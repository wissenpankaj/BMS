package com.battery_inventory.controller;

import com.battery_inventory.dto.BatteryDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/batteries")
public class BatteryController {

    // Retrieve battery details
    @GetMapping
    public ResponseEntity<?> getBatteryDetails(@RequestParam(required = false) String batteryId) {
        // Logic to fetch battery details
        return ResponseEntity.ok("List of batteries with their details.");
    }

    // Add a new battery
    @PostMapping
    public ResponseEntity<?> addNewBattery(@RequestBody BatteryDto batteryDto) {
        // Logic to add a new battery
        return ResponseEntity.status(HttpStatus.CREATED).body("Battery added successfully.");
    }

    // Update battery status or location
    @PatchMapping("/{batteryId}")
    public ResponseEntity<?> updateBatteryStatus(
            @PathVariable String batteryId,
            @RequestBody BatteryDto updateDto) {
        // Logic to update battery status or location
        return ResponseEntity.ok("Battery status updated successfully.");
    }

    // Mark a battery for scrapping
    @PatchMapping("/{batteryId}/scrap")
    public ResponseEntity<?> markBatteryForScrapping(@PathVariable String batteryId) {
        // Logic to mark a battery for scrapping
        return ResponseEntity.ok("Battery marked for scrapping successfully.");
    }
}
