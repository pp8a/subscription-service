package com.example.subscription.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.subscription.dto.AvailableSubscriptionDTO;
import com.example.subscription.dto.SubscriptionDetailsDTO;
import com.example.subscription.dto.SubscriptionStatsDTO;
import com.example.subscription.exception.custom.EntityNotFoundException;
import com.example.subscription.service.SubscriptionService;

@SpringBootTest
@AutoConfigureMockMvc
class SubscriptionControllerTest {
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubscriptionService subscriptionService;
    
    @TestConfiguration
    static class TestConfig {
        @Bean
        SubscriptionService subscriptionService() {
            return Mockito.mock(SubscriptionService.class);
        }
    }
    
    @Test
    @DisplayName("Add subscription - Success (201)")
    void addSubscription_Success() throws Exception {
        Long userId = 1L;
        Long subscriptionId = 2L;

        doNothing().when(subscriptionService).addSubscription(userId, subscriptionId);

        mockMvc.perform(post("/subscriptions/add")
                .param("userId", userId.toString())
                .param("subscriptionId", subscriptionId.toString()))
                .andExpect(status().isCreated())
                .andExpect(content().string("Subscription added successfully for user ID: " + userId));

        verify(subscriptionService, times(1)).addSubscription(userId, subscriptionId);
    }

    @Test
    @DisplayName("Add subscription - User or Subscription not found (404)")
    void addSubscription_NotFound() throws Exception {
        Long userId = 1L;
        Long subscriptionId = 99L;

        doThrow(new EntityNotFoundException("Subscription not found"))
                .when(subscriptionService).addSubscription(userId, subscriptionId);

        mockMvc.perform(post("/subscriptions/add")
                .param("userId", userId.toString())
                .param("subscriptionId", subscriptionId.toString()))
                .andExpect(status().isNotFound());

        verify(subscriptionService, times(1)).addSubscription(userId, subscriptionId);
    }

    @Test
    @DisplayName("Get user subscriptions - Success (200)")
    void getUserSubscriptions_Success() throws Exception {
        Long userId = 1L;
        List<SubscriptionDetailsDTO> subscriptions = List.of(
                new SubscriptionDetailsDTO("John Doe", "Premium Plan"),
                new SubscriptionDetailsDTO("Alice Smith", "Basic Plan")
        );

        when(subscriptionService.getUserSubscriptions(userId)).thenReturn(subscriptions);

        mockMvc.perform(get("/subscriptions/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].userName").value("John Doe"))
                .andExpect(jsonPath("$[0].subscriptionName").value("Premium Plan"));

        verify(subscriptionService, times(1)).getUserSubscriptions(userId);
    }

    @Test
    @DisplayName("Get user subscriptions - User not found (404)")
    void getUserSubscriptions_UserNotFound() throws Exception {
        Long userId = 99L;

        doThrow(new EntityNotFoundException("User not found"))
                .when(subscriptionService).getUserSubscriptions(userId);

        mockMvc.perform(get("/subscriptions/user/{userId}", userId))
                .andExpect(status().isNotFound());

        verify(subscriptionService, times(1)).getUserSubscriptions(userId);
    }

    @Test
    @DisplayName("Remove subscription - Success (204)")
    void removeSubscription_Success() throws Exception {
        Long userId = 1L;
        Long subscriptionId = 2L;

        doNothing().when(subscriptionService).removeSubscription(userId, subscriptionId);

        mockMvc.perform(delete("/subscriptions/remove")
                .param("userId", userId.toString())
                .param("subscriptionId", subscriptionId.toString()))
                .andExpect(status().isNoContent());

        verify(subscriptionService, times(1)).removeSubscription(userId, subscriptionId);
    }

    @Test
    @DisplayName("Remove subscription - Subscription not found (404)")
    void removeSubscription_NotFound() throws Exception {
        Long userId = 1L;
        Long subscriptionId = 99L;

        doThrow(new EntityNotFoundException("Subscription not found"))
                .when(subscriptionService).removeSubscription(userId, subscriptionId);

        mockMvc.perform(delete("/subscriptions/remove")
                .param("userId", userId.toString())
                .param("subscriptionId", subscriptionId.toString()))
                .andExpect(status().isNotFound());

        verify(subscriptionService, times(1)).removeSubscription(userId, subscriptionId);
    }

    @Test
    @DisplayName("Get all available subscriptions - Success (200)")
    void getAllAvailableSubscriptions_Success() throws Exception {
        List<AvailableSubscriptionDTO> availableSubscriptions = List.of(
                new AvailableSubscriptionDTO("Premium Plan"),
                new AvailableSubscriptionDTO("Basic Plan")
        );

        when(subscriptionService.getAllAvailableSubscriptions()).thenReturn(availableSubscriptions);

        mockMvc.perform(get("/subscriptions/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Premium Plan"))
                .andExpect(jsonPath("$[1].name").value("Basic Plan"));

        verify(subscriptionService, times(1)).getAllAvailableSubscriptions();
    }

    @Test
    @DisplayName("Get top 3 popular subscriptions - Success (200)")
    void getTop3PopularSubscriptions_Success() throws Exception {
        List<SubscriptionStatsDTO> topSubscriptions = List.of(
                new SubscriptionStatsDTO("Premium Plan", 10L),
                new SubscriptionStatsDTO("Basic Plan", 8L),
                new SubscriptionStatsDTO("Standard Plan", 5L)
        );

        when(subscriptionService.getTop3PopularSubscriptions()).thenReturn(topSubscriptions);

        mockMvc.perform(get("/subscriptions/top3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].subscriptionName").value("Premium Plan"))
                .andExpect(jsonPath("$[1].subscriptionName").value("Basic Plan"))
                .andExpect(jsonPath("$[2].subscriptionName").value("Standard Plan"));

        verify(subscriptionService, times(1)).getTop3PopularSubscriptions();
    }
}