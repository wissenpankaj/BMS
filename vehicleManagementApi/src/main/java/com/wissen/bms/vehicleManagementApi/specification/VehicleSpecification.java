package com.wissen.bms.vehicleManagementApi.specification;

import com.wissen.bms.vehicleManagementApi.model.Vehicle;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class VehicleSpecification {

    public static Specification<Vehicle> getVehicles(
            String vehicleId, String make, String model, String vehicleType, String batteryId) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(vehicleId)) {
                predicates.add(criteriaBuilder.equal(root.get("vehicleId"), vehicleId));
            }
            if (StringUtils.hasText(make)) {
                predicates.add(criteriaBuilder.equal(root.get("make"), make));
            }
            if (StringUtils.hasText(model)) {
                predicates.add(criteriaBuilder.equal(root.get("model"), model));
            }
            if (StringUtils.hasText(vehicleType)) {
                predicates.add(criteriaBuilder.equal(root.get("vehicleType"), vehicleType));
            }
            if (StringUtils.hasText(batteryId)) {
                predicates.add(criteriaBuilder.equal(root.get("batteryId"), batteryId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
