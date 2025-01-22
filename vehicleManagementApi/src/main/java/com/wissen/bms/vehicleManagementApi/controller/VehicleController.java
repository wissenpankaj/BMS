package com.wissen.bms.vehicleManagementApi.controller;

import com.wissen.bms.vehicleManagementApi.model.Vehicle;
import com.wissen.bms.vehicleManagementApi.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    // Endpoint to add a new vehicle
    @PostMapping("/add")
    public ResponseEntity<String> addVehicle(@RequestBody Vehicle vehicle) {
        vehicleService.addVehicle(vehicle);
        return ResponseEntity.ok("Vehicle added successfully");
    }

    // Endpoint to get vehicles based on filter criteria
    @GetMapping("/get")
    public ResponseEntity<List<Vehicle>> getVehicles(
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String vehicleType,
            @RequestParam(required = false) String batteryId) {
        List<Vehicle> vehicles = vehicleService.getVehicles(vehicleId, make, model, vehicleType, batteryId);
        return ResponseEntity.ok(vehicles);
    }

    // Endpoint to update a vehicle
    @PatchMapping("/update")
    public ResponseEntity<String> updateVehicle(
            @RequestParam String vehicleId,
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String vehicleType,
            @RequestParam(required = false) String batteryId) {
        vehicleService.updateVehicle(vehicleId, make, model, vehicleType, batteryId);
        return ResponseEntity.ok("Vehicle updated successfully");
    }
}
