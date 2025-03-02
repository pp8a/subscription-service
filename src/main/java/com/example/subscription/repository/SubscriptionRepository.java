package com.example.subscription.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.subscription.dto.SubscriptionStatsDTO;
import com.example.subscription.model.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
	List<Subscription> findByUserId(Long userId);
	Optional<Subscription> findByUserIdAndAvailableSubscriptionId(Long userId, Long availableSubscriptionId);
    boolean existsByUserIdAndAvailableSubscriptionId(Long userId, Long availableSubscriptionId);
    
	@Query("SELECT new com.example.subscription.dto.SubscriptionStatsDTO(s.availableSubscription.name, COUNT(s)) " +
	           "FROM Subscription s GROUP BY s.availableSubscription ORDER BY COUNT(s) DESC")
    List<SubscriptionStatsDTO> findTop3Subscriptions(Pageable pageable);
}
