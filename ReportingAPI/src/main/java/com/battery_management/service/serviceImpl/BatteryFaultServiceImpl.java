package com.battery_management.service.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battery_management.model.BatteryFault;
import com.battery_management.service.BatteryFaultService;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

@Service
public class BatteryFaultServiceImpl implements BatteryFaultService{

	@Autowired
    private InfluxDBClient influxDBClient;

	@Override
	public List<BatteryFault> getAllBatteries() {
		// TODO Auto-generated method stub
		QueryApi queryApi = influxDBClient.getQueryApi();
		
		 String fluxQuery = "from(bucket: \"EV\")\r\n" +
                 "|> range(start: -30d) " +
                 "|> filter(fn: (r) => r._measurement == \"battery_faults\")";

		List<FluxTable> tables = queryApi.query(fluxQuery);
		Map<String, BatteryFault> faultMap = new HashMap<>();
		
		for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
            	
            	String vehicleId = (String) record.getValueByKey("vehicle_id");
                String batteryId = (String) record.getValueByKey("battery_id");
                String gps = (String) record.getValueByKey("gps");
                String key = vehicleId + "-" + batteryId;
                
                // Get or create a BatteryFault object
                BatteryFault fault = faultMap.getOrDefault(key, new BatteryFault());
                fault.setVehicleId(vehicleId);
                fault.setBatteryId(batteryId);
                fault.setGps(gps);
                fault.setTime(record.getTime().toString());
                
                // Map fields
                String field = (String) record.getValueByKey("_field");

                if ("fault_reason".equals(field)) {
                    fault.setFaultReason((String) record.getValue());
                } else if ("recommendation".equals(field)) {
                    fault.setRecommendation((String) record.getValue());
                }
                
                faultMap.put(key, fault);
            }
        }
		List<BatteryFault> faults = new ArrayList<>(faultMap.values());
		
		for (BatteryFault fault : faults) {
            System.out.println(fault);
        }
		return faults;
	}

}
