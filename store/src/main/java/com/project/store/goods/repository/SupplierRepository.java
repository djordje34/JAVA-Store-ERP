package com.project.store.goods.repository;

import com.project.store.goods.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    void flush();

    Supplier save(Supplier supplier);

    Optional<Supplier> findById(Long id);

    void delete(Supplier supplier);
}
