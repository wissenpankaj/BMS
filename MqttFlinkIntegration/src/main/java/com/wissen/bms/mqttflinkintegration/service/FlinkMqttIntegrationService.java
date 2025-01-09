package com.wissen.bms.mqttflinkintegration.service;

import com.ruleengine.EVBatteryRuleEngine.rules.RuleContext;
import com.ruleengine.EVBatteryRuleEngine.service.TeleRuleEngineService;
import com.wissen.bms.common.model.BatteryFault;
import com.wissen.bms.common.model.TelemetryData;
import com.wissen.bms.mqttflinkintegration.utility.EVUtil;
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

    @Autowired
    private TeleRuleEngineService teleRuleEngineService;


    @Value("${mqtt.broker.url}")
    private String mqttUrl;

    @Value("${mqtt.topic}")
    private String mqttTopic;

    @Autowired
    private FlinkKafkaProducer<String> kafkaProducer;

    public void process() throws Exception {

        // Set up Flink execution environment
        log.info("changes Inside @class FlinkMqttIntegrationService @method process");

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Create a custom MQTT source
        DataStream<String> telemetryStream = env.addSource(new FlinkMqttIntegrationService.MqttSource(mqttUrl, mqttTopic));

        // Real-time processing for immediate data sink
        telemetryStream
                .map(EVUtil::deserializeTelemetryData)
                .addSink(new InfluxDBSink());


        telemetryStream
                .map(EVUtil::deserializeTelemetryData)
                .keyBy(TelemetryData::getBatteryId)
                .process(new CriticalThresholdProcessFunction(teleRuleEngineService))
                .addSink(kafkaProducer);

        telemetryStream
                .map(EVUtil::deserializeTelemetryData)  // Convert String to TelemetryData
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<TelemetryData>forBoundedOutOfOrderness(Duration.ofSeconds(5)) // Handle late data with 5 second delay
                                .withTimestampAssigner((event, timestamp) -> event.getTime()) // Use event timestamp for windowing
                )
                .keyBy(TelemetryData::getBatteryId) // Group data by Battery ID
                .timeWindow(Time.minutes(1)) // Create a 10-second tumbling window
                .apply(new BatchProcessor(teleRuleEngineService))
                .addSink(kafkaProducer); // Process data within each 10-second window

        log.info("Inside @class FlinkMqttIntegrationService @method process on execute");
        // Execute the Flink job
        env.execute("MQTT to Flink Integration");

        new GradualDegradationProcessor().detectDailyGradualFaults();

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

        private TeleRuleEngineService ruleEngineService;

        public BatchProcessor(TeleRuleEngineService ruleEngineService) {
            this.ruleEngineService = ruleEngineService;
        }

        @Override
        public void apply(String key, TimeWindow timeWindow, Iterable<TelemetryData> input, Collector<String> out) throws Exception {
            List<TelemetryData> batch = new ArrayList<>();
            // Collect all telemetry data into a batch
            log.info("Inside @class FlinkMqttIntegrationService BatchProcessor @method apply key : {}, timeWindow : {}, input: {}", key, timeWindow, input);
            input.forEach(batch::add);

//            RuleContext ruleContext = ruleEngineService.processTelemetryData(batch);

            RuleContext ruleContext = new RuleContext();
            if ("High".equals(ruleContext.getRiskLevel())) {
                BatteryFault batteryFault = EVUtil.convertRuleContextToBatteryFault(ruleContext);
                batteryFault.setGps(batch.get(0).getGps());
                batteryFault.setTime(String.valueOf(batch.get(0).getTime()));
                String batteryFault1 = EVUtil.deserializeBatteryFault(batteryFault);
                out.collect(batteryFault1);
            }

        }
    }

    public static class CriticalThresholdProcessFunction extends org.apache.flink.streaming.api.functions.KeyedProcessFunction<String, TelemetryData, String> {
        private ValueState<TelemetryData> lastTelemetryState;
        private TeleRuleEngineService ruleEngineService;

        public CriticalThresholdProcessFunction(TeleRuleEngineService ruleEngineService) {
            this.ruleEngineService = ruleEngineService;
        }

        @Override
        public void open(org.apache.flink.configuration.Configuration parameters) throws Exception {
            ValueStateDescriptor<TelemetryData> descriptor = new ValueStateDescriptor<>(
                    "lastTelemetryState",
                    TelemetryData.class
            );
            lastTelemetryState = getRuntimeContext().getState(descriptor);
        }

        @Override
        public void processElement(TelemetryData telemetryData, KeyedProcessFunction<String, TelemetryData, String>.Context context, Collector<String> out) throws Exception {

//           RuleContext ruleContext = ruleEngineService.processTelemetryData(new ArrayList(telemetryData));

            RuleContext ruleContext = new RuleContext();
            if ("High".equals(ruleContext.getRiskLevel())) {
                BatteryFault batteryFault = EVUtil.convertRuleContextToBatteryFault(ruleContext);
                batteryFault.setGps(telemetryData.getGps());
                batteryFault.setTime(String.valueOf(telemetryData.getTime()));
                String batteryFault1 = EVUtil.deserializeBatteryFault(batteryFault);
                out.collect(batteryFault1);
            }

            TelemetryData lastTelemetry = lastTelemetryState.value();

//            RuleContext ruleContext = ruleEngineService.processTelemetryData(telemetryData, lastTelemetry);
            log.info("going to collect FaultSink @method invoke");
            lastTelemetryState.update(telemetryData);
        }
    }


    public static class CriticalThresholdProcessFunction1 extends org.apache.flink.streaming.api.functions.KeyedProcessFunction<String, TelemetryData, String> {
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
        public void processElement(TelemetryData telemetryData, KeyedProcessFunction<String, TelemetryData, String>.Context context, Collector<String> out) throws Exception {

            TeleRuleEngineService ruleEngineService = new TeleRuleEngineService();
//            ruleEngineService.processTelemetryData(telemetryData);

            RuleContext ruleContext = new RuleContext();
            ruleContext.setRiskLevel("High");
            out.collect("ruleContext");

            TelemetryData lastTelemetry = lastTelemetryState.value();

//            RuleContext ruleContext = ruleEngineService.processTelemetryData(telemetryData, lastTelemetry);
            log.info("going to collect FaultSink @method invoke");
//            out.collect("ruleContext");

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


//    public static class CriticalThresholdProcessFunction extends org.apache.flink.streaming.api.functions.KeyedProcessFunction<String, TelemetryData, RuleContext> {
//        private ValueState<TelemetryData> lastTelemetryState;
//        TeleRuleEngineService ruleEngineService;
//        CriticalThresholdProcessFunction(TeleRuleEngineService ruleEngineService){
//            this.ruleEngineService = ruleEngineService;
//        }
//        @Override
//        public void open(org.apache.flink.configuration.Configuration parameters) throws Exception {
//            ValueStateDescriptor<TelemetryData> descriptor = new ValueStateDescriptor<>(
//                    "lastTelemetryState",
//                    TelemetryData.class
//            );
//            lastTelemetryState = getRuntimeContext().getState(descriptor);
//        }
//
//        @Override
//        public void processElement(TelemetryData telemetryData, KeyedProcessFunction<String, TelemetryData, RuleContext>.Context context, Collector<RuleContext> out) throws Exception {
//
////           RuleContext ruleContext = ruleEngineService.processTelemetryData(new ArrayList(telemetryData));
//
//            RuleContext ruleContext = new RuleContext();
//            if("High".equals(ruleContext.getRiskLevel())){
//
//            }
//            ruleContext.setRiskLevel("High");
//            out.collect(ruleContext);
//
//            TelemetryData lastTelemetry = lastTelemetryState.value();
//
////            RuleContext ruleContext = ruleEngineService.processTelemetryData(telemetryData, lastTelemetry);
//            log.info("going to collect FaultSink @method invoke");
//            out.collect(ruleContext);
//
//            // Update state
//            lastTelemetryState.update(telemetryData);
//        }
//    }

}


