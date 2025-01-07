package com.mqttflink.config;

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.key-serializer}")
    private String keySerializer;

    @Value("${kafka.value-serializer}")
    private String valueSerializer;

    @Value("${kafka.acks}")
    private String acks;

    @Value("${kafka.retries}")
    private String retries;

    @Value("${kafka.batch-size}")
    private String batchSize;

    @Value("${kafka.linger-ms}")
    private String lingerMs;

    @Value("${kafka.topic}")
    private String topic;

    @Bean
    public FlinkKafkaProducer<String> kafkaProducer() {
        // Create Kafka producer properties dynamically from the application.properties
        Properties kafkaProps = new Properties();
        kafkaProps.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        kafkaProps.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        kafkaProps.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        kafkaProps.setProperty(ProducerConfig.ACKS_CONFIG, acks);
        kafkaProps.setProperty(ProducerConfig.RETRIES_CONFIG, retries);
        kafkaProps.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        kafkaProps.setProperty(ProducerConfig.LINGER_MS_CONFIG, lingerMs);

        // Create and return the Kafka producer
        return new FlinkKafkaProducer<>(
                topic,            // Kafka topic to send data
                new SimpleStringSchema(),      // Serializer for string data
                kafkaProps                     // Kafka producer properties from the config
        );
    }
}
