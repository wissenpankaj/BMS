package com.mqttflink.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mqttflink.model.TelemetryData;
import com.mqttflink.rule.RuleEngine;
import com.mqttflink.rule.SimpleRule;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlinkMqttConsumerService {

    private static final String MQTT_BROKER_URL = "tcp://localhost:1883";  // MQTT Broker URL
    private static final String MQTT_TOPIC = "sensors/battery/data";        // Topic to subscribe to
    private static final String KAFKA_TOPIC = "fault_alerts";               // Kafka topic for fault alerts
//    private final KafkaProducer<String, String> kafkaProducer;

    @Autowired
    private InfluxDBService dbService;  // Assuming there's a service to write data to InfluxDB

    public void processTelemetryData() throws Exception {
        // Set up Flink streaming execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Define a custom MQTT Source (use the Paho MQTT client)
        DataStream<String> mqttStream = env.addSource(new FlinkMqttConsumerService.MqttSource(MQTT_BROKER_URL, MQTT_TOPIC, 1));

        //Example of processing the stream (map to a message with additional text)
        DataStream<String> processedStream = mqttStream.map(new MapFunction<String, String>() {
            @Override
            public String map(String value) throws Exception {
                // Convert JSON string to TelemetryData object

                System.out.println("data processed");
                return "value";//data.convertObjToString();
            }
        });
        // Print out the processed stream (you can replace this with any Flink sink)
        processedStream.print();

        // Execute the Flink job
        env.execute("MQTT Data Stream Processor");
    }

    // Convert JSON string to TelemetryData object
    public TelemetryData convertStringToObject(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Register module for handling time
            return objectMapper.readValue(json, TelemetryData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to send the fault alert to Kafka
    private void sendToKafka(String alertMessage) {
        // kafkaProducer.send(new ProducerRecord<>(KAFKA_TOPIC, alertMessage));
        System.out.println("Sent fault alert to Kafka: " + alertMessage);
    }

    // Create a fault alert message based on telemetry data and rule actions
    private String createFaultAlert(TelemetryData data, List<String> actions) {
        String riskLevel = actions.size() > 1 ? "High" : actions.size() > 0 ? "Medium" : "Low";  // Simplified risk level
        return String.format("{\"batteryId\":\"%s\",\"vehicleId\":\"%s\",\"riskLevel\":\"%s\",\"gps\":\"%s\",\"time\":\"%d\"}",
                data.getBatteryId(), data.getVehicleId(), riskLevel, data.getGps(), data.getTime());
    }

    // Custom Source to pull data from MQTT
    public static class MqttSource implements SourceFunction<String> {
        private final String broker;
        private final String topic;
        private final int qos;
        private volatile boolean isRunning = true;
        private MqttClient client;

        public MqttSource(String broker, String topic, int qos) {
            this.broker = broker;
            this.topic = topic;
            this.qos = qos;
        }

        @Override
        public void run(SourceContext<String> ctx) throws Exception {
            // Initialize MQTT Client
            client = new MqttClient(broker, MqttClient.generateClientId());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            // Connect to the broker
            client.connect(options);

            // Subscribe to the specified topic
            client.subscribe(topic, qos, (topic, message) -> {
                String payload = new String(message.getPayload());
                ctx.collect(payload);  // Collect the message for Flink processing
            });
        }

        @Override
        public void cancel() {
            isRunning = false;
            try {
                if (client != null && client.isConnected()) {
                    client.disconnect();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}
