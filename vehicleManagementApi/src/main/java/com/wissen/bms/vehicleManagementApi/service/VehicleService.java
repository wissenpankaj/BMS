package com.wissen.bms.vehicleManagementApi.service;

import com.wissen.bms.vehicleManagementApi.model.Vehicle;
import java.util.List;

public interface VehicleService {

    // Method to add a new vehicle
    public void addVehicle(Vehicle vehicle);

    // Method to get vehicles based on filter criteria
    public List<Vehicle> getVehicles(String vehicleId, String make, String model, String vehicleType, String batteryId);

    public void updateVehicle(String vehicleId, String make, String model, String vehicleType, String batteryId);
}
