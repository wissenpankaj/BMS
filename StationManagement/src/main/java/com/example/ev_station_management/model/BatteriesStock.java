package com.example.ev_station_management.model;

import jakarta.persistence.*;

@Entity
public class BatteriesStock {

    @Id
    @Column(name = "station_id") // Renamed the id to station_id as per your request
    private Long stationId; // The station_id field is now the primary key and manually provided

    @Column(name = "battery_id")
    private Long batteryId; // Foreign key to the Battery table (battery ID)

    // Constructors
    public BatteriesStock() {}

    public BatteriesStock(Long stationId, Long batteryId) {
        this.stationId = stationId; // Manually set the stationId
        this.batteryId = batteryId;
    }

    // Getters and Setters
    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(Long batteryId) {
        this.batteryId = batteryId;
    }

    @Override
    public String toString() {
        return "BatteriesStock{" +
                "stationId=" + stationId +
                ", batteryId=" + batteryId +
                '}';
    }
}
