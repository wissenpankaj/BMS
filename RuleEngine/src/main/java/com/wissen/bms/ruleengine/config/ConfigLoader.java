package com.wissen.bms.ruleengine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.File;
import org.springframework.beans.factory.annotation.Value;

@Component
public class ConfigLoader {
    private RiskConfig riskConfig;
    private final String configFilePath;

    public ConfigLoader(@Value("${config.file.path}") String configFilePath) {
        this.configFilePath = configFilePath;
    }

    @PostConstruct
    public void init() {
        try {
            System.out.println("ConfigLoader instantiated with file path: " + configFilePath);
            ObjectMapper objectMapper = new ObjectMapper();
            this.riskConfig = objectMapper.readValue(new File(configFilePath), RiskConfig.class);
            printConfigValues();
        } catch (IOException e) {
            System.err.println("Failed to load configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public RiskConfig getRiskConfig() {
        return riskConfig;
    }

    public void printConfigValues() {
        if (riskConfig != null) {
            System.out.println("printConfigValues called");
            System.out.println("Critical Voltage: " + riskConfig.getCriticalVoltage());
            System.out.println("Critical Temperature: " + riskConfig.getCriticalTemperature());
            System.out.println("Critical Internal Resistance: " + riskConfig.getCriticalInternalResistance());
            System.out.println("High Risk Voltage: " + riskConfig.getHighRiskVoltage());
            System.out.println("High Risk Temperature: " + riskConfig.getHighRiskTemperature());
            System.out.println("High Risk Internal Resistance: " + riskConfig.getHighRiskInternalResistance());
            System.out.println("Moderate Voltage: " + riskConfig.getModerateVoltage());
            System.out.println("Moderate Temperature: " + riskConfig.getModerateTemperature());
            System.out.println("Moderate Internal Resistance: " + riskConfig.getModerateInternalResistance());
        } else {
            System.err.println("RiskConfig is null!");
        }
    }
}
