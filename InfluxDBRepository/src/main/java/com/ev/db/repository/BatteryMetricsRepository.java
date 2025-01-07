package com.ev.db.repository;

import com.ev.db.client.InfluxDBClientManager;
import com.ev.db.model.BatteryMetrics;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.QueryApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxTable;
import com.influxdb.query.FluxRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BatteryMetricsRepository implements AutoCloseable {

    @Value("${influxdb.bucket}")
    private String BUCKET;

    @Value("${influxdb.org}")
    private String ORG;

    private final InfluxDBClient influxDBClient;
    private final WriteApiBlocking writeApiBlocking;
    private final QueryApi queryApi;

    public BatteryMetricsRepository(InfluxDBClientManager influxDBClientManager) {
        this.influxDBClient = influxDBClientManager.getInstance();
        this.writeApiBlocking = influxDBClient.getWriteApiBlocking();
        this.queryApi = influxDBClient.getQueryApi();
    }

    public void save(BatteryMetrics batteryMetrics) {
        String lineProtocol = String.format(
                "battery_metrics,vehicle_id=%s,battery_id=%s longitude=%f,latitude=%f,voltage=%f,current=%f,soc=%f," +
                        "temperature=%f,internal_resistance=%f,cycle_count=%d,energy_throughput=%f,charging_time=%f,soh=%f",
                batteryMetrics.getVehicleId(),
                batteryMetrics.getBatteryId(),
                batteryMetrics.getLongitude(),
                batteryMetrics.getLatitude(),
                batteryMetrics.getVoltage(),
                batteryMetrics.getCurrent(),
                batteryMetrics.getSoc(),
                batteryMetrics.getTemperature(),
                batteryMetrics.getInternalResistance(),
                batteryMetrics.getCycleCount(),
                batteryMetrics.getEnergyThroughput(),
                batteryMetrics.getChargingTime(),
                batteryMetrics.getSoh()
        );
        try {
            writeApiBlocking.writeRecord(BUCKET, ORG, WritePrecision.S, lineProtocol);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to save metrics: " + e.getMessage());
        }
    }

    public void saveAll(List<BatteryMetrics> metricsList) {
        if (metricsList == null || metricsList.isEmpty()) {
            System.out.println("No metrics to save.");
            return;
        }

        try {
            List<Point> points = metricsList.stream()
                    .map(metric -> {
                        // Convert BatteryMetrics to a Point object for InfluxDB
                        return Point.measurement("battery_metrics")
                                .addTag("vehicle_id", metric.getVehicleId())
                                .addTag("battery_id", metric.getBatteryId())
                                .addField("longitude", metric.getLongitude() != null ? metric.getLongitude() : 0.0)
                                .addField("latitude", metric.getLatitude() != null ? metric.getLatitude() : 0.0)
                                .addField("voltage", metric.getVoltage() != null ? metric.getVoltage() : 0.0)
                                .addField("current", metric.getCurrent() != null ? metric.getCurrent() : 0.0)
                                .addField("soc", metric.getSoc() != null ? metric.getSoc() : 0.0)
                                .addField("temperature", metric.getTemperature() != null ? metric.getTemperature() : 0.0)
                                .addField("internal_resistance", metric.getInternalResistance() != null ? metric.getInternalResistance() : 0.0)
                                .addField("cycle_count", metric.getCycleCount() != null ? metric.getCycleCount() : 0)
                                .addField("energy_throughput", metric.getEnergyThroughput() != null ? metric.getEnergyThroughput() : 0.0)
                                .addField("charging_time", metric.getChargingTime() != null ? metric.getChargingTime() : 0.0)
                                .addField("soh", metric.getSoh() != null ? metric.getSoh() : 0.0)
                                .time(Instant.now(), WritePrecision.S);
                    })
                    .toList();

            // Write all points to the database
            writeApiBlocking.writePoints(BUCKET, ORG, points);
            System.out.println("Successfully saved " + metricsList.size() + " records.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to save metrics: " + e.getMessage());
        }
    }


    public List<BatteryMetrics> findAll(OffsetDateTime startTime, OffsetDateTime endTime) {
        List<BatteryMetrics> metricsList = new ArrayList<>();
        try {
            String query = String.format(
                    "from(bucket: \"%s\") " +
                            "|> range(start: %s, stop: %s) " +
                            "|> filter(fn: (r) => r[\"_measurement\"] == \"battery_metrics\") " +
                            "|> filter(fn: (r) => r[\"_field\"] == \"charging_time\" or r[\"_field\"] == \"cycle_count\" or r[\"_field\"] == \"current\" or r[\"_field\"] == \"energy_throughput\" or r[\"_field\"] == \"voltage\" or r[\"_field\"] == \"soc\" or r[\"_field\"] == \"temperature\" or r[\"_field\"] == \"internal_resistance\" or r[\"_field\"] == \"battery_id\" or r[\"_field\"] == \"longitude\" or r[\"_field\"] == \"latitude\" ) " +
                            "|> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\") " +
                            "|> keep(columns: [\"_time\", \"vehicle_id\", \"charging_time\", \"cycle_count\", \"current\", \"energy_throughput\", \"voltage\", \"soc\", \"temperature\", \"internal_resistance\", \"battery_id\", \"longitude\", \"latitude\"])",
                    BUCKET, startTime.toString(), endTime.toString()
            );

            List<FluxTable> tables = queryApi.query(query);

            for (FluxTable table : tables) {
                for (FluxRecord record : table.getRecords()) {
                    BatteryMetrics metric = new BatteryMetrics();

                    metric.setVehicleId(getStringValue(record, "vehicle_id"));
                    metric.setBatteryId(getStringValue(record, "battery_id"));
                    metric.setLongitude(getDoubleValue(record, "longitude"));
                    metric.setLatitude(getDoubleValue(record, "latitude"));
                    metric.setVoltage(getDoubleValue(record, "voltage"));
                    metric.setCurrent(getDoubleValue(record, "current"));
                    metric.setSoc(getDoubleValue(record, "soc"));
                    metric.setTemperature(getDoubleValue(record, "temperature"));
                    metric.setInternalResistance(getDoubleValue(record, "internal_resistance"));
                    metric.setCycleCount(getIntValue(record, "cycle_count"));
                    metric.setEnergyThroughput(getDoubleValue(record, "energy_throughput"));
                    metric.setChargingTime(getDoubleValue(record, "charging_time"));
                    metricsList.add(metric);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return metricsList;
    }

    public List<BatteryMetrics> findByBatteryAndVehicleId(String batteryId, String vehicleId, OffsetDateTime startTime, OffsetDateTime endTime) {
        List<BatteryMetrics> metricsList = new ArrayList<>();
        try {
            // Build the Flux query
            String query = String.format(
                    "from(bucket: \"%s\") " +
                            "|> range(start: %s, stop: %s) " +
                            "|> filter(fn: (r) => r[\"_measurement\"] == \"battery_metrics\") " +
                            "|> filter(fn: (r) => r[\"battery_id\"] == \"%s\" and r[\"vehicle_id\"] == \"%s\") " +
                            "|> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")",
                    BUCKET, startTime.toString(), endTime.toString(), batteryId, vehicleId
            );

            // Execute the query
            List<FluxTable> tables = queryApi.query(query);

            // Process the results
            for (FluxTable table : tables) {
                for (FluxRecord record : table.getRecords()) {
                    BatteryMetrics metric = new BatteryMetrics();

                    // Populate BatteryMetrics fields
                    metric.setVehicleId(getStringValue(record, "vehicle_id"));
                    metric.setBatteryId(getStringValue(record, "battery_id"));
                    metric.setLongitude(getDoubleValue(record, "longitude"));
                    metric.setLatitude(getDoubleValue(record, "latitude"));
                    metric.setVoltage(getDoubleValue(record, "voltage"));
                    metric.setCurrent(getDoubleValue(record, "current"));
                    metric.setSoc(getDoubleValue(record, "soc"));
                    metric.setTemperature(getDoubleValue(record, "temperature"));
                    metric.setInternalResistance(getDoubleValue(record, "internal_resistance"));
                    metric.setCycleCount(getIntValue(record, "cycle_count"));
                    metric.setEnergyThroughput(getDoubleValue(record, "energy_throughput"));
                    metric.setChargingTime(getDoubleValue(record, "charging_time"));
                    metric.setSoh(getDoubleValue(record, "soh"));

                    metricsList.add(metric);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return metricsList;
    }

    public void deleteAll() {
        // Define the time range
        OffsetDateTime start = OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC); // Start from epoch
        OffsetDateTime stop = OffsetDateTime.now(ZoneOffset.UTC); // Current time in UTC

        // Define the predicate for deletion (e.g., delete all records for "battery_metrics")
        String predicate = "_measurement=\"battery_metrics\"";

        // Perform deletion
        try {
            influxDBClient.getDeleteApi().delete(start, stop, predicate, BUCKET, ORG);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to delete metrics table: " + e.getMessage());
        }
    }

    // Helper method to safely get String values from FluxRecord
    private String getStringValue(FluxRecord record, String key) {
        Object value = record.getValueByKey(key);
        return (value != null) ? value.toString() : "";  // Default to "unknown" if value is null
    }

    // Helper method to safely get Double values from FluxRecord
    private Double getDoubleValue(FluxRecord record, String key) {
        Object value = record.getValueByKey(key);
        return (value != null) ? Double.parseDouble(value.toString()) : 0.0;  // Default to 0.0 if value is null
    }

    // Helper method to safely get Integer values from FluxRecord
    private Integer getIntValue(FluxRecord record, String key) {
        Object value = record.getValueByKey(key);
        return (value != null) ? ((Number) value).intValue() : 0;  // Default to 0 if value is null
    }

    @Override
    public void close() {
        if (influxDBClient != null) {
            influxDBClient.close();
        }
    }
}
