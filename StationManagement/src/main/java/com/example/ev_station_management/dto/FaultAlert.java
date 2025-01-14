package com.example.ev_station_management.dto;

import java.time.LocalDateTime;

public class FaultAlert {
    private String batteryId;
    private String vehicleId;
    private String gps;
    private String risk;
    private String level;
    private String recommendation;
    private String faultReason;
    private LocalDateTime timestamp;

    // Getters and setters
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

    @Override
    public String toString() {
        return "FaultAlert{" +
                "batteryId='" + batteryId + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", gps='" + gps + '\'' +
                ", risk='" + risk + '\'' +
                ", level='" + level + '\'' +
                ", recommendation='" + recommendation + '\'' +
                ", faultReason='" + faultReason + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
