package com.project.store.messaging.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.project.store.goods.entity.Product;

import java.io.Serializable;

public class ProductEvent implements Serializable {
    public static enum EventType { NONE, NEW_PRODUCT, DELETED_PRODUCT, UPDATED_PRODUCT };
    EventType eventType = EventType.NONE;
    Product product;

    public ProductEvent() {
    }

    public ProductEvent(EventType eventType, Product product) {
        this.eventType = eventType;
        this.product = product;
    }

    public static ProductEvent createNewProductEvent(Product product){
        return new ProductEvent(EventType.NEW_PRODUCT, product);
    }

    public static ProductEvent createDeletedProductEvent(Product product){
        return new ProductEvent(EventType.DELETED_PRODUCT, product);
    }

    public static ProductEvent createUpdatedProductEvent(Product product){
        return new ProductEvent(EventType.UPDATED_PRODUCT, product);
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

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Product Event").append(" of type ")
                .append(eventType)
                .append("\n--------------------------------------\n");
        if(product!=null)
            sb.append(product);
        return sb.toString();
    }
}
