package com.wissen.bms.streamprocessing.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;


public class InfluxDBConfig {

//    @Value("${influxdb.url}")
//    private String influxDbUrl;
//
//    @Value("${influxdb.token}")
//    private String influxDbToken;
//
//    @Value("${influxdb.org}")
//    private String influxDbOrg;
//
//    @Value("${influxdb.bucket}")
//    private String influxDbBucket;
//
//    @Bean
//    public InfluxDBClient influxDBClient() {
//        // Create and return the InfluxDB client instance
//        return InfluxDBClientFactory.create(influxDbUrl, influxDbToken.toCharArray());
//    }
//
//    @Bean
//    public String influxDbOrg() {
//        return influxDbOrg;
//    }
//
//    @Bean
//    public String influxDbBucket() {
//        return influxDbBucket;
//    }
}