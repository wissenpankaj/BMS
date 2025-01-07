package com.ruleengine.EVBatteryRuleEngine.service;

import com.ruleengine.EVBatteryRuleEngine.dto.*;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.wissen.bms.common.model.TelemetryData;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.*;

@Service
public class InfluxDBService {

    private static final String MEASUREMENT = "telemetry_battery_data1"; // for telemetry data
    private static final String FAULT_ALERT_MEASUREMENT = "fault_alerts"; // for fault alerts

    private final InfluxDBClient influxDBClient;

    private String influxDbOrg;
    private String influxDbBucket;

    @Autowired
    public InfluxDBService(InfluxDBClient influxDBClient, @Value("${influxdb.org}") String influxDbOrg,
                           @Value("${influxdb.bucket}") String influxDbBucket) {
        this.influxDBClient = influxDBClient;
        this.influxDbOrg = influxDbOrg;
        this.influxDbBucket = influxDbBucket;
    }
    
    @Autowired
    public InfluxDB influxDB;
    
    // Write telemetry data to InfluxDB
    public void writeData(TelemetryData telemetryData) {
        if (telemetryData == null) {
            System.out.println("Telemetry data is null");
            return;
        }
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        Point point = Point.measurement(MEASUREMENT)
                .time(Instant.now(), WritePrecision.NS)
                .addField("battery_id", telemetryData.getBatteryId())
                .addTag("vehicle_id", telemetryData.getVehicleId())
                .addField("voltage", telemetryData.getVoltage())
                .addField("current", telemetryData.getCurrent())
                .addField("temperature", telemetryData.getTemperature())

                .addField("soc", telemetryData.getSoc())
                .addField("soh", telemetryData.getSoh())


                .addField("cycle_count", telemetryData.getCycleCount()) // New field from telemetry schema
                .addField("energy_throughput", telemetryData.getEnergyThroughput()) // New field from telemetry schema
                .addField("charging_time", telemetryData.getChargingTime()) // New field from telemetry schema
                 // New field from telemetry schema
                .addField("gps", telemetryData.getGps())
                .addField("time", telemetryData.getTime()) // Use the timestamp field from the schema
        		.addField("riskLevel", telemetryData.getRiskLevel());
        try {
            writeApi.writePoint(influxDbBucket, influxDbOrg, point);
        } catch (Exception e) {
            System.err.println("Error writing telemetry data to InfluxDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Write fault alert to InfluxDB
    public void writeFaultAlert(String batteryId, String vehicleId, String riskLevel, String gps, long time) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        
        Map<String, Object> fields= new HashMap<>();
        fields.put("risk_level", "1");
        fields.put("gps", "1");
        fields.put("time", 123);
        
        Map<String, String> tags= new HashMap<>();
        tags.put("battery_id", "1");
        tags.put("vehicle_id", "1");
        
        Point point = Point.measurement(FAULT_ALERT_MEASUREMENT+"1")
                .time(Instant.now(), WritePrecision.NS)
                .addTags(tags)
                .addFields(fields);
        /*Point point = Point.measurement(FAULT_ALERT_MEASUREMENT)
                .time(Instant.now(), WritePrecision.NS)
                .addTag("battery_id", batteryId)
                .addTag("vehicle_id", vehicleId)
                .addField("risk_level", riskLevel)
                .addField("gps", gps)
                .addField("time", time); // The time of the fault alert*/
        try {
            writeApi.writePoint(influxDbBucket, influxDbOrg, point);
            System.out.println("Fault alert written to InfluxDB");
        } catch (Exception e) {
            System.err.println("Error writing fault alert to InfluxDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Query telemetry data from InfluxDB
    public List<FluxTable> queryData(String bucket, String org , String batteryId) {
      //  String measurement = "telemetry_battery_data";
      //  String fluxQuery = String.format("from(bucket: \"%s\") |> range(start: -1h) |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")", bucket, measurement);
       String fluxQuery =  "from(bucket: \"EV\")"+
                " |> range(start: -10h)"+
                " |> filter(fn: (r) => r[\"_measurement\"] == \"telemetry_battery_data1\")"+
                " |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")"+
                " |> keep(columns: [\"_time\", \"vehicle_id\", \"battery_id\",  \"current\", \"temperature\",\"soc\",\"soh\", \"gps\", \"charging_time\", \"cycle_count\", \"current\", \"energy_throughput\"])"+
                " |> rename(columns: {_time:\"time\", battery_id:\"batteryId\", vehicle_id:\"vehicleId\", charging_time:\"chargingTime\", cycle_count:\"cycleCount\", energy_throughput:\"energyThroughput\" })"+
                " |> filter(fn: (r) =>  r.batteryId == \""+batteryId+"\"  )";
        
        QueryApi queryApi = influxDBClient.getQueryApi();
        return queryApi.query(fluxQuery, org);
    }

    // Query and process Fault Alerts from InfluxDB
    public List<FluxTable> queryFaultAlerts(String bucket, String org) { 
        String fluxQuery = "from(bucket: \""+bucket+"\") |> range(start: -1h) |> filter(fn: (r) => r[\"_measurement\"] == \"fault_alerts1\")";
       
        QueryApi queryApi = influxDBClient.getQueryApi();
        return queryApi.query(fluxQuery, org);

        // Process the FluxTable results
     /*   for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                // Handle the fault alert data, e.g., print or analyze it
                System.out.println("Fault Alert Data: " + record);
            }
        }
        */
    }
    
    
   
  
}
