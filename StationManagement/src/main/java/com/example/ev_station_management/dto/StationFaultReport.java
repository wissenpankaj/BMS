package com.example.ev_station_management.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@Component
public class StationFaultReport {

    // Getters and setters
    private String stationId;
    private List<FaultyBattery> faultyBatteries;

}

