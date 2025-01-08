package com.wissen.bms.mqttflinkintegration.rule;


import com.wissen.bms.mqttflinkintegration.model.TelemetryData;

import java.util.List;

public interface RuleModel {
    List<String> evaluateBatch(List<TelemetryData> batch);
}
