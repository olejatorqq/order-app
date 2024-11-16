package com.taskmanagment.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanagment.userservice.config.SecurityConfig;
import com.taskmanagment.userservice.dto.UserDTO;
import com.taskmanagment.userservice.model.User;
import com.taskmanagment.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Отключаем фильтры безопасности
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void registerUser_Success() throws Exception {
        UserDTO userDTO = UserDTO.builder()
                .username("testuser")
                .email("testuser@example.com")
                .password("securepassword")
                .build();

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("testuser@example.com")
                .password("hashedpassword")
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(userService.registerUser(Mockito.any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"))
                .andExpect(jsonPath("$.createdAt").exists());
    }
}
