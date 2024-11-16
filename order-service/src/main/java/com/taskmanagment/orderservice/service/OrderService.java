package com.taskmanagment.orderservice.service;

import com.taskmanagment.orderservice.model.Order;
import com.taskmanagment.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);

        // Отправка сообщения в RabbitMQ о создании нового заказа
        rabbitTemplate.convertAndSend("orderExchange", "order.created", savedOrder);

        return savedOrder;
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}