package com.battery_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.battery_management.model.BatteryFault;

@Service
public interface BatteryFaultService {
	List<BatteryFault> getAllBatteries();
}
