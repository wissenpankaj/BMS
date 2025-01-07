package com.ruleengine.EVBatteryRuleEngine.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.influxdb.query.FluxTable;
import com.ruleengine.EVBatteryRuleEngine.dto.TelemetryData;
import com.ruleengine.EVBatteryRuleEngine.service.InfluxDBService;
import com.ruleengine.EVBatteryRuleEngine.service.TeleRuleEngineService;

@RequestMapping("ruleEngine")
@Controller
public class TeleRuleEngineController {
		
	@Autowired
	TeleRuleEngineService teleRuleEngineService;
	
	@Autowired
	InfluxDBService influxDBService;
	
	@GetMapping("/processData")
	@ResponseBody
	public Object evaluate() throws IllegalAccessException, InvocationTargetException {		
		String telemetryDataStr = createTelemetryData();
		Gson gson = new Gson();
		TelemetryData telemetryData = gson.fromJson(telemetryDataStr, TelemetryData.class); 
		System.out.print("telemetryData "+telemetryData);
		return teleRuleEngineService.processTelemetryData(telemetryData);	
	}

	
	@GetMapping("/influxData")
	@ResponseBody
	public Object processInfluxData() {		
		String telemetryDataStr = createTelemetryData();
		Gson gson = new Gson();
		TelemetryData telemetryData = gson.fromJson(telemetryDataStr, TelemetryData.class); 		

	    influxDBService.writeData(telemetryData);	
	    
	    Object result = teleRuleEngineService.queryData("B8");
	    
	    System.out.print(result);
	    return result;
	}
	
	String createTelemetryData() {
			Random random = new Random();
			String vehicleId = "EV" + random.nextInt(10); // Simulate 10 different EVs
			String batteryId = "B" + random.nextInt(10); // Simulate 10 different EVs
		    return String.format(
		            "{\"vehicleId\": \"%s\", \"batteryId\": \"%s\", \"voltage\": %.2f, \"current\": %.2f, \"soc\": %.2f, " +
		                    "\"temperature\": %.2f, \"internalResistance\": %.3f, \"cycleCount\": %d, " +
		                    "\"energyThroughput\": %.2f, \"chargingTime\": %.2f}",
		            vehicleId,batteryId,
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