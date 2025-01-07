package com.wissen.bms.reportingAPI.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wissen.bms.reportingAPI.model.Vehicle;

@Service
public interface VehicleService {
	List<Vehicle> getAllVehicles();

    List<Vehicle> getActiveVehicles();

    List<Vehicle> getVehiclesWithIssues();
}
