package com.battery_management.service.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battery_management.model.Vehicle;
import com.battery_management.service.VehicleService;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

@Service
public class VehicleServiceImpl implements VehicleService
{
	@Autowired
    private InfluxDBClient influxDBClient;
	
	@Override
	public List<Vehicle> getAllVehicles() {
		// TODO Auto-generated method stub
		
		String query = "from(bucket: \"Vehicle\")\r\n" +
                "|> range(start: -30d) " +
                "|> filter(fn: (r) => r._measurement == \"vehicle\")";
		QueryApi queryApi = influxDBClient.getQueryApi();
		
        List<FluxTable> tables = queryApi.query(query);
        List<Vehicle> vehicles = new ArrayList<>();
        
        for(FluxTable table: tables)
        {
        	for(FluxRecord record : table.getRecords())
        	{
        		String id = (String) record.getValueByKey("Id"); // Tag: Id
                String name = (String) record.getValueByKey("name"); // Tag: name

                // Handle fields from _field/_value columns
                String field = (String) record.getValueByKey("_field");
                String value = (String) record.getValue();
                
                Vehicle vehicle = vehicles.stream()
                        .filter(v -> v.getId().equals(id) && v.getName().equals(name))
                        .findFirst()
                        .orElseGet(() -> {
                            Vehicle newVehicle = new Vehicle();
                            newVehicle.setId(id);
                            newVehicle.setName(name);
                            vehicles.add(newVehicle);
                            return newVehicle;
                        });
                
                if ("Status".equals(field)) {
                    vehicle.setStatus(value);
                } else if ("Issues".equals(field)) {
                    vehicle.setIssues(value);
                }

                // Set timestamp (once per record)
                if (vehicle.getTime() == null) {
                    vehicle.setTime(record.getTime().toString());
                }
        	}
        }
        
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }
        
        return vehicles;
	}

	@Override
	public List<Vehicle> getActiveVehicles() {
		// TODO Auto-generated method stub
		String query = """
	            from(bucket: "vehicle_data")
	            |> range(start: -7d)
	            |> filter(fn: (r) => r["Status"] == "Active")
	        """;
	        QueryApi queryApi = influxDBClient.getQueryApi();
	        
	        List<Vehicle> active_vehicles = queryApi.query(query, Vehicle.class);
	        return active_vehicles;
	}

	@Override
	public List<Vehicle> getVehiclesWithIssues() {
		// TODO Auto-generated method stub
		String query = """
	            from(bucket: "vehicle_data")
	            |> range(start: -7d)
	            |> filter(fn: (r) => r["Issues"] != "None")
	        """;
        QueryApi queryApi = influxDBClient.getQueryApi();
        
        List<Vehicle> vehicles_with_issue = queryApi.query(query, Vehicle.class);
        return vehicles_with_issue;
	}
}
