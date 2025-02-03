package com.wissen.bms.ruleengine.service;
import com.wissen.bms.common.model.TelemetryData;
import com.wissen.bms.ruleengine.rules.RuleContext;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@ExtendWith(MockitoExtension.class)
class TeleRuleEngineServiceTest {
    @InjectMocks
    TeleRuleEngineService teleRuleEngineService;

    @Test
    void createTelemetryFactsSuccessCase() {
        Facts telemetryFacts = teleRuleEngineService.createTelemetryFacts(getTelemetryDataList());
        Assertions.assertNotNull(telemetryFacts);
    }

    @Test
    void createTelemetryFactsFailureCase() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            teleRuleEngineService.createTelemetryFacts(null);
        });
    }

    @Test
    void createTelemetryFactsFailureCaseEmptyList() {
        Assertions.assertNotNull(teleRuleEngineService.createTelemetryFacts(new ArrayList<>()));
    }

    @Test
    void evaluateFactsTestForCriticalFault() {
        Facts facts = new Facts();
        facts.put("voltage", 10.5);
        facts.put("temperature", 38.4);
        facts.put("internalResistance", 0.2);
        Map<Rule, Boolean> ruleBooleanMap = teleRuleEngineService.evaluateFacts(facts);
        Assertions.assertEquals(true, ruleBooleanMap.values().stream().toList().get(0));
    }

    @Test
    void evaluateFactsTestForNotCriticalFault() {
        Facts facts = new Facts();
        facts.put("voltage", 400.5);
        facts.put("temperature", 38.4);
        facts.put("internalResistance", 0.02);
        Map<Rule, Boolean> ruleBooleanMap = teleRuleEngineService.evaluateFacts(facts);
        Assertions.assertEquals(false, ruleBooleanMap.values().stream().toList().get(0));
    }

    @Test
    void evaluateRiskSuccess() {
        Assertions.assertNotNull(teleRuleEngineService.evaluateRisk(getTelemetryDataList()));
    }

    @Test
    void evaluateRiskFailure() {
        Assertions.assertDoesNotThrow(() -> teleRuleEngineService.evaluateRisk(null));
    }

    @Test
    void processSingleTelemetryDataSuccess() {
        TelemetryData telemetry1 = new TelemetryData();
        telemetry1.setBatteryId("1");
        telemetry1.setVehicleId("v1");
        telemetry1.setVoltage(35.6);
        telemetry1.setTemperature(28);
        telemetry1.setInternalResistance(2);
        telemetry1.setGps("gps1");
        RuleContext ruleContext = teleRuleEngineService.processSingleTelemetryData(telemetry1);
        Assertions.assertNotNull(ruleContext);
    }

    @Test
    void processTelemetryDataSuccessTest() {
        try {
            Assertions.assertNotNull(teleRuleEngineService.processTelemetryData(getTelemetryDataList()));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void processTelemetryDataFailureTest() {
        Assertions.assertThrows(Exception.class, () -> teleRuleEngineService.processTelemetryData(null));
    }

    private List<TelemetryData> getTelemetryDataList() {
        List<TelemetryData> data = new ArrayList<>();
        TelemetryData telemetry1 = new TelemetryData();
        telemetry1.setBatteryId("1");
        telemetry1.setVehicleId("v1");
        telemetry1.setVoltage(35.6);
        telemetry1.setTemperature(28);
        telemetry1.setInternalResistance(2);
        telemetry1.setGps("gps1");
        data.add(telemetry1);
        TelemetryData telemetry2 = new TelemetryData();
        telemetry2.setBatteryId("2");
        telemetry2.setGps("gps2");
        telemetry2.setVehicleId("v2");
        telemetry2.setVoltage(36.6);
        telemetry2.setTemperature(38);
        telemetry2.setInternalResistance(3);
        data.add(telemetry2);
        return data;
    }
}
