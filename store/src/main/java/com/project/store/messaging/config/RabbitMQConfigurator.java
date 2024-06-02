package com.project.store.messaging.config;

import com.project.store.goods.listeners.AccountingListener;
import com.project.store.goods.listeners.ReservationListener;
import com.project.store.sales.listeners.ProductListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// product add radi preko rabbitMQ za sad

@Configuration
public class RabbitMQConfigurator {
    public static final String PRODUCT_TOPIC_EXCHANGE = "products-exchange";
    public static final String ORDER_TOPIC_EXCHANGE = "orders-exchange";
    public static final String PRODUCTS_QUEUE = "products-queue";
    public static final String RESERVATION_QUEUE = "reservation-queue";

    public static final String ACCOUNTING_QUEUE = "accounting-queue";

    @Bean
    Queue productQueue() {
        return new Queue(PRODUCTS_QUEUE, false);
    }
    @Bean
    Queue reservationQueue() {
        return new Queue(RESERVATION_QUEUE, false);
    }
    @Bean
    Queue accountingQueue() {
        return new Queue(ACCOUNTING_QUEUE, false);
    }

    @Bean
    TopicExchange productsExchange(){
        return new TopicExchange(PRODUCT_TOPIC_EXCHANGE);
    }

    @Bean
    TopicExchange ordersExchange(){
        return new TopicExchange(ORDER_TOPIC_EXCHANGE);
    }

    @Bean
    Binding productDeletedBinding(Queue productQueue, TopicExchange productsExchange){
        return BindingBuilder.bind(productQueue).to(productsExchange).with("products.deleted");
    }

    @Bean
    Binding productAddedBinding(Queue productQueue, TopicExchange productsExchange){
        return BindingBuilder.bind(productQueue).to(productsExchange).with("products.created");
    }
    @Bean
    Binding productUpdatedBinding(Queue productQueue, TopicExchange productsExchange){
        return BindingBuilder.bind(productQueue).to(productsExchange).with("products.updated");
    }

    @Bean
    Binding productAvailableBinding(Queue productQueue, TopicExchange productsExchange){
        return BindingBuilder.bind(productQueue).to(productsExchange).with("products.available");
    }

    @Bean
    Binding productCheckBinding(Queue productQueue, TopicExchange productsExchange){
        return BindingBuilder.bind(productQueue).to(productsExchange).with("products.check"); // ovde treba da ima dva tipa za check
    }

    // tri tipa *.created, *.cancelled i *.successful
    @Bean
    Binding reservationCancelledBinding(Queue reservationQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(reservationQueue).to(ordersExchange).with("reservations.cancelled");
    }

    @Bean
    Binding reservationCreatedBinding(Queue reservationQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(reservationQueue).to(ordersExchange).with("reservations.created");
    }

    @Bean
    Binding reservationSuccessfulBinding(Queue reservationQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(reservationQueue).to(ordersExchange).with("reservations.successful");
    }

    // Uspesan predracun
    @Bean
    Binding accountingSuccessful(Queue accountingQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(accountingQueue).to(ordersExchange).with("accounting.successful");
    }

    @Bean
    Binding accountingFailed(Queue accountingQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(accountingQueue).to(ordersExchange).with("accounting.failed");
    }

    @Bean
    SimpleMessageListenerContainer productContainer(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter productListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(PRODUCTS_QUEUE);
        container.setMessageListener(productListenerAdapter);
        return container;
    }

    @Bean
    SimpleMessageListenerContainer reservationContainer(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter reservationListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RESERVATION_QUEUE);
        container.setMessageListener(reservationListenerAdapter);
        return container;
    }


    @Bean
    SimpleMessageListenerContainer accountingContainer(ConnectionFactory connectionFactory,
                                                        MessageListenerAdapter accountingListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(ACCOUNTING_QUEUE);
        container.setMessageListener(accountingListenerAdapter);
        return container;
    }


    // tek dodati adaptere
    @Bean
    MessageListenerAdapter productListenerAdapter(ProductListener productListener, MessageConverter messageConverter) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(productListener, "productAction");
        adapter.setMessageConverter(messageConverter);
        return adapter;
    }

    @Bean
    MessageListenerAdapter reservationListenerAdapter(ReservationListener reservationListener, MessageConverter messageConverter) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(reservationListener, "reservationAction");
        adapter.setMessageConverter(messageConverter);
        return adapter;
    }

    @Bean
    MessageListenerAdapter accountingListenerAdapter(AccountingListener accountingListener, MessageConverter messageConverter) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(accountingListener, "accountingAction");
        adapter.setMessageConverter(messageConverter);
        return adapter;
    }

    // ne moze bez JSON convertera
    @Bean
    public MessageConverter jsonConverter() {
        DefaultClassMapper defaultClassMapper = new DefaultClassMapper();
        defaultClassMapper.setTrustedPackages("*");
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        jackson2JsonMessageConverter.setClassMapper(defaultClassMapper);
        return jackson2JsonMessageConverter;
    }

}
