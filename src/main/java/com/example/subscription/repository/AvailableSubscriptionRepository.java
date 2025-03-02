package com.example.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.subscription.model.AvailableSubscription;

@Repository
public interface AvailableSubscriptionRepository extends JpaRepository<AvailableSubscription, Long>{

}
