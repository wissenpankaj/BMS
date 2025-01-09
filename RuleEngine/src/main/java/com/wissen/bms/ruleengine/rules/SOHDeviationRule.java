package com.wissen.bms.ruleengine.rules;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.wissen.bms.common.model.TelemetryData;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;



public class SOHDeviationRule implements Rule {
	
    public static final double SOH_THRESHOLD = 0.80; // Example threshold for failure
    public static final double MAX_DEGRADATION_RATE = 0.05; // Max SOH degradation rate (per time unit)

    private List<TelemetryData> histTelemetryData;
    private RuleContext ruleContext;

    public SOHDeviationRule(List<TelemetryData> histTelemetryData, RuleContext ruleContext) {
        this.histTelemetryData = histTelemetryData;
        this.ruleContext = ruleContext;
    }
    
    @Override
	public boolean evaluate(Facts facts) {
    	 System.out.println("SOH : ");
    	 return detectFault(histTelemetryData);
	}

	@Override
	public void execute(Facts facts) throws Exception {
		String riskReason = "Warning: SOH is out of bounds for battery ";
		ruleContext.setSohRisk(0.1); //Medium risk for out of bounds SOC
		if(ruleContext.getRiskReason().equals(null) || ruleContext.getRiskReason().equals("")) {
			ruleContext.setRiskReason(riskReason);
		} 
		else {
			String prevRiskReason = ruleContext.getRiskReason();
			ruleContext.setRiskReason(prevRiskReason+""+riskReason+" | ");
		};
        System.out.println("Warning: SOH is out of bounds for battery ");
	}

    
    @Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 6;
	}

	@Override
	public int compareTo(Rule o) {
		// TODO Auto-generated method stub
		return 6;
	}
	
    public static boolean detectFault(List<TelemetryData> sohData) {
    	 int faultObservation = 0;
        // Check for faults based on the threshold and degradation rate
        for (int i = 1; i < sohData.size(); i++) {
        	TelemetryData previous = sohData.get(i - 1);
        	TelemetryData current = sohData.get(i);

            // Check if the current SOH is below the threshold
            if (current.getSoh() < SOH_THRESHOLD) {
                System.out.println("Fault detected! SOH dropped below threshold at timestamp " + current.getTime());
                return true;
            }

            // Check if the degradation rate is too high (based on time and SOH difference)
            double timeDiff = (Instant.parse(previous.getTime()).until(Instant.parse(current.getTime()) , ChronoUnit.SECONDS)) / 1000.0; // in seconds
            double sohDiff = previous.getSoh() - current.getSoh();
            if (sohDiff / timeDiff > MAX_DEGRADATION_RATE) {
                System.out.println("Abnormal degradation detected between timestamps " + previous.getTime() + " and " + current.getTime());
                faultObservation++;
            }
        }
        
      //Out of total historic records if 30% of records are faulty then battery is faulty
        double deviation = faultObservation / sohData.size();
        return (deviation*100) > 30;
    }
}