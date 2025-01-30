package com.example.ev_station_management.model;

import com.example.ev_station_management.dto.FaultyBattery;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Replenishment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replenishmentId; // Renamed field to match naming conventions

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false) // Link to the station using station_id
    private Station station; // Use the Station object directly to link the station

    private String batteryType; // New field for the type of battery
    private int quantity;       // New field for the requested quantity
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) // Relationship to FaultyBattery
    @JoinColumn(name = "replenishment_id") // Foreign key column in FaultyBattery table
    private List<FaultyBattery> faultyBatteries; // New field for a list of faulty batteries

    // Getters and Setters
    public long getReplenishmentId() {
        return replenishmentId;
    }

    public void setReplenishmentId(long replenishmentId) {
        this.replenishmentId = replenishmentId;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(String batteryType) {
        this.batteryType = batteryType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<FaultyBattery> getFaultyBatteries() {
        return faultyBatteries;
    }

    public void setFaultyBatteries(List<FaultyBattery> faultyBatteries) {
        this.faultyBatteries = faultyBatteries;
    }

    @Override
    public String toString() {
        return "Replenishment{" +
                "replenishmentId=" + replenishmentId +
                ", station=" + (station != null ? station.getStationId() : null) +
                ", batteryType='" + batteryType + '\'' +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                ", faultyBatteries=" + faultyBatteries +
                '}';
    }
}
