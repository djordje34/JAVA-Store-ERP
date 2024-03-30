package com.project.store.sales.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "accountings")
public class Accounting {    //predracun

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "state") //-1 nije jos 0 nije 1 jeste
    private Byte state;

    public Accounting(Order order, Double totalPrice, LocalDate dueDate, Byte state) {
        this.order = order;
        this.totalPrice = totalPrice;
        this.dueDate = dueDate;
        this.state = state;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }
}
