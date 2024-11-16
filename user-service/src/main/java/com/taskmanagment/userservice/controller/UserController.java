package com.taskmanagment.userservice.controller;

import com.taskmanagment.userservice.dto.UserDTO;
import com.taskmanagment.userservice.dto.UserResponseDTO;
import com.taskmanagment.userservice.model.User;
import com.taskmanagment.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        User createdUser = userService.registerUser(userDTO);
        UserResponseDTO responseDTO = UserResponseDTO.builder()
                .id(createdUser.getId())
                .username(createdUser.getUsername())
                .email(createdUser.getEmail())
                .createdAt(createdUser.getCreatedAt())
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        if(user != null) {
            UserResponseDTO responseDTO = UserResponseDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .createdAt(user.getCreatedAt())
                    .build();
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
