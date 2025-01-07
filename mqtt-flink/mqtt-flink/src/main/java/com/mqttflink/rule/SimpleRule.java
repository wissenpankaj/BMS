package com.mqttflink.rule;

import com.mqttflink.model.TelemetryData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleRule implements RuleModel{
    private Predicate<TelemetryData> condition;
    private String action;

    public SimpleRule(){}

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

    @Override
    public List<String> evaluateBatch(List<TelemetryData> batch) {
        System.out.println("inside Rule" + batch.size());
        return batch.stream().map(TelemetryData::getBatteryId).collect(Collectors.toList());
    }
}
