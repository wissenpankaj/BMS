package com.wissen.bms.ruleengine.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

public class TelemetryModerateRule implements Rule {

	@Override
    public String getName() {
        return "Moderate";
    }

    @Override
    public String getDescription() {
        return "An Moderate rule to identify EV faulty battery.";
    }

    @Override
    public boolean evaluate(Facts facts) {
        Double voltage = (Double) facts.get("voltage");
        Double temperature = (Double) facts.get("temperature");
        Double internalResistance = (Double) facts.get("internalResistance");
        
        return isModearteFault(voltage, temperature, internalResistance);
    }

    @Override
    public void execute(Facts facts) throws Exception {
        System.out.println("The battery is faulty.");
    }

	@Override
	public int compareTo(Rule o) {
		// TODO Auto-generated method stub
		return 3;
	}
	
    @Override
    public int getPriority() {
        return 1; // Higher priority
    }

		
	private static boolean isModearteFault(double voltage, double temperature, double internalResistance) {
		return voltage < 360 || temperature > 55 || internalResistance > 0.05;
	}

}

