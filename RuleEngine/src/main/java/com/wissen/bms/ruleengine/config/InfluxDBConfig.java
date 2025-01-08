package com.wissen.bms.ruleengine.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;

@Configuration
public class InfluxDBConfig {

	@Value("${influxdb.url}")
	private String influxDbUrl;

	@Value("${influxdb.token}")
	private String influxDbToken;

	@Value("${influxdb.org}")
	private String influxDbOrg;

	@Value("${influxdb.bucket}")
	private String influxDbBucket;
	
	@Value("${influxdb.username}")
	private String influxDbUsername;

	@Value("${influxdb.password}")
	private String influxDbpassword;


	@Bean
	public InfluxDBClient influxDBClient() {
		// Create and return the InfluxDB client instance
		return InfluxDBClientFactory.create(influxDbUrl, influxDbToken.toCharArray());
	}
	
    @Bean
    public InfluxDB influxDB() {
        return InfluxDBFactory.connect(influxDbUrl, influxDbUsername, influxDbpassword);
    }


	@Bean
	public String influxDbOrg() {
		return influxDbOrg;
	}

	@Bean
	public String influxDbBucket() {
		return influxDbBucket;
	}
}