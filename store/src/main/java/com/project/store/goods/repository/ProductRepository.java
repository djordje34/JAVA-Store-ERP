package com.project.store.goods.repository;

import com.project.store.goods.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    void flush();
    List<Product> findAll();

    Product save(Product product);

    Optional<Product> findById(Long id);

    void delete(Product product);
}
