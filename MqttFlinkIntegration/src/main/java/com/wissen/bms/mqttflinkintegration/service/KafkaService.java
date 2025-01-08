package com.wissen.bms.mqttflinkintegration.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class KafkaService {
    @Autowired
    private KafkaPublisher kafkaPublisher;

    public static KafkaService service = new KafkaService();

    private KafkaService(){}

    public static KafkaService getInstance(){
        return service;
    }
    public void sendMessage(String message) {
        log.info("inside class KafkaService");
        kafkaPublisher.sendMessage(message);
    }
}
