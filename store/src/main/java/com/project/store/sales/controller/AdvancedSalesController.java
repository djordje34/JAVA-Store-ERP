package com.project.store.sales.controller;

import com.project.store.goods.entity.Product;
import com.project.store.sales.entity.Customer;
import com.project.store.sales.service.AdvancedSalesService;
import com.project.store.sales.service.CustomerService;
import com.project.store.sales.service.OrderItemService;
import com.project.store.sales.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/advancedSales")
public class AdvancedSalesController {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final AdvancedSalesService advancedSalesService;


    public AdvancedSalesController(CustomerService customerService, OrderService orderService, OrderItemService orderItemService, AdvancedSalesService advancedSalesService) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.advancedSalesService = advancedSalesService;
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> requestBody) {
        Long customerId = ((Integer) requestBody.get("customerId")).longValue();
        List<Map<String, Object>> productMaps = (List<Map<String, Object>>) requestBody.get("products");

        Optional<Customer> existingCustomer = customerService.getCustomerById(customerId);
        if (existingCustomer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        List<Product> products = new ArrayList<>();
        for (Map<String, Object> productMap : productMaps) {
            Product product = new Product();
            product.setId(((Integer) productMap.get("id")).longValue());
            product.setProductName((String) productMap.get("productName"));
            product.setUnitOfMeasure((String) productMap.get("unitOfMeasure"));

            products.add(product);
        }

        advancedSalesService.placeOrder(existingCustomer.get(), products);
        return ResponseEntity.ok("Order pending");
    }
}
