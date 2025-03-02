package com.example.subscription.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.subscription.dto.AvailableSubscriptionDTO;
import com.example.subscription.dto.SubscriptionDetailsDTO;
import com.example.subscription.dto.SubscriptionStatsDTO;
import com.example.subscription.exception.custom.DuplicateSubscriptionException;
import com.example.subscription.exception.custom.EntityNotFoundException;
import com.example.subscription.mapper.AvailableSubscriptionMapper;
import com.example.subscription.mapper.SubscriptionMapper;
import com.example.subscription.model.AvailableSubscription;
import com.example.subscription.model.Subscription;
import com.example.subscription.model.User;
import com.example.subscription.repository.AvailableSubscriptionRepository;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.repository.UserRepository;
import com.example.subscription.service.SubscriptionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
	private final UserRepository userRepository;
    private final AvailableSubscriptionRepository availableSubscriptionRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final AvailableSubscriptionMapper availableSubscriptionMapper;

	@Override
	@Transactional
	public void addSubscription(Long userId, Long subscriptionId) {
		log.info("Adding subscription: userId={}, subscriptionId={}", userId, subscriptionId);
		
		User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));

        AvailableSubscription availableSubscription = availableSubscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new EntityNotFoundException(
                		"Subscription with ID " + subscriptionId + " not found"));
                    
        boolean exists = subscriptionRepository.existsByUserIdAndAvailableSubscriptionId(userId, subscriptionId);
        if (exists) {
            throw new DuplicateSubscriptionException("User already has this subscription");
        }
        
        subscriptionRepository.save(new Subscription(user, availableSubscription));
	}

	@Override
	@Transactional(readOnly = true)
	public List<SubscriptionDetailsDTO> getUserSubscriptions(Long userId) {
		log.info("Fetching subscriptions for userId={}", userId);
		
		if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User with ID " + userId + " not found");
        }
      
        return subscriptionRepository.findByUserId(userId).stream()
                .map(subscriptionMapper::toDetailsDto)
                .collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void removeSubscription(Long userId, Long subscriptionId) {
		log.info("Removing subscription: userId={}, subscriptionId={}", userId, subscriptionId);
		
		Subscription subscription = subscriptionRepository
				.findByUserIdAndAvailableSubscriptionId(userId, subscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found for user with ID " + userId));

        subscriptionRepository.delete(subscription);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AvailableSubscriptionDTO> getAllAvailableSubscriptions() {
		log.info("Fetching all available subscriptions");
		
		return availableSubscriptionRepository.findAll().stream()
                .map(availableSubscriptionMapper::toDto)
                .collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<SubscriptionStatsDTO> getTop3PopularSubscriptions() {
		log.info("Fetching top 3 popular subscriptions");
		
		Pageable pageable = PageRequest.of(0, 3);
        return subscriptionRepository.findTop3Subscriptions(pageable);
	}
}
