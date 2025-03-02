package com.example.subscription.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDetailsDTO {
	@NotBlank(message = "User name is required")
    private String userName;

    @NotBlank(message = "Subscription name is required")
    private String subscriptionName;
}
