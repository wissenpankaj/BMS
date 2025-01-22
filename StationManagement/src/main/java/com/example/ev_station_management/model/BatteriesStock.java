package com.example.ev_station_management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "batteries_stock")
public class BatteriesStock {

    @Id
    @Column(name = "station_id")
    private Long stationId; // The station_id field is now the primary key and manually provided

    @Column(name = "battery_id")
    private String batteryId; // Foreign key to the Battery table (battery ID)

    // Constructors
    public BatteriesStock() {}

    public BatteriesStock(Long stationId, String batteryId) {
        this.stationId = stationId;
        this.batteryId = batteryId;
    }

    // Getters and Setters
    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    @Override
    public String toString() {
        return "BatteriesStock{" +
                "stationId=" + stationId +
                ", batteryId='" + batteryId + '\'' +
                '}';
    }
}
