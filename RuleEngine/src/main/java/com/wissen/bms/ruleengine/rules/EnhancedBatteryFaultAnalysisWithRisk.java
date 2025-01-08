package com.wissen.bms.ruleengine.rules;

import java.util.List;

import com.ruleengine.EVBatteryRuleEngine.dto.*;
import com.wissen.bms.common.model.TelemetryData;

import java.util.ArrayList;

public class EnhancedBatteryFaultAnalysisWithRisk {

    // Fault thresholds (static thresholds for individual readings)
    private static final double MIN_VOLTAGE = 10.0; // Minimum voltage (V)
    private static final double MAX_VOLTAGE = 15.0; // Maximum voltage (V)
    private static final double MAX_TEMPERATURE = 50.0; // Maximum temperature (°C)
    private static final double MIN_SOC = 10.0; // Minimum State of Charge (SOC) (%)
    private static final double MAX_SOC = 90.0; // Maximum State of Charge (SOC) (%)

    // Trend analysis thresholds
    private static final double VOLTAGE_DROP_THRESHOLD = 0.05; // 5% voltage drop per time window
    private static final double TEMPERATURE_SPIKE_THRESHOLD = 5.0; // 5°C sudden temperature spike
    private static final long TIME_WINDOW_MS = 60000; // 1 minute (time window for trend analysis)

    // Risk classifier thresholds
    private static final double HIGH_RISK_THRESHOLD = 0.2; // 20%+ drop in voltage or 10°C temperature spike for high risk
    private static final double MEDIUM_RISK_THRESHOLD = 0.1; // 10% voltage drop or 5°C temperature spike for medium risk

    // Method to detect faulty batteries based on current and trend analysis and classify risk
    public static List<String> findFaultyBatteriesWithRisk(List<TelemetryData> telemetryData) {
        List<String> faultyBatteries = new ArrayList<>();

        for (int i = 1; i < telemetryData.size(); i++) {
        	TelemetryData currentData = telemetryData.get(i);
        	TelemetryData previousData = telemetryData.get(i - 1);

            boolean isFaulty = false;
            double riskScore = 0.0;  // Initialize risk score

            // 1. Check current data against thresholds (voltage, temperature, SOC)
            if (currentData.getVoltage() < MIN_VOLTAGE || currentData.getVoltage() > MAX_VOLTAGE) {
                isFaulty = true;
                riskScore += 0.1;  // Increment risk score for out-of-bounds voltage
            }
            if (currentData.getTemperature() > MAX_TEMPERATURE) {
                isFaulty = true;
                riskScore += 0.1;  // Increment risk score for high temperature
            }
            if (currentData.getSoc() < MIN_SOC || currentData.getSoc() > MAX_SOC) {
                isFaulty = true;
                riskScore += 0.1;  // Increment risk score for out-of-bounds SOC
            }

            // 2. Trend Analysis: Check for voltage drop over time
            double voltageDrop = hasSignificantVoltageDrop(previousData, currentData);
            if (voltageDrop > 0) {
                isFaulty = true;
                riskScore += voltageDrop;  // Increment risk score based on voltage drop percentage
            }

            // 3. Trend Analysis: Check for sudden temperature spike
            double temperatureSpike = hasTemperatureSpike(previousData, currentData);
            if (temperatureSpike > 0) {
                isFaulty = true;
                riskScore += temperatureSpike;  // Increment risk score based on temperature spike (in °C)
            }

            // 4. If any fault is detected, classify the battery with risk and mark it as faulty
            if (isFaulty) {
                String riskCategory = classifyRisk(riskScore);
                System.out.println("Battery ID: " + currentData.getBatteryId() + " - Risk: " + riskCategory + " (Score: " + riskScore + ")");
                faultyBatteries.add(currentData.getBatteryId());
            }
        }

        return faultyBatteries;
    }

    // Check if voltage dropped significantly over time (e.g., 5% drop within the time window)
    private static double hasSignificantVoltageDrop(TelemetryData previousData, TelemetryData currentData) {
        double voltageDrop = (previousData.getVoltage() - currentData.getVoltage()) / previousData.getVoltage();
        long timeDiff = 0;//currentData.getTimestamp() - previousData.getTimestamp();

        // Return the percentage drop if it is significant within the time window
        if (voltageDrop > VOLTAGE_DROP_THRESHOLD && timeDiff < TIME_WINDOW_MS) {
            return voltageDrop;  // Return the percentage drop as a risk factor
        }
        return 0.0;  // No significant drop
    }

    // Check if there was a sudden temperature spike (e.g., more than 5°C in 1 minute)
    private static double hasTemperatureSpike(TelemetryData previousData, TelemetryData currentData) {
        double temperatureChange = Math.abs(currentData.getTemperature() - previousData.getTemperature());
        long timeDiff = 0;//currentData.getTimestamp() - previousData.getTimestamp();

        // Return the temperature change if it exceeds the threshold within the time window
        if (temperatureChange > TEMPERATURE_SPIKE_THRESHOLD && timeDiff < TIME_WINDOW_MS) {
            return temperatureChange;  // Return the spike in temperature as a risk factor
        }
        return 0.0;  // No significant temperature spike
    }

    // Classify risk level based on the calculated risk score
    private static String classifyRisk(double riskScore) {
        if (riskScore >= HIGH_RISK_THRESHOLD) {
            return "High Risk";
        } else if (riskScore >= MEDIUM_RISK_THRESHOLD) {
            return "Medium Risk";
        } else {
            return "Low Risk";
        }
    }
  
    public static void main(String[] args) {
//        // Sample battery telemetry data with timestamps
//        List<BatteryTelemetry> telemetryData = new ArrayList<>();
//        telemetryData.add(new BatteryTelemetry("Battery1", 12.5, 2.0, 45.0, 85.0, System.currentTimeMillis()));
//        telemetryData.add(new BatteryTelemetry("Battery1", 12.0, 2.0, 46.0, 84.0, System.currentTimeMillis() + 60000));
//        telemetryData.add(new BatteryTelemetry("Battery1", 11.4, 2.0, 47.0, 83.0, System.currentTimeMillis() + 120000));
//        telemetryData.add(new BatteryTelemetry("Battery2", 13.0, 1.5, 40.0, 70.0, System.currentTimeMillis() + 60000));
//        telemetryData.add(new BatteryTelemetry("Battery2", 14.0, 1.5, 51.0, 75.0, System.currentTimeMillis() + 120000));

        // Detect faulty batteries with risk analysis
    //    List<String> faultyBatteries = findFaultyBatteriesWithRisk(telemetryData);

        // Output faulty batteries
        System.out.println("\nFaulty batteries with risk classification");
    }
}
