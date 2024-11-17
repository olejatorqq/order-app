package com.taskmanagment.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TelegramService {

    private static final Logger logger = LoggerFactory.getLogger(TelegramService.class);
    private final RestTemplate restTemplate;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.chat.id}")
    private String chatId;

    private String getTelegramApiUrl() {
        return "https://api.telegram.org/bot" + botToken + "/sendMessage";
    }

    /**
     * Отправка сообщения в Telegram чат.
     */
    public void sendMessage(String message) {
        try {
            logger.debug("Bot Token: {}", botToken);
            logger.debug("Chat ID: {}", chatId);
            logger.debug("Сообщение для отправки: {}", message);

            // Создание тела запроса с использованием Map
            Map<String, String> payload = new HashMap<>();
            payload.put("chat_id", chatId);
            payload.put("text", message);
            payload.put("parse_mode", "Markdown");

            logger.debug("Отправка сообщения в Telegram: {}", payload);

            // Установка заголовков
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Создание HttpEntity с заголовками и телом запроса
            HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

            // Отправка POST-запроса
            ResponseEntity<String> response = restTemplate.postForEntity(getTelegramApiUrl(), request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Сообщение успешно отправлено в Telegram: {}", message);
                logger.debug("Ответ Telegram API: {}", response.getBody());
            } else {
                logger.error("Неудачная попытка отправки сообщения в Telegram. Статус: {}, Ответ: {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            logger.error("Ошибка при отправке сообщения в Telegram: {}", e.getMessage());
        }
    }
}
