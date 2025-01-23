package com.wissen.bms.ruleengine.config;

public class RiskConfig {
    private double criticalVoltage;
    private double criticalTemperature;
    private double criticalInternalResistance;

    private double highRiskVoltage;
    private double highRiskTemperature;
    private double highRiskInternalResistance;

    private double moderateVoltage;
    private double moderateTemperature;
    private double moderateInternalResistance;

    // Getters and setters
    public double getCriticalVoltage() {
        return criticalVoltage;
    }

    public void setCriticalVoltage(double criticalVoltage) {
        this.criticalVoltage = criticalVoltage;
    }

    public double getCriticalTemperature() {
        return criticalTemperature;
    }

    public void setCriticalTemperature(double criticalTemperature) {
        this.criticalTemperature = criticalTemperature;
    }

    public double getCriticalInternalResistance() {
        return criticalInternalResistance;
    }

    public void setCriticalInternalResistance(double criticalInternalResistance) {
        this.criticalInternalResistance = criticalInternalResistance;
    }

    public double getHighRiskVoltage() {
        return highRiskVoltage;
    }

    public void setHighRiskVoltage(double highRiskVoltage) {
        this.highRiskVoltage = highRiskVoltage;
    }

    public double getHighRiskTemperature() {
        return highRiskTemperature;
    }

    public void setHighRiskTemperature(double highRiskTemperature) {
        this.highRiskTemperature = highRiskTemperature;
    }

    public double getHighRiskInternalResistance() {
        return highRiskInternalResistance;
    }

    public void setHighRiskInternalResistance(double highRiskInternalResistance) {
        this.highRiskInternalResistance = highRiskInternalResistance;
    }

    public double getModerateVoltage() {
        return moderateVoltage;
    }

    public void setModerateVoltage(double moderateVoltage) {
        this.moderateVoltage = moderateVoltage;
    }

    public double getModerateTemperature() {
        return moderateTemperature;
    }

    public void setModerateTemperature(double moderateTemperature) {
        this.moderateTemperature = moderateTemperature;
    }

    public double getModerateInternalResistance() {
        return moderateInternalResistance;
    }

    public void setModerateInternalResistance(double moderateInternalResistance) {
        this.moderateInternalResistance = moderateInternalResistance;
    }
}

