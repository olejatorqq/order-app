package com.taskmanagment.notificationservice.controller;

import com.taskmanagment.notificationservice.dto.NotificationDTO;
import com.taskmanagment.notificationservice.dto.NotificationResponseDTO;
import com.taskmanagment.notificationservice.exception.ResourceNotFoundException;
import com.taskmanagment.notificationservice.model.Notification;
import com.taskmanagment.notificationservice.repository.NotificationRepository;
import com.taskmanagment.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        List<NotificationResponseDTO> responseDTOs = notifications.stream()
                .map(notification -> NotificationResponseDTO.builder()
                        .id(notification.getId())
                        .orderId(notification.getOrderId())
                        .userId(notification.getUserId())
                        .message(notification.getMessage())
                        .sentAt(notification.getSentAt())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Уведомление с ID " + id + " не найдено."));

        NotificationResponseDTO responseDTO = NotificationResponseDTO.builder()
                .id(notification.getId())
                .orderId(notification.getOrderId())
                .userId(notification.getUserId())
                .message(notification.getMessage())
                .sentAt(notification.getSentAt())
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {
        // Создание объекта Notification и передача его в сервис
        Notification savedNotification = notificationService.sendNotification(notificationDTO);

        // Создание DTO для ответа
        NotificationResponseDTO responseDTO = NotificationResponseDTO.builder()
                .id(savedNotification.getId())
                .orderId(savedNotification.getOrderId())
                .userId(savedNotification.getUserId())
                .message(savedNotification.getMessage())
                .sentAt(savedNotification.getSentAt())
                .build();

        return ResponseEntity.ok(responseDTO);
    }
}
