package com.kafkaflink.service;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Properties;

public class FlinkKafkaSourceExample {
    public void process() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Properties for Kafka Consumer
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "flink-consumer-group");



        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setTopics("test-flink")
                .setGroupId("flink-consumer-group")
                .setProperties(properties)
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new SimpleStringSchema()) // How to deserialize Kafka message
                .build();

        // Create a DataStream by consuming data from Kafka
        DataStream<String> stream = env.fromSource(kafkaSource, org.apache.flink.api.common.eventtime.WatermarkStrategy.noWatermarks(), "Kafka Source");

        // Process the stream (you can add any processing logic here)
        stream.map(value -> "Processed: " + value).print();

        // Execute the Flink job
        env.execute("Flink Kafka Consumer Job");
    }
}
