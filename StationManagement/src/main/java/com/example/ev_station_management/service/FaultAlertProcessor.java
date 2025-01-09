package com.example.ev_station_management.service;

import com.example.ev_station_management.model.FaultAlertEntity;
import com.example.ev_station_management.model.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FaultAlertProcessor {

    @Autowired
    private StationService stationService;

    @Autowired
    private NotificationService notificationService;

    public void processFaultAlert(FaultAlertEntity faultAlertEntity) {
        System.out.println("Processing Fault Alert: " + faultAlertEntity);

        switch (faultAlertEntity.getLevel().toUpperCase()) {
            case "CRITICAL":
                handleCriticalAlert(faultAlertEntity);
                break;

            case "LOW":
                handleLowAlert(faultAlertEntity);
                break;

            default:
                System.out.println("No specific action for alert level: " + faultAlertEntity.getLevel());
                break;
        }
    }

    private void handleCriticalAlert(FaultAlertEntity faultAlertEntity) {
        System.out.println("Handling CRITICAL Alert for Vehicle: " + faultAlertEntity.getVehicleId());

        // Validate GPS coordinates
        String gps = faultAlertEntity.getGps();
        if (gps == null || gps.isEmpty()) {
            System.out.println("GPS data is missing or invalid. Unable to recommend a station.");
            return;
        }

        // Find the nearest station
        Station nearestStation = stationService.findNearestStation(gps);
        if (nearestStation != null) {
            System.out.println("Nearest Station: " + nearestStation.getName() +
                    ", Stock: " + nearestStation.getAvailableStock());

            if (nearestStation.getAvailableStock() > 0) {
                // Sufficient stock available
                System.out.println("Station has sufficient stock. Recommending to driver.");
                notifyDriver(faultAlertEntity.getVehicleId(), nearestStation);
            } else {
                // Insufficient stock, find next nearest station
                System.out.println("Station does not have sufficient stock. Finding next nearest station...");
                Station nextNearestStation = stationService.findNextNearestStation(gps);
                if (nextNearestStation != null) {
                    System.out.println("Next Nearest Station: " + nextNearestStation.getName() +
                            ", Stock: " + nextNearestStation.getAvailableStock());
                    notifyDriver(faultAlertEntity.getVehicleId(), nextNearestStation);
                } else {
                    System.out.println("No alternative stations with sufficient stock found. Triggering replenishment.");
                    stationService.triggerReplenishment(nearestStation);
                    notifyDriverOfDelay(faultAlertEntity.getVehicleId(), nearestStation);
                }
            }
        } else {
            System.out.println("No station found for the given GPS coordinates.");
        }
    }

    private void handleLowAlert(FaultAlertEntity faultAlertEntity) {
        System.out.println("Handling LOW Alert for Vehicle: " + faultAlertEntity.getVehicleId());
        // Add specific logic for handling low alerts if needed
    }

    private void notifyDriver(String vehicleId, Station station) {
        String message = "Nearest station for swap: " + station.getName() +
                " (Stock: " + station.getAvailableStock() + "). Location: " +
                station.getLatitude() + ", " + station.getLongitude();
        notificationService.notifyDriver(vehicleId, message);
    }

    private void notifyDriverOfDelay(String vehicleId, Station station) {
        String message = "No batteries available at the nearest station: " + station.getName() +
                ". Replenishment triggered. Please wait for further updates.";
        notificationService.notifyDriver(vehicleId, message);
    }
}
