package com.battery_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.battery_management.model.BatteryFault;
import com.battery_management.service.BatteryFaultService;

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
