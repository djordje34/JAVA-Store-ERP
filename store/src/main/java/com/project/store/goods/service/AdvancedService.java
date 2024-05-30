package com.project.store.goods.service;

import com.project.store.goods.entity.InventoryItem;
import com.project.store.goods.entity.Product;
import com.project.store.goods.entity.Supplier;
import com.project.store.goods.entity.Warehouse;
import com.project.store.goods.repository.InventoryItemRepository;
import com.project.store.goods.repository.ProductRepository;
import com.project.store.goods.repository.WarehouseRepository;
import com.project.store.messaging.config.RabbitMQConfigurator;
import com.project.store.messaging.events.ProductEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdvancedService {
    private final ProductRepository productRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final WarehouseRepository warehouseRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AdvancedService(ProductRepository productRepository, InventoryItemRepository inventoryItemRepository, WarehouseRepository warehouseRepository, RabbitTemplate rabbitTemplate) {
        this.productRepository = productRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.warehouseRepository = warehouseRepository;
        this.rabbitTemplate = rabbitTemplate;
    }
    @Transactional
    public void receptionOfProducts(Supplier supplier, List<InventoryItem> items){ // NIJE TESTIRANO
        LocalDate curr = LocalDate.now();

        for(InventoryItem item : items){
            Product product = item.getProduct();
            Optional<Product> existingProduct = productRepository.findById(product.getId());

            if (existingProduct.isEmpty()) {
                throw new IllegalArgumentException("Product with ID " + product.getId() + " does not exist");
            }
            inventoryItemRepository.save(item);

            Warehouse warehouse = new Warehouse(item, 1, curr, supplier); // quantity nepotreban
            warehouseRepository.save(warehouse);

            ProductEvent productEvent = ProductEvent.createAvailableProductEvent(product);
            rabbitTemplate.convertAndSend(RabbitMQConfigurator.PRODUCT_TOPIC_EXCHANGE, "products.available", productEvent);
        }
    }

    public boolean getProductState(Long id){
        List<Warehouse> warehouses = warehouseRepository.findAll();

        for(Warehouse warehouse : warehouses){
            if(Objects.equals(warehouse.getInventoryItem().getProduct().getId(), id)) return true;
        }
        return false;
    }

    // default impl za formPrice
    public Double formPrice(Warehouse warehouse){
        return warehouse.getInventoryItem().getPurchasePrice()* 1.2;
    }
    //spec. impl
    public Double formPrice(Warehouse warehouse, Double factor){
        return warehouse.getInventoryItem().getPurchasePrice() * factor;
    }
}
