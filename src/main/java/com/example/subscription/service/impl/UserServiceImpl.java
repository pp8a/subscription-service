package com.example.subscription.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.subscription.dto.UserDTO;
import com.example.subscription.exception.custom.EntityNotFoundException;
import com.example.subscription.mapper.UserMapper;
import com.example.subscription.model.User;
import com.example.subscription.repository.UserRepository;
import com.example.subscription.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
    private final UserMapper userMapper;

	@Override
	public UserDTO createUser(UserDTO userDTO) {	
		log.info("Creating new user: name={}, email={}", userDTO.getName(), userDTO.getEmail());
		
		User user = userMapper.toEntity(userDTO);
		return userMapper.toDto(userRepository.save(user));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserDTO> getUserById(Long id) {		
		log.info("Fetching user by ID={}", id);
		
		return userRepository.findById(id).map(userMapper::toDto);
	}

	@Override
	public UserDTO updateUser(Long id, UserDTO userDTO) {
		log.info("Updating user ID={}: newName={}, newEmail={}", id, userDTO.getName(), userDTO.getEmail());
		
		User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
		
		userMapper.updateUserFromDto(userDTO, user);
        return userMapper.toDto(userRepository.save(user));	
	}

	@Override
	public void deleteUser(Long id) {
		log.info("Deleting user ID={}", id);
		
		if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> getAllUsers() {	
		log.info("Fetching all users");
		
		return userRepository.findAll()
				.stream()
				.map(userMapper::toDto)
				.collect(Collectors.toList());
	}
}
