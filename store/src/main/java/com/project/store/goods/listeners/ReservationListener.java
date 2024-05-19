package com.project.store.goods.listeners;

import com.project.store.goods.repository.ReservationRepository;
import com.project.store.goods.repository.WarehouseRepository;
import com.project.store.messaging.events.ReservationEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservationListener {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    public void reservationAction(ReservationEvent reservationEvent){

    }
}
