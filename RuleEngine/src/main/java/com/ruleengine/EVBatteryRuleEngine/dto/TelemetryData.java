package com.ruleengine.EVBatteryRuleEngine.dto;

public class TelemetryData {

	private String batteryId;

	private String vehicleId;

	private Double voltage;

	private Double current;

	public String getBatteryId() {
		return batteryId;
	}

	public void setBatteryId(String batteryId) {
		this.batteryId = batteryId;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Double getVoltage() {
		return voltage;
	}

	public void setVoltage(Double voltage) {
		this.voltage = voltage;
	}

	public Double getCurrent() {
		return current;
	}

	public void setCurrent(Double current) {
		this.current = current;
	}

	public double getSoc() {
		return soc;
	}

	public void setSoc(double soc) {
		this.soc = soc;
	}

	public double getSoh() {
		return soh;
	}

	public void setSoh(double soh) {
		this.soh = soh;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getEnergyThroughput() {
		return energyThroughput;
	}

	public void setEnergyThroughput(double energyThroughput) {
		this.energyThroughput = energyThroughput;
	}

	public double getChargingTime() {
		return chargingTime;
	}

	public void setChargingTime(double chargingTime) {
		this.chargingTime = chargingTime;
	}

	public int getCycleCount() {
		return cycleCount;
	}

	public void setCycleCount(int cycleCount) {
		this.cycleCount = cycleCount;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	private double soc; // State of Charge in %

	private double soh; // State of Health in %

	private double temperature; // Temperature in °C

	private double energyThroughput; // Energy throughput (in Wh)

	private double chargingTime; // Charging time (in minutes)

	private int cycleCount; // Charge cycles

	public double getInternalResistance() {
		return internalResistance;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setInternalResistance(double internalResistance) {
		this.internalResistance = internalResistance;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	@Override
	public String toString() {
		return "TelemetryData [batteryId=" + batteryId + ", vehicleId=" + vehicleId + ", voltage=" + voltage
				+ ", current=" + current + ", soc=" + soc + ", soh=" + soh + ", temperature=" + temperature
				+ ", energyThroughput=" + energyThroughput + ", chargingTime=" + chargingTime + ", cycleCount="
				+ cycleCount + ", gps=" + gps + ", time=" + time + ", internalResistance=" + internalResistance
				+ ", riskLevel=" + riskLevel + "]";
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	private String gps; // GPS coordinates

	private String time;

	private double internalResistance;

	private String riskLevel;

}
