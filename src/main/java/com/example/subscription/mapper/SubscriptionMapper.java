package com.example.subscription.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.subscription.dto.SubscriptionDTO;
import com.example.subscription.dto.SubscriptionDetailsDTO;
import com.example.subscription.model.Subscription;

@Mapper(componentModel = "spring", uses = {UserMapper.class, AvailableSubscriptionMapper.class})
public interface SubscriptionMapper {
	@Mapping(target = "userId", source = "user", qualifiedByName = "mapUserToId")
    @Mapping(target = "availableSubscriptionId", source = "availableSubscription", 
    	qualifiedByName = "mapSubscriptionToId")
    SubscriptionDTO toDto(Subscription entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "userId", qualifiedByName = "mapIdToUser")
    @Mapping(target = "availableSubscription", source = "availableSubscriptionId", 
    	qualifiedByName = "mapIdToAvailableSubscription")
    Subscription toEntity(SubscriptionDTO dto);
    
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "subscriptionName", source = "availableSubscription.name")
    SubscriptionDetailsDTO toDetailsDto(Subscription entity);
}
