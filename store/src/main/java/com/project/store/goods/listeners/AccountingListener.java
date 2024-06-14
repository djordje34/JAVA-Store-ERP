package com.project.store.goods.listeners;

import com.project.store.goods.entity.Product;
import com.project.store.goods.entity.Reservation;
import com.project.store.goods.entity.Warehouse;
import com.project.store.goods.repository.ReservationRepository;
import com.project.store.goods.service.ReservationService;
import com.project.store.goods.service.WarehouseService;
import com.project.store.messaging.events.AccountingEvent;
import com.project.store.sales.entity.Accounting;
import com.project.store.sales.entity.Invoice;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Component
public class AccountingListener implements Serializable {
    private final WarehouseService warehouseService;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final RabbitTemplate rabbitTemplate;

    public AccountingListener(WarehouseService warehouseService, ReservationRepository reservationRepository, ReservationService reservationService, RabbitTemplate rabbitTemplate) {
        this.warehouseService = warehouseService;
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional  // pod pretpostavkom da Transactional radi nad ovom metodom !!!
    public void accountingAction(AccountingEvent accountingEvent) {
        System.out.println("GOODS: Accounting Event Occurred");

        AccountingEvent.EventType currEvent = accountingEvent.getEventType();

        try {
            switch (currEvent) {
                case NONE -> {
                }
                case ACCOUNTING_SUCCESSFUL -> { // moze kroz rezervacije
                    Accounting accounting = accountingEvent.getAccounting();
                    // nadjemo sve rezervacije sa istim orderId kao kod acc
                    Optional<List<Reservation>> optReservations = reservationRepository.findByOrderId(accounting.getOrder().getId());
                    if (optReservations.isEmpty()) return;

                    List<Reservation> reservations = optReservations.get();
                    //ovo sve radi
                    for (Reservation res : reservations) {
                        Product product = res.getProduct();
                        int quantity = res.getQuantity();
                        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
                        for (Warehouse warehouse : warehouses) {
                            if (quantity == 0) break;
                            if (warehouse.getInventoryItem().getProduct() != product) continue;

                            int availableQuantity = Math.min(warehouse.getQuantity(), quantity); //ukoliko wh ima vise q nego sto je potrebno
                            warehouse.setQuantity(warehouse.getQuantity() - availableQuantity);
                            quantity -= availableQuantity;
                            warehouseService.saveWarehouse(warehouse);
                            //ovde oduzmi quantity sa rezervacije od svakog warehousea dok se ne dodje do nule i posle sacuvaj warehouse-e
                        }
                        //obrisi rezervaciju kad se zavrsi skidanje produkta
                        reservationService.deleteReservation(res.getId());

                    }//ovo nije jos gotovo
                }
                case ACCOUNTING_FAILED -> {
                    Accounting accounting = accountingEvent.getAccounting();
                    Optional<List<Reservation>> optReservations = reservationRepository.findByOrderId(accounting.getOrder().getId());
                    if (optReservations.isEmpty()) return;
                    List<Reservation> reservations = optReservations.get();
                    for (Reservation res : reservations) {
                        reservationService.deleteReservation(res.getId());
                    }
                }
            }
        }
        catch (Exception e) {
            System.err.println("Error handling accounting event: " + e.getMessage());
            throw e;
        }
}
}
