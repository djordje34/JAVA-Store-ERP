package com.project.store.sales.listeners;

import com.project.store.messaging.events.ProductEvent;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class ProductListener implements Serializable {

    public void productAction(ProductEvent productEvent){
        System.out.println("SALES: Product Event Occurred");

        ProductEvent.EventType currEvent = productEvent.getEventType();
        if(currEvent.equals(ProductEvent.EventType.NEW_PRODUCT)){
            System.out.println(currEvent + " - new product - " + productEvent.getProduct().toString());
        }
        else if(currEvent.equals(ProductEvent.EventType.UPDATED_PRODUCT)){
            System.out.println(currEvent + " - updated product -" + productEvent.getProduct().toString());
        }
        else{
            System.out.println(currEvent + " - deleted product -" + productEvent.getProduct().toString());
        }
    }
}
