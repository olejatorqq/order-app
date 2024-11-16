package com.taskmanagment.userservice;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USER_EXCHANGE = "userExchange";

    @Bean
    public Exchange userExchange() {
        return ExchangeBuilder.topicExchange(USER_EXCHANGE).durable(true).build();
    }
}