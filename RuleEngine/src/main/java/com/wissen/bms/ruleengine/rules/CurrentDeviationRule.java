package com.wissen.bms.ruleengine.rules;

import java.util.List;

import com.wissen.bms.common.model.TelemetryData;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;



public class CurrentDeviationRule implements Rule {

	private List<TelemetryData> histTeleData;
	private RuleContext ruleContext;

    // Define thresholds for fault detection
    private static final double OVERCURRENT_THRESHOLD = 5.0; // Amperes (example)
    private static final double UNDERCURRENT_THRESHOLD = 0.1; // Amperes (example)
    private static final double MAX_FLUCTUATION = 2.0; // Max allowable fluctuation (example)
    private static final int TREND_WINDOW_SIZE = 3; // Size of window to analyze for trend (example)


	public CurrentDeviationRule(List<TelemetryData> histTeleData, RuleContext ruleContext) {
		this.histTeleData = histTeleData;
		this.ruleContext = ruleContext;
	}

	@Override
	public boolean evaluate(Facts facts) {
		System.out.println("Current : ");	
		
		return detectFault(histTeleData);
	}

	@Override
	public void execute(Facts facts) throws Exception {
		String riskReason = "Warning: Current is abornormal ";
		ruleContext.setCurrentRisk(0.1); // High risk for Current
		if(ruleContext.getRiskReason().equals(null) || ruleContext.getRiskReason().equals("")) {
			ruleContext.setRiskReason(riskReason);
		} 
		else {
			String prevRiskReason = ruleContext.getRiskReason();
			ruleContext.setRiskReason(prevRiskReason+""+riskReason+" | ");
		}
		System.out.println("Warning: Current is abornormal for battery ");
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int compareTo(Rule o) {
		// TODO Auto-generated method stub
		return 2;
	}
	
    // Method to check for faults based on historical current trends
    public static boolean detectFault(List<TelemetryData> historicalData) {
        if (historicalData.size() < TREND_WINDOW_SIZE) {
            System.out.println("Insufficient data for analysis.");
            return false;
        }
        
        int faultObservation = 0;
        // Iterate through the historical data
        for (int i = TREND_WINDOW_SIZE; i < historicalData.size(); i++) {
            List<TelemetryData> currentWindow = historicalData.subList(i - TREND_WINDOW_SIZE, i);

            double current = historicalData.get(i).getCurrent();
            double previousAverage = calculateAverage(currentWindow);
            double rateOfChange = (current - previousAverage) / previousAverage * 100; // Percentage change

            System.out.println("Analyzing current: " + current + " A");

            // Check for overcurrent
            if (current > OVERCURRENT_THRESHOLD) {
                System.out.println("Fault Detected: Overcurrent! The current is too high.");
                faultObservation++;
            }
            // Check for undercurrent
            else if (current < UNDERCURRENT_THRESHOLD) {
                System.out.println("Fault Detected: Undercurrent! The current is too low.");
                faultObservation++;
            }
            // Check for large fluctuations in the trend
            else if (Math.abs(rateOfChange) > MAX_FLUCTUATION) {
                System.out.println("Fault Detected: Sudden fluctuation in current.");
                faultObservation++;
            }
            // Check for gradual decline in current
            else if (rateOfChange < -20) {
                System.out.println("Warning: Significant decline in current detected. This may indicate battery degradation.");
                faultObservation++;
            }
            // Check for sudden drop in current (based on a threshold for sudden change)
            else if (Math.abs(current - previousAverage) > 2.0) {
                System.out.println("Fault Detected: Sudden drop in current.");
                faultObservation++;
            }
            System.out.println("-----------");
        }
        
        //Out of total historic records if 30% of records are faulty then battery is faulty
        double deviation = faultObservation / historicalData.size();
        return (deviation*100) > 30;
    }

    // Method to calculate the average of a list of currents (used for trend analysis)
    private static double calculateAverage(List<TelemetryData> histTelemetryData) {
        double sum = 0;
        for (TelemetryData telemetryData : histTelemetryData) {
            sum += telemetryData.getCurrent();
        }
        return sum / histTelemetryData.size();
    }
}
