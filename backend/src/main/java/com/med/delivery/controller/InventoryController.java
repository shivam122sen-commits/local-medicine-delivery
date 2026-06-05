package com.med.delivery.controller;

import com.med.delivery.model.Inventory;
import com.med.delivery.repository.InventoryRepository;
import com.med.delivery.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/add")
    public ResponseEntity<Inventory> addMedicine(@RequestBody Inventory item) {
        item.setCreatedAt(LocalDateTime.now());
        Inventory saved = inventoryRepository.save(item);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateMedicine(@PathVariable Long id, @RequestBody Inventory item) {
        Inventory existing = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
        
        existing.setMedicineName(item.getMedicineName());
        existing.setCategory(item.getCategory());
        existing.setManufacturer(item.getManufacturer());
        existing.setBatchNumber(item.getBatchNumber());
        existing.setMrp(item.getMrp());
        existing.setSellingPrice(item.getSellingPrice());
        existing.setStock(item.getStock());
        existing.setExpiryDate(item.getExpiryDate());
        existing.setPrescriptionRequired(item.getPrescriptionRequired());
        existing.setDescription(item.getDescription());
        existing.setImageUrl(item.getImageUrl());
        existing.setUpdatedAt(LocalDateTime.now());
        
        // Check for low stock and send notification
        if (existing.isLowStock()) {
            notificationService.createLowStockNotification(existing.getChemistId(), existing.getMedicineName());
        }
        
        return ResponseEntity.ok(inventoryRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        inventoryRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/chemist/{chemistId}")
    public ResponseEntity<List<Inventory>> getChemistInventory(@PathVariable Long chemistId) {
        return ResponseEntity.ok(inventoryRepository.findByChemistId(chemistId));
    }

    @GetMapping("/chemist/{chemistId}/low-stock")
    public ResponseEntity<List<Inventory>> getLowStockItems(@PathVariable Long chemistId) {
        return ResponseEntity.ok(inventoryRepository.findLowStockByChemistId(chemistId));
    }

    @GetMapping("/chemist/{chemistId}/out-of-stock")
    public ResponseEntity<List<Inventory>> getOutOfStockItems(@PathVariable Long chemistId) {
        return ResponseEntity.ok(inventoryRepository.findOutOfStockByChemistId(chemistId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Inventory>> getAllMedicines() {
        return ResponseEntity.ok(inventoryRepository.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Inventory>> searchMedicines(@RequestParam String query) {
        return ResponseEntity.ok(inventoryRepository.findByMedicineNameContainingIgnoreCase(query));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Inventory>> getMedicinesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(inventoryRepository.findByCategory(category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getMedicineById(@PathVariable Long id) {
        return inventoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}