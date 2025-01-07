package com.ruleengine.EVBatteryRuleEngine.rules;

import java.util.ArrayList;
import java.util.List;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import com.ruleengine.EVBatteryRuleEngine.dto.TelemetryData;

public class SOCDeviationRule implements Rule {
	
    private List<TelemetryData> histTelemetryData;
    private RuleContext ruleContext;

    public SOCDeviationRule(List<TelemetryData> histTelemetryData, RuleContext ruleContext) {
        this.histTelemetryData = histTelemetryData;
        this.ruleContext = ruleContext;
    }
    
    @Override
	public boolean evaluate(Facts facts) {
    	 System.out.println("SOC : ");
    	 return detectFault(histTelemetryData);
	}

	@Override
	public void execute(Facts facts) throws Exception {
		String riskReason = "Warning: SOC is out of bounds for battery ";
		ruleContext.setSocRisk(0.1); //Medium risk for out of bounds SOC
		if(ruleContext.getRiskReason().equals(null) || ruleContext.getRiskReason().equals("")) {
			ruleContext.setRiskReason(riskReason);
		} 
		else {
			String prevRiskReason = ruleContext.getRiskReason();
			ruleContext.setRiskReason(prevRiskReason+""+riskReason+" | ");
		};
        System.out.println("Warning: SOC is out of bounds for battery ");
	}

    
    @Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public int compareTo(Rule o) {
		// TODO Auto-generated method stub
		return 5;
	}
	
    public static boolean detectFault(List<TelemetryData> socData) {
        if (socData == null || socData.size() < 2) {
            return false; // Not enough data to detect faults
        }

        double previousSOC = socData.get(0).getSoc();
        int faultObservation = 0;
        
        for (int i = 1; i < socData.size(); i++) {
        	TelemetryData current = socData.get(i);

            // Calculate the change in SOC (delta SOC)
            double deltaSOC = current.getSoc() - previousSOC;
            boolean isAnomoly = false;
            // If SOC suddenly drops too much or increases unexpectedly, flag it as a fault
            if (Math.abs(deltaSOC) > 20.0) { // Threshold for abnormal change (20% drop or increase)
                System.out.println("Fault detected at timestamp " + current.getTime() + ": " +
                        "SOC change is " + deltaSOC + "%, which is too large.");
                isAnomoly = true; // Fault detected
            }

            // SOC should not stagnate for too long (e.g., under constant discharge or charge)
            if (i > 1) {
                double prevDeltaSOC = socData.get(i - 1).getSoc() - socData.get(i - 2).getSoc();
                if (Math.abs(prevDeltaSOC) < 1.0 && Math.abs(deltaSOC) < 1.0) {
                    System.out.println("Fault detected at timestamp " + current.getTime() + ": " +
                            "SOC is stagnant, no significant change.");
                    isAnomoly = true; // Fault detected
                }
            }
            
            if(isAnomoly) {
            	faultObservation++;
            }

            // Update previous SOC for next iteration
            previousSOC = current.getSoc();
        }

      //Out of total historic records if 30% of records are faulty then battery is faulty
        double deviation = faultObservation/ socData.size();
        return (deviation*100) > 30;
    }

}

