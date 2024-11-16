package com.taskmanagment.userservice.service;

import com.taskmanagment.userservice.config.RabbitMQConfig;
import com.taskmanagment.userservice.dto.UserDTO;
import com.taskmanagment.userservice.model.User;
import com.taskmanagment.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final BCryptPasswordEncoder passwordEncoder;

    public User registerUser(UserDTO userDTO) {
        // Проверяем, существует ли уже пользователь с таким email
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует.");
        }

        // Создаём объект User из DTO и хешируем пароль
        User user = User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        // Сохраняем нового пользователя
        User createdUser = userRepository.save(user);

        // Отправляем сообщение в очередь после создания пользователя
        rabbitTemplate.convertAndSend(RabbitMQConfig.USER_QUEUE, createdUser);
        return createdUser;
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}