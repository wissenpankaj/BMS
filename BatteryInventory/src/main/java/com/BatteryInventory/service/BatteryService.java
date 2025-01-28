package com.BatteryInventory.service;

import com.BatteryInventory.DTO.FaultyBatteryRequest;
import com.BatteryInventory.DTO.FaultyBatteryTypeRequest;
import com.BatteryInventory.model.Battery;
import com.BatteryInventory.model.SalesOrder;
import com.BatteryInventory.repository.BatteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BatteryService {

    @Autowired
    private BatteryRepository batteryRepository;

    /**
     * Allocates available batteries of a specific type to a sales order.
     */
    public List<Battery> allocateAvailableBatteries(String type, SalesOrder salesOrder) {
        List<Battery> availableBatteries = batteryRepository.findAvailableBatteriesByType(type);
        for (Battery battery : availableBatteries) {
            battery.setStatus("allocated");
            battery.setSalesOrder(salesOrder);
        }
        return batteryRepository.saveAll(availableBatteries);
    }

    /**
     * Saves a list of new batteries to the database.
     */
    public void saveBatteries(List<Battery> batteries) {
        batteryRepository.saveAll(batteries);
    }

    public List<String> getAvailableBatteryIds(String batteryType) {
        return batteryRepository.findAvailableBatteriesByType(batteryType)
                .stream()
                .map(Battery::getBatteryId)
                .collect(Collectors.toList());
    }

    void makeBatteryAsFaulty(FaultyBatteryRequest request) {
        for (FaultyBatteryTypeRequest batteryRequest : request.getBatteries()) {
            // Update the status of each battery ID in the request
            for (String batteryId : batteryRequest.getBatteryIds()) {
                Battery battery = batteryRepository.findByBatteryId(batteryId)
                        .orElseThrow(() -> new IllegalArgumentException("Battery with ID " + batteryId + " not found"));
                battery.setStatus("faulty");
                batteryRepository.save(battery);
            }
        }
    }

}

