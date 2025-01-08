//package com.wissen.bms.mqttflinkintegration.service;
//
//import com.influxdb.client.InfluxDBClient;
//import com.influxdb.client.InfluxDBClientFactory;
//import com.influxdb.client.WriteApi;
//import org.apache.flink.api.common.serialization.SimpleStringSchema;
//import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.ProducerFactory;
//
//import java.awt.*;
//import java.lang.module.Configuration;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
//public class KafkaAndInfluxDBSink extends RichSinkFunction<String> {
//    private FlinkKafkaProducer<String> kafkaProducer;
//    private transient WriteApi writeApi;
//
////    @Override
////    public void open(Configuration parameters) throws Exception {
////        super.open(parameters);
////
////        // Initialize Kafka producer
////        Properties kafkaProperties = new Properties();
////        kafkaProperties.setProperty("bootstrap.servers", "localhost:9092");
////        kafkaProducer = new FlinkKafkaProducer<>(
////            "kafka-topic",
////            new SimpleStringSchema(),
////            kafkaProperties
////        );
//
//        // Initialize InfluxDB client
////        InfluxDBClient influxClient = InfluxDBClientFactory.create(
////            "http://localhost:8086",
////            "token".toCharArray()
////        );
////        writeApi = influxClient.getWriteApi();
////    }
//
//    @Override
//    public void invoke(String value, Context context) throws Exception {
//
//        Properties kafkaProps = new Properties();
//        kafkaProps.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        kafkaProps.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
//        kafkaProps.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
//        kafkaProps.setProperty(ProducerConfig.ACKS_CONFIG, acks);
//        kafkaProps.setProperty(ProducerConfig.RETRIES_CONFIG, retries);
//        kafkaProps.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
//        kafkaProps.setProperty(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
//        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
//
//
//        // Create and return the Kafka producer
//        kafkaProducer = new FlinkKafkaProducer<>(
//                "",            // Kafka topic to send data
//                new SimpleStringSchema(),      // Serializer for string data
//                kafkaProps                     // Kafka producer properties from the config
//        );
//        // Write to Kafka
//        kafkaProducer.invoke(value, context);
//
//        // Write to InfluxDB
////        Point point = Point.measurement("measurement-name")
////            .addField("field1", value.getField1())
////            .addField("field2", value.getField2())
////            .time(Instant.now(), WritePrecision.NS);
////        writeApi.writePoint("bucket-name", "org-name", point);
//    }
//
//    @Override
//    public void close() throws Exception {
//        // Close Kafka producer
//        if (kafkaProducer != null) {
//            kafkaProducer.close();
//        }
//
//        // Close InfluxDB client
//        if (writeApi != null) {
//            writeApi.close();
//        }
//
//        super.close();
//    }
//}
