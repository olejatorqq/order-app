package com.taskmanagment.notificationservice.service;

import com.taskmanagment.notificationservice.dto.NotificationDTO;
import com.taskmanagment.notificationservice.model.Notification;
import com.taskmanagment.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;
    private final TelegramService telegramService;

    /**
     * Получение всех уведомлений.
     */
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    /**
     * Получение уведомления по ID.
     */
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    /**
     * Отправка уведомления.
     */
    public Notification sendNotification(NotificationDTO notificationDTO) {
        // Создание уведомления в базе данных
        Notification notification = Notification.builder()
                .orderId(notificationDTO.getOrderId())
                .userId(notificationDTO.getUserId())
                .message(notificationDTO.getMessage())
                .sentAt(LocalDateTime.now())
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        logger.info("Уведомление сохранено: {}", savedNotification);

        // Формирование сообщения для Telegram
        String telegramMessage = String.format("*📦 Заказ #%d*\n*Пользователь ID:* %d\n*Сообщение:* %s",
                savedNotification.getOrderId(),
                savedNotification.getUserId(),
                savedNotification.getMessage());

        // Отправка сообщения в Telegram
        telegramService.sendMessage(telegramMessage);

        return savedNotification; // Возвращаем сохранённое уведомление
    }
}

