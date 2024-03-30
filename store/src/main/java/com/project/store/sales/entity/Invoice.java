package com.project.store.sales.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity

@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "accounting_id", nullable = false)
    private Accounting accounting;

    @Column(name="pay_date", nullable = false)
    private LocalDate payDate;

    @Column(name="total_pay", nullable = false)
    private Double totalPay;

    public Invoice(Accounting accounting, LocalDate payDate, Double totalPay) {
        this.accounting = accounting;
        this.payDate = payDate;
        this.totalPay = totalPay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Accounting getAccounting() {
        return accounting;
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public Double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(Double totalPay) {
        this.totalPay = totalPay;
    }

}
