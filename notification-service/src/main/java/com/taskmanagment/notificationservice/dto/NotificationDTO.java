package com.taskmanagment.notificationservice.dto;

import lombok.*;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    @NotNull(message = "Order ID is mandatory")
    private Long orderId;

    @NotNull(message = "User ID is mandatory")
    private Long userId;

    @NotNull(message = "Message is mandatory")
    private String message;
}
