package com.wissen.bms.ruleengine.rules;

import java.util.List;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import com.wissen.bms.common.model.TelemetryData;

public class ChargingTimeRule implements Rule {

	private List<TelemetryData> histTeleData;
	private RuleContext ruleContext;

	public ChargingTimeRule(List<TelemetryData> histTeleData, RuleContext ruleContext) {
		this.histTeleData = histTeleData;
		this.ruleContext = ruleContext;
	}

	@Override
	public boolean evaluate(Facts facts) {
		System.out.println("ChargingTime : ");
        // Calculate the average charging time
        double average = calculateAverage(histTeleData);
        
        // Calculate the standard deviation
        double standardDeviation = calculateStandardDeviation(histTeleData, average);
        
        // Define a threshold for abnormal charging times
        double threshold = average + 2 * standardDeviation;  // Example threshold (2 standard deviations above the mean)
        
        // Check for faulty battery based on historic data
        return checkForBatteryFault(histTeleData, threshold);
	}

	@Override
	public void execute(Facts facts) throws Exception {
		String riskReason = "Warning: ChargingTime is deviated for battery ";
		ruleContext.setChargingTimeRisk(0.1); // Medium risk for out of bounds Charging time
		ruleContext.getRiskReason().add(riskReason);
//
//		if (ruleContext.getRiskReason().equals(null) || ruleContext.getRiskReason().equals("")) {
//			ruleContext.getRiskReason().add(riskReason);
//		} else {
//			String prevRiskReason = ruleContext.getRiskReason();
//			ruleContext.setRiskReason(prevRiskReason + "" + riskReason + " | ");
//		}
		System.out.println("Warning: ChargingTime is deviated for battery ");
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int compareTo(Rule o) {
		// TODO Auto-generated method stub
		return 1;
	}

	// Method to calculate the average of charging times
    public static double calculateAverage(List<TelemetryData> histTeleData) {
        double sum = 0;
        for (TelemetryData telemetryData : histTeleData) {
            sum += telemetryData.getChargingTime();
        }
        return sum / histTeleData.size();
    }

    // Method to calculate the standard deviation of charging times
    public static double calculateStandardDeviation(List<TelemetryData> histTeleData, double average) {
        double variance = 0;
        for (TelemetryData telemetryData : histTeleData) {
            variance += Math.pow(telemetryData.getChargingTime() - average, 2);
        }
        return Math.sqrt(variance / histTeleData.size());
    }

    
	public static boolean checkForBatteryFault(List<TelemetryData> histTeleData , double threshold) {
		int faultObservation = 0;
		
		for (int i = 0; i < histTeleData.size(); i++) {
            double chargingTime = histTeleData.get(i).getChargingTime();
            if (chargingTime > threshold) {
                System.out.println("Potential fault detected at index " + i + " with charging time: " + chargingTime + " minutes.");
                faultObservation++;
            }
        }
        
		//Out of total historic records if 30% of records are faulty then battery is faulty
        double deviation = faultObservation / histTeleData.size();
        return (deviation*100) > 30;

	}
}