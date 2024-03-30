package com.project.store.sales.repository;

import com.project.store.sales.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    void flush();

    Invoice save(Invoice invoice);

    Optional<Invoice> findById(Long id);

    void delete(Invoice invoice);
}
