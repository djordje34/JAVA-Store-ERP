package com.project.store.goods.listeners;

import com.project.store.messaging.events.AccountingEvent;
import com.project.store.messaging.events.ReservationEvent;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class AccountingListener implements Serializable {

    @Transactional
    public void processAccounting(AccountingEvent accountingEvent){
        System.out.println("GOODS: Accounting Event Occurred");

        AccountingEvent.EventType currEvent = accountingEvent.getEventType();

        switch (currEvent) {
            case NONE -> {
            }
            case ACCOUNTING_SUCCESSFUL -> {
                //brisi warehouse iteme - simuliraj skidanje itema i stavljanje porudzbine kao i pravljenje Invoice-a
            }
            case ACCOUNTING_FAILED -> {
                //brisi rezervacije
            }
        }
    }
}
