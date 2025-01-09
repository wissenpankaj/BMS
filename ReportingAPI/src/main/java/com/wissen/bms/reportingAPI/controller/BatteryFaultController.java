package com.wissen.bms.reportingAPI.controller;

import java.util.List;

import com.wissen.bms.reportingAPI.service.BatteryFaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wissen.bms.common.model.BatteryFault;

@RestController
@RequestMapping("api/batteries")
public class BatteryFaultController {
	
	@Autowired
	private BatteryFaultService batteryFaultService;
	
	@GetMapping("/all")
	public List<BatteryFault> getAllBatteries()
	{
		return batteryFaultService.getAllBatteries();
	}
}
