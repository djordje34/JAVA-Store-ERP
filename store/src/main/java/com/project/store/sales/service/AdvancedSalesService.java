package com.project.store.sales.service;

import com.project.store.goods.entity.Product;
import com.project.store.messaging.config.RabbitMQConfigurator;
import com.project.store.messaging.events.ReservationEvent;
import com.project.store.sales.entity.Customer;
import com.project.store.sales.entity.Order;
import com.project.store.sales.entity.OrderItem;
import com.project.store.sales.repository.OrderItemRepository;
import com.project.store.sales.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdvancedSalesService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AdvancedSalesService(OrderItemRepository orderItemRepository, OrderRepository orderRepository, RabbitTemplate rabbitTemplate){
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    public void placeOrder(Customer customer, List<Product> products){   // i need to fix this
        Order order = new Order(customer);
        orderRepository.save(order);
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        for (Product product : products) {
            OrderItem orderItem = new OrderItem(order, product, 1, -1.0);
            orderItemList.add(orderItem);
        }
        ReservationEvent reservationEvent = ReservationEvent.createNewReservationEvent(orderItemList);
        rabbitTemplate.convertAndSend(RabbitMQConfigurator.ORDER_TOPIC_EXCHANGE, "reservations.created", reservationEvent);

    }
}
