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
    Binding productDeletedBinding(Queue queue, TopicExchange goodsExchange){
        return BindingBuilder.bind(queue).to(goodsExchange).with("products.deleted");
    }

    @Bean
    Binding productAddedBinding(Queue queue, TopicExchange goodsExchange){
        return BindingBuilder.bind(queue).to(goodsExchange).with("products.created");
    }
    @Bean
    Binding productUpdatedBinding(Queue queue, TopicExchange goodsExchange){
        return BindingBuilder.bind(queue).to(goodsExchange).with("products.updated");
    }

    // tri tipa *.created, *.cancelled i *.successful
    @Bean
    Binding reservationCancelledBinding(Queue queue, TopicExchange goodsExchange) {
        return BindingBuilder.bind(queue).to(goodsExchange).with("reservations.cancelled");
    }

    @Bean
    Binding reservationCreatedBinding(Queue queue, TopicExchange goodsExchange) {
        return BindingBuilder.bind(queue).to(goodsExchange).with("reservations.created");
    }

    @Bean
    Binding reservationSuccessfulBinding(Queue queue, TopicExchange goodsExchange) {
        return BindingBuilder.bind(queue).to(goodsExchange).with("reservations.successful");
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
