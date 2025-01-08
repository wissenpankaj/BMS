package com.wissen.bms.ruleengine.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

public class TelemetryHighRiskRule implements Rule {

	@Override
    public String getName() {
        return "HighRisk";
    }

    @Override
    public String getDescription() {
        return "An HighRisk rule to identify EV faulty battery.";
    }

    @Override
    public boolean evaluate(Facts facts) {
        Double voltage = (Double) facts.get("voltage");
        Double temperature = (Double) facts.get("temperature");
        Double internalResistance = (Double) facts.get("internalResistance");
        
        return isHighRiskFault(voltage, temperature, internalResistance);
    }

    @Override
    public void execute(Facts facts) throws Exception {
        System.out.println("The battery is faulty.");
    }

	@Override
	public int compareTo(Rule o) {
		// TODO Auto-generated method stub
		return 2;
	}
	
    @Override
    public int getPriority() {
        return 2; // Higher priority
    }

		
	private static boolean isHighRiskFault(double voltage, double temperature, double internalResistance) {
		return voltage < 340 || temperature > 60 || internalResistance > 0.06;
	}

}
