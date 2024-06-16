package com.project.store.sales.controller;

import com.project.store.goods.entity.Product;
import com.project.store.sales.entity.Accounting;
import com.project.store.sales.entity.Customer;
import com.project.store.sales.entity.Invoice;
import com.project.store.sales.service.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("api/advancedSales")
public class AdvancedSalesController {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final AdvancedSalesService advancedSalesService;
    private final AccountingService accountingService;
    private final RabbitTemplate rabbitTemplate;



    public AdvancedSalesController(CustomerService customerService, OrderService orderService, OrderItemService orderItemService, AdvancedSalesService advancedSalesService, AccountingService accountingService, RabbitTemplate rabbitTemplate) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.advancedSalesService = advancedSalesService;
        this.accountingService = accountingService;
        this.rabbitTemplate = rabbitTemplate;
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

    @Scheduled(cron = "0 * * * * *")    //stavljeno svaki min -> ease of testing
    public void checkExpiredAccountings() {
        System.out.println("Checking for expired accountings...");
        advancedSalesService.checkExpiredAccountings();
    }

    @PostMapping("/payAccounting")
    public ResponseEntity<?> payAccounting(@RequestBody Map<String, ?> requestBody) { // ovde long id jer je JSON predugacak
        Long id = ((Integer) requestBody.get("id")).longValue();
        Double totalPay = (Double) requestBody.get("pay");
        Optional<Accounting> optAccounting = accountingService.getAccountingById(id);
        if (optAccounting.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Accounting not found");

        Accounting accounting = optAccounting.get();

        boolean state = accounting.getState() == 0;
        if (!state)
            return ResponseEntity.status(HttpStatus.OK).body("Accounting pay date has passed");

        Double priceDiff = totalPay - accounting.getTotalPrice();
        if (priceDiff < 0.0)
            return ResponseEntity.status(HttpStatus.OK).body("Requested sum is "+accounting.getTotalPrice().toString()+", you provided "+totalPay.toString());

        Invoice invoice = advancedSalesService.payAccounting(accounting, totalPay);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("invoice", invoice);
        map.put("change", priceDiff);
        return ResponseEntity.ok(map);
    }
}
