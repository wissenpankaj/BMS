package com.example.ev_station_management.controller;

import com.example.ev_station_management.dto.BatteryReceiveRequestDTO;
import com.example.ev_station_management.service.BatteriesReceivedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/batteries-received")
public class BatteryReceiveController {
    @Autowired
    BatteriesReceivedService batteriesReceivedService;
    // Endpoint to receive the battery data
    @PostMapping("/receive")
    @ResponseStatus(HttpStatus.OK)
    public String receiveBatteries(@RequestBody BatteryReceiveRequestDTO batteryReceiveRequestDTO) {

        System.out.println("Received Battery Order: " + batteryReceiveRequestDTO.toString());
        batteriesReceivedService.refillStock(batteryReceiveRequestDTO);
        return "Batteries received successfully!";
    }
}
