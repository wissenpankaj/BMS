package com.wissen.bms.vehicleManagementApi.repo;

import com.wissen.bms.vehicleManagementApi.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle, String>, JpaSpecificationExecutor<Vehicle> {
}
