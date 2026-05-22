package com.med.delivery.repository;

import com.med.delivery.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByChemistId(Long chemistId);
}
