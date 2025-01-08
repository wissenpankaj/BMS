package com.wissen.bms.mqttflink.rule;


import com.wissen.bms.mqttflink.model.TelemetryData;

import java.util.List;

public interface RuleModel {
    List<String> evaluateBatch(List<TelemetryData> batch);
}
