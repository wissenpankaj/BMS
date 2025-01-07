package com.wissen.bms.db;

import com.wissen.bms.db.model.BatteryMetrics;
import com.wissen.bms.db.repository.BatteryMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BatteryMetricsController {

    @Autowired
    BatteryMetricsRepository repository;

    @GetMapping("/batteries")
    public void doOperation(){
        // Get the current time
        OffsetDateTime endTime = OffsetDateTime.now();
        // Calculate the start time (30 days ago from the current time)
        OffsetDateTime startTime = endTime.minus(30, ChronoUnit.DAYS);
        String batteryId = "battery1";
        String vehicleId = "vehicle1";

        List<BatteryMetrics> metricsList = new ArrayList<>();
        // Add BatteryMetrics objects to the metricsList
        metricsList.add(new BatteryMetrics("vehicle1", "battery1", 12.34, 56.78, 3.7, 1.2, 75.0, 25.0, 0.003, 500, 1234.5, 120.0, 98.5));
        metricsList.add(new BatteryMetrics("vehicle2", "battery2", 34.56, 78.90, 3.6, 1.1, 80.0, 30.0, 0.002, 450, 987.6, 110.0, 97.0));
        repository.saveAll(metricsList);

        System.out.println("Find All");
        List<BatteryMetrics> metrics = repository.findAll(startTime, endTime);
        metrics.forEach(System.out::println);

        System.out.println("Find by");
        List<BatteryMetrics> metrics1 = repository.findByBatteryAndVehicleId(batteryId, vehicleId, startTime, endTime);
        metrics1.forEach(System.out::println);
    }
}
