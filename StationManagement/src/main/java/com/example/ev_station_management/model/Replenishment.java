package com.example.ev_station_management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Replenishment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replenishment_id; // Renamed to match the "replenishment_id"

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false) // Link to the station using station_id
    private Station station;  // Use the Station object directly to link the station

    private int requestedStock;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
    public long getReplenishment_id() {
        return replenishment_id;
    }

    public void setReplenishment_id(long replenishment_id) {
        this.replenishment_id = replenishment_id;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public int getRequestedStock() {
        return requestedStock;
    }

    public void setRequestedStock(int requestedStock) {
        this.requestedStock = requestedStock;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
