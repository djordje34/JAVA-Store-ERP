package com.project.store.goods.repository;

import com.project.store.goods.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    void flush();

    InventoryItem save(InventoryItem invItem);

    Optional<InventoryItem> findById(Long id);

    void delete(InventoryItem invItem);

    Optional<List<InventoryItem>> findByProductId(Long productId);
}
