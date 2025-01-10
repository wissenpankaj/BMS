package com.publisher;

import com.publisher.service.MqttPublisherService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PublisherApplication implements CommandLineRunner {

	private final MqttPublisherService mqttPublisherService;

	public PublisherApplication(MqttPublisherService mqttPublisherService) {
		this.mqttPublisherService = mqttPublisherService;
	}

	public static void main(String[] args) {
		SpringApplication.run(PublisherApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mqttPublisherService.connectAndPublish();
	}

}
