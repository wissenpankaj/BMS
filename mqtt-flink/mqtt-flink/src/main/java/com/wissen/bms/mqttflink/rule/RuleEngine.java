package com.wissen.bms.mqttflink.rule;

import java.util.ArrayList;
import java.util.List;

public class RuleEngine {
    private List<SimpleRule> rules = new ArrayList<>();

    // Add a rule to the engine
    public void addRule(SimpleRule rule) {
        rules.add(rule);
    }

    // Evaluate all rules on the given telemetry data and return actions
    public List<String> evaluate(TelemetryData data) {
        List<String> actions = new ArrayList<>();
        for (SimpleRule rule : rules) {
            if (rule.evaluate(data)) {
                actions.add(rule.getAction());
            }
        }
        return actions;
    }
}
