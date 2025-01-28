package com.wissen.bms.vehicleManagementApi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @Column(name = "vehicle_id", length = 50)
    @NotBlank(message = "Vehicle ID is mandatory")
    private String vehicleId;

    @Column(name = "make")
    @NotBlank(message = "Make is mandatory")
    @Size(max = 255, message = "Make cannot exceed 255 characters")
    private String make;

    @Column(name = "model")
    @NotBlank(message = "Model is mandatory")
    @Size(max = 255, message = "Model cannot exceed 255 characters")
    private String model;

    @Column(name = "vehicle_type", length = 50)
    @Size(max = 50, message = "Vehicle type cannot exceed 50 characters")
    private String vehicleType;

    @Column(name = "battery_id", length = 50)
    @NotNull(message = "Battery ID is mandatory")
    private String batteryId;
}
