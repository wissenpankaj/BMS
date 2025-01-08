package com.wissen.bms.mqttflink.service;

import com.wissen.bms.mqttflink.model.TelemetryData;
import com.wissen.bms.mqttflink.rule.RuleModel;
import com.wissen.bms.mqttflink.rule.SimpleRule;
import com.wissen.bms.mqttflink.service.InfluxDBSink;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FlinkMqttIntegrationService {

    @Value("${mqtt.broker.url}")
    private String mqttUrl;

    @Value("${mqtt.topic}")
    private String mqttTopic;

    @Autowired
    private RuleModel ruleModel;

    @Autowired
    private FlinkKafkaProducer<String> kafkaProducer;

    public void process() throws Exception {
        // Set up Flink execution environment
        log.info("Inside @class FlinkMqttIntegrationService @method process");

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Create a custom MQTT source
        DataStream<String> mqttStream = env.addSource(new FlinkMqttIntegrationService.MqttSource(mqttUrl, mqttTopic));

        // Real-time processing for immediate data sink
        mqttStream
                .map(TelemetryData::convertStringToObj)
                .addSink(new InfluxDBSink());

        // 2. Real-time telemetry data processing for faulty batteries
        mqttStream
                .map(TelemetryData::convertStringToObj)
                .filter(new FaultDetectionFilter())
                .map(data -> {
                    String errorMessage = "Fault detected for batteryId: " + data.getBatteryId() +
                            ", Temperature: " + data.getTemperature() +
                            ", Voltage: " + data.getVoltage();
                    return errorMessage;
                })
                .addSink(kafkaProducer);

        mqttStream
                .map(TelemetryData::convertStringToObj)  // Convert String to TelemetryData
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<TelemetryData>forBoundedOutOfOrderness(Duration.ofSeconds(5)) // Handle late data with 5 second delay
                                .withTimestampAssigner((event, timestamp) -> event.getTime()) // Use event timestamp for windowing
                )
                .keyBy(TelemetryData::getBatteryId) // Group data by Battery ID
                .timeWindow(Time.seconds(10)) // Create a 10-second tumbling window
                .apply(new BatchProcessor()) // Process data within each 10-second window
                .print();

        log.info("Inside @class FlinkMqttIntegrationService @method process on execute");
        // Execute the Flink job
        env.execute("MQTT to Flink Integration");
    }

    // Custom Source Function for MQTT
    public static class MqttSource implements SourceFunction<String> {
        private final String brokerUrl;
        private final String topic;
        private transient MqttClient mqttClient;
        private volatile boolean running = true;

        public MqttSource(String brokerUrl, String topic) {
            this.brokerUrl = brokerUrl;
            this.topic = topic;
        }

        @Override
        public void run(SourceContext<String> ctx) throws Exception {
            log.info("Inside @class FlinkMqttIntegrationService MqttSource @method run brokerUrl : {}, topic : {}", brokerUrl, topic);
            mqttClient = new MqttClient(brokerUrl, MqttClient.generateClientId());
            mqttClient.connect();

            mqttClient.subscribe(topic, (topic, message) -> {
                synchronized (ctx.getCheckpointLock()) {
                    ctx.collect(new String(message.getPayload()));
                }
            });

            while (running) {
                Thread.sleep(100);
            }
        }

        @Override
        public void cancel() {
            running = false;
            if (mqttClient != null && mqttClient.isConnected()) {
                try {
                    mqttClient.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class BatchProcessor
            implements WindowFunction<TelemetryData, String, String, TimeWindow> {

        @Override
        public void apply(String key, TimeWindow timeWindow, Iterable<TelemetryData> input, Collector<String> out) throws Exception {
            List<TelemetryData> batch = new ArrayList<>();
            // Collect all telemetry data into a batch
            log.info("Inside @class FlinkMqttIntegrationService BatchProcessor @method apply key : {}, timeWindow : {}, input: {}", key, timeWindow, input);
            input.forEach(batch::add);

            RuleModel ruleModel = new SimpleRule();
            // Pass the batch to RuleModel for evaluation
            List<String> responses = ruleModel.evaluateBatch(batch);

            for (int i = 0; i < batch.size(); i++) {
                TelemetryData data = batch.get(i);
                String response = responses.get(i);
                System.out.println(response);

                if ("Failure".equalsIgnoreCase(response)) {
                    //mySQLService.writeFailure(data, response);
                    //push to kafka
                }
                // influxdbService.writeData(data);
            }

            out.collect("Batch processed successfully for key: " + key);
        }
    }

    public static class FaultDetectionFilter implements FilterFunction<TelemetryData> {
        @Autowired
        private RuleModel ruleModel;

        @Override
        public boolean filter(TelemetryData data) {
            String status = ruleModel.evaluateData(data);
            return "HIGH".equals(status);
        }
    }
}
