package com.ruleengine.EVBatteryRuleEngine.rules;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import com.ruleengine.EVBatteryRuleEngine.dto.TelemetryData;

public class TemperatureDeviationRule implements Rule {

	private List<TelemetryData> previousData;
	private RuleContext ruleContext;

    private static final double MAX_TEMPERATURE_THRESHOLD = 60.0; // Maximum temperature in Celsius
    private static final double MIN_TEMPERATURE_THRESHOLD = 0.0;  // Minimum temperature in Celsius
    private static final int CONSISTENT_FAULT_DURATION = 3;       // Consecutive readings required to confirm a fault
    private static final double MAX_RATE_OF_CHANGE = 10.0;        // Maximum allowed temperature change per reading

	public TemperatureDeviationRule( List<TelemetryData> previousData, RuleContext ruleContext) {
		this.previousData = previousData;
		this.ruleContext = ruleContext;
	}

	@Override
	public boolean evaluate(Facts facts) {
		System.out.println("Temperature : ");				
		return isBatteryFaulty(previousData);// || true;
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
	
	 static boolean isBatteryFaulty(List<TelemetryData> histTeleData) {
	        int consecutiveHighCount = 0;
	        int consecutiveLowCount = 0;
	        int faultObservation = 0;
	        for (int i = 0; i < histTeleData.size(); i++) {
	            double currentTemperature = histTeleData.get(i).getTemperature();

	            // Check for high temperature
	            if (currentTemperature > MAX_TEMPERATURE_THRESHOLD) {
	                consecutiveHighCount++;
	                consecutiveLowCount = 0; // Reset low count
	                if (consecutiveHighCount >= CONSISTENT_FAULT_DURATION) {
	                    System.out.println("Fault Detected: Over-temperature at index " + i);
	                    return true;
	                }
	            }
	            // Check for low temperature
	            else if (currentTemperature < MIN_TEMPERATURE_THRESHOLD) {
	                consecutiveLowCount++;
	                consecutiveHighCount = 0; // Reset high count
	                if (consecutiveLowCount >= CONSISTENT_FAULT_DURATION) {
	                    System.out.println("Fault Detected: Under-temperature at index " + i);
	                    return true;
	                }
	            }
	            // Normal temperature
	            else {
	                consecutiveHighCount = 0;
	                consecutiveLowCount = 0;
	            }

	            // Check for sudden spikes or drops (rate of change)
	            if (i > 0) {
	                double previousTemperature = histTeleData.get(i - 1).getTemperature();
	                double rateOfChange = Math.abs(currentTemperature - previousTemperature);
	                if (rateOfChange > MAX_RATE_OF_CHANGE) {
	                    System.out.println("Fault Detected: Sudden temperature change at index " + i);
	                    faultObservation++;
	                }
	            }
	        }
	        int totalHistCount = histTeleData.size();
	        if(totalHistCount > 0) {
		        double deviation = faultObservation/totalHistCount;
		        return (deviation*100) > 30;
	        }
	        return false;

	    }
}
