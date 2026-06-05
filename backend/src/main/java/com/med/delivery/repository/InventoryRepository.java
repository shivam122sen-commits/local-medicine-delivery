package com.med.delivery.repository;

import com.med.delivery.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByChemistId(Long chemistId);
    List<Inventory> findByCategory(String category);
    List<Inventory> findByMedicineNameContainingIgnoreCase(String medicineName);
    
    @Query("SELECT i FROM Inventory i WHERE i.chemistId = ?1 AND i.stock <= i.lowStockThreshold")
    List<Inventory> findLowStockByChemistId(Long chemistId);
    
    @Query("SELECT i FROM Inventory i WHERE i.chemistId = ?1 AND i.stock = 0")
    List<Inventory> findOutOfStockByChemistId(Long chemistId);
    
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.chemistId = ?1 AND i.stock <= i.lowStockThreshold")
    Long countLowStockByChemistId(Long chemistId);
    
    List<Inventory> findByChemistIdAndCategory(Long chemistId, String category);
}
