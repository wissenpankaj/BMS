package com.wissen.bms.mqttflinkintegration.service;

import lombok.extern.slf4j.Slf4j;
import com.wissen.bms.ruleengine.rules.RuleContext;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

@Slf4j
class FaultSink implements SinkFunction<RuleContext> {

    private final FlinkKafkaProducer<String> kafkaProducer;

    public FaultSink(FlinkKafkaProducer<String> kafkaProducer) {
        // Initialize Kafka producer with the topic and configuration
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void invoke(RuleContext value, Context context) {
        log.info("Processing RuleContext: {}", value);

        try {

            // Send the message to Kafka
            kafkaProducer.invoke("kafkaMessage", context);
            log.info("Message sent to Kafka: ");

        } catch (Exception e) {
            log.error("Error sending data to Kafka", e);
            throw new RuntimeException(e);
        }
    }

    private static Properties createKafkaProperties(String bootstrapServers) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", bootstrapServers);
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }
}