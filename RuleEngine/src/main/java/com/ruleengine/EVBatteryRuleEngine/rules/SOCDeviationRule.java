package com.ruleengine.EVBatteryRuleEngine.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import com.ruleengine.EVBatteryRuleEngine.dto.TelemetryData;

public class SOCDeviationRule implements Rule {

    private TelemetryData currentData;
    private RuleContext ruleContext;

    public SOCDeviationRule(TelemetryData currentData, RuleContext ruleContext) {
        this.currentData = currentData;
        this.ruleContext = ruleContext;
    }
    
    @Override
	public boolean evaluate(Facts facts) {
    	 System.out.println("SOC : ");
    	 return currentData.getSoc() < 10.0 || currentData.getSoc() > 90.0;
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
        System.out.println("Warning: SOC is out of bounds for battery " + currentData.getBatteryId());
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
}

