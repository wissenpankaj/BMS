package com.ruleengine.EVBatteryRuleEngine.rules;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.wissen.bms.common.model.TelemetryData;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import com.ruleengine.EVBatteryRuleEngine.dto.*;

public class TemperatureSpikeRule implements Rule {

	private List<TelemetryData> previousData;
	private RuleContext ruleContext;

	private static final double MIN_TEMP = 20.0; // Min acceptable temperature (°C)
    private static final double MAX_TEMP = 50.0; // Max acceptable temperature (°C)
    private static final int WINDOW_SIZE = 10; // Number of recent readings to analyze for trends

	public TemperatureSpikeRule( List<TelemetryData> previousData, RuleContext ruleContext) {
		this.previousData = previousData;
		this.ruleContext = ruleContext;
	}

	@Override
	public boolean evaluate(Facts facts) {
		System.out.println("Temperature : ");
		return isBatteryFaulty(previousData);
	}

	@Override
	public void execute(Facts facts) throws Exception {
		String riskReason = "Warning: Temperature spiked for battery ";
		ruleContext.setTemperatureRisk(0.05); // Medium risk for out of bounds SOC
		if(ruleContext.getRiskReason().equals(null) || ruleContext.getRiskReason().equals("")) {
			ruleContext.setRiskReason(riskReason);
		}
		else {
			String prevRiskReason = ruleContext.getRiskReason();
			ruleContext.setRiskReason(prevRiskReason+""+riskReason+" | ");
		}
		System.out.println("Warning: Temperature spiked for battery ");
	}

	@Override
	public int compareTo(Rule o) {
		// TODO Auto-generated method stub
		return 7;
	}

	@Override
	public int getPriority() {
		return 7; // Higher priority
	}

	// Function to detect battery fault based on historical temperature
    public static boolean isBatteryFaulty(List<TelemetryData> previousData) {
        int size = previousData.size();

        // If we have fewer than the window size of data points, can't analyze trends effectively
        if (size < WINDOW_SIZE) {
            System.out.println("Not enough data to analyze.");
            return false;
        }

        // Analyze the last N readings
        List<TelemetryData> recentTemperatures = previousData.subList(size - WINDOW_SIZE, size);

        // Check for temperature spikes or out-of-bound temperatures
        for (TelemetryData telemetryData : recentTemperatures) {
            if (telemetryData.getTemperature() < MIN_TEMP || telemetryData.getTemperature() > MAX_TEMP) {
                System.out.println("Temperature out of bounds: " + telemetryData.getTemperature() + "°C");
                return true;
            }
        }

        // Check for sudden temperature spike in the last N readings
        if (detectSuddenSpike( recentTemperatures)) {
            return true;
        }

        // Optionally, check for a gradual temperature rise over time
        if (detectGradualRise(recentTemperatures)) {
            return true;
        }

        // Otherwise, the battery temperature appears normal
        return false;
    }

    // Detect if there's a sudden spike in temperature (defined as a rapid increase)
    private static boolean detectSuddenSpike(List<TelemetryData> recentTemperatures) {
        for (int i = 1; i < recentTemperatures.size(); i++) {
            if (recentTemperatures.get(i).getTemperature() > recentTemperatures.get(i - 1).getTemperature() + 10) { // Example: spike of more than 10°C
                System.out.println("Sudden temperature spike detected: " + recentTemperatures.get(i) + "°C");
                return true;
            }
        }
        return false;
    }

    // Detect a gradual increase in temperature
    private static boolean detectGradualRise(List<TelemetryData> recentTemperatures) {
        double initialTemp = recentTemperatures.get(0).getTemperature();
        double finalTemp = recentTemperatures.get(recentTemperatures.size() - 1).getTemperature();

        // If temperature has gradually risen by more than 5°C in the last N readings
        if (finalTemp - initialTemp > 5) {
            System.out.println("Gradual temperature rise detected: " + (finalTemp - initialTemp) + "°C");
            return true;
        }
        return false;
    }

}
