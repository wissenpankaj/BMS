package com.example.ev_station_management.service;

import com.example.ev_station_management.dto.StationInfoDto;
import com.example.ev_station_management.model.Station;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationNotificationService {

    private static final String TOPIC = "nearstation";  // The topic name

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void notifyDriver(String vehicleId, Station station) {
        // Create StationInfoDto from Station
        StationInfoDto stationInfoDto = new StationInfoDto(
                station.getName(),
                station.getAvailableStock(),
                station.getLatitude(),
                station.getLongitude(),
                vehicleId
        );

        try {
            // Convert StationInfoDto to JSON string


            // Send message to Kafka topic
            kafkaTemplate.send(TOPIC, stationInfoDto.toString());
            System.out.println("Message sent to Kafka: " + stationInfoDto.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}