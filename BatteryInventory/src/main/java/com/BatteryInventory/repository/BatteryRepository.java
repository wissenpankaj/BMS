package com.BatteryInventory.repository;

import com.BatteryInventory.model.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface BatteryRepository extends JpaRepository<Battery, Long> {
    @Query("SELECT b FROM Battery b WHERE b.type = :type AND b.status = 'available'")
    List<Battery> findAvailableBatteriesByType(@Param("type") String type);

    List<Battery> findAllByBatteryIdIn(List<String> batteryIds);

    Optional<Battery> findByBatteryId(String batteryId);
}
