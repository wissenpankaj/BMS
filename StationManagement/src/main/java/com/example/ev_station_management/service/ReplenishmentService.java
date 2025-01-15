package com.example.ev_station_management.service;

import com.example.ev_station_management.model.Replenishment;
import com.example.ev_station_management.model.Station;
import com.example.ev_station_management.repository.ReplenishmentRepository;
import com.example.ev_station_management.dto.BatteriesReceived;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class ReplenishmentService {

    @Autowired
    private ReplenishmentRepository replenishmentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BatteriesStockService batteriesStockService; // Dependency Injection for BatteriesStockService

    public void triggerReplenishment(Station station) {
        int requestedStock = (int) Math.ceil(2.0 * station.getMinStockLevel());
        System.out.println("Replenishment requested for Station: " + station.getName() +
                ". Requested Stock: " + requestedStock);

        // Create and save a replenishment request
        Replenishment replenishment = new Replenishment();
        replenishment.setStation(station);
        replenishment.setRequestedStock(requestedStock);
        replenishmentRepository.save(replenishment);

        // Call the external service instead of Kafka and pass the response to BatteriesStockService
        BatteriesReceived response = sendReplenishmentToExternalService(replenishment);

        // Now, save the response data to BatteriesStock using BatteriesStockService
        if (response != null) {
            batteriesStockService.saveBatteriesStock(response);
        }
    }

    private BatteriesReceived sendReplenishmentToExternalService(Replenishment replenishment) {
        // Define the URL for the external service
        String externalServiceUrl = "";

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
            return response.getBody(); // Return the body of the response
        } else {
            System.err.println("Failed to send replenishment request to external service. Status: " + response.getStatusCode());
            return null;
        }
    }
}
