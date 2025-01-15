package com.wissen.bms.reportingAPI.specification;

import com.wissen.bms.reportingAPI.model.BatteryFaultModel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BatteryFaultSpecification {

    public static Specification<BatteryFaultModel> getBatteryFaults(String faultId, String gps, String vehicleId, String batteryId, String faultReason, String recommendation, String level, String risk, String time) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(faultId)) {
                predicates.add(criteriaBuilder.equal(root.get("faultId"), faultId));
            }

            if (StringUtils.hasText(gps)) {
                predicates.add(criteriaBuilder.equal(root.get("gps"), gps));
            }

            if (StringUtils.hasText(vehicleId)) {
                predicates.add(criteriaBuilder.equal(root.get("vehicleId"), vehicleId));
            }

            if (StringUtils.hasText(batteryId)) {
                predicates.add(criteriaBuilder.equal(root.get("batteryId"), batteryId));
            }

            if (StringUtils.hasText(faultReason)) {
                predicates.add(criteriaBuilder.equal(root.get("faultReason"), faultReason));
            }

            if (StringUtils.hasText(recommendation)) {
                predicates.add(criteriaBuilder.equal(root.get("recommendation"), recommendation));
            }

            if (StringUtils.hasText(level)) {
                predicates.add(criteriaBuilder.equal(root.get("level"), level));
            }

            if (StringUtils.hasText(risk)) {
                predicates.add(criteriaBuilder.equal(root.get("risk"), risk));
            }

            if (StringUtils.hasText(time)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(time, formatter);
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.atTime(23, 59, 59);
                predicates.add(criteriaBuilder.between(root.get("time"), Timestamp.valueOf(startOfDay), Timestamp.valueOf(endOfDay)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
