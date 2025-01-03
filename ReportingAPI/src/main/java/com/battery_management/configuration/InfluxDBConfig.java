package com.battery_management.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;

@Configuration
public class InfluxDBConfig {
	
	//Test Configuration
	/*
	private static final String INFLUXDB_URL = "http://localhost:8086";
    private static final String TOKEN = "mIU8rAEfCFl0WGPMzfY3ThhqXnEnegrScLBPlGUC4eh6s3AMimEKV0poAnBQ9QNLodTo_HBVFXm-azdPzVmFOw==";
    private static final String ORG = "Wissen";
    private static final String BUCKET = "Vehicles";
    */
	
	private static final String INFLUXDB_URL = "http://localhost:8086";
    private static final String TOKEN = "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef";
    private static final String ORG = "Wissen";
    private static final String BUCKET = "EV";

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(INFLUXDB_URL, TOKEN.toCharArray(), ORG, BUCKET);
    }
}
