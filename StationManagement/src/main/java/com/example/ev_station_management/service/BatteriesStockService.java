package com.example.ev_station_management.service;

import com.example.ev_station_management.model.BatteriesStock;
import com.example.ev_station_management.dto.BatteriesReceived;
import com.example.ev_station_management.model.Station;
import com.example.ev_station_management.repository.BatteriesStockRepository;
import com.example.ev_station_management.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatteriesStockService {

    @Autowired
    private BatteriesStockRepository batteriesStockRepository;

    @Autowired
    private StationRepository stationRepository;

    public void saveBatteriesStock(BatteriesReceived batteriesReceived) {
        // Extract the station ID and list of battery IDs from the request
        Long stationId = batteriesReceived.getStation_id();
        List<Long> batteryIds = batteriesReceived.getListOfBattery();

        // Retrieve the Station object from the database using the stationId
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Station not found"));

        // Increment the availableStock based on the number of batteries being received
        int numberOfBatteriesReceived = batteryIds.size();
        station.setAvailableStock(station.getAvailableStock() + numberOfBatteriesReceived);

        // Save the updated Station entity with the incremented available stock
        stationRepository.save(station);
        for (Long batteryId : batteryIds) {
            BatteriesStock batteriesStock = new BatteriesStock();
            batteriesStock.setStationId(stationId); // Assuming station ID is being set as the BatteriesStock ID
            batteriesStock.setBatteryId(batteryId); // Set the battery ID

            // Save each entry in the database
            batteriesStockRepository.save(batteriesStock);
        }
    }
}
