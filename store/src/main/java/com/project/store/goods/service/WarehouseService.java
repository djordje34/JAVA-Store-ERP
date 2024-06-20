package com.project.store.goods.service;

import com.project.store.goods.entity.Warehouse;
import com.project.store.goods.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Autowired
    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public Optional<Warehouse> getWarehouseById(Long id) {
        return warehouseRepository.findById(id);
    }

    public Warehouse saveWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    public void deleteWarehouse(Long id) {
        Optional<Warehouse> warehouse = warehouseRepository.findById(id);
        warehouse.ifPresent(warehouseRepository::delete);
    }

    public Integer findQuantityByProductId(Long id) {
        return warehouseRepository.findQuantityByProductId(id);
    }


    public Double findAveragePurchasePrice(Long id) {
        return warehouseRepository.findAveragePurchasePrice(id);
    }
}
