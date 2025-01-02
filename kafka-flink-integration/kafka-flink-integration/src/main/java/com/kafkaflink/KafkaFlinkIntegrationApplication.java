package com.kafkaflink;

import com.kafkaflink.service.FlinkKafkaSourceExample;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaFlinkIntegrationApplication  implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(KafkaFlinkIntegrationApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		FlinkKafkaSourceExample source = new FlinkKafkaSourceExample();
		// Start the Flink stream processing
		source.process(); // Process telemetry data through Flink and Rule Engine
	}
}
