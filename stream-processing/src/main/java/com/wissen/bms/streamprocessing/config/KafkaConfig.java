package com.wissen.bms.streamprocessing.config;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaConfig {

    public static ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-broker-1:39092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }


    public static KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    public static FlinkKafkaProducer<String> kafkaProducer() {
        // Create Kafka producer properties dynamically from the application.properties
        Properties kafkaProps = new Properties();
        kafkaProps.setProperty("bootstrap.servers", "kafka-broker-1:9092");
        kafkaProps.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        kafkaProps.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        kafkaProps.setProperty(ProducerConfig.RETRIES_CONFIG, "3");
        kafkaProps.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        kafkaProps.setProperty(ProducerConfig.LINGER_MS_CONFIG, "5");
        kafkaProps.setProperty("group.id", "flink-consumer-group");
        kafkaProps.setProperty("request.timeout.ms", "120000");
        kafkaProps.setProperty("metadata.max.age.ms", "5000");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");


        // Create and return the Kafka producer
        return new FlinkKafkaProducer<>(
                "faultalert",            // Kafka topic to send data
                new SimpleStringSchema(),      // Serializer for string data
                kafkaProps                     // Kafka producer properties from the config
        );
    }
}
