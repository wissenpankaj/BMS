package com.wissen.bms.notification.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class VehicleData {

    @JsonProperty("batteryId")
    private String batteryId;

    @JsonProperty("vehicleId")
    private String vehicleId;

    @JsonProperty("gps")
    private String gps;

    @JsonProperty("risk")
    private String risk;

    @JsonProperty("level")
    private String level;

    @JsonProperty("recommendation")
    private String recommendation;

    @JsonProperty("faultReason")
    private String faultReason;

    @JsonProperty("timestamp")
    private String timestamp;

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "VehicleData{" +
                "batteryId='" + batteryId + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", gps='" + gps + '\'' +
                ", risk='" + risk + '\'' +
                ", level='" + level + '\'' +
                ", recommendation='" + recommendation + '\'' +
                ", faultReason='" + faultReason + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
