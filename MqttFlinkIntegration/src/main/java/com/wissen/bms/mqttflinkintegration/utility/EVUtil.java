package com.wissen.bms.mqttflinkintegration.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wissen.bms.common.model.BatteryFault;
import com.wissen.bms.common.model.TelemetryData;
import com.wissen.bms.ruleengine.rules.RuleContext;

public class EVUtil {

    public static String deserializeBatteryFault(BatteryFault batteryFault){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Register for handling time if needed
            return objectMapper.writeValueAsString(batteryFault);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception occurred on parsing BatteryFault");
        }
    }

    // Convert object to JSON string
    public static String serializeTelemetryData(TelemetryData telemetryData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Register for handling time if needed
            return objectMapper.writeValueAsString(telemetryData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception occurred on serializing Telemetry Data");
        }
    }

    // Deserialize from JSON string back to object
    public static TelemetryData deserializeTelemetryData(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(json, TelemetryData.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception occurred on deserializing Telemetry Data");
        }
    }

    public static BatteryFault convertRuleContextToBatteryFault(RuleContext ruleContext){
        BatteryFault batteryFault = new BatteryFault();
        batteryFault.setGps("gps");
        batteryFault.setVehicleId(ruleContext.getVehicleId());
        batteryFault.setBatteryId(ruleContext.getBatterId());
        batteryFault.setFaultReason(ruleContext.getRiskReason());
        batteryFault.setRecommendation("Recommendation");
        batteryFault.setTime("time");
        batteryFault.setLevel(ruleContext.getRiskLevel());
        batteryFault.setRisk("Risk");
        return batteryFault;
    }
}
