package com.wissen.bms.ruleengine.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;


public class RiskClassificationRule implements Rule{

    private RuleContext context;

    public RiskClassificationRule(RuleContext context) {
        this.context = context;
    }
    
    @Override
	public boolean evaluate(Facts facts) {
    	// The classification happens if any risk score is non-zero
        return context.getVoltageRisk() > 0.0 || context.getTemperatureRisk() > 0.0 || context.getSocRisk() > 0.0 ||
        	   context.getChargingTimeRisk() > 0.0 || context.getCurrentRisk() > 0.0 || context.getCycleCountRisk() > 0.0 ||
        	   context.getEnergyThroughputRisk() > 0.0 || context.getSohRisk() > 0.0;
	}

	@Override
	public void execute(Facts facts) throws Exception {
		double totalRisk = context.getVoltageRisk() + context.getTemperatureRisk() + context.getSocRisk() +
		 	   context.getChargingTimeRisk() + context.getCurrentRisk() + context.getCycleCountRisk() +
 	           context.getEnergyThroughputRisk() + context.getSohRisk();
		
		System.out.println("TotalRisk value "+totalRisk);
        if (totalRisk >= 0.8) {
            context.setRiskLevel("High Risk");
        } else if (totalRisk >= 0.5) {
            context.setRiskLevel("Medium Risk");
        } else {
            context.setRiskLevel("Low Risk");
        }	
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

}
