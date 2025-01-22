package com.example.ev_station_management.controller;

import com.example.ev_station_management.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stations")
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping("/{id}/swap")
    public String swapBattery(@PathVariable Long id) {
        System.out.println("Driver Request Swap");
        return stationService.handleSwapRequest(id);
    }
}
