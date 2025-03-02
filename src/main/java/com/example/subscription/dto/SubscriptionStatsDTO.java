package com.example.subscription.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionStatsDTO {
	@NotBlank(message = "Subscription name is required")
    @Size(min = 3, max = 100, message = "Subscription name must be between 3 and 100 characters")
    private String subscriptionName;

    @NotNull(message = "Subscription count is required")
    @Min(value = 0, message = "Subscription count cannot be negative")
    private Long count;
}
