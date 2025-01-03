package com.battery_management.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.battery_management.model.Vehicle;

@Service
public interface VehicleService {
	List<Vehicle> getAllVehicles();

    List<Vehicle> getActiveVehicles();

    List<Vehicle> getVehiclesWithIssues();
}
