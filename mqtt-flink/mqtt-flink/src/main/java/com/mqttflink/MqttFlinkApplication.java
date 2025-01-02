package com.mqttflink;

import com.mqttflink.service.FlinkMqttConsumerService;
import com.mqttflink.service.FlinkMqttConsumerService2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MqttFlinkApplication implements CommandLineRunner {

    private final FlinkMqttConsumerService flinkMqttConsumerService; // Service for Flink

    public MqttFlinkApplication(FlinkMqttConsumerService flinkMqttConsumerService) {
        this.flinkMqttConsumerService = flinkMqttConsumerService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MqttFlinkApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Start the Flink stream processing
        flinkMqttConsumerService.processTelemetryData(); // Process telemetry data through Flink and Rule Engine
    }
}
