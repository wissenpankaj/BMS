package com.example.ev_station_management.dto;

import com.example.ev_station_management.model.Replenishment;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "faulty_battery")
public class FaultyBattery {

    @Id
    private String batteryId; // Assuming batteryId is the primary key

    private String type;
    private String serialNumber;
    @ManyToOne
    @JoinColumn(name = "replenishment_id")
    private Replenishment replenishment;
    // Getter and Setter for batteryId
    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    // Getter and Setter for type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Getter and Setter for serialNumber
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Replenishment getReplenishment() {
        return replenishment;
    }

    public void setReplenishment(Replenishment replenishment) {
        this.replenishment = replenishment;
    }
}
