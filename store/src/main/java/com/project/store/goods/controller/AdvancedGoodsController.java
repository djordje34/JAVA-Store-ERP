package com.project.store.goods.controller;

import com.project.store.goods.entity.InventoryItem;
import com.project.store.goods.entity.Product;
import com.project.store.goods.entity.Supplier;
import com.project.store.goods.entity.Warehouse;
import com.project.store.goods.service.AdvancedGoodsService;
import com.project.store.goods.service.ProductService;
import com.project.store.goods.service.SupplierService;
import com.project.store.goods.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/advancedGoods")
public class AdvancedGoodsController {

    private final AdvancedGoodsService advancedGoodsService;
    private final SupplierService supplierService;
    private final ProductService productService;
    private final WarehouseService warehouseService;

    @Autowired
    public AdvancedGoodsController(AdvancedGoodsService advancedGoodsService, SupplierService supplierService, ProductService productService, WarehouseService warehouseService) {
        this.advancedGoodsService = advancedGoodsService;
        this.supplierService = supplierService;
        this.productService = productService;
        this.warehouseService = warehouseService;
    }

    @PostMapping("/receptionOfProducts") // za cenu nadji max cenu za produkt i dodaj marginu
    public ResponseEntity<?> updateInventory(@RequestBody Map<String, Object> requestBody) {
        Long supplierId = ((Integer) requestBody.get("supplierId")).longValue();
        List<Map<String, Object>> inventoryItemMaps = (List<Map<String, Object>>) requestBody.get("inventoryItems");

        Optional<Supplier> existingSupplier = supplierService.getSupplierById(supplierId);

        if (existingSupplier.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supplier not found");
        }

        for (Map<String, Object> inventoryItemMap : inventoryItemMaps) {
            Map<String, Object> productMap = (Map<String, Object>) inventoryItemMap.get("product");
            Long productId = ((Integer) productMap.get("id")).longValue();

            if (productService.getProductById(productId).isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product with ID " + productId + " does not exist");
            }
        }

        List<InventoryItem> inventoryItems = convertToInventoryItems(inventoryItemMaps);
        advancedGoodsService.receptionOfProducts(existingSupplier.get(), inventoryItems);
        return ResponseEntity.ok(inventoryItems);
    }

    @GetMapping("/getProductState")
    public ResponseEntity<Boolean> getProductState(@PathVariable Long id){
        return ResponseEntity.ok((Boolean) advancedGoodsService.getProductState(id));
    }

    @GetMapping("/getProductPrice")
    public ResponseEntity<Double> getProductPrice(@RequestParam(defaultValue = "1.2", name = "factor") Double factor, @RequestParam(required = true, name = "id") Long id){
        Optional<Warehouse> warehouseOptional = warehouseService.getWarehouseById(id);
        if (warehouseOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.ok(advancedGoodsService.formPrice(warehouseOptional.get(), factor));
    }

    //utility methods
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
