package com.BatteryInventory.service;

import com.BatteryInventory.dto.FaultyBatteryRequest;
import com.BatteryInventory.dto.FaultyBatteryTypeRequest;
import com.BatteryInventory.model.Battery;
import com.BatteryInventory.model.SalesOrder;
import com.BatteryInventory.repository.BatteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BatteryService {

    @Autowired
    private BatteryRepository batteryRepository;

    /**
     * Allocates available batteries of a specific type to a sales order.
     */
    @Transactional
    public List<Battery> allocateAvailableBatteries(String type, SalesOrder salesOrder) {
        List<Battery> availableBatteries = batteryRepository.findAvailableBatteriesByType(type);
        for (Battery battery : availableBatteries) {
            battery.setStatus("allocated");
            battery.setSalesOrder(salesOrder);
        }
        if (!availableBatteries.isEmpty())
            return batteryRepository.saveAll(availableBatteries);
        else throw new RuntimeException("Batteries not available for allocation");
    }

    /**
     * Saves a list of new batteries to the database.
     */
    public void saveBatteries(List<Battery> batteries) {
        batteryRepository.saveAll(batteries);
    }

    public List<String> getAvailableBatteryIds(String batteryType) {
        return batteryRepository.findAvailableBatteriesByType(batteryType).stream().map(Battery::getBatteryId).collect(Collectors.toList());
    }

    @Transactional
    void makeBatteryAsFaulty(FaultyBatteryRequest request) {
        // Collecting all battery IDs from request
        List<String> batteryIds = request.getBatteries().stream()
                .flatMap(batteryRequest -> batteryRequest.getBatteryIds().stream())
                .collect(Collectors.toList());

        // Fetch batteries from DB
        List<Battery> batteries = batteryRepository.findAllByBatteryIdIn(batteryIds);
        if (!batteries.isEmpty()) {

            // Update status to faulty
            batteries.forEach(battery -> {
                battery.setStatus("faulty");
            });

            // Save all modified batteries in a single transaction
            batteryRepository.saveAll(batteries);
        } else {
            throw new RuntimeException("No batteries found for IDs: " + batteryIds);
        }

        //old
        /*
        for (FaultyBatteryTypeRequest batteryRequest : request.getBatteries()) {
            // Update the status of each battery ID in the request
            for (String batteryId : batteryRequest.getBatteryIds()) {
                Battery battery = batteryRepository.findByBatteryId(batteryId).orElseThrow(() -> new IllegalArgumentException("Battery with ID " + batteryId + " not found"));
                battery.setStatus("faulty");
                batteryRepository.save(battery);
            }
        }
        */
    }

}

