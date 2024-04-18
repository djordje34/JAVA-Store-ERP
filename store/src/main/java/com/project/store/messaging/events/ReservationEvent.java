package com.project.store.messaging.events;

import com.project.store.goods.entity.Product;
import com.project.store.goods.entity.Reservation;
import com.project.store.sales.entity.Accounting;

import java.io.Serializable;

public class ReservationEvent implements Serializable {
    public static enum EventType { NONE, NEW_RESERVATION, CANCELLED_RESERVATION, SUCCESSFUL_RESERVATION };

    EventType eventType = EventType.NONE;
    Reservation reservation;
    Accounting accounting;
    public ReservationEvent(){

    }
    public ReservationEvent(EventType eventType, Reservation reservation, Accounting accounting){
        this.eventType = eventType;
        this.reservation = reservation;
        this.accounting = accounting;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Accounting getAccounting() {
        return accounting;
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }

    public static ReservationEvent createNewReservationEvent(Reservation reservation, Accounting accounting){
        return new ReservationEvent(EventType.NEW_RESERVATION, reservation, accounting);
    }

    public static ReservationEvent createCancelledReservationEvent(Reservation reservation, Accounting accounting){
        return new ReservationEvent(EventType.CANCELLED_RESERVATION, reservation, accounting);
    }

    public static ReservationEvent createSucessfulReservationEvent(Reservation reservation, Accounting accounting){
        return new ReservationEvent(EventType.SUCCESSFUL_RESERVATION, reservation, accounting);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Reservation Event").append(" of type ")
                .append(eventType)
                .append("\n--------------------------------------\n");
        if(reservation!=null)
            sb.append(reservation);
        if(accounting!=null)
            sb.append(accounting);
        return sb.toString();
    }
}
