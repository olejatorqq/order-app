package com.taskmanagment.orderservice.service;

import com.taskmanagment.orderservice.config.RabbitMQConfig;
import com.taskmanagment.orderservice.dto.OrderDTO;
import com.taskmanagment.orderservice.model.Order;
import com.taskmanagment.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    public Order createOrder(OrderDTO orderDTO) {
        // Валидация логики бизнес-процессов (например, проверка существования пользователя)
        // Добавить вызов user-service для проверки, существует ли пользователь с userId

        // Создание объекта Order из DTO
        Order order = Order.builder()
                .userId(orderDTO.getUserId())
                .product(orderDTO.getProduct())
                .quantity(orderDTO.getQuantity())
                .price(orderDTO.getPrice())
                .createdAt(LocalDateTime.now())
                .build();

        // Сохранение заказа в базе данных
        Order createdOrder = orderRepository.save(order);

        // Отправка сообщения в очередь RabbitMQ
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE, createdOrder);

        return createdOrder;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}