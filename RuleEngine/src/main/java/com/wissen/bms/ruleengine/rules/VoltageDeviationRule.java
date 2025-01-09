package com.wissen.bms.ruleengine.rules;

import java.util.List;

import com.wissen.bms.common.model.TelemetryData;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;



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
		boolean isDropping = isVoltageDropping(previousData);
		boolean isFluctuating = isVoltageFluctuating(previousData);
		boolean isTooLow = isVoltageTooLow(previousData);
		return isDropping || isFluctuating || isTooLow;
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
    public static boolean isVoltageDropping(List<TelemetryData> data) {
        if (data.size() < 2) {
            return false;  // Can't detect a trend with fewer than 2 data points
        }
        int faultObservation = 0;
        // Check for sudden voltage drops
        for (int i = 1; i < data.size(); i++) {
            double prevVoltage = data.get(i - 1).getVoltage();
            double currentVoltage = data.get(i).getVoltage();

            // If the voltage drops significantly between two consecutive readings, it's a warning
            if (prevVoltage - currentVoltage > VOLTAGE_DROP_THRESHOLD) {
                System.out.println("Voltage drop detected between timestamps " +
                        data.get(i - 1).getTime() + " and " + data.get(i).getTime());
                faultObservation++;
            }
        }
        //Out of total historic records if 30% of records are faulty then battery is faulty
        double deviation = faultObservation/ data.size();
        return (deviation*100) > 30;
    }

    // Method to check for voltage fluctuation
    public static boolean isVoltageFluctuating(List<TelemetryData> data) {
    	 int faultObservation = 0;
        for (int i = 1; i < data.size(); i++) {
            double prevVoltage = data.get(i - 1).getVoltage();
            double currentVoltage = data.get(i).getVoltage();

            // If fluctuation between readings exceeds the allowed threshold
            if (Math.abs(prevVoltage - currentVoltage) > MAX_VOLTAGE_FLUCTUATION) {
                System.out.println("Voltage fluctuation detected between timestamps " +
                        data.get(i - 1).getTime() + " and " + data.get(i).getTime());
                faultObservation++;
            }
        }
      //Out of total historic records if 30% of records are faulty then battery is faulty
        double deviation = (Math.abs(data.size() - faultObservation) / data.size());
        return (deviation*100) > 30;
    }

    // Method to check if the battery is below an acceptable voltage
    public static boolean isVoltageTooLow(List<TelemetryData> data) {
    	int faultObservation = 0;
        for (TelemetryData telemetryData : data) {
            if (telemetryData.getVoltage() < VOLTAGE_THRESHOLD_LOW) {
                System.out.println("Battery voltage is too low at timestamp " + telemetryData.getTime());
                faultObservation++;
            }
        }
      //Out of total historic records if 30% of records are faulty then battery is faulty
        double deviation = (Math.abs(data.size() - faultObservation) / data.size());
        return (deviation*100) > 30;
    }


}
