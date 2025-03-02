package com.example.subscription.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {
	@NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Available Subscription ID is required")
    private Long availableSubscriptionId;
}
