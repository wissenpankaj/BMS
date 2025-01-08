package com.wissen.bms.ruleengine.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.wissen.bms.common.model.TelemetryData;
import com.wissen.bms.ruleengine.rules.*;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.springframework.stereotype.Service;

@Service
public class TeleRuleEngineService {

	// Process the raw telemetry data and create facts
	public Facts createTelemetryFacts(List<TelemetryData> telemetryDataList) {
		Facts facts = new Facts();
		for (TelemetryData telemetryData : telemetryDataList) {
			facts.put("vehicleId", telemetryData.getVehicleId());
			facts.put("voltage", telemetryData.getVoltage());
			facts.put("temperature", telemetryData.getTemperature());
			facts.put("internalResistance", telemetryData.getInternalResistance());
		}
		return facts;
	}

	// Evaluate facts based on rules
	public Map<Rule, Boolean> evaluateFacts(Facts telemetryFacts) {
		RulesEngine ruleEngine = new DefaultRulesEngine();

		Rule telemetryCriticalRule = new TelemetryCriticalRule();
		Rule telemetryHighRiskRule = new TelemetryHighRiskRule();
		Rule telemetryModerateRule = new TelemetryModerateRule();

		Rules rules = new Rules();
		rules.register(telemetryCriticalRule);
		rules.register(telemetryHighRiskRule);
		rules.register(telemetryModerateRule);

		return ruleEngine.check(rules, telemetryFacts);
	}

	// Get RiskClassifier
	public String getRiskClassifier(Map<Rule, Boolean> riskData) {
		int highestPriority = 0;
		String riskLevel = "No Risk";

		for (Map.Entry<Rule, Boolean> entry : riskData.entrySet()) {
			if (entry.getKey().getPriority() > highestPriority && entry.getValue()) {
				highestPriority = entry.getKey().getPriority();
				riskLevel = entry.getKey().getName();
			}
		}
		return riskLevel;
	}

	// Evaluate risk and generate RuleContext
	public RuleContext evaluateRisk(List<TelemetryData> telemetryDataList) {
		RuleContext ruleContext = new RuleContext();
		try {
			RulesEngine rulesEngine = new DefaultRulesEngine();
			int lastIndex = telemetryDataList.size() - 1;
			ruleContext.setVehicleId(telemetryDataList.get(lastIndex).getVehicleId());
			ruleContext.setBatterId(telemetryDataList.get(lastIndex).getBatteryId());

			// Define the rules
			ChargingTimeRule chargingTimeRule = new ChargingTimeRule(telemetryDataList, ruleContext);
			CurrentDeviationRule currentDeviationRule = new CurrentDeviationRule(telemetryDataList, ruleContext);
			CycleCountRule cycleCountRule = new CycleCountRule(telemetryDataList, ruleContext);
			EnergyThroughputRule energyThroughputRule = new EnergyThroughputRule(telemetryDataList, ruleContext);
			SOCDeviationRule socDeviationRule = new SOCDeviationRule(telemetryDataList, ruleContext);
			SOHDeviationRule sohDeviationRule = new SOHDeviationRule(telemetryDataList, ruleContext);
			TemperatureSpikeRule temperatureSpikeRule = new TemperatureSpikeRule(telemetryDataList, ruleContext);
			VoltageDeviationRule voltageDropRule = new VoltageDeviationRule(telemetryDataList, ruleContext);
			RiskClassificationRule riskClassificationRule = new RiskClassificationRule(ruleContext);

			Rules rules = new Rules();
			rules.register(chargingTimeRule);
			rules.register(currentDeviationRule);
			rules.register(cycleCountRule);
			rules.register(energyThroughputRule);
			rules.register(socDeviationRule);
			rules.register(sohDeviationRule);
			rules.register(temperatureSpikeRule);
			rules.register(voltageDropRule);
			rules.register(riskClassificationRule);

			rulesEngine.fire(rules, new Facts());

			System.out.println("Risk Level: " + ruleContext);
			System.out.println("********************************************");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ruleContext;
	}

	// Process telemetry data and return RuleContext
	public RuleContext processTelemetryData(List<TelemetryData> telemetryDataList) throws IllegalAccessException, InvocationTargetException {
		Facts telemetryFacts = createTelemetryFacts(telemetryDataList);
		Map<Rule, Boolean> riskData = evaluateFacts(telemetryFacts);
		String riskClassifier = getRiskClassifier(riskData);

		RuleContext ruleContext = evaluateRisk(telemetryDataList);
		ruleContext.setRiskReason("Risk: " + riskClassifier);

		return ruleContext;
	}
}
