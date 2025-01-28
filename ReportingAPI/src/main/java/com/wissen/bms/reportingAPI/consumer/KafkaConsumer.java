package com.wissen.bms.reportingAPI.consumer;

import com.wissen.bms.common.model.BatteryFault;
import com.wissen.bms.reportingAPI.model.BatteryFaultModel;
import com.wissen.bms.reportingAPI.repo.BatteryFaultRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@EnableKafka
public class KafkaConsumer {

    @Autowired
    private BatteryFaultRepo batteryFaultRepo;

    private static final String TOPIC_NAME = "faultalert";

    @KafkaListener(topics = TOPIC_NAME, groupId = "vehicle-group")
    public void listen(BatteryFault faultData, Acknowledgment acknowledgment) {
        //System.out.println(faultData);

        try {
            Optional<BatteryFaultModel> existingModel = batteryFaultRepo.findByBatteryId(faultData.getBatteryId());

            BatteryFaultModel model;
            if (existingModel.isPresent()) {
                // If the battery_id exists, update the existing record
                model = existingModel.get();
                if(!model.getFaultReason().equals(faultData.getFaultReason())) {
                    BatteryFaultModel newModel = getBatteryFaultModel(faultData);

                    System.out.println("New Fault reason log data updated for battery_id: " + faultData.getBatteryId());
                    batteryFaultRepo.save(newModel);
                    System.out.println("Data Updated in database successfully.");
                }
            }
            else {
                // Convert the incoming BatteryFault DTO to BatteryFaultModel (which is an entity)
                model = new BatteryFaultModel();
                model.setGps(faultData.getGps());
                model.setVehicleId(faultData.getVehicleId());
                model.setBatteryId(faultData.getBatteryId());
                model.setFaultReason(faultData.getFaultReason());
                model.setRecommendation(faultData.getRecommendation());
                //model.setTime(Timestamp.valueOf(faultData.getTime()));
                model.setLevel(faultData.getLevel());
                model.setRisk(faultData.getRisk());

                System.out.println("Creating new BatteryFaultModel of Battery_Id: " + faultData.getBatteryId());

                batteryFaultRepo.save(model);
                System.out.println("Data saved to database successfully.");
            }
            // Save the fault data into the database
            acknowledgment.acknowledge();
        }
        catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }

    }

    @NotNull
    private static BatteryFaultModel getBatteryFaultModel(BatteryFault faultData) {
        BatteryFaultModel newModel = new BatteryFaultModel();
        newModel.setGps(faultData.getGps());
        newModel.setVehicleId(faultData.getVehicleId());
        newModel.setFaultReason(faultData.getFaultReason());
        newModel.setRecommendation(faultData.getRecommendation());
        //newModel.setTime(Timestamp.valueOf(faultData.getTime()));
        newModel.setLevel(faultData.getLevel());
        newModel.setRisk(faultData.getRisk());
        return newModel;
    }
}
