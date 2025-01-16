package com.wissen.bms.reportingAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "battery_fault")
public class BatteryFaultModel {

        @Id
        @GeneratedValue(generator = "custom-fault-id")
        @GenericGenerator(name = "custom-fault-id", strategy = "com.wissen.bms.reportingAPI.customIdGenerator.CustomFaultIdGenerator")
        @Column(name = "fault_id", updatable = false, nullable = false)
        private String faultId;

        @NotNull
        @Size(max = 50)
        @Column(name = "gps")
        private String gps;

        @NotNull
        @Size(max = 50)
        @Column(name = "vehicle_id")
        private String vehicleId;

        @NotNull
        @Size(max = 50)
        @Column(name = "battery_id")
        private String batteryId;

        @NotNull
        @Size(max = 255)
        @Column(name = "fault_reason")
        private String faultReason;

        @NotNull
        @Size(max = 255)
        @Column(name = "recommendation")
        private String recommendation;

        @NotNull
        @Column(name = "time")
        private Timestamp time;

        @NotNull
        @Size(max = 50)
        @Column(name = "level")
        private String level;

        @NotNull
        @Size(max = 50)
        @Column(name = "risk")
        private String risk;
}

