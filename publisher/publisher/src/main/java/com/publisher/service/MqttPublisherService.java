package com.publisher.service;

import com.publisher.model.TelemetryData;
import com.publisher.model.TelemetryData1;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class MqttPublisherService {

    List<TelemetryData> telemetryList = new ArrayList<>();

    private static final String MQTT_BROKER_URL = "tcp://localhost:1883";
    private static final String MQTT_TOPIC = "ev/fleet/battery/telemetry";
    private MqttClient mqttClient;

    public void connectAndPublish() throws MqttException, InterruptedException {
        mqttClient = new MqttClient(MQTT_BROKER_URL, MqttClient.generateClientId());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);

        mqttClient.connect(options);

        while (true){
               for(int i=0; i<10; i++){
                   publishData(generateTelemetryData(i).convertObjToString());
                   // publishData("telemetry data : "+i);
                   Thread.sleep(10);
               }
        }
    }

    private void publishData(String telemetryData) {
        try {
            MqttMessage message = new MqttMessage(telemetryData.getBytes());
            mqttClient.publish(MQTT_TOPIC, message);
            System.out.println("Published telemetry data: " + telemetryData);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    private TelemetryData1 generateTelemetryData() {
        TelemetryData1 data = new TelemetryData1();
        Random rand = new Random();
        int id= rand.nextInt(100);
        // Generating random values for each field
        data.setBatteryId("Battery-" + id);
        data.setVehicleId("Vehicle-" + id);
        data.setVoltage(3.0 + rand.nextDouble() * 3.0); // Random voltage between 3.0V and 6.0V
        data.setCurrent(5.0 + rand.nextDouble() * 10.0); // Random current between 5A and 15A
        data.setSoc(rand.nextDouble() * 100); // Random State of Charge (0-100%)
        data.setSoh(rand.nextDouble() * 100); // Random State of Health (0-100%)
        data.setTemperature(20.0 + rand.nextDouble() * 45.0); // Random temperature between 20°C and 65°C
        data.setEnergyThroughput(rand.nextDouble() * 5000); // Random energy throughput (0-5000 Wh)
        data.setChargingTime(rand.nextDouble() * 180); // Random charging time (0-180 minutes)
        data.setCycleCount(rand.nextInt(1500)); // Random cycle count (0-1500 cycles)
        data.setGps("Lat-" + (rand.nextDouble() * 180 - 90) + ", Long-" + (rand.nextDouble() * 360 - 180)); // Random GPS coordinates
//        data.setTime(System.currentTimeMillis()); // Current timestamp
        data.setInternalResistance(rand.nextDouble() * 10); // Random internal resistance (0-10 Ohms)
        data.setRiskLevel(rand.nextBoolean() ? "Low" : rand.nextBoolean() ? "Medium" : "High"); // Random risk level (Low, Medium, High)
        return data;
    }

    private TelemetryData generateTelemetryData(int i) {
        Random random = new Random();
//        telemetryList.clear();
//
//        for (int i = 0; i < 100; i++) {
            TelemetryData data = new TelemetryData();

            // Generating random values for the fields
            data.setBatteryId("batid"+(i+1));
            data.setVehicleId("Vehicle_" + (i + 1));
            data.setVoltage(3.0 + (random.nextDouble() * (4.5 - 3.0)));  // Random voltage between 3.0V and 4.5V
            data.setCurrent(10 + random.nextDouble() * 20);  // Random current between 10A and 30A
            data.setTemperature(10 + random.nextDouble() * 40);  // Random temperature between 10°C and 50°C
            data.setSoc(random.nextDouble() * 100);  // Random SOC between 0% and 100%
            data.setSoh(random.nextDouble() * 100);  // Random SOH between 0% and 100%
            data.setTime(System.currentTimeMillis());

            return data;
//        }
    }

//    private void generateTelemetryData() {
//        Random random = new Random();
//        telemetryList.clear();
//
//        for (int i = 0; i < 100; i++) {
//            TelemetryData data = new TelemetryData();
//
//            // Generating random values for the fields
//            data.setVehicleId("Vehicle_" + (i + 1));
//            data.setVoltage(3.0 + (random.nextDouble() * (4.5 - 3.0)));  // Random voltage between 3.0V and 4.5V
//            data.setCurrent(10 + random.nextDouble() * 20);  // Random current between 10A and 30A
//            data.setTemperature(10 + random.nextDouble() * 40);  // Random temperature between 10°C and 50°C
//            data.setDischargeRate(0 + random.nextDouble() * 10);  // Random discharge rate between 0 and 10%
//            data.setSoc(random.nextDouble() * 100);  // Random SOC between 0% and 100%
//            data.setSoh(random.nextDouble() * 100);  // Random SOH between 0% and 100%
//            data.setVoltageDeviation(random.nextDouble() * 5);  // Random voltage deviation between 0% and 5%
//            data.setChargeCycles(random.nextInt(200));  // Random charge cycles between 0 and 200
//
//            telemetryList.add(data);
//        }
//    }

}
