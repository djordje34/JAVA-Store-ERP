package com.project.store.sales.listeners;

import com.project.store.goods.entity.Reservation;
import com.project.store.messaging.events.ProductEvent;
import com.project.store.sales.entity.Accounting;
import com.project.store.sales.entity.OrderItem;
import com.project.store.sales.service.AccountingService;
import com.project.store.sales.service.InvoiceService;
import com.project.store.sales.service.OrderItemService;
import com.project.store.sales.service.OrderService;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductListener implements Serializable {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final InvoiceService invoiceService;
    private final AccountingService accountingService;

    public ProductListener(OrderService orderService, OrderItemService orderItemService, InvoiceService invoiceService, AccountingService accountingService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.invoiceService = invoiceService;
        this.accountingService = accountingService;
    }

    public void productAction(ProductEvent productEvent) {
        System.out.println("SALES: Product Event Occurred");

        ProductEvent.EventType currEvent = productEvent.getEventType();
        switch (currEvent) {
            case NONE -> {
            }
            case NEW_PRODUCT ->
                    System.out.println(currEvent + " - NEW PRODUCT AVAILABLE ON THE MARKET - " + productEvent.getProduct().toString());
            case UPDATED_PRODUCT ->
                    System.out.println(currEvent + " - UPDATED PRODUCT ON THE MARKET - " + productEvent.getProduct().toString());
            case DELETED_PRODUCT ->
                    System.out.println(currEvent + " - REMOVED PRODUCT FROM THE MARKET - " + productEvent.getProduct().toString());
            case CHECK_PRODUCT_FAILED -> {
                orderService.deleteOrder(productEvent.getReservation().getOrder().getId());
                System.out.println(currEvent + " - ORDER ID = " + productEvent.getReservation().getOrder().getId() + " FAILED - Not enough quantity of PRODUCT(s) with ID = "
                        + productEvent.getReservation().getProduct().getId() + " (Requested " + productEvent.getReservation().getQuantity() + ")");
            }
            case CHECK_PRODUCT_SUCCESSFUL -> {
                List<Double> prices = productEvent.getPrices();
                List<Reservation> reservations = productEvent.getReservations();

                List<OrderItem> orderItems = new ArrayList<OrderItem>();
                for (int i = 0; i < reservations.size(); i++) {
                    orderItemService.saveOrderItem(new OrderItem(reservations.get(i).getOrder(), reservations.get(i).getProduct(), reservations.get(i).getQuantity(), prices.get(i)));
                }

                Accounting accounting = new Accounting(reservations.get(0).getOrder(),
                        prices.stream().reduce(0.0, Double::sum),
                        LocalDate.now().plusDays(3),
                        (byte) 0);
                accountingService.saveAccounting(accounting);
                System.out.println(currEvent + " - ORDER SUCCESSFUL - " + accounting.getOrder().toString());
            }
            case AVAILABLE_PRODUCT -> {
                System.out.println(currEvent + " - ITEM ADDED TO THE INVENTORY - " + productEvent.getProduct().toString());
            }
            default -> throw new IllegalStateException("Unexpected value: " + currEvent);
        }
    }
}
