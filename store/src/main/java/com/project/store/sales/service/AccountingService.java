package com.project.store.sales.service;

import com.project.store.sales.entity.Accounting;
import com.project.store.sales.entity.Invoice;
import com.project.store.sales.repository.AccountingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AccountingService {
    private final AccountingRepository accountingRepository;

    @Autowired
    public AccountingService(AccountingRepository accountingRepository){
        this.accountingRepository = accountingRepository;
    }

    public List<Accounting> getAllAccountings(){
        return accountingRepository.findAll();
    }

    public Optional<Accounting> getAccountingById(Long id){
        return accountingRepository.findById(id);
    }

    public Accounting saveAccounting(Accounting accounting){
        return accountingRepository.save(accounting);
    }

    public void deleteAccounting(Long id){
        Optional<Accounting> accounting = accountingRepository.findById(id);
        accounting.ifPresent(accountingRepository::delete);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkExpiredAccountings() {
        List<Accounting> expiredAccountings = accountingRepository.findExpiredAccountings(LocalDate.now());
        for (Accounting accounting : expiredAccountings) {
            Byte state = 2;
            accounting.setState(state);
            accountingRepository.save(accounting);
            // ovde da cancel rez itd

        }
        }
}
