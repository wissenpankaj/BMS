package com.ruleengine.EVBatteryRuleEngine.rules;

public class RuleContext {

	private String vehicleId = "";
	private String batterId = "";
    private double voltageRisk = 0.0;
    private double temperatureRisk = 0.0;
    private double socRisk = 0.0;
    private String riskLevel = "Low";  // Default risk level
    private String riskReason = "";
    
    public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getBatterId() {
		return batterId;
	}

	public void setBatterId(String batterId) {
		this.batterId = batterId;
	}

	public String getRiskReason() {
		return riskReason;
	}

	public void setRiskReason(String riskReason) {
		this.riskReason = riskReason;
	}

	// Getters and setters
    public double getVoltageRisk() {
        return voltageRisk;
    }

    public void setVoltageRisk(double voltageRisk) {
        this.voltageRisk = voltageRisk;
    }

    public double getTemperatureRisk() {
        return temperatureRisk;
    }

    public void setTemperatureRisk(double temperatureRisk) {
        this.temperatureRisk = temperatureRisk;
    }

    public double getSocRisk() {
        return socRisk;
    }

    public void setSocRisk(double socRisk) {
        this.socRisk = socRisk;
    }

    @Override
	public String toString() {
		return "RuleContext [vehicleId=" + vehicleId + ", batterId=" + batterId + ", voltageRisk=" + voltageRisk
				+ ", temperatureRisk=" + temperatureRisk + ", socRisk=" + socRisk + ", riskLevel=" + riskLevel
				+ ", riskReason=" + riskReason + "]";
	}

	public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
}

