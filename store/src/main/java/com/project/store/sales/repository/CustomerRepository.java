package com.project.store.sales.repository;

import com.project.store.sales.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    void flush();

    List<Customer> findAll();

    Customer save(Customer customer);

    Optional<Customer> findById(Long id);

    void delete(Customer customer);
}
