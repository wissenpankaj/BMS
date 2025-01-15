package com.wissen.bms.vehicleManagementApi.specification;

import com.wissen.bms.vehicleManagementApi.model.Vehicle;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class VehicleSpecification {

    public static Specification<Vehicle> getVehicles(
            String vehicleId, String make, String model, String vehicleType, String batteryId) {

        return (root, query, criteriaBuilder) -> {
            // Initialize a conjunction (AND)
            var predicates = criteriaBuilder.conjunction();

            // Add predicates based on non-null parameters
            if (StringUtils.hasText(vehicleId)) {
                predicates.getExpressions().add(
                        criteriaBuilder.equal(root.get("vehicleId"), vehicleId)
                );
            }
            if (StringUtils.hasText(make)) {
                predicates.getExpressions().add(
                        criteriaBuilder.equal(root.get("make"), make)
                );
            }
            if (StringUtils.hasText(model)) {
                predicates.getExpressions().add(
                        criteriaBuilder.equal(root.get("model"), model)
                );
            }
            if (StringUtils.hasText(vehicleType)) {
                predicates.getExpressions().add(
                        criteriaBuilder.equal(root.get("vehicleType"), vehicleType)
                );
            }
            if (StringUtils.hasText(batteryId)) {
                predicates.getExpressions().add(
                        criteriaBuilder.equal(root.get("batteryId"), batteryId)
                );
            }

            return predicates;
        };
    }
}
