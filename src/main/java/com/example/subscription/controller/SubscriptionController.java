package com.example.subscription.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.subscription.dto.AvailableSubscriptionDTO;
import com.example.subscription.dto.SubscriptionDetailsDTO;
import com.example.subscription.dto.SubscriptionStatsDTO;
import com.example.subscription.service.SubscriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Subscriptions", description = "API for managing user subscriptions")
public class SubscriptionController {
	private final SubscriptionService subscriptionService;
	
	@Operation(summary = "Add a subscription", 
			description = "Adds a subscription to a user by user ID and subscription ID.")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "201", description = "Subscription added successfully"),
	        @ApiResponse(responseCode = "404", description = "User or Subscription not found"),
	        @ApiResponse(responseCode = "400", description = "User already has this subscription")
	})
	@PostMapping("/add")
    public ResponseEntity<String> addSubscription(@RequestParam Long userId, @RequestParam Long subscriptionId) {
        subscriptionService.addSubscription(userId, subscriptionId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Subscription added successfully for user ID: " + userId);
    }
	
	@Operation(summary = "Get user subscriptions", 
			description = "Retrieves all subscriptions of a user by user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscriptions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionDetailsDTO>> getUserSubscriptions(@PathVariable Long userId) {        
        return ResponseEntity.ok(subscriptionService.getUserSubscriptions(userId));
    }
	
	@Operation(summary = "Remove a subscription", 
			description = "Removes a subscription from a user by user ID and subscription ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Subscription removed successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeSubscription(
            @RequestParam Long userId, 
            @RequestParam Long subscriptionId) {
        subscriptionService.removeSubscription(userId, subscriptionId);
        return ResponseEntity.noContent().build();
	}
    
	@Operation(summary = "Get all available subscriptions", 
			description = "Retrieves a list of all available subscriptions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available subscriptions retrieved successfully")
    })
    @GetMapping("/available")
    public ResponseEntity<List<AvailableSubscriptionDTO>> getAllAvailableSubscriptions() {
        List<AvailableSubscriptionDTO> subscriptions = subscriptionService.getAllAvailableSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }
	
	@Operation(summary = "Get top 3 popular subscriptions", 
			description = "Retrieves the top 3 most popular subscriptions based on the number of users subscribed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top 3 popular subscriptions retrieved successfully")
    })
    @GetMapping("/top3")
    public ResponseEntity<List<SubscriptionStatsDTO>> getTop3PopularSubscriptions() {
        List<SubscriptionStatsDTO> topSubscriptions = subscriptionService.getTop3PopularSubscriptions();
        return ResponseEntity.ok(topSubscriptions);
    }
}
