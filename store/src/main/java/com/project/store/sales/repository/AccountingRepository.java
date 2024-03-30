package com.project.store.sales.repository;

import com.project.store.sales.entity.Accounting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountingRepository extends JpaRepository<Accounting, Long> {
    void flush();

    Accounting save(Accounting accounting);

    Optional<Accounting> findById(Long id);

    void delete(Accounting accounting);
}
