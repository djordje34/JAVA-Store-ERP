package com.project.store.goods.service;

import com.project.store.goods.entity.Product;
import com.project.store.goods.repository.ProductRepository;
import com.project.store.messaging.config.RabbitMQConfigurator;
import com.project.store.messaging.events.ProductEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    public ProductService(ProductRepository productRepository, RabbitTemplate rabbitTemplate) {
        this.productRepository = productRepository;
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
        System.out.println(savedProduct);
        rabbitTemplate.convertAndSend(RabbitMQConfigurator.PRODUCT_TOPIC_EXCHANGE, "products.created", productEvent);
        return savedProduct;
    }

    public void deleteProduct(Long id){
        Optional<Product> product = productRepository.findById(id);

        product.ifPresent(productRepository::delete);
    }
}
