package com.example.ev_station_management.service;

import com.example.ev_station_management.dto.BatteryReceiveRequestDTO;
import com.example.ev_station_management.model.BatteriesStock;
import com.example.ev_station_management.model.Station;
import com.example.ev_station_management.repository.BatteriesStockRepository;
import com.example.ev_station_management.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BatteriesReceivedService {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private BatteriesStockRepository batteriesStockRepository;

    /**
     * Refill stock for a given station based on the BatteryReceiveRequestDTO.
     *
     * @param request the BatteryReceiveRequestDTO containing stock details
     */
    public void refillStock(BatteryReceiveRequestDTO request) {
        Long stationId = request.getStationId().longValue();

        // Fetch the station from the database
        Optional<Station> optionalStation = stationRepository.findById(stationId);
        if (optionalStation.isEmpty()) {
            throw new IllegalArgumentException("Station with ID " + stationId + " not found.");
        }

        Station station = optionalStation.get();

        // Update available stock in the station
        int currentStock = station.getAvailableStock();
        int newStock = Integer.parseInt(request.getQuantity());
        station.setAvailableStock(currentStock + newStock);
        stationRepository.save(station);

        // Update batteries in the batteries_stock table
        List<BatteryReceiveRequestDTO.BatteryDTO> batteries = request.getBatteries();
        if (batteries != null) {
            for (BatteryReceiveRequestDTO.BatteryDTO battery : batteries) {
                BatteriesStock batteriesStock = new BatteriesStock(stationId, battery.getBatteryId());
                batteriesStockRepository.save(batteriesStock);
            }
        }
    }
}
