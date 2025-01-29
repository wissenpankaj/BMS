package com.BatteryInventory.repository;

import com.BatteryInventory.model.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatteryRepository extends JpaRepository<Battery, Long> {

    // Find by batteryId (unique field)
    Battery findByBatteryId(String batteryId);

    // Example custom query for availability grouping
    @Query("SELECT b.type as type, b.status as status, COUNT(b) as totalCount " +
            "FROM Battery b GROUP BY b.type, b.status")
    List<BatteryAvailabilityProjection> findBatteryAvailability();

    interface BatteryAvailabilityProjection {
        String getType();
        String getStatus();
        long getTotalCount();
    }
}
