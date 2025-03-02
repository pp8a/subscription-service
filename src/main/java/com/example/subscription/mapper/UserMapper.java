package com.example.subscription.mapper;

import com.example.subscription.dto.UserDTO;
import com.example.subscription.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserDTO toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserDTO dto, @MappingTarget User entity);
    
    @Named("mapUserToId")
    default Long mapUserToId(User user) {
        return user != null ? user.getId() : null;
    }

    @Named("mapIdToUser")
    default User mapIdToUser(Long userId) {
        return userId != null ? new User(userId, null, null) : null;
    }
}
