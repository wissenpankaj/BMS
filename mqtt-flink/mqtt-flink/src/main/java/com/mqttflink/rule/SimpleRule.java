package com.mqttflink.rule;

import com.mqttflink.model.TelemetryData;

import java.util.function.Predicate;

public class SimpleRule {
    private Predicate<TelemetryData> condition;
    private String action;

    // Constructor to initialize rule condition and associated action
    public SimpleRule(Predicate<TelemetryData> condition, String action) {
        this.condition = condition;
        this.action = action;
    }

    // Evaluate the condition for the given telemetry data
    public boolean evaluate(TelemetryData data) {
        return condition.test(data);
    }

    // Get the action associated with the rule
    public String getAction() {
        return action;
    }
}
