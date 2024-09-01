package com.accounts.microservices.accountmovement.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String MOVEMENT_QUEUE = "account-movement-queue";
    public static final String MOVEMENT_EXCHANGE = "account-movement-exchange";

    @Bean
    public Queue movementQueue() {
        return new Queue(MOVEMENT_QUEUE, false);
    }

    @Bean
    public TopicExchange movementExchange() {
        return new TopicExchange(MOVEMENT_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue movementQueue, TopicExchange movementExchange) {
        return BindingBuilder.bind(movementQueue).to(movementExchange).with("movement.*");
    }
}
