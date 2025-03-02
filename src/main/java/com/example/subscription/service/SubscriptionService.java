package com.example.subscription.service;

import java.util.List;

import com.example.subscription.dto.AvailableSubscriptionDTO;
import com.example.subscription.dto.SubscriptionDetailsDTO;
import com.example.subscription.dto.SubscriptionStatsDTO;

public interface SubscriptionService {
	void addSubscription(Long userId, Long subscriptionId);
    List<SubscriptionDetailsDTO> getUserSubscriptions(Long userId);
    void removeSubscription(Long userId, Long subscriptionId);
    List<AvailableSubscriptionDTO> getAllAvailableSubscriptions();
    List<SubscriptionStatsDTO> getTop3PopularSubscriptions();
}
