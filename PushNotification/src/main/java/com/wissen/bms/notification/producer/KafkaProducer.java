package com.wissen.bms.notification.producer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wissen.bms.common.model.BatteryFault;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.faultalerts}")
    private String topicName;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Produces mock data every 10 seconds
    @Scheduled(fixedRate = 10000)
    public void produceMockData() {
        String mockVehicleData = generateMockVehicleData();
        kafkaTemplate.send(topicName, mockVehicleData);
        System.out.println("Produced mock data: " + mockVehicleData);

 System.out.println("producer running");
 }

    // Generates a mock VehicleData object
    private String generateMockVehicleData() {
        ObjectMapper objectMapper = new ObjectMapper();
        BatteryFault vehicleData = new BatteryFault();
        vehicleData.setBatteryId("BAT" + UUID.randomUUID().toString().substring(0, 5));
        vehicleData.setVehicleId("VH" + UUID.randomUUID().toString().substring(0, 5));
        vehicleData.setGps("37.7749,-122.4194");
        vehicleData.setRisk("High");
        vehicleData.setLevel("Critical");
        vehicleData.setRecommendation("Service Required Immediately");
        vehicleData.setFaultReason("Battery Overheating");
        vehicleData.setTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        String vehicleDataString = null;
        try {
            vehicleDataString = objectMapper.writeValueAsString(vehicleData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return vehicleDataString;
    }
}
