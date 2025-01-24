package com.wissen.bms.streamprocessing.service;

import com.wissen.bms.common.model.BatteryFault;
import com.wissen.bms.common.model.TelemetryData;
import com.wissen.bms.streamprocessing.utility.EVUtil;
import com.wissen.bms.ruleengine.rules.RuleContext;
import com.wissen.bms.ruleengine.service.TeleRuleEngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class GradualDegradationProcessor {

    @Autowired
    private KafkaPublisher kafkaPublisher;

    @Autowired
    private TeleRuleEngineService teleRuleEngineService;

    public void detectDailyGradualFaults() {
        log.info("Loading telemetry data from InfluxDB...");

        // Fetch data for the past day from InfluxDB
        List<TelemetryData> telemetryData = new ArrayList<>();//InfluxDBClient.fetchTelemetryData(Instant.now().minusSeconds(86400), Instant.now());

        log.info("Processing gradual degradation rules...");
        for (String batteryId : getUniqueBatteryIds(telemetryData)) {
            List<TelemetryData> batteryTelemetry = filterTelemetryByBatteryId(telemetryData, batteryId);
//            RuleContext ruleContext = teleRuleEngineService.processTelemetryData(batteryTelemetry);

            RuleContext ruleContext = new RuleContext();
            if ("High".equals(ruleContext.getRiskLevel())) {
                BatteryFault batteryFault = EVUtil.convertRuleContextToBatteryFault(ruleContext);
                batteryFault.setGps(batteryTelemetry.get(0).getGps());
                batteryFault.setTime(String.valueOf(batteryTelemetry.get(0).getTime()));
                String batteryFault1 = EVUtil.serializeBatteryFault(batteryFault);
                kafkaPublisher.sendMessage(batteryFault1);
            }
        }
    }

    private static Set<String> getUniqueBatteryIds(List<TelemetryData> telemetryData) {
        return telemetryData.stream().map(TelemetryData::getBatteryId).collect(Collectors.toSet());
    }

    private static List<TelemetryData> filterTelemetryByBatteryId(List<TelemetryData> telemetryData, String batteryId) {
        // Implement logic to filter telemetry data by battery ID
        return telemetryData.stream().filter(data -> data.getBatteryId().equals(batteryId)).collect(Collectors.toList());
    }
}