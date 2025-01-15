package com.wissen.bms.ruleengine.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.wissen.bms.common.model.TelemetryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wissen.bms.ruleengine.service.TeleRuleEngineService;


import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/ruleEngine")
public class TeleRuleEngineController {

	private static final Logger logger = LoggerFactory.getLogger(TeleRuleEngineController.class);

	@Autowired
	private TeleRuleEngineService teleRuleEngineService;

	/**
	 * Endpoint to process a list of telemetry data
	 *
	 * @param telemetryDataList list of telemetry data
	 * @return processed result
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@PostMapping("/processListData")
	public Object evaluateList(@RequestBody List<TelemetryData> telemetryDataList) throws IllegalAccessException, InvocationTargetException {
		logger.info("Received list of telemetry data: {}", telemetryDataList);
		try {
			return teleRuleEngineService.processTelemetryData(telemetryDataList);
		} catch (Exception e) {
			logger.error("Error processing telemetry data list", e);
			throw e;
		}
	}

	/**
	 * Endpoint to process a single telemetry data
	 *
	 * @param telemetryData single telemetry data
	 * @return processed result
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@PostMapping("/processSingleData")
	public Object evaluateSingle(@RequestBody TelemetryData telemetryData) throws IllegalAccessException, InvocationTargetException {
		logger.info("Received single telemetry data: {}", telemetryData);
		try {
			return teleRuleEngineService.processSingleTelemetryData(telemetryData);
		} catch (Exception e) {
			logger.error("Error processing single telemetry data", e);
			throw e;
		}
	}
}