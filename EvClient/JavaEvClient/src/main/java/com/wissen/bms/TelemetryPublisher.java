package com.wissen.bms;

import com.wissen.bms.common.model.TelemetryData;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public class TelemetryPublisher {

    private static final String BROKER_URL = "tcp://localhost:1883"; // MQTT broker URL
//    private static final String BROKER_URL = "tcp://mqtt-broker:1883";
    private static final String CLIENT_ID = "TelemetryPublisher";
    private static final String TOPIC = "ev/fleet/battery/telemetry";

    public static void main(String[] args) {
        try {
            // Create an MQTT client
            try (MqttClient client = new MqttClient(BROKER_URL, CLIENT_ID)) {

                // Connect to the MQTT broker
                client.connect();
                System.out.println("Connected to MQTT broker: " + BROKER_URL);

                // Create a Gson instance for converting objects to JSON
                Gson gson = new Gson();

                // Use a Timer to send telemetry data every second
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            // Generate telemetry data
                            TelemetryData telemetryData = generateTelemetryData();

                            // Convert the object to JSON
                            String jsonData = gson.toJson(telemetryData);

                            // Publish the JSON data to the MQTT topic
                            MqttMessage message = new MqttMessage(jsonData.getBytes());
                            message.setQos(1); // Set QoS to 1
                            client.publish(TOPIC, message);

                            System.out.println("Published: " + jsonData);

                        } catch (MqttException e) {
                            System.err.println("Error publishing telemetry data: " + e.getMessage());
                           // e.printStackTrace();
                        }
                    }
                }, 0, 1000); // Publish every 1 second
            }

        } catch (MqttException e) {
            System.err.println("Error connecting to MQTT broker: " + e.getMessage());
           // e.printStackTrace();
        }
    }

    // Generate random telemetry data

    private static TelemetryData generateTelemetryData() {

        List<String> vehicleIds = Arrays.asList("user123","user456","user789");
        Random random = new Random();
        TelemetryData data = new TelemetryData();
        int index = random.nextInt(2);

        data.setBatteryId("BATTERY-" + index);
        data.setVehicleId(vehicleIds.get(index));
        data.setVoltage(3.0 + random.nextDouble() * 5); // Voltage (3.0V - 3.5V)
        data.setCurrent(random.nextDouble() * 100); // Current (0 - 100A)
        data.setSoc(random.nextDouble() * 100); // SOC (0-100%)
        data.setSoh(random.nextDouble() * 100); // SOH (0-100%)
        data.setTemperature(20.0 + random.nextDouble() * 10); // Temperature (20°C - 30°C)
        data.setEnergyThroughput(random.nextDouble() * 500); // Energy throughput (0-500Wh)
        data.setChargingTime(random.nextDouble() * 120); // Charging time (0-120 minutes)
        data.setCycleCount(random.nextInt(500)); // Charge cycles
        data.setGps("Lat: " + (random.nextDouble() * 180 - 90) + ", Lon: " + (random.nextDouble() * 360 - 180));
//        data.setTime(LocalDateTime.now().toString()); // Current timestamp
        data.setTime(Instant.now().toString());
        data.setTimeStamp(System.currentTimeMillis());

        return data;
    }
}
