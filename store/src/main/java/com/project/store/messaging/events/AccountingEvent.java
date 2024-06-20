package com.project.store.messaging.events;

import com.project.store.sales.entity.Accounting;

import java.io.Serializable;

public class AccountingEvent implements Serializable {

    public static enum EventType {NONE, ACCOUNTING_SUCCESSFUL, ACCOUNTING_FAILED}

    ;
    EventType eventType = EventType.NONE;

    Accounting accounting;

    public AccountingEvent() {

    }

    public AccountingEvent(EventType eventType, Accounting accounting) {
        this.eventType = eventType;
        this.accounting = accounting;
    }

    public static AccountingEvent createSuccessfulAccountingEvent(Accounting accounting) {
        return new AccountingEvent(EventType.ACCOUNTING_SUCCESSFUL, accounting);
    }

    public static AccountingEvent createFailedAccountingEvent(Accounting accounting) {
        return new AccountingEvent(EventType.ACCOUNTING_FAILED, accounting);
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Accounting getAccounting() {
        return accounting;
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Accounting Event").append(" of type ")
                .append(eventType)
                .append("\n--------------------------------------\n");
        if (accounting != null)
            sb.append(accounting);
        return sb.toString();
    }
}
