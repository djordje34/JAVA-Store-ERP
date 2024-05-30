package com.project.store.goods.service;

import com.project.store.goods.entity.InventoryItem;
import com.project.store.goods.entity.Product;
import com.project.store.goods.entity.Supplier;
import com.project.store.goods.entity.Warehouse;
import com.project.store.goods.repository.InventoryItemRepository;
import com.project.store.goods.repository.ProductRepository;
import com.project.store.goods.repository.SupplierRepository;
import com.project.store.goods.repository.WarehouseRepository;
import com.project.store.messaging.config.RabbitMQConfigurator;
import com.project.store.messaging.events.ProductEvent;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final WarehouseRepository warehouseRepository;
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    public ProductService(ProductRepository productRepository, InventoryItemRepository inventoryItemRepository, WarehouseRepository warehouseRepository, RabbitTemplate rabbitTemplate) {
        this.productRepository = productRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.warehouseRepository = warehouseRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        ProductEvent productEvent = ProductEvent.createNewProductEvent(product);
        rabbitTemplate.convertAndSend(RabbitMQConfigurator.PRODUCT_TOPIC_EXCHANGE, "products.created", productEvent);
        return savedProduct;
    }

    public void deleteProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        Product product = productOptional.get();
        productRepository.delete(product);
        ProductEvent productEvent = ProductEvent.createDeletedProductEvent(product);
        rabbitTemplate.convertAndSend(RabbitMQConfigurator.PRODUCT_TOPIC_EXCHANGE, "products.deleted", productEvent);
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isEmpty()) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        Product existingProduct = existingProductOptional.get();

        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setUnitOfMeasure(updatedProduct.getUnitOfMeasure());

        Product savedProduct = productRepository.save(existingProduct);
        ProductEvent productEvent = ProductEvent.createUpdatedProductEvent(savedProduct);
        System.out.println(savedProduct);
        rabbitTemplate.convertAndSend(RabbitMQConfigurator.PRODUCT_TOPIC_EXCHANGE, "products.updated", productEvent);
        return savedProduct;

    }

    @Transactional
    public void receptionOfProducts(Supplier supplier, List<InventoryItem> items){ // NIJE TESTIRANO
        LocalDate curr = LocalDate.now();

        for(InventoryItem item : items){
            Product product = item.getProduct();
            if(productRepository.findById(product.getId()).isEmpty()) saveProduct(product);

            inventoryItemRepository.save(item);

            Warehouse warehouse = new Warehouse(item, 1, curr, supplier); // quantity nepotreban
            warehouseRepository.save(warehouse);

            ProductEvent productEvent = ProductEvent.createAvailableProductEvent(product);
            rabbitTemplate.convertAndSend(RabbitMQConfigurator.PRODUCT_TOPIC_EXCHANGE, "products.available", productEvent);
        }
    }


}
