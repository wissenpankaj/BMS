package com.example.ev_station_management.service;

import com.example.ev_station_management.model.FaultAlertEntity;
import com.example.ev_station_management.repository.FaultAlertRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FaultAlertConsumer {

    @Autowired
    private FaultAlertRepository faultAlertRepository;

    @Autowired
    private FaultAlertProcessor faultAlertProcessor;

    @KafkaListener(topics = "fault-alerts", groupId = "fault-alert-group")
    public void consumeFaultAlert(String message) {
        try {
            // Parse the JSON message
            JSONObject jsonObject = new JSONObject(message);

            // Parse timestamp from JSON
            String timestampStr = jsonObject.optString("timestamp");
            LocalDateTime timestamp = null;
            if (!timestampStr.isEmpty()) {
                timestamp = LocalDateTime.parse(timestampStr, DateTimeFormatter.ISO_DATE_TIME);
            }

            // Map JSON fields directly to FaultAlertEntity
            FaultAlertEntity faultAlertEntity = new FaultAlertEntity();
            faultAlertEntity.setBatteryId(jsonObject.optString("batteryId", null));
            faultAlertEntity.setVehicleId(jsonObject.optString("vehicleId", null));
            faultAlertEntity.setGps(jsonObject.optString("gps", null));
            faultAlertEntity.setRisk(jsonObject.optString("risk", null));
            faultAlertEntity.setLevel(jsonObject.optString("level", null));
            faultAlertEntity.setRecommendation(jsonObject.optString("recommendation", null));
            faultAlertEntity.setFaultReason(jsonObject.optString("faultReason", null));
            faultAlertEntity.setTimestamp(timestamp);

            // Save the FaultAlertEntity to the database
            faultAlertRepository.save(faultAlertEntity);
            System.out.println("Saved Fault Alert: " + faultAlertEntity);

            // Process the fault alert
            faultAlertProcessor.processFaultAlert(faultAlertEntity);

        } catch (Exception e) {
            System.err.println("Failed to process fault alert: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
