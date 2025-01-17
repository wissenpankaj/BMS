package com.wissen.bms.db.client;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InfluxDBClientManager {

    private static InfluxDBClient influxDBClientInstance;

    @Value("${influxdb.url}")
    private String url;

    @Value("${influxdb.token}")
    private String token;

    @Value("${influxdb.org}")
    private String org;

    @Value("${influxdb.bucket}")
    private String bucket;

    // Private constructor to prevent instantiation
    private InfluxDBClientManager() {}

    // Lazy loading of the InfluxDB client
    public InfluxDBClient getInstance() {
        if (influxDBClientInstance == null) {
            influxDBClientInstance = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
        }
        return influxDBClientInstance;
    }

    // Close the client connection
    @PreDestroy
    public static synchronized void close() {
        if (influxDBClientInstance != null) {
            influxDBClientInstance.close();
            influxDBClientInstance = null;
            System.out.println("InfluxDB client closed.");
        }
    }
}

