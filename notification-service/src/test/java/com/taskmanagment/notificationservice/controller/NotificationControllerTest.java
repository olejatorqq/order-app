package com.taskmanagment.notificationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanagment.notificationservice.dto.NotificationDTO;
import com.taskmanagment.notificationservice.model.Notification;
import com.taskmanagment.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тестовый класс для NotificationController.
 */
@WebMvcTest(NotificationController.class)
@WithMockUser(username = "testuser", roles = {"USER"}) // Симулируем аутентифицированного пользователя
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService; // Мокаем только сервис

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Тест успешного получения всех уведомлений.
     */
    @Test
    public void getAllNotifications_Success() throws Exception {
        Notification notification1 = Notification.builder()
                .id(1L)
                .orderId(101L)
                .userId(1L)
                .message("Ваш заказ был успешно оформлен.")
                .sentAt(LocalDateTime.now())
                .build();

        Notification notification2 = Notification.builder()
                .id(2L)
                .orderId(102L)
                .userId(2L)
                .message("Ваш заказ отправлен.")
                .sentAt(LocalDateTime.now())
                .build();

        Mockito.when(notificationService.getAllNotifications()).thenReturn(Arrays.asList(notification1, notification2));

        mockMvc.perform(get("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(notification1.getId()))
                .andExpect(jsonPath("$[0].orderId").value(notification1.getOrderId()))
                .andExpect(jsonPath("$[0].userId").value(notification1.getUserId()))
                .andExpect(jsonPath("$[0].message").value(notification1.getMessage()))
                .andExpect(jsonPath("$[0].sentAt").exists())
                .andExpect(jsonPath("$[1].id").value(notification2.getId()))
                .andExpect(jsonPath("$[1].orderId").value(notification2.getOrderId()))
                .andExpect(jsonPath("$[1].userId").value(notification2.getUserId()))
                .andExpect(jsonPath("$[1].message").value(notification2.getMessage()))
                .andExpect(jsonPath("$[1].sentAt").exists());
    }

    /**
     * Тест успешного получения уведомления по ID.
     */
    @Test
    public void getNotificationById_Success() throws Exception {
        Long notificationId = 1L;
        Notification notification = Notification.builder()
                .id(notificationId)
                .orderId(101L)
                .userId(1L)
                .message("Ваш заказ был успешно оформлен.")
                .sentAt(LocalDateTime.now())
                .build();

        Mockito.when(notificationService.getNotificationById(notificationId)).thenReturn(Optional.of(notification));

        mockMvc.perform(get("/api/notifications/{id}", notificationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notification.getId()))
                .andExpect(jsonPath("$.orderId").value(notification.getOrderId()))
                .andExpect(jsonPath("$.userId").value(notification.getUserId()))
                .andExpect(jsonPath("$.message").value(notification.getMessage()))
                .andExpect(jsonPath("$.sentAt").exists());
    }

    /**
     * Тест получения уведомления по ID, которого нет.
     */
    @Test
    public void getNotificationById_NotFound() throws Exception {
        Long notificationId = 999L;

        Mockito.when(notificationService.getNotificationById(notificationId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/notifications/{id}", notificationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Уведомление с ID " + notificationId + " не найдено."));
    }

    /**
     * Тест успешного создания уведомления.
     */
    @Test
    public void createNotification_Success() throws Exception {
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .orderId(103L)
                .userId(3L)
                .message("Ваш заказ готов к отправке.")
                .build();

        Notification savedNotification = Notification.builder()
                .id(3L)
                .orderId(notificationDTO.getOrderId())
                .userId(notificationDTO.getUserId())
                .message(notificationDTO.getMessage())
                .sentAt(LocalDateTime.now())
                .build();

        Mockito.when(notificationService.sendNotification(any(NotificationDTO.class))).thenReturn(savedNotification);

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificationDTO))
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())) // Добавляем CSRF-токен
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedNotification.getId()))
                .andExpect(jsonPath("$.orderId").value(savedNotification.getOrderId()))
                .andExpect(jsonPath("$.userId").value(savedNotification.getUserId()))
                .andExpect(jsonPath("$.message").value(savedNotification.getMessage()))
                .andExpect(jsonPath("$.sentAt").exists());
    }
}
