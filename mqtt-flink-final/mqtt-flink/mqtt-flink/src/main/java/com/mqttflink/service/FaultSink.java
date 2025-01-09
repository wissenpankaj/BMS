package com.mqttflink.service;

import com.ruleengine.EVBatteryRuleEngine.rules.RuleContext;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

class FaultSink implements SinkFunction<RuleContext> {
    @Override
    public void invoke(RuleContext value, Context context) {
        // Mocked fault event write
//        System.out.println("Fault detected: " + value);
    }
}