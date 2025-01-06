package com.ruleengine.EVBatteryRuleEngine.rules;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import com.ruleengine.EVBatteryRuleEngine.dto.TelemetryData;

public class TemperatureSpikeRule implements Rule {

	private TelemetryData currentData;
	private TelemetryData previousData;
	private RuleContext ruleContext;

	private static final long TIME_WINDOW_MS = 60000; // 1 minute (time window for trend analysis)

	public TemperatureSpikeRule(TelemetryData currentData, TelemetryData previousData, RuleContext ruleContext) {
		this.currentData = currentData;
		this.previousData = previousData;
		this.ruleContext = ruleContext;
	}

	@Override
	public boolean evaluate(Facts facts) {
		System.out.println("Temperature : ");		
		
		Instant previousDateTime = Instant.parse(previousData.getTime());
		long timeDiff = previousDateTime.until(Instant.now(), ChronoUnit.MINUTES);

		double temperatureChange = Math.abs(currentData.getTemperature() - previousData.getTemperature());
		return temperatureChange > 5.0 && timeDiff < TIME_WINDOW_MS; // 5°C temperature spike
	}

	@Override
	public void execute(Facts facts) throws Exception {
		String riskReason = "Warning: Temperature spiked for battery ";
		ruleContext.setTemperatureRisk(0.05); // Medium risk for out of bounds SOC
		if(ruleContext.getRiskReason().equals(null) || ruleContext.getRiskReason().equals("")) {
			ruleContext.setRiskReason(riskReason);
		} 
		else {
			String prevRiskReason = ruleContext.getRiskReason();
			ruleContext.setRiskReason(prevRiskReason+""+riskReason+" | ");
		}
		System.out.println("Warning: Temperature spiked for battery " + currentData.getBatteryId());
	}

	@Override
	public int compareTo(Rule o) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getPriority() {
		return 1; // Higher priority
	}

}
