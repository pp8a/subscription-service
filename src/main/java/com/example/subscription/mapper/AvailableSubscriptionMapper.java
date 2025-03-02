package com.example.subscription.mapper;

import com.example.subscription.dto.AvailableSubscriptionDTO;
import com.example.subscription.model.AvailableSubscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AvailableSubscriptionMapper {
	AvailableSubscriptionDTO toDto(AvailableSubscription entity);

    @Mapping(target = "id", ignore = true)
    AvailableSubscription toEntity(AvailableSubscriptionDTO dto);
    
    @Named("mapSubscriptionToId")
    default Long mapSubscriptionToId(AvailableSubscription availableSubscription) {
        return availableSubscription != null ? availableSubscription.getId() : null;
    }
    
    @Named("mapIdToAvailableSubscription")
    default AvailableSubscription mapIdToAvailableSubscription(Long subscriptionId) {
        return subscriptionId != null ? new AvailableSubscription(subscriptionId, null) : null;
    }

}
