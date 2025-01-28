package com.wissen.bms.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wissen.bms.notification.entity.UserSubscription;

import java.util.List;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription,String> {
    // Find all subscriptions for a given vehicleId
    List<UserSubscription> findAllByVehicleId(String vehicleId);
}
