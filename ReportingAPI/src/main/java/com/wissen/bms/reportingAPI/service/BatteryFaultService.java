package com.wissen.bms.reportingAPI.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wissen.bms.common.model.BatteryFault;

@Service
public interface BatteryFaultService {
	List<BatteryFault> getAllBatteries();
}
