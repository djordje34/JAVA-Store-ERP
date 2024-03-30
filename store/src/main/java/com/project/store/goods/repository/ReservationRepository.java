package com.project.store.goods.repository;

import com.project.store.goods.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    void flush();

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long id);

    void delete(Reservation reservation);
}
