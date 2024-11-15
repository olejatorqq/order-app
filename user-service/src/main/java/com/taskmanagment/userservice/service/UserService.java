package com.taskmanagment.userservice;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    public User registerUser(User user) {
        User savedUser = userRepository.save(user);

        // Отправка сообщения в RabbitMQ о регистрации нового пользователя
        rabbitTemplate.convertAndSend("userExchange", "user.registered", savedUser);

        return savedUser;
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}