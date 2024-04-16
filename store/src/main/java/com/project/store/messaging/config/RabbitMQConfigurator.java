package com.project.store.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfigurator {
    public static final String GOODS_TOPIC_EXCHANGE = "goods-exchange";
    public static final String PRODUCTS_QUEUE = "products-queue";
    public static final String RESERVATION_QUEUE = "reservation-queue";
    @Bean
    TopicExchange goodsExchange(){
        return new TopicExchange(GOODS_TOPIC_EXCHANGE);
    }
    @Bean
    Binding productsBinding(Queue queue, TopicExchange goodsExchange){
        return BindingBuilder.bind(queue).to(goodsExchange).with("products.#");
    }

    // tri tipa *.failed, *.cancelled i *.successful
    @Bean
    Binding reservationBinding(Queue queue, TopicExchange goodsExchange){
        return BindingBuilder.bind(queue).to(goodsExchange).with("reservations.#");
    }

    @Bean
    SimpleMessageListenerContainer productContainer(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(PRODUCTS_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    SimpleMessageListenerContainer reservationContainer(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RESERVATION_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    // tek dodati adaptere

}
