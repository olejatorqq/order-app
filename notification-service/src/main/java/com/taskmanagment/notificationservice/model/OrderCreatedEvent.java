package com.taskmanagment.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long id; // Соответствует Order.id
    private Long userId;
    private String product;
    private Integer quantity;
    private Double price;
    private LocalDateTime createdAt;
}
