package com.project.store.goods.service;

import com.project.store.goods.entity.Reservation;
import com.project.store.goods.entity.Warehouse;
import com.project.store.goods.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservations(){
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long id){
        return reservationRepository.findById(id);
    }

    public Reservation saveReservation(Reservation reservation){
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id){
        Optional<Reservation> reservation = reservationRepository.findById(id);
        reservation.ifPresent(reservationRepository::delete);
    }
}
