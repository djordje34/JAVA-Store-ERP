package com.project.store.sales.repository;

import com.project.store.sales.entity.Accounting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountingRepository extends JpaRepository<Accounting, Long> {
    void flush();

    Accounting save(Accounting accounting);

    Optional<Accounting> findById(Long id);

    void delete(Accounting accounting);

    // 0-pending, 1-successful i 2-cancelled
    @Query("SELECT a FROM Accounting a WHERE a.dueDate < :now AND a.state = 0")
    List<Accounting> findExpiredAccountings(LocalDate now);
}
