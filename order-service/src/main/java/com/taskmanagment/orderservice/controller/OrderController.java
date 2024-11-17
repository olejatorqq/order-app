package com.taskmanagment.orderservice.controller;

import com.taskmanagment.orderservice.dto.OrderDTO;
import com.taskmanagment.orderservice.dto.OrderResponseDTO;
import com.taskmanagment.orderservice.model.Order;
import com.taskmanagment.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.context.support.DefaultMessageSourceResolvable;


import jakarta.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(400).body(errors);
        }

        try {
            Order createdOrder = orderService.createOrder(orderDTO);
            OrderResponseDTO responseDTO = OrderResponseDTO.builder()
                    .id(createdOrder.getId())
                    .userId(createdOrder.getUserId())
                    .product(createdOrder.getProduct())
                    .quantity(createdOrder.getQuantity())
                    .price(createdOrder.getPrice())
                    .createdAt(createdOrder.getCreatedAt())
                    .build();
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            // Обработка ошибок бизнес-логики
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            // Обработка общих ошибок
            return ResponseEntity.status(500).body("Произошла ошибка при создании заказа.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderById(id);
            if (order == null) {
                return ResponseEntity.status(404).body("Заказ с ID " + id + " не найден.");
            }
            OrderResponseDTO responseDTO = OrderResponseDTO.builder()
                    .id(order.getId())
                    .userId(order.getUserId())
                    .product(order.getProduct())
                    .quantity(order.getQuantity())
                    .price(order.getPrice())
                    .createdAt(order.getCreatedAt())
                    .build();
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Произошла ошибка при получении заказа.");
        }
    }
}