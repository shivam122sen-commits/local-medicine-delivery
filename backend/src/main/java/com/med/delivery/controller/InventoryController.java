package com.med.delivery.controller;

import com.med.delivery.model.Inventory;
import com.med.delivery.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping("/add")
    public Inventory addMedicine(@RequestBody Inventory item) {
        return inventoryRepository.save(item);
    }

    @GetMapping("/chemist/{chemistId}")
    public List<Inventory> getChemistInventory(@PathVariable Long chemistId) {
        return inventoryRepository.findByChemistId(chemistId);
    }

    @GetMapping("/all")
    public List<Inventory> getAllMedicines() {
        return inventoryRepository.findAll();
    }
}