package com.example.subscription.service;

import com.example.subscription.dto.UserDTO;
import java.util.List;
import java.util.Optional;

public interface UserService {
	UserDTO createUser(UserDTO userDTO);
    Optional<UserDTO> getUserById(Long id);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    List<UserDTO> getAllUsers();
}
