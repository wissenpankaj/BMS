package com.example.ev_station_management.service;

import com.example.ev_station_management.dto.FaultyBattery;
import com.example.ev_station_management.model.BatterySwapLog;
import com.example.ev_station_management.model.Replenishment;
import com.example.ev_station_management.model.Station;
import com.example.ev_station_management.repository.BatterySwapLogRepository;
import com.example.ev_station_management.repository.ReplenishmentRepository;
import com.example.ev_station_management.dto.BatteriesReceived;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReplenishmentService {

    @Autowired
    private ReplenishmentRepository replenishmentRepository;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BatterySwapLogRepository batterySwapLogRepository;


    public void createReplenishmentRequest(Long stationId) {
        // Create a new Replenishment object
        Replenishment replenishment = new Replenishment();

        // Create a Station object and set the stationId
        Station station = new Station();
        station.setStationId(stationId);
        replenishment.setStation(station);

        int requestedStock = 100;

        // Set battery type and quantity
        replenishment.setBatteryType("A-1");
        replenishment.setQuantity(requestedStock);
        replenishmentRepository.save(replenishment);

        // Fetch FaultyBattery data by stationId using BatterySwapLog
        List<BatterySwapLog> swapLogs = batterySwapLogRepository.findByStationId(stationId);
        List<FaultyBattery> faultyBatteries = new ArrayList<>();

        for (BatterySwapLog log : swapLogs) {
            FaultyBattery faultyBattery = new FaultyBattery();
            faultyBattery.setBatteryId(String.valueOf(log.getFaultBatteryId()));
            faultyBattery.setSerialNumber(log.getSwappedBatteryId());
            // Assuming battery type is derived from the battery ID in this example
            faultyBattery.setType("A-1");
            faultyBatteries.add(faultyBattery);
        }

        // Set the list of FaultyBattery objects in the Replenishment object
        replenishment.setFaultyBatteries(faultyBatteries);

        sendReplenishmentToExternalService(replenishment);
    }


    private BatteriesReceived sendReplenishmentToExternalService(Replenishment replenishment) {
        // Define the URL for the external service
        String externalServiceUrl = "";

        System.out.println("-*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*-");
        System.out.println("Replenishment API structure:");

        try {
            // Convert the replenishment object to a JSON string for clear structure
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Optional: Use ISO format
            String replenishmentJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(replenishment);
            System.out.println(replenishmentJson);
        } catch (Exception e) {
            System.err.println("Error converting Replenishment object to JSON: " + e.getMessage());
        }

        System.out.println("-*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*-");


        // Set the headers (optional, depending on the external service requirements)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Prepare the HTTP entity (body + headers)
        HttpEntity<Replenishment> requestEntity = new HttpEntity<>(replenishment, headers);

        // Send the request to the external service
        ResponseEntity<BatteriesReceived> response = restTemplate.exchange(
                externalServiceUrl,
                HttpMethod.POST,
                requestEntity,
                BatteriesReceived.class); // Response as BatteriesReceived DTO

        // Check if the response is successful
        if (response.getStatusCode().is2xxSuccessful()) {
//            return response.getBody(); // Return the body of the response
            System.err.println("Successfully send replenishment request to external service. Status: " + response.getStatusCode());
            return null;
        } else {
            System.err.println("Failed to send replenishment request to external service. Status: " + response.getStatusCode());
            return null;
        }
    }
}
