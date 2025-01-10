package com.wissen.bms.reportingAPI.model;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "fault_log")
public class FaultLogModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String faultId;

    @Column(nullable = false)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    private String createdBy;

    private String vehicleId;

    private String batteryId;

    @Column(nullable = false)
    private String serviceStationId;

    private String description;

    @Column(length = 255)
    private String faultType;

}
