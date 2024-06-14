package com.project.store.messaging.events;

import com.project.store.sales.entity.OrderItem;

import java.io.Serializable;
import java.util.List;

public class ReservationEvent implements Serializable {
    public static enum EventType { NONE, NEW_RESERVATION, CANCELLED_RESERVATION, SUCCESSFUL_RESERVATION };

    EventType eventType = EventType.NONE;
    OrderItem orderItem;
    List<OrderItem> orderItemList;
    public ReservationEvent(){

    }
    public ReservationEvent(EventType eventType, OrderItem orderItem){
        this.eventType = eventType;
        this.orderItem = orderItem;
    }

    public ReservationEvent(EventType eventType, List<OrderItem> orderItems){
        this.eventType = eventType;
        this.orderItemList = orderItems;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public static ReservationEvent createNewReservationEvent(List<OrderItem> orderItems){
        return new ReservationEvent(EventType.NEW_RESERVATION, orderItems);
    }

    public static ReservationEvent createCancelledReservationEvent(OrderItem orderItem){
        return new ReservationEvent(EventType.CANCELLED_RESERVATION, orderItem);
    }

    public static ReservationEvent createSuccessfulReservationEvent(OrderItem orderItem){
        return new ReservationEvent(EventType.SUCCESSFUL_RESERVATION, orderItem);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Reservation Event").append(" of type ")
                .append(eventType)
                .append("\n--------------------------------------\n");
        if(orderItem!=null)
            sb.append(orderItem);
        return sb.toString();
    }
}
