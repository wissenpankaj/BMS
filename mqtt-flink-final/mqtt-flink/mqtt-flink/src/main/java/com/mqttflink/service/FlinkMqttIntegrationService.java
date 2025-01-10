package com.mqttflink.service;

import com.mqttflink.model.TelemetryData;
import com.ruleengine.EVBatteryRuleEngine.rules.RuleContext;
import com.ruleengine.EVBatteryRuleEngine.service.TeleRuleEngineService;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FlinkMqttIntegrationService {

    //@Autowired
    TeleRuleEngineService teleRuleEngineService = new TeleRuleEngineService();


    @Value("${mqtt.broker.url}")
    private String mqttUrl;

    @Value("${mqtt.topic}")
    private String mqttTopic;

    public void process() throws Exception {

        // Set up Flink execution environment
        log.info("Inside @class FlinkMqttIntegrationService @method process");

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Create a custom MQTT source
        DataStream<String> telemetryStream = env.addSource(new FlinkMqttIntegrationService.MqttSource(mqttUrl, mqttTopic));

        // Real-time processing for immediate data sink
        telemetryStream
                .map(TelemetryData::convertStringToObj)
                .addSink(new InfluxDBSink());

//        DataStream<RuleContext> realTimeFaults = telemetryStream
//                .map(TelemetryData::convertStringToObj)
//                .keyBy(TelemetryData::getBatteryId)
//                .process(new CriticalThresholdProcessFunction());
//
//        DataStream<RuleContext> trendFaults = telemetryStream
//                .map(TelemetryData::convertStringToObj)  // Convert String to TelemetryData
//                .assignTimestampsAndWatermarks(
//                        WatermarkStrategy.<TelemetryData>forBoundedOutOfOrderness(Duration.ofSeconds(5)) // Handle late data with 5 second delay
//                                .withTimestampAssigner((event, timestamp) -> event.getTime()) // Use event timestamp for windowing
//                )
//                .keyBy(TelemetryData::getBatteryId) // Group data by Battery ID
//                .timeWindow(Time.seconds(10)) // Create a 10-second tumbling window
//                .apply(new BatchProcessor()); // Process data within each 10-second window

//        DataStream<RuleContext> combinedFaults = realTimeFaults.union(trendFaults);

//        combinedFaults.addSink(new FaultSink());
        log.info("Inside @class FlinkMqttIntegrationService @method process on execute");
        // Execute the Flink job
        env.execute("MQTT to Flink Integration");

        GradualDegradationProcessor.detectDailyGradualFaults();

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
            implements WindowFunction<TelemetryData, RuleContext, String, TimeWindow> {

        @Override
        public void apply(String key, TimeWindow timeWindow, Iterable<TelemetryData> input, Collector<RuleContext> out) throws Exception {
            List<TelemetryData> batch = new ArrayList<>();
            // Collect all telemetry data into a batch
            log.info("Inside @class FlinkMqttIntegrationService BatchProcessor @method apply key : {}, timeWindow : {}, input: {}", key, timeWindow, input);
            input.forEach(batch::add);

            TeleRuleEngineService ruleEngineService = new TeleRuleEngineService();

//            ruleEngineService.evaluateRisk(batch);

            out.collect(new RuleContext());
        }
    }
    public static class CriticalThresholdProcessFunction extends org.apache.flink.streaming.api.functions.KeyedProcessFunction<String, TelemetryData, RuleContext> {
        private ValueState<TelemetryData> lastTelemetryState;

        @Override
        public void open(org.apache.flink.configuration.Configuration parameters) throws Exception {
            ValueStateDescriptor<TelemetryData> descriptor = new ValueStateDescriptor<>(
                    "lastTelemetryState",
                    TelemetryData.class
            );
            lastTelemetryState = getRuntimeContext().getState(descriptor);
        }

        @Override
//        public void processElement(BatteryTelemetry telemetry, Context ctx, Collector<FaultEvent> out) throws Exception {
        public void processElement(TelemetryData telemetryData, KeyedProcessFunction<String, TelemetryData, RuleContext>.Context context, Collector<RuleContext> out) throws Exception {
            // Check for critical threshold breaches
            /*if (telemetryData.getVoltage() < 2.5) {
                out.collect(new RuleContext());
            } else if (telemetry.getVoltage() > 4.3) {
                out.collect(new FaultEvent(telemetry.getBatteryId(), "High Voltage Detected"));
            }

            if (telemetry.getStateOfCharge() < 20) {
                out.collect(new FaultEvent(telemetry.getBatteryId(), "Low State of Charge Detected"));
            } else if (telemetry.getStateOfCharge() > 95) {
                out.collect(new FaultEvent(telemetry.getBatteryId(), "High State of Charge Detected"));
            }

            if (telemetry.getTemperature() < 0) {
                out.collect(new FaultEvent(telemetry.getBatteryId(), "Low Temperature Detected"));
            } else if (telemetry.getTemperature() > 45) {
                out.collect(new FaultEvent(telemetry.getBatteryId(), "High Temperature Detected"));
            }*/
            TeleRuleEngineService ruleEngineService = new TeleRuleEngineService();
//            ruleEngineService.processTelemetryData(telemetryData);

            RuleContext ruleContext = new RuleContext();
            ruleContext.setRiskLevel("High");
            out.collect(ruleContext);

            TelemetryData lastTelemetry = lastTelemetryState.value();

//            RuleContext ruleContext = ruleEngineService.processTelemetryData(telemetryData, lastTelemetry);
            out.collect(ruleContext);

           /* if (lastTelemetry != null) {
                double voltageDelta = Math.abs(telemetry.getVoltage() - lastTelemetry.getVoltage());
                double temperatureDelta = Math.abs(telemetry.getTemperature() - lastTelemetry.getTemperature());
                double dischargeRateDelta = Math.abs(telemetry.getDischargeRate() - lastTelemetry.getDischargeRate());

                if (voltageDelta > 0.2 || temperatureDelta > 10 || dischargeRateDelta > 0.5) {
                    out.collect(new FaultEvent(telemetry.getBatteryId(), "Sudden Change Detected"));
                }
            }*/

            // Update state
            lastTelemetryState.update(telemetryData);
        }
    }

    public static class GradualDegradationProcessor {

        public static void detectDailyGradualFaults() {
            System.out.println("Loading telemetry data from InfluxDB...");

            // Fetch data for the past day from InfluxDB
            List<TelemetryData> telemetryData = new ArrayList<>();//InfluxDBClient.fetchTelemetryData(Instant.now().minusSeconds(86400), Instant.now());

            System.out.println("Processing gradual degradation rules...");
            for (String batteryId : getUniqueBatteryIds(telemetryData)) {
                List<TelemetryData> batteryTelemetry = filterTelemetryByBatteryId(telemetryData, batteryId);

                double sohSum = 0;
                int cycleCount = 0;
                double totalInternalResistance = 0;
                int elementCount = batteryTelemetry.size();

                for (TelemetryData telemetry : batteryTelemetry) {
                    sohSum += telemetry.getSoh();
                    cycleCount = telemetry.getCycleCount();
//                    totalInternalResistance += telemetry.getInternalResistance();
                }

                double averageSoH = sohSum / elementCount;
//                double avgInternalResistance = totalInternalResistance / elementCount;

                if (averageSoH < 70) {
//                    pushToKafka
                }

                if (cycleCount > 900) { // Example threshold for rated cycle count
//                    pushToKafka
                }

//                if (avgInternalResistance > 1.5) { // Example threshold for high internal resistance
//                    pushToKafka
//                }
            }
        }

        private static Set<String> getUniqueBatteryIds(List<TelemetryData> telemetryData) {
            return telemetryData.stream().map(TelemetryData::getBatteryId).collect(Collectors.toSet());
        }

        private static List<TelemetryData> filterTelemetryByBatteryId(List<TelemetryData> telemetryData, String batteryId) {
            // Implement logic to filter telemetry data by battery ID
            return telemetryData.stream().filter(data->data.getBatteryId().equals(batteryId)).collect(Collectors.toList());
        }
    }
}
