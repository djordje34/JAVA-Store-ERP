package com.project.store.goods.controller;

import com.project.store.goods.entity.InventoryItem;
import com.project.store.goods.entity.Product;
import com.project.store.goods.entity.Supplier;
import com.project.store.goods.service.ProductService;
import com.project.store.goods.service.SupplierService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final SupplierService supplierService;
    @Autowired
    public ProductController(ProductService productService, SupplierService supplierService) {
        this.productService = productService;
        this.supplierService = supplierService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return ResponseEntity.ok(product.orElse(null));
    }

    @PostMapping("/")
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/receptionOfProducts")
    public ResponseEntity<List<InventoryItem>> updateInventory(@RequestBody Map<String, Object> requestBody) throws EntityNotFoundException {
        Long supplierId = ((Integer) requestBody.get("supplierId")).longValue();
        List<Map<String, Object>> inventoryItemMaps = (List<Map<String, Object>>) requestBody.get("inventoryItems");

        Optional<Supplier> existingSupplier = supplierService.getSupplierById(supplierId);

        if (existingSupplier.isEmpty()) throw new EntityNotFoundException();

        List<InventoryItem> inventoryItems = convertToInventoryItems(inventoryItemMaps);
        productService.receptionOfProducts(existingSupplier.get(), inventoryItems);
        return ResponseEntity.ok(inventoryItems);
    }

    private List<InventoryItem> convertToInventoryItems(List<Map<String, Object>> inventoryItemMaps) {
        List<InventoryItem> list = new ArrayList<>();
        for (Map<String, Object> inventoryItemMap : inventoryItemMaps) {
            InventoryItem inventoryItem = new InventoryItem();
            inventoryItem.setPurchasePrice((Double) inventoryItemMap.get("purchasePrice"));

            Map<String, Object> productMap = (Map<String, Object>) inventoryItemMap.get("product");
            Product product = new Product();
            product.setId(((Integer) productMap.get("id")).longValue());
            product.setProductName((String) productMap.get("productName"));
            product.setUnitOfMeasure((String) productMap.get("unitOfMeasure"));

            inventoryItem.setProduct(product);
            list.add(inventoryItem);
        }
        return list;
    }

}
