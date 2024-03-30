package com.project.store.goods.service;

import com.project.store.goods.entity.InventoryItem;
import com.project.store.goods.entity.Warehouse;
import com.project.store.goods.repository.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;

    @Autowired
    public InventoryItemService(InventoryItemRepository inventoryItemRepository){
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public List<InventoryItem> getAllInventoryItems(){
        return inventoryItemRepository.findAll();
    }

    public Optional<InventoryItem> getInventoryItemById(Long id){
        return inventoryItemRepository.findById(id);
    }

    public InventoryItem saveInventoryItem(InventoryItem inventoryItem){
        return inventoryItemRepository.save(inventoryItem);
    }

    public void deleteInventoryItem(Long id){
        Optional<InventoryItem> inventoryItem = inventoryItemRepository.findById(id);
        inventoryItem.ifPresent(inventoryItemRepository::delete);
    }



}
