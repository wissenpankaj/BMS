package com.ruleengine.EVBatteryRuleEngine.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.ruleengine.EVBatteryRuleEngine.dto.TelemetryData;
import com.ruleengine.EVBatteryRuleEngine.rules.ChargingTimeRule;
import com.ruleengine.EVBatteryRuleEngine.rules.CurrentDeviationRule;
import com.ruleengine.EVBatteryRuleEngine.rules.CycleCountRule;
import com.ruleengine.EVBatteryRuleEngine.rules.EnergyThroughputRule;
import com.ruleengine.EVBatteryRuleEngine.rules.RiskClassificationRule;
import com.ruleengine.EVBatteryRuleEngine.rules.RuleContext;
import com.ruleengine.EVBatteryRuleEngine.rules.SOCDeviationRule;
import com.ruleengine.EVBatteryRuleEngine.rules.SOHDeviationRule;
import com.ruleengine.EVBatteryRuleEngine.rules.TelemetryCriticalRule;
import com.ruleengine.EVBatteryRuleEngine.rules.TelemetryHighRiskRule;
import com.ruleengine.EVBatteryRuleEngine.rules.TelemetryModerateRule;
import com.ruleengine.EVBatteryRuleEngine.rules.TemperatureSpikeRule;
import com.ruleengine.EVBatteryRuleEngine.rules.VoltageDeviationRule;

@Service
public class TeleRuleEngineService {

	@Value("${influxdb.org}")
	private String influxDbOrg;

	@Value("${influxdb.bucket}")
	private String influxDbBucket;

	@Autowired
	private InfluxDBService influxDBService;

	public List<FluxTable> queryData(String batteryId) {
		influxDBService.writeFaultAlert("", "", "", "", 1);
		return influxDBService.queryData(influxDbBucket, influxDbOrg, batteryId);
	}

	// Process the raw telemetry data and create a fact
	public Facts createTelemetryFact(TelemetryData telemetryData) {
		Facts facts = new Facts();

		facts.put("vehicleId", telemetryData.getVehicleId());
		facts.put("voltage", telemetryData.getVoltage());
		facts.put("temperature", telemetryData.getTemperature());
		facts.put("internalResistance", telemetryData.getInternalResistance());
		return facts;
	}

	// Evaluate fact based on rules
	Map<Rule, Boolean> evaluateFact(Facts telemetryFact) {
		// Create the rule engine
		RulesEngine ruleEngine = new DefaultRulesEngine();

		// Create and add the rule
		Rule telemetryCriticalRule = new TelemetryCriticalRule();
		Rule telemetryHighRiskRule = new TelemetryHighRiskRule();
		Rule telemetryModerateRule = new TelemetryModerateRule();

		Rules rules = new Rules();
		rules.register(telemetryCriticalRule);
		rules.register(telemetryHighRiskRule);
		rules.register(telemetryModerateRule);

		// Evaluate the rules
		Map<Rule, Boolean> result = ruleEngine.check(rules, telemetryFact);

		return result;
	}

	// get RiskClassifier
	String getRiskClassifier(Map<Rule, Boolean> riskData) {
		int highestPriority = 0;
		String riskLevel = "No Risk";

		for (Map.Entry<Rule, Boolean> entry : riskData.entrySet()) {
			System.out.print("Risk Level: " + entry.getKey().getName() + " ");
			System.out.print("Has Risk: " + entry.getValue() + " ");
			System.out.print("Priority: " + entry.getKey().getPriority() + " ");
			System.out.println();

			if (entry.getKey().getPriority() > highestPriority && entry.getValue()) {
				highestPriority = entry.getKey().getPriority();
				riskLevel = entry.getKey().getName();
			}
		}
		return riskLevel;
	}

	public TelemetryData processTelemetryData(TelemetryData telemetryData)
			throws IllegalAccessException, InvocationTargetException {

		List<FluxTable> historicTeleDataResult = queryData(telemetryData.getBatteryId()); // get Historic data based on
																							// the batteryId

		influxDBService.writeData(telemetryData);
		influxDBService.writeFaultAlert("", "", "", "", 1);

		if (historicTeleDataResult.size() == 0) { // If Historic data is not available then calculate risk for received
			// telemetry data
			// Create the facts
			Facts telemetryFact = createTelemetryFact(telemetryData);
			// Evaluate fact
			Map<Rule, Boolean> riskData = evaluateFact(telemetryFact);
			// Get Risk classifier
			String riskClassifier = getRiskClassifier(riskData);

			telemetryData.setRiskLevel(riskClassifier);
			System.err.println("Risk Level " + riskClassifier);
		} else {

			List<TelemetryData> historicList = new LinkedList<>();
			// Process the FluxTable results
			for (FluxTable table : historicTeleDataResult) {

				for (FluxRecord record : table.getRecords()) {

					TelemetryData historicTeleData = new TelemetryData();
					// Copy fluxRecord to historicTeleData object
					BeanUtils.populate(historicTeleData, record.getValues());

					historicList.add(historicTeleData);

				}
			}
			int endIndex = historicList.size();
			historicList.add(endIndex, telemetryData);
			evaluateRisk(historicList);
		}
		return telemetryData;
	}

	void evaluateRisk(List<TelemetryData> historicTeleData) {
		List<TelemetryData> historicTeleDataResult = new ArrayList<>();
		try {
			RulesEngine rulesEngine = new DefaultRulesEngine();
			int lastIndex = historicTeleData.size()-1;
					RuleContext ruleContext = new RuleContext();
					ruleContext.setVehicleId(historicTeleData.get(lastIndex).getVehicleId());
					ruleContext.setBatterId(historicTeleData.get(lastIndex).getBatteryId());
 
										
					ChargingTimeRule chargingTimeRule = new ChargingTimeRule(historicTeleData, ruleContext);
					CurrentDeviationRule currentDeviationRule = new CurrentDeviationRule(historicTeleDataResult, ruleContext);
					CycleCountRule cycleCountRule = new CycleCountRule( historicTeleDataResult,
							ruleContext);
					EnergyThroughputRule energyThroughputRule = new EnergyThroughputRule(historicTeleDataResult, ruleContext);
					SOCDeviationRule socDeviationRule = new SOCDeviationRule(historicTeleDataResult, ruleContext);
					SOHDeviationRule sohDeviationRule = new SOHDeviationRule(historicTeleDataResult, ruleContext);
					TemperatureSpikeRule temperatureSpikeRule = new TemperatureSpikeRule( historicTeleDataResult,
							ruleContext);
					VoltageDeviationRule voltageDropRule = new VoltageDeviationRule(historicTeleData, ruleContext);

					
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

					// Execute rules
					rulesEngine.fire(rules, new Facts());
					
					System.out.println("Risk Level:        " + ruleContext);
					System.out.println("********************************************");

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}