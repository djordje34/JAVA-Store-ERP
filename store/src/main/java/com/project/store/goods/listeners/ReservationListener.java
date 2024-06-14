package com.project.store.goods.listeners;

import com.project.store.goods.entity.Product;
import com.project.store.goods.entity.Reservation;
import com.project.store.goods.entity.Warehouse;
import com.project.store.goods.service.AdvancedGoodsService;
import com.project.store.goods.service.ProductService;
import com.project.store.goods.service.ReservationService;
import com.project.store.goods.service.WarehouseService;
import com.project.store.messaging.config.RabbitMQConfigurator;
import com.project.store.messaging.events.ProductEvent;
import com.project.store.messaging.events.ReservationEvent;
import com.project.store.sales.entity.OrderItem;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ReservationListener implements Serializable {

    private final ProductService productService;
    private final WarehouseService warehouseService;
    private final ReservationService reservationService;
    private final AdvancedGoodsService advancedGoodsService;
    private final RabbitTemplate rabbitTemplate;
    public ReservationListener(ProductService productService, WarehouseService warehouseService, ReservationService reservationService, AdvancedGoodsService advancedGoodsService, RabbitTemplate rabbitTemplate) {
        this.productService = productService;
        this.warehouseService = warehouseService;
        this.reservationService = reservationService;
        this.advancedGoodsService = advancedGoodsService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public void reservationAction(ReservationEvent reservationEvent){
        System.out.println("GOODS: Reservation Event Occurred");

        ReservationEvent.EventType currEvent = reservationEvent.getEventType();

        switch(currEvent){

            case NONE -> {
            }
            case NEW_RESERVATION -> {
                List<OrderItem> orderItems = reservationEvent.getOrderItemList();
                List<Reservation> newReservations = new ArrayList<Reservation>();
                Warehouse temp = null;
                List<Double> prices = new ArrayList<Double>();

                for(OrderItem item : orderItems){
                    Reservation newReservation = new Reservation();
                    newReservation.setOrder(item.getOrder());
                    newReservation.setQuantity(item.getQuantity());
                    Product product = item.getProduct();
                    newReservation.setProduct(product);

                    if(productService.getProductById(product.getId()).isEmpty()){
                        for(Reservation tempRes : newReservations){
                            reservationService.deleteReservation(tempRes.getId());
                        }
                        ProductEvent productEvent = ProductEvent.createCheckProductFailedEvent(newReservation);
                        rabbitTemplate.convertAndSend(RabbitMQConfigurator.PRODUCT_TOPIC_EXCHANGE, "products.check", productEvent);
                        return;
                    }
                    int quantity = 0;
                    List<Warehouse> warehouses = warehouseService.getAllWarehouses();
                    for(Warehouse warehouse : warehouses){
                        if(Objects.equals(warehouse.getInventoryItem().getProduct().getId(), product.getId())){
                            quantity += warehouse.getQuantity();
                            temp = warehouse;
                        }
                    }
                    List<Reservation> reservations = reservationService.getAllReservations();
                    for(Reservation reservation : reservations){
                        if(Objects.equals(reservation.getProduct().getId(), product.getId())){
                            quantity -= reservation.getQuantity();
                        }
                    }
                    if (newReservation.getQuantity() > quantity) {
                        for(Reservation tempRes : newReservations){
                            reservationService.deleteReservation(tempRes.getId());
                        }
                        ProductEvent productEvent = ProductEvent.createCheckProductFailedEvent(newReservation);
                        rabbitTemplate.convertAndSend(RabbitMQConfigurator.PRODUCT_TOPIC_EXCHANGE, "products.check", productEvent);
                        return;
                    }
                    if(temp == null) throw new IllegalArgumentException("Unexpected behaviour");
                    prices.add(advancedGoodsService.formPrice(temp, 1.2));

                    newReservations.add(newReservation);
                    reservationService.saveReservation(newReservation);
                }

                ProductEvent productEvent = ProductEvent.createCheckProductSuccessfulEvent(newReservations, prices);
                rabbitTemplate.convertAndSend(RabbitMQConfigurator.PRODUCT_TOPIC_EXCHANGE, "products.check", productEvent);
            }
            default -> throw new IllegalStateException("Unexpected value: " + currEvent);
        }
    }
}
