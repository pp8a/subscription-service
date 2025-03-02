package com.example.subscription.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.subscription.dto.UserDTO;
import com.example.subscription.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService; 
    
    @TestConfiguration
    static class TestConfig {
        @Bean
        UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }
    	
	@Test
	@DisplayName("Successfully creates a user (201 Created)")
    void createUser_Success() throws Exception {
        UserDTO userDTO = new UserDTO("John Doe", "john.doe@example.com");
        Mockito.when(userService.createUser(Mockito.any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(userDTO.getName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));

        Mockito.verify(userService, times(1)).createUser(Mockito.any(UserDTO.class));
    }
	
	@Test
    @DisplayName("Fails to create user due to invalid email (400 Bad Request)")
    void createUser_InvalidEmail() throws Exception {
        UserDTO userDTO = new UserDTO("John Doe", "invalid-email");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }
	
	@Test
    @DisplayName("Successfully retrieves user by ID (200 OK)")
    void getUserById_Success() throws Exception {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO("John Doe", "john.doe@example.com");

        when(userService.getUserById(userId)).thenReturn(Optional.of(userDTO));

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @DisplayName("Fails to retrieve user - Not Found (404 Not Found)")
    void getUserById_NotFound() throws Exception {
        Long userId = 99L;
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Successfully updates user (200 OK)")
    void updateUser_Success() throws Exception {
        Long userId = 1L;
        UserDTO updatedUserDTO = new UserDTO("John Smith", "john.smith@example.com");

        when(userService.updateUser(eq(userId), any(UserDTO.class))).thenReturn(updatedUserDTO);

        mockMvc.perform(put("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.email").value("john.smith@example.com"));
    }

    @Test
    @DisplayName("Successfully deletes user (204 No Content)")
    void deleteUser_Success() throws Exception {
        Long userId = 1L;

        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    @DisplayName("Successfully retrieves all users (200 OK)")
    void getAllUsers_Success() throws Exception {
        List<UserDTO> users = List.of(
                new UserDTO("John Doe", "john.doe@example.com"),
                new UserDTO("Alice Smith", "alice.smith@example.com")
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Alice Smith"));
    }
}