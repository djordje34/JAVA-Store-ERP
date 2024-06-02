package com.project.store.goods.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "warehouses")
public class Warehouse implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inventory_item_id")
    private InventoryItem inventoryItem;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "addition_date")
    private LocalDate additionDate;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    public Warehouse(){

    }
    public Warehouse(InventoryItem inventoryItem, Integer quantity, LocalDate additionDate, Supplier supplier) {
        this.inventoryItem = inventoryItem;
        this.quantity = quantity;
        this.additionDate = additionDate;
        this.supplier = supplier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getAdditionDate() {
        return additionDate;
    }

    public void setAdditionDate(LocalDate additionDate) {
        this.additionDate = additionDate;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
