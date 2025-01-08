package com.wissen.bms.reportingAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.bms.reportingAPI.model.Vehicle;
import com.wissen.bms.reportingAPI.service.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
	
	@Autowired
    private VehicleService vehicleService;
	
	@GetMapping("/all")
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/active")
    public List<Vehicle> getActiveVehicles() {
        return vehicleService.getActiveVehicles();
    }

    @GetMapping("/issues")
    public List<Vehicle> getVehiclesWithIssues() {
        return vehicleService.getVehiclesWithIssues();
    }
}
