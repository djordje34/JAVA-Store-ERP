package com.project.store.goods.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "inventory_items")    //postavi bidir. assoc sa warehouse
public class InventoryItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    @Column(name = "purchase_price", nullable = false)
    private Double purchasePrice;

    public InventoryItem(){

    }
    public InventoryItem(Product product, Double purchasePrice) {
        this.product = product;
        this.purchasePrice = purchasePrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
}
