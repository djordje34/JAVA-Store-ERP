package com.project.store.sales.controller;

import com.project.store.goods.entity.Product;
import com.project.store.sales.entity.Customer;
import com.project.store.sales.entity.Order;
import com.project.store.sales.service.CustomerService;
import com.project.store.sales.service.OrderItemService;
import com.project.store.sales.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/advancedSales")
public class AdvancedSalesController {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;


    public AdvancedSalesController(CustomerService customerService, OrderService orderService, OrderItemService orderItemService) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> requestBody) {
        return null;
    }
}
