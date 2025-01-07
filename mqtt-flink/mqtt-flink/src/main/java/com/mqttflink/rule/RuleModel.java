package com.mqttflink.rule;

import com.mqttflink.model.TelemetryData;

import java.util.List;

public interface RuleModel {
    List<String> evaluateBatch(List<TelemetryData> batch);

    String evaluateData(TelemetryData telemetryData);
}
