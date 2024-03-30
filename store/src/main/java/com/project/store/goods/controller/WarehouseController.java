package com.project.store.goods.controller;

import com.project.store.goods.entity.Warehouse;
import com.project.store.goods.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService){
        this.warehouseService = warehouseService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Warehouse>> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable Long id) {
        Optional<Warehouse> warehouse = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(warehouse.orElse(null));
    }

    @PostMapping("/")
    public ResponseEntity<Warehouse> saveWarehouse(@RequestBody Warehouse warehouse) {
        Warehouse savedWarehouse = warehouseService.saveWarehouse(warehouse);
        return ResponseEntity.ok(savedWarehouse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/inventoryItems/products/:product_id/quantity")
    public ResponseEntity<Integer> findQuantityByProductId(Long id){
        Integer val = warehouseService.findQuantityByProductId(id);
        return ResponseEntity.ok(val);
    }

    @GetMapping("/inventoryItems/products/:product_id/averageBuyPrice")
    public ResponseEntity<Double> findAveragePurchasePrice(Long id){
        Double val = warehouseService.findAveragePurchasePrice(id);
        return ResponseEntity.ok(val);
    }

}
