package com.taskmanagment.orderservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long id;
    private Long userId;
    private String product;
    private Integer quantity;
    private Double price;
    private LocalDateTime createdAt;
}