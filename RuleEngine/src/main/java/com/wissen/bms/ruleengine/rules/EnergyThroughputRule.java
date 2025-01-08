package com.wissen.bms.ruleengine.rules;

import java.util.ArrayList;
import java.util.List;

import com.wissen.bms.common.model.TelemetryData;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import com.ruleengine.EVBatteryRuleEngine.dto.*;

public class EnergyThroughputRule implements Rule {
	
    public static final double SOH_THRESHOLD = 0.80; // Example threshold for failure
    public static final double MAX_DEGRADATION_RATE = 0.05; // Max SOH degradation rate (per time unit)

    private List<TelemetryData> histTelemetryData;
    private RuleContext ruleContext;

    static int WINDOW_SIZE = 3; // Moving average window size
    static double threshold = 15.0; // Deviation threshold for anomaly detection

    public EnergyThroughputRule(List<TelemetryData> histTelemetryData, RuleContext ruleContext) {
        this.histTelemetryData = histTelemetryData;
        this.ruleContext = ruleContext;
    }
    
    @Override
	public boolean evaluate(Facts facts) {
    	 System.out.println("Energy Throuhput : ");
    	 return detectFault(histTelemetryData);
	}

	@Override
	public void execute(Facts facts) throws Exception {
		String riskReason = "Warning: Energy Throuhput is abnormal for battery ";
		ruleContext.setEnergyThroughputRisk(0.1); //Medium risk for Energy Through put
		if(ruleContext.getRiskReason().equals(null) || ruleContext.getRiskReason().equals("")) {
			ruleContext.setRiskReason(riskReason);
		} 
		else {
			String prevRiskReason = ruleContext.getRiskReason();
			ruleContext.setRiskReason(prevRiskReason+""+riskReason+" | ");
		};
        System.out.println("Warning: Energy Throuhput is abnormal for battery");
	}
	
	 @Override
		public int getPriority() {
			// TODO Auto-generated method stub
			return 4;
		}

		@Override
		public int compareTo(Rule o) {
			// TODO Auto-generated method stub
			return 4;
		}
		
	    public static boolean detectFault(List<TelemetryData> data) {
	        List<Double> energyThroughputs = new ArrayList<>();
	        for (TelemetryData ed : data) {
	            energyThroughputs.add(ed.getEnergyThroughput());
	        }

	        List<Double> movingAverages = calculateMovingAverage(energyThroughputs, WINDOW_SIZE);
	        int faultObservation = 0;

	        for (int i = WINDOW_SIZE; i < data.size(); i++) {
	            double actualValue = data.get(i).getEnergyThroughput();
	            double predictedValue = movingAverages.get(i - WINDOW_SIZE);

	            // Check if the deviation from the moving average exceeds the threshold
	            if (Math.abs(actualValue - predictedValue) > threshold) {
	            	faultObservation++;
	            }
	        }
	        
	        //Out of total historic records if 30% of records are faulty then battery is faulty
	        double deviation = faultObservation/data.size();
	        return (deviation*100) > 30;
	    }
	    
	    // Method to compute moving average over a given window size
	    public static List<Double> calculateMovingAverage(List<Double> data, int WINDOW_SIZE) {
	        List<Double> movingAverages = new ArrayList<>();
	        double sum = 0;

	        // Calculate the first window sum
	        for (int i = 0; i < WINDOW_SIZE && i < data.size(); i++) {
	            sum += data.get(i);
	        }

	        movingAverages.add(sum / WINDOW_SIZE);

	        // Slide the window over the rest of the data
	        for (int i = WINDOW_SIZE; i < data.size(); i++) {
	            sum -= data.get(i - WINDOW_SIZE);
	            sum += data.get(i);
	            movingAverages.add(sum / WINDOW_SIZE);
	        }

	        return movingAverages;
	    }

}