package com.wissen.bms.notification.producer;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.wissen.bms.notification.model.VehicleData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, VehicleData> kafkaTemplate;

    @Value("${kafka.topic.faultalerts}")
    private String topicName;

    public KafkaProducer(KafkaTemplate<String, VehicleData> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Produces mock data every 10 seconds
    @Scheduled(fixedRate = 10000)
    public void produceMockData() {
        VehicleData mockVehicleData = generateMockVehicleData();
        kafkaTemplate.send(topicName, mockVehicleData);
        System.out.println("Produced mock data: " + mockVehicleData);
        
 System.out.println("producer running");
 }

    // Generates a mock VehicleData object
    private VehicleData generateMockVehicleData() {
        VehicleData vehicleData = new VehicleData();
        vehicleData.setBatteryId("BAT" + UUID.randomUUID().toString().substring(0, 5));
        vehicleData.setVehicleId("VH" + UUID.randomUUID().toString().substring(0, 5));
        vehicleData.setGps("37.7749,-122.4194");
        vehicleData.setRisk("High");
        vehicleData.setLevel("Critical");
        vehicleData.setRecommendation("Service Required Immediately");
        vehicleData.setFaultReason("Battery Overheating");
        vehicleData.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        return vehicleData;
    }
}
