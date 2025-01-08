package com.wissen.bms.mqttflink;

import com.wissen.bms.mqttflink.service.FlinkMqttIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MqttFlinkApplication implements CommandLineRunner {

    private final FlinkMqttIntegrationService flinkMqttIntegrationService;

    @Autowired
    public MqttFlinkApplication(FlinkMqttIntegrationService flinkMqttIntegrationService) {
        this.flinkMqttIntegrationService = flinkMqttIntegrationService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MqttFlinkApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        flinkMqttIntegrationService.process();
    }
}
