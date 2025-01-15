package com.example.ev_station_management.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class FaultyBattery {
    // Getters and setters
    private String batteryId;
    private String type;
    private String serialNumber;

}
