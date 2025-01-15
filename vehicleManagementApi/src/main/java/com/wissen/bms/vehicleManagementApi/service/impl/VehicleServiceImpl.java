package com.wissen.bms.vehicleManagementApi.service.impl;

import com.wissen.bms.vehicleManagementApi.model.Vehicle;
import com.wissen.bms.vehicleManagementApi.repo.VehicleRepo;
import com.wissen.bms.vehicleManagementApi.service.VehicleService;
import com.wissen.bms.vehicleManagementApi.specification.VehicleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepo vehicleRepo;

    @Override
    public void addVehicle(Vehicle vehicle) {
        vehicleRepo.save(vehicle);
    }

    @Override
    public List<Vehicle> getVehicles(String vehicleId, String make, String model, String vehicleType, String batteryId) {
        Specification<Vehicle> spec = VehicleSpecification.getVehicles(vehicleId, make, model, vehicleType, batteryId);
        return vehicleRepo.findAll(spec);
    }

    @Override public void updateVehicle(String vehicleId, String make, String model, String vehicleType, String batteryId) {
        // Fetch the vehicle to be updated
        Optional<Vehicle> vehicleOptional = vehicleRepo.findById(vehicleId);
        if (vehicleOptional.isPresent()) {
            Vehicle vehicle = vehicleOptional.get();
            // Update fields based on provided parameters
            if (StringUtils.hasText(make)) { vehicle.setMake(make); }
            if (StringUtils.hasText(model)) { vehicle.setModel(model); }
            if (StringUtils.hasText(vehicleType)) { vehicle.setVehicleType(vehicleType); }
            if (StringUtils.hasText(batteryId)) { vehicle.setBatteryId(batteryId); }
            // Save the updated vehicle
            vehicleRepo.save(vehicle);
        } else {
            // Handle the case where the vehicle is not found
            throw new RuntimeException("Vehicle with ID " + vehicleId + " not found");
        }
    }
}
