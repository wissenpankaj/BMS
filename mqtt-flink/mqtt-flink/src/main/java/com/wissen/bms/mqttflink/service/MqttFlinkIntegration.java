package com.wissen.bms.mqttflink.service;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

@Service
public class MqttFlinkIntegration {
    //private static final String MQTT_BROKER_URL = "tcp://host.docker.internal:1883";
    private static final String MQTT_BROKER_URL = "tcp://localhost:1883";
    private static final String MQTT_TOPIC = "ev/fleet/battery/telemetry";

    public void process()  throws Exception {
        // Set up Flink execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Create a custom MQTT source
        DataStream<String> mqttStream = env.addSource(new MqttSource(MQTT_BROKER_URL, MQTT_TOPIC));

        // Process the MQTT stream
        //log.info("inside class MqttFlinkIntegrationDocker method cusumer started");

        // Process the MQTT stream
        mqttStream.map(value ->{
//            log.info("inside class MqttFlinkIntegrationDocker method cusumer map value : {}", value);
            System.out.println("inside class MqttFlinkIntegrationDocker method console map value : "+value);
            return "Processed: " + value;
        }).print();


        // Define a FileSink to write to the file
        // mqttStream.addSink(createFileSink());



        // Execute the Flink job
        env.execute("MQTT to Flink Integration");
    }

    // Custom Source Function for MQTT
    public static class MqttSource implements SourceFunction<String> {
        private final String brokerUrl;
        private final String topic;
        private transient MqttClient mqttClient;
        private volatile boolean running = true;

        public MqttSource(String brokerUrl, String topic) {
            this.brokerUrl = brokerUrl;
            this.topic = topic;
        }

        @Override
        public void run(SourceContext<String> ctx) throws Exception {
            mqttClient = new MqttClient(brokerUrl, MqttClient.generateClientId());
            mqttClient.connect();

            mqttClient.subscribe(topic, (topic, message) -> {
                synchronized (ctx.getCheckpointLock()) {
                    ctx.collect(new String(message.getPayload()));
                }
            });

            while (running) {
                Thread.sleep(100); // Prevent busy-waiting
            }
        }

        @Override
        public void cancel() {
            running = false;
            if (mqttClient != null && mqttClient.isConnected()) {
                try {
                    mqttClient.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
