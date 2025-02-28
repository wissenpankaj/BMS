package com.wissen.bms.ruleengine.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

//import com.example.ev_telemetrysb.utils.DatabaseUtil;

//@Rule(priority = 1)
public class TelemetryCriticalRule implements Rule {

    @Override
    public String getName() {
        return "Critical";
    }

    @Override
    public String getDescription() {
        return "An critical rule to identify EV faulty battery.";
    }

    @Override
    public boolean evaluate(Facts facts) {
        Double voltage = (Double) facts.get("voltage");
        Double temperature = (Double) facts.get("temperature");
        Double internalResistance = (Double) facts.get("internalResistance");
        System.out.println("voltage: "+voltage+" temp: "+temperature+"resistence "+internalResistance);
        return isCriticalFault(voltage, temperature, internalResistance);
    }

    @Override
    public void execute(Facts facts) throws Exception {
        System.out.println("The battery is faulty.");
    }

	@Override
	public int compareTo(Rule o) {
		// TODO Auto-generated method stub
		return 1;
	}
	
    @Override
    public int getPriority() {
        return 3; // Higher priority
    }

		
	private static boolean isCriticalFault(double voltage, double temperature, double internalResistance) {
		return voltage < 320 || temperature > 65 || internalResistance > 0.07 ;
	}

}
