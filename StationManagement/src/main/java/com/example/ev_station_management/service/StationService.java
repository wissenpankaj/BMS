package com.example.ev_station_management.service;

import com.example.ev_station_management.model.Replenishment;
import com.example.ev_station_management.model.Station;
import com.example.ev_station_management.repository.ReplenishmentRepository;
import com.example.ev_station_management.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class StationService {

    private static final double EARTH_RADIUS_KM = 6371;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private ReplenishmentRepository replenishmentRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    /**
     * Handle a swap request from the driver.
     */
    public String handleSwapRequest(Long stationId) {
        Station station = getStationById(stationId);
        if (station == null) {
            return "Station not found.";
        }

        // Decrement the available stock
        station.setAvailableStock(station.getAvailableStock() - 1);
        stationRepository.save(station);

        System.out.println("Battery swapped at Station: " + station.getName() +
                ". Updated Stock: " + station.getAvailableStock());

        // Trigger replenishment if stock falls below the minimum threshold
        if (station.getAvailableStock() < station.getMinStockLevel()) {
            System.out.println("Station " + station.getName() + " stock below minimum threshold. Triggering replenishment.");
            triggerReplenishment(station);
        }

        return "Swap successful at station: " + station.getName();
    }

    /**
     * Finds the nearest station based on GPS coordinates.
     */
    public Station findNearestStation(String gps) {
        // Parse latitude and longitude from the GPS string
        String[] coords = gps.split(",");
        double vehicleLatitude = Double.parseDouble(coords[0]);
        double vehicleLongitude = Double.parseDouble(coords[1]);

        // Fetch all stations from the database
        List<Station> stations = stationRepository.findAll();

        // Calculate the nearest station
        return stations.stream()
                .min(Comparator.comparingDouble(station ->
                        calculateDistance(vehicleLatitude, vehicleLongitude, station.getLatitude(), station.getLongitude())))
                .orElse(null);
    }

    /**
     * Finds the next nearest station with sufficient stock.
     */
    public Station findNextNearestStation(String gps) {
        // Parse latitude and longitude from the GPS string
        String[] coords = gps.split(",");
        double vehicleLatitude = Double.parseDouble(coords[0]);
        double vehicleLongitude = Double.parseDouble(coords[1]);

        // Fetch all stations from the database
        List<Station> stations = stationRepository.findAll();

        // Find the nearest station with sufficient stock
        return stations.stream()
                .filter(station -> station.getAvailableStock() > 0) // Filter only stations with stock
                .min(Comparator.comparingDouble(station ->
                        calculateDistance(vehicleLatitude, vehicleLongitude, station.getLatitude(), station.getLongitude())))
                .orElse(null);
    }

    /**
     * Trigger a replenishment request for a station.
     */
    public void triggerReplenishment(Station station) {
        int requestedStock = (int) Math.ceil(1.2 * station.getMinStockLevel());
        System.out.println("Replenishment requested for Station: " + station.getName() +
                ". Requested Stock: " + requestedStock);

        // Create and save a replenishment request
        Replenishment replenishment = new Replenishment();
        replenishment.setStation(station);
        replenishment.setRequestedStock(requestedStock);
        replenishmentRepository.save(replenishment);

        // Publish replenishment request to Kafka
        publishReplenishmentToKafka(replenishment);
    }

    /**
     * Publish the replenishment request to a Kafka topic.
     */
    private void publishReplenishmentToKafka(Replenishment replenishment) {
        String message = "{\"stationId\":\"" + replenishment.getStation().getId() +
                "\",\"requestedStock\":" + replenishment.getRequestedStock() + "}";
        kafkaProducerService.sendReplenishmentRequest(message);
        System.out.println("Replenishment request published to Kafka for Station: " + replenishment.getStation().getName());
    }

    /**
     * Fetch a station by its ID.
     */
    public Station getStationById(Long id) {
        return stationRepository.findById(id).orElse(null);
    }

    /**
     * Calculates the distance between two GPS coordinates using the Haversine formula.
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert degrees to radians
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);

        // Haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(rLat1) * Math.cos(rLat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance in kilometers
        return EARTH_RADIUS_KM * c;
    }
}
