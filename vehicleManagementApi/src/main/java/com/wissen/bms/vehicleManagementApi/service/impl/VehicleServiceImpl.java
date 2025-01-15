package com.wissen.bms.vehicleManagementApi.service.impl;

import com.wissen.bms.vehicleManagementApi.model.Vehicle;
import com.wissen.bms.vehicleManagementApi.repo.VehicleRepo;
import com.wissen.bms.vehicleManagementApi.service.VehicleService;
import com.wissen.bms.vehicleManagementApi.specification.VehicleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
