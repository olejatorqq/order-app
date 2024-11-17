package com.taskmanagment.notificationservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDTO {
    private Long id;
    private Long orderId;
    private Long userId;
    private String message;
    private LocalDateTime sentAt;
}