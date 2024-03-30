package com.project.store.goods.repository;

import com.project.store.goods.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    void flush();

    Warehouse save(Warehouse warehouse);

    Optional<Warehouse> findById(Long id);
    void delete(Warehouse warehouse);

    @Query("SELECT SUM(w.quantity) FROM Warehouse w WHERE w.inventoryItem.product.id = :productId")
    Integer findQuantityByProductId(@Param("productId") Long productId);

    @Query("SELECT AVG(w.inventoryItem.purchasePrice) from Warehouse w WHERE w.inventoryItem.product.id = :productId")
    Double findAveragePurchasePrice(@Param("productId") Long productId);
}