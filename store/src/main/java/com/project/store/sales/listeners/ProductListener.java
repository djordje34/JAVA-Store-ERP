package com.project.store.sales.listeners;

import com.project.store.messaging.events.ProductEvent;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class ProductListener implements Serializable {

    public void productAction(ProductEvent productEvent){
        System.out.println("SALES: Product Event Occurred");

        ProductEvent.EventType currEvent = productEvent.getEventType();
        switch (currEvent) {
            case NONE -> {
            }
            case NEW_PRODUCT ->
                    System.out.println(currEvent + " - NEW PRODUCT AVAILABLE ON THE MARKET - " + productEvent.getProduct().toString());
            case UPDATED_PRODUCT ->
                    System.out.println(currEvent + " - UPDATED PRODUCT ON THE MARKET -" + productEvent.getProduct().toString());
            case DELETED_PRODUCT ->
                    System.out.println(currEvent + " - REMOVED PRODUCT FROM THE MARKET -" + productEvent.getProduct().toString());
            case AVAILABLE_PRODUCT -> {
                System.out.println(currEvent + " - REMOVED PRODUCT IN THE INVENTORY -" + productEvent.getProduct().toString());
            }
            default -> throw new IllegalStateException("Unexpected value: " + currEvent);
        }
    }
}
