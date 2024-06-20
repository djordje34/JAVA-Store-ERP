package com.project.store.sales.repository;

import com.project.store.sales.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    void flush();

    Order save(Order order);

    Optional<Order> findById(Long id);

    void delete(Order order);
}
