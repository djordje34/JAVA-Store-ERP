package com.project.store.goods.listeners;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class AccountingListener implements Serializable {

    @Transactional
    public void processAccounting(){

    }
}
