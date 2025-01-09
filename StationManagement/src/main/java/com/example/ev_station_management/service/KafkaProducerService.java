package com.example.ev_station_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String REPLENISHMENT_TOPIC = "station-replenishments";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Send a replenishment request message to the Kafka topic.
     *
     * @param message The message to send.
     */
    public void sendReplenishmentRequest(String topic, String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Message sent to Kafka topic: " + topic);
        System.out.println("Message: " + message);
    }

    /**
     * Overloaded method to use the default topic for replenishment requests.
     *
     * @param message The message to send.
     */
    public void sendReplenishmentRequest(String message) {
        sendReplenishmentRequest(REPLENISHMENT_TOPIC, message);
    }
}
