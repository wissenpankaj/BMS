package com.ruleengine.EVBatteryRuleEngine.rules;

import java.util.ArrayList;
import java.util.List;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import com.ruleengine.EVBatteryRuleEngine.dto.TelemetryData;

public class CycleCountRule implements Rule {

	private List<TelemetryData> histTeleData;
	private RuleContext ruleContext;
	private static final int WINDOW_SIZE = 3;

	public CycleCountRule(List<TelemetryData> histTeleData, RuleContext ruleContext) {
		this.histTeleData = histTeleData;
		this.ruleContext = ruleContext;
	}

	@Override
	public boolean evaluate(Facts facts) {
		System.out.println("Cycle count : ");
     
        // Check for faulty battery based on historic data
        return checkForBatteryFault(histTeleData, WINDOW_SIZE);
	}

	@Override
	public void execute(Facts facts) throws Exception {
		String riskReason = "Warning: CycleCount is deviated for battery ";
		ruleContext.setCycleCountRisk(0.1); // Medium risk for abnornal cycleCount
		if (ruleContext.getRiskReason().equals(null) || ruleContext.getRiskReason().equals("")) {
			ruleContext.setRiskReason(riskReason);
		} else {
			String prevRiskReason = ruleContext.getRiskReason();
			ruleContext.setRiskReason(prevRiskReason + "" + riskReason + " | ");
		}
		;
		System.out.println("Warning: CycleCount is deviated for battery ");
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public int compareTo(Rule o) {
		// TODO Auto-generated method stub
		return 3;
	}
	
    public static boolean checkForBatteryFault(List<TelemetryData> histTelemetryData, int windowSize) {
        if (histTelemetryData == null || histTelemetryData.size() < windowSize + 1) {
            System.out.println("Not enough data to detect faults.");
            return false;
        }

        int faultObservation = 0;
        // Loop through cycle counts and calculate moving average for trend analysis
        for (int i = windowSize; i < histTelemetryData.size(); i++) {
            double average = calculateMovingAverage(histTelemetryData, i, windowSize);
            int currentCount = histTelemetryData.get(i).getCycleCount();
            
            // If current count deviates significantly from the moving average
            double deviation = Math.abs(currentCount - average) / average;
            if (deviation > 0.2) {  // 20% deviation from the moving average
                System.out.println("Fault detected at index " + i + " (Time " + i + "):");
                System.out.println("Current count: " + currentCount);
                System.out.println("Moving average: " + average);
                System.out.println("Deviation: " + (deviation * 100) + "% - Possible battery fault detected.\n");
                faultObservation++;
            }
        }
        
        //Out of total historic records if 30% of records are faulty then battery is faulty
        double deviation = faultObservation / histTelemetryData.size();
        return (deviation*100) > 30;
        
    }

    // Calculates the moving average for the given window size
    public static double calculateMovingAverage(List<TelemetryData> histTelemetryData, int currentIndex, int windowSize) {
        double sum = 0;
        for (int i = currentIndex - windowSize + 1; i <= currentIndex; i++) {
            sum += histTelemetryData.get(i).getCycleCount();
        }
        return sum / windowSize;
    }
}