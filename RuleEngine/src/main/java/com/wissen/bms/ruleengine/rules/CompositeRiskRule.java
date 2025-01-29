package com.wissen.bms.ruleengine.rules;

import com.wissen.bms.ruleengine.config.RiskConfig;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

//public class CompositeRiskRule implements Rule {
//
//    @Override
//    public String getName() {
//        return "CompositeRisk";
//    }
//
//    @Override
//    public String getDescription() {
//        return "A composite rule to identify EV faulty battery with varying risk levels.";
//    }
//
//    @Override
//    public boolean evaluate(Facts facts) {
//        Double voltage = (Double) facts.get("voltage");
//        Double temperature = (Double) facts.get("temperature");
//        Double internalResistance = (Double) facts.get("internalResistance");
//        return voltage < 360 || temperature > 55 || internalResistance > 0.05;
//    }
//
//    @Override
//    public void execute(Facts facts) throws Exception {
//        RuleContext ruleContext = facts.get("ruleContext");
//
//        Double voltage = (Double) facts.get("voltage");
//        Double temperature = (Double) facts.get("temperature");
//        Double internalResistance = (Double) facts.get("internalResistance");
//
//        String riskLevel = "None";  // Default value
//
//        if (isCriticalFault(voltage, temperature, internalResistance, ruleContext)) {
//            ruleContext.setRiskLevel("Critical");
//            riskLevel = "Critical";
//        } else if (isHighRiskFault(voltage, temperature, internalResistance, ruleContext)) {
//            ruleContext.setRiskLevel("HighRisk");
//            riskLevel = "HighRisk";
//        } else if (isModerateFault(voltage, temperature, internalResistance, ruleContext)) {
//            ruleContext.setRiskLevel("Moderate");
//            riskLevel = "Moderate";
//        }
//
//        // Define color codes
//        String red = "\033[31m";
//        String yellow = "\033[33m";  // For HighRisk, you can use yellow as a substitute for orange
//        String green = "\033[32m";
//        String reset = "\033[0m";
//
//        // Color based on the risk level
//        String coloredRiskLevel = switch (riskLevel) {
//            case "Critical" -> red + riskLevel + reset;
//            case "HighRisk" -> yellow + riskLevel + reset;
//            case "Moderate" -> green + riskLevel + reset;
//            default -> riskLevel;
//        };
//
//        System.out.println("The battery has risks with reasons: " + String.join(" | ", ruleContext.getRiskReason()));
//        System.out.println("Risk Level: " + coloredRiskLevel);
//    }
//
//    @Override
//    public int compareTo(Rule o) {
//        return 1;
//    }
//
//    @Override
//    public int getPriority() {
//        return 1; // Highest priority
//    }
//
//    private boolean isCriticalFault(double voltage, double temperature, double internalResistance, RuleContext ruleContext) {
//        boolean isFault = false;
//        if (voltage < 320) {
//            ruleContext.addRiskReason("Voltage < 320V");
//            isFault = true;
//        }
//        if (temperature > 65) {
//            ruleContext.addRiskReason("Temperature > 65°C");
//            isFault = true;
//        }
//        if (internalResistance > 0.07) {
//            ruleContext.addRiskReason("Internal Resistance > 0.07Ω");
//            isFault = true;
//        }
//        return isFault;
//    }
//
//    private boolean isHighRiskFault(double voltage, double temperature, double internalResistance, RuleContext ruleContext) {
//        boolean isFault = false;
//        if (voltage < 340) {
//            ruleContext.addRiskReason("Voltage < 340V");
//            isFault = true;
//        }
//        if (temperature > 60) {
//            ruleContext.addRiskReason("Temperature > 60°C");
//            isFault = true;
//        }
//        if (internalResistance > 0.06) {
//            ruleContext.addRiskReason("Internal Resistance > 0.06Ω");
//            isFault = true;
//        }
//        return isFault;
//    }
//
//    private boolean isModerateFault(double voltage, double temperature, double internalResistance, RuleContext ruleContext) {
//        boolean isFault = false;
//        if (voltage < 360) {
//            ruleContext.addRiskReason("Voltage < 360V");
//            isFault = true;
//        }
//        if (temperature > 55) {
//            ruleContext.addRiskReason("Temperature > 55°C");
//            isFault = true;
//        }
//        if (internalResistance > 0.05) {
//            ruleContext.addRiskReason("Internal Resistance > 0.05Ω");
//            isFault = true;
//        }
//        return isFault;
//    }
//}
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CompositeRiskRule implements Rule {

    private RiskConfig riskConfig; // Remove final keyword since Lombok handles it

    @Override
    public String getName() {
        return "CompositeRisk";
    }

    @Override
    public String getDescription() {
        return "A composite rule to identify EV faulty battery with varying risk levels.";
    }

    @Override
    public boolean evaluate(Facts facts) {
        Double voltage = (Double) facts.get("voltage");
        Double temperature = (Double) facts.get("temperature");
        Double internalResistance = (Double) facts.get("internalResistance");
        return voltage < riskConfig.getModerateVoltage() || temperature > riskConfig.getModerateTemperature() || internalResistance > riskConfig.getModerateInternalResistance();
    }

    @Override
    public int compareTo(Rule o) {
        return 1;
    }

    @Override
    public int getPriority() {
        return 1; // Highest priority
    }

    @Override
    public void execute(Facts facts) throws Exception {
        RuleContext ruleContext = facts.get("ruleContext");

        Double voltage = (Double) facts.get("voltage");
        Double temperature = (Double) facts.get("temperature");
        Double internalResistance = (Double) facts.get("internalResistance");

        String riskLevel = "None";  // Default value

        if (isCriticalFault(voltage, temperature, internalResistance, ruleContext)) {
            ruleContext.setRiskLevel("Critical");
            riskLevel = "Critical";
        } else if (isHighRiskFault(voltage, temperature, internalResistance, ruleContext)) {
            ruleContext.setRiskLevel("HighRisk");
            riskLevel = "HighRisk";
        } else if (isModerateFault(voltage, temperature, internalResistance, ruleContext)) {
            ruleContext.setRiskLevel("Moderate");
            riskLevel = "Moderate";
        }

        // Log the output using a logging framework like SLF4J
        System.out.println("The battery has risks with reasons: " + String.join(" | ", ruleContext.getRiskReason()));
        System.out.println("Risk Level: " + riskLevel);
    }

    private boolean isCriticalFault(double voltage, double temperature, double internalResistance, RuleContext ruleContext) {
        return voltage < riskConfig.getCriticalVoltage() || temperature > riskConfig.getCriticalTemperature() || internalResistance > riskConfig.getCriticalInternalResistance();
    }

    private boolean isHighRiskFault(double voltage, double temperature, double internalResistance, RuleContext ruleContext) {
        return voltage < riskConfig.getHighRiskVoltage() || temperature > riskConfig.getHighRiskTemperature() || internalResistance > riskConfig.getHighRiskInternalResistance();
    }

    private boolean isModerateFault(double voltage, double temperature, double internalResistance, RuleContext ruleContext) {
        return voltage < riskConfig.getModerateVoltage() || temperature > riskConfig.getModerateTemperature() || internalResistance > riskConfig.getModerateInternalResistance();
    }
}
