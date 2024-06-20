package com.project.store.sales.service;

import com.project.store.goods.entity.Product;
import com.project.store.messaging.config.RabbitMQConfigurator;
import com.project.store.messaging.events.AccountingEvent;
import com.project.store.messaging.events.ReservationEvent;
import com.project.store.sales.entity.*;
import com.project.store.sales.repository.AccountingRepository;
import com.project.store.sales.repository.OrderItemRepository;
import com.project.store.sales.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdvancedSalesService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final AccountingRepository accountingRepository;
    private final InvoiceService invoiceService;
    private final AccountingService accountingService;

    @Autowired
    public AdvancedSalesService(OrderItemRepository orderItemRepository, OrderRepository orderRepository, RabbitTemplate rabbitTemplate, AccountingRepository accountingRepository, InvoiceService invoiceService, AccountingService accountingService) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.accountingRepository = accountingRepository;
        this.invoiceService = invoiceService;
        this.accountingService = accountingService;
    }


    public void placeOrder(Customer customer, List<Product> products) {   // i need to fix this
        Order order = new Order(customer);
        orderRepository.save(order);
        List<OrderItem> orderItemList = new ArrayList<>();
        for (Product product : products) {
            OrderItem orderItem = new OrderItem(order, product, 1, -1.0);
            orderItemList.add(orderItem);
        }
        ReservationEvent reservationEvent = ReservationEvent.createNewReservationEvent(orderItemList);
        rabbitTemplate.convertAndSend(RabbitMQConfigurator.ORDER_TOPIC_EXCHANGE, "reservations.created", reservationEvent);

    }

    public void checkExpiredAccountings() {
        List<Accounting> expiredAccountings = accountingRepository.findExpiredAccountings(LocalDate.now());
        for (Accounting accounting : expiredAccountings) {
            byte state = -1; // za failed accounting(s) -1 za uspesne 1 za pending 0
            accounting.setState(state);
            accountingRepository.save(accounting);
            AccountingEvent accountingEvent = AccountingEvent.createFailedAccountingEvent(accounting);
            rabbitTemplate.convertAndSend(RabbitMQConfigurator.ORDER_TOPIC_EXCHANGE, "accountings.failed", accountingEvent);

        }
    }

    public Invoice payAccounting(Accounting accounting, Double totalPay) {
        AccountingEvent accountingEvent = AccountingEvent.createSuccessfulAccountingEvent(accounting);
        rabbitTemplate.convertAndSend(RabbitMQConfigurator.ORDER_TOPIC_EXCHANGE, "accountings.successful", accountingEvent);
        accounting.setState((byte) 1);
        accountingService.saveAccounting(accounting);
        Invoice invoice = new Invoice(accounting, LocalDate.now(), totalPay);
        return invoiceService.saveInvoice(invoice);
    }

}
