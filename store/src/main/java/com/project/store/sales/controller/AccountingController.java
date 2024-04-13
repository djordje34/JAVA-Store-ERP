package com.project.store.sales.controller;

import com.project.store.goods.entity.InventoryItem;
import com.project.store.sales.entity.Accounting;
import com.project.store.sales.service.AccountingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/accountings")
public class AccountingController {
    private final AccountingService accountingService;

    @Autowired
    public AccountingController(AccountingService accountingService){
        this.accountingService = accountingService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Accounting>> getAllAccountings() {
        List<Accounting> accountings = accountingService.getAllAccountings();
        return ResponseEntity.ok(accountings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Accounting> getAccountingById(@PathVariable Long id) {
        Optional<Accounting> accounting = accountingService.getAccountingById(id);
        return ResponseEntity.ok(accounting.orElse(null));
    }

    @PostMapping("/")
    public ResponseEntity<Accounting> saveAccounting(@RequestBody Accounting accounting) {
        Accounting savedAccounting = accountingService.saveAccounting(accounting);
        return ResponseEntity.ok(savedAccounting);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccounting(@PathVariable Long id) {
        accountingService.deleteAccounting(id);
        return ResponseEntity.noContent().build();
    }

}
