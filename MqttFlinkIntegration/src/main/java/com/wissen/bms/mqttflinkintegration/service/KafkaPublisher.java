package com.wissen.bms.mqttflinkintegration.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaPublisher {

    @Value("${kafka.topic}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        log.info("Inside @class KafkaPublisher @method send message topic : {}, message : {}", topic, message);
        kafkaTemplate.send(topic, message);
        log.info("message sent");
    }
}
