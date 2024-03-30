package com.project.store.sales.repository;

import com.project.store.sales.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    void flush();

    OrderItem save(OrderItem orderItem);

    Optional<OrderItem> findById(Long id);

    void delete(OrderItem orderItem);
}
