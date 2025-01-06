package com.ruleengine.EVBatteryRuleEngine.rules;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import com.ruleengine.EVBatteryRuleEngine.dto.TelemetryData;

public class VoltageDropRule implements Rule {

	private TelemetryData currentData;
	private TelemetryData previousData;
	private RuleContext ruleContext;

	private static final long TIME_WINDOW_MS = 60000; // 1 minute (time window for trend analysis)

	public VoltageDropRule(TelemetryData currentData, TelemetryData previousData, RuleContext ruleContext) {
		this.currentData = currentData;
		this.previousData = previousData;
		this.ruleContext = ruleContext;
	}

	@Override
	public boolean evaluate(Facts facts) {
		System.out.println("Voltage : ");
		
		Instant previousDateTime = Instant.parse(previousData.getTime());
		long timeDiff = previousDateTime.until(Instant.now(), ChronoUnit.MINUTES);

		if (previousData.getVoltage() != null && currentData.getVoltage() != null) {
			double voltageDrop = (previousData.getVoltage() - currentData.getVoltage()) / previousData.getVoltage();
			return voltageDrop > 0.05 && timeDiff < TIME_WINDOW_MS; // 5% voltage drop
		}
		return false;
	}

	@Override
	public void execute(Facts facts) throws Exception {
		String riskReason = "Warning: Voltage dropped significantly for battery ";
		ruleContext.setVoltageRisk(0.1); // High risk for out of bounds SOC
		if(ruleContext.getRiskReason().equals(null) || ruleContext.getRiskReason().equals("")) {
			ruleContext.setRiskReason(riskReason);
		} 
		else {
			String prevRiskReason = ruleContext.getRiskReason();
			ruleContext.setRiskReason(prevRiskReason+""+riskReason+" | ");
		}
		System.out.println("Warning: Voltage dropped significantly for battery " + currentData.getBatteryId());
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

}
