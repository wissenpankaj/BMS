package com.wissen.bms.ruleengine.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.wissen.bms.common.model.TelemetryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.wissen.bms.ruleengine.service.TeleRuleEngineService;

@RequestMapping("ruleEngine")
@Controller
public class TeleRuleEngineController {

	@Autowired
	TeleRuleEngineService teleRuleEngineService;

	// Process list of telemetry data
	@GetMapping("/processListData")
	@ResponseBody
	public Object evaluateList() throws IllegalAccessException, InvocationTargetException {
		// Create multiple telemetry data samples
		List<TelemetryData> telemetryDataList = new ArrayList<>();
		Gson gson = new Gson();

		// Simulate generating and adding telemetry data to the list
		for (int i = 0; i < 5; i++) { // For example, generating 5 telemetry data samples
			String telemetryDataStr = createTelemetryData();
			TelemetryData telemetryData = gson.fromJson(telemetryDataStr, TelemetryData.class);
			telemetryDataList.add(telemetryData);
		}

		System.out.println("Telemetry Data List: " + telemetryDataList);
		return teleRuleEngineService.processTelemetryData(telemetryDataList);
	}

	// Process single telemetry data
	@GetMapping("/processSingleData")
	@ResponseBody
	public Object evaluateSingle() throws IllegalAccessException, InvocationTargetException {
		// Create a single telemetry data sample
		Gson gson = new Gson();
		String telemetryDataStr = createTelemetryData();
		TelemetryData telemetryData = gson.fromJson(telemetryDataStr, TelemetryData.class);

		System.out.println("Single Telemetry Data: " + telemetryData);
		return teleRuleEngineService.processSingleTelemetryData(telemetryData);
	}

	String createTelemetryData() {
		Random random = new Random();
		String vehicleId = "EV" + random.nextInt(10); // Simulate 10 different EVs
		String batteryId = "B" + random.nextInt(10); // Simulate 10 different batteries
		return String.format(
				"{\"vehicleId\": \"%s\", \"batteryId\": \"%s\", \"voltage\": %.2f, \"current\": %.2f, \"soc\": %.2f, " +
						"\"temperature\": %.2f, \"internalResistance\": %.3f, \"cycleCount\": %d, " +
						"\"energyThroughput\": %.2f, \"chargingTime\": %.2f}",
				vehicleId, batteryId,
				300 + random.nextDouble() * 50, // Voltage
				random.nextDouble() * 100, // Current
				random.nextDouble() * 200, // State of Charge (SoC)
				20 + random.nextDouble() * 40, // Temperature
				random.nextDouble() * 0.1, // Internal resistance
				random.nextInt(1000), // Cycle count
				random.nextDouble() * 500, // Energy throughput
				random.nextDouble() * 10 // Charging time
		);
	}
}
