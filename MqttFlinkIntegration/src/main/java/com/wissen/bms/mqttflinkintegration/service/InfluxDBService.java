package com.wissen.bms.mqttflinkintegration.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.wissen.bms.common.model.TelemetryData;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class InfluxDBService {

    private static final String MEASUREMENT = "telemetry_data";
    private InfluxDBClient influxDBClient;
    private String influxDbOrg;
    private String influxDbBucket;

    // Constructor for use in Flink Sink
    public InfluxDBService(InfluxDBClient influxDBClient, String influxDbOrg, String influxDbBucket) {
        this.influxDBClient = influxDBClient;
        this.influxDbOrg = influxDbOrg;
        this.influxDbBucket = influxDbBucket;
    }

    // Write data to InfluxDB
    public void writeData(TelemetryData value) {
        log.trace("Inside @class InfluxDBService @method writeData telemetry data : {}", value);
        if (value == null) {
            log.info("Telemetry data is null");
            return;
        }

        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        Point point = Point.measurement(MEASUREMENT)
                .time(Instant.now(), WritePrecision.NS)
                .addTag("batteryId", value.getBatteryId())    // Tag: batteryId
                .addTag("vehicleId", value.getVehicleId())    // Tag: vehicleId
                .addField("voltage", value.getVoltage())      // Field: voltage
                .addField("current", value.getCurrent())      // Field: current
                .addField("soc", value.getSoc())              // Field: state of charge
                .addField("soh", value.getSoh())              // Field: state of health
                .addField("temperature", value.getTemperature()) // Field: temperature
                .addField("energyThroughput", value.getEnergyThroughput()) // Field: energy throughput
                .addField("chargingTime", value.getChargingTime())         // Field: charging time
                .addField("cycleCount", value.getCycleCount())             // Field: cycle count
                .addField("gps", value.getGps());

        try {
            writeApi.writePoint(influxDbBucket, influxDbOrg, point);
        } catch (Exception e) {
            System.err.println("Error writing to InfluxDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public InfluxDBClient getInfluxDBClient() {
        return influxDBClient;
    }
}