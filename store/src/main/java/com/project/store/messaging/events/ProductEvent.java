package com.project.store.messaging.events;

import com.project.store.goods.entity.Product;
import com.project.store.goods.entity.Reservation;

import java.io.Serializable;
import java.util.List;

public class ProductEvent implements Serializable {
    public static enum EventType {NONE, NEW_PRODUCT, DELETED_PRODUCT, UPDATED_PRODUCT, CHECK_PRODUCT_FAILED, CHECK_PRODUCT_SUCCESSFUL, AVAILABLE_PRODUCT}

    ;
    EventType eventType = EventType.NONE;
    Product product = null;
    List<Reservation> reservations = null;
    Reservation reservation;
    List<Double> prices = null;

    public ProductEvent() {
    }

    public ProductEvent(EventType eventType, Reservation reservation) {
        this.eventType = eventType;
        this.reservation = reservation;
    }

    public ProductEvent(EventType eventType, Product product) {
        this.eventType = eventType;
        this.product = product;
    }

    public ProductEvent(EventType eventType, List<Reservation> reservations, List<Double> prices) {
        this.eventType = eventType;
        this.reservations = reservations;
        this.prices = prices;
    }

    public static ProductEvent createNewProductEvent(Product product) {
        return new ProductEvent(EventType.NEW_PRODUCT, product);
    }

    public static ProductEvent createDeletedProductEvent(Product product) {
        return new ProductEvent(EventType.DELETED_PRODUCT, product);
    }

    public static ProductEvent createUpdatedProductEvent(Product product) {
        return new ProductEvent(EventType.UPDATED_PRODUCT, product);
    }

    public static ProductEvent createAvailableProductEvent(Product product) {
        return new ProductEvent(EventType.AVAILABLE_PRODUCT, product);
    }

    public static ProductEvent createCheckProductFailedEvent(Reservation reservation) {
        return new ProductEvent(EventType.CHECK_PRODUCT_FAILED, reservation);
    }

    public static ProductEvent createCheckProductSuccessfulEvent(List<Reservation> reservations, List<Double> prices) {
        return new ProductEvent(EventType.CHECK_PRODUCT_SUCCESSFUL, reservations, prices);
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Double> getPrices() {
        return prices;
    }

    public void setPrices(List<Double> prices) {
        this.prices = prices;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product Event").append(" of type ")
                .append(eventType)
                .append("\n--------------------------------------\n");
        if (product != null)
            sb.append(product);
        if (reservations != null) {
            for (Reservation reservation : reservations) {
                sb.append(reservation);
            }
        }
        return sb.toString();
    }
}
