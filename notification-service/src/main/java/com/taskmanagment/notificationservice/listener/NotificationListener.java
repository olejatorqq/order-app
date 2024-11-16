package com.taskmanagment.notificationservice.listener;

import com.taskmanagment.notificationservice.model.Notification;
import com.taskmanagment.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = "notification.queue")
    public void handleNotificationMessage(Notification notification) {
        // Обработка полученного сообщения
        System.out.println("Получено уведомление: " + notification);

        // Сохранение уведомления в базе данных
        notificationService.saveNotification(notification);

        // Логика отправки уведомлений (например, email, SMS и т.д.)
    }
}
