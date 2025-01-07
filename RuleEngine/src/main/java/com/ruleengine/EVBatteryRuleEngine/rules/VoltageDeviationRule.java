package com.ruleengine.EVBatteryRuleEngine.rules;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import com.ruleengine.EVBatteryRuleEngine.dto.TelemetryData;

public class VoltageDeviationRule implements Rule {

	private List<TelemetryData> previousData;
	private RuleContext ruleContext;

	private static final long TIME_WINDOW_MS = 60000; // 1 minute (time window for trend analysis)

	public VoltageDeviationRule(List<TelemetryData> previousData, RuleContext ruleContext) {
		this.previousData = previousData;
		this.ruleContext = ruleContext;
	}

	@Override
	public boolean evaluate(Facts facts) {
		System.out.println("Voltage : ");	
		int isDroppingFaultCount = isVoltageDropping(previousData);
		int isFluctuatingFaultCount = isVoltageFluctuating(previousData);
		int isTooLowFaultCount = isVoltageTooLow(previousData);
		
		//Out of total historic records if 30% of records are faulty then battery is faulty
		int totalHistCount = previousData.size();
        if(totalHistCount > 0) {
	        double deviation = (isDroppingFaultCount + isFluctuatingFaultCount + isTooLowFaultCount)/totalHistCount;
	        return (deviation*100) > 30;
        }
		return false;
	}

	@Override
	public void execute(Facts facts) throws Exception {
		String riskReason = "Warning: Voltage dropped significantly for battery ";
		ruleContext.setVoltageRisk(0.1); // High risk for out of bounds SOC
		if(ruleContext.getRiskReason().equals(null) || ruleContext.getRiskReason().equals("")) {
			ruleContext.setRiskReason(riskReason);
		} 
		else {
			String prevRiskReason = ruleContext.getRiskReason();
			ruleContext.setRiskReason(prevRiskReason+""+riskReason+" | ");
		}
		System.out.println("Warning: Voltage dropped significantly for battery ");
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public int compareTo(Rule o) {
		// TODO Auto-generated method stub
		return 8;
	}
	
    private static final double VOLTAGE_THRESHOLD_LOW = 3.0;  // Minimum acceptable voltage
    private static final double VOLTAGE_DROP_THRESHOLD = 0.5;  // Significant voltage drop threshold
    private static final double MAX_VOLTAGE_FLUCTUATION = 0.2; // Max allowed fluctuation

    // Method to check for voltage drops
    public static int isVoltageDropping(List<TelemetryData> data) {
        if (data.size() < 2) {
            return 0;  // Can't detect a trend with fewer than 2 data points
        }
        int faultObservation = 0;
        // Check for sudden voltage drops
        for (int i = 1; i < data.size(); i++) {
            double prevVoltage = Objects.isNull(data.get(i - 1).getVoltage())?0.0:data.get(i - 1).getVoltage();
            double currentVoltage = Objects.isNull(data.get(i).getVoltage())?0.0:data.get(i).getVoltage();

            // If the voltage drops significantly between two consecutive readings, it's a warning
            if (prevVoltage - currentVoltage > VOLTAGE_DROP_THRESHOLD) {
                System.out.println("Voltage drop detected between timestamps " +
                        data.get(i - 1).getTime() + " and " + data.get(i).getTime());
                faultObservation++;
            }
        }
        System.out.println("faultObservation in VoltageDropping : "+ faultObservation);

 
        return faultObservation;    }
    

    // Method to check for voltage fluctuation
    public static int isVoltageFluctuating(List<TelemetryData> data) {
    	 int faultObservation = 0;
        for (int i = 1; i < data.size(); i++) {
        	double prevVoltage = Objects.isNull(data.get(i - 1).getVoltage())?0.0:data.get(i - 1).getVoltage();
            double currentVoltage = Objects.isNull(data.get(i).getVoltage())?0.0:data.get(i).getVoltage();

            // If fluctuation between readings exceeds the allowed threshold
            if (Math.abs(prevVoltage - currentVoltage) > MAX_VOLTAGE_FLUCTUATION) {
                System.out.println("Voltage fluctuation detected between timestamps " +
                        data.get(i - 1).getTime() + " and " + data.get(i).getTime());
                faultObservation++;
            }
        }
        System.out.println("faultObservation  in VoltageFluctuating : "+ faultObservation);

        return faultObservation;    }

    // Method to check if the battery is below an acceptable voltage
    public static int isVoltageTooLow(List<TelemetryData> data) {
    	int faultObservation = 0;
        for (TelemetryData telemetryData : data) {
        	double voltage = Objects.isNull(telemetryData.getVoltage())?0.0:telemetryData.getVoltage();

            if (voltage < VOLTAGE_THRESHOLD_LOW) {
                System.out.println("Battery voltage is too low at timestamp " + telemetryData.getTime());
                faultObservation++;
            }
        }
        System.out.println("faultObservation   in VoltageTooLow "+ faultObservation);
        return faultObservation;    
    }


}
