package com.project.store.goods.controller;

import com.project.store.goods.entity.InventoryItem;
import com.project.store.goods.service.InventoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventoryItems") //u sustini ovo je utility i nepotrebno jer warehouse direktno radi sa inventory-jem, ali cisto radi reda
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    @Autowired
    public InventoryItemController(InventoryItemService inventoryItemService){
        this.inventoryItemService = inventoryItemService;
    }

    @GetMapping("/")
    public ResponseEntity<List<InventoryItem>> getAllInventoryItems() {
        List<InventoryItem> inventoryItems = inventoryItemService.getAllInventoryItems();
        return ResponseEntity.ok(inventoryItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> getInventoryItemById(@PathVariable Long id) {
        Optional<InventoryItem> inventoryItem = inventoryItemService.getInventoryItemById(id);
        return ResponseEntity.ok(inventoryItem.orElse(null));
    }

    @PostMapping("/")
    public ResponseEntity<InventoryItem> saveInventoryItem(@RequestBody InventoryItem inventoryItem) {
        InventoryItem savedInventoryItem = inventoryItemService.saveInventoryItem(inventoryItem);
        return ResponseEntity.ok(savedInventoryItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable Long id) {
        inventoryItemService.deleteInventoryItem(id);
        return ResponseEntity.noContent().build();
    }
}
