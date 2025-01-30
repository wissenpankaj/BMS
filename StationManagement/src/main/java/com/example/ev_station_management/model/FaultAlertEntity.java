package com.example.ev_station_management.model;

import jakarta.persistence.*;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@Entity
@Table(name = "fault_alerts") // Map to table "fault_alerts"
public class FaultAlertEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-generated primary key

    private String batteryId;
    private String vehicleId;
    private String gps;
    private String risk;
    private String level;
    private String recommendation;
    private String faultReason;

    private LocalDateTime timestamp;

    // Add timestamps for database tracking
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getFaultReason() {
        return faultReason;
    }

    public void setFaultReason(String faultReason) {
        this.faultReason = faultReason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "FaultAlertEntity{" +
                "id=" + id +
                ", batteryId='" + batteryId + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", gps='" + gps + '\'' +
                ", risk='" + risk + '\'' +
                ", level='" + level + '\'' +
                ", recommendation='" + recommendation + '\'' +
                ", faultReason='" + faultReason + '\'' +
                ", timestamp=" + timestamp +
                ", createdAt=" + createdAt +
                '}';
    }
}
