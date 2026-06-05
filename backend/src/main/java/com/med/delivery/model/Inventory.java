package com.med.delivery.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long chemistId;
    
    @Column(nullable = false)
    private String medicineName;
    
    @Column(nullable = false)
    private String category; // Tablets, Capsules, Syrups, Injections, Supplements, etc.
    
    private String manufacturer;
    
    private String batchNumber;
    
    @Column(nullable = false)
    private Double mrp; // Maximum Retail Price
    
    @Column(nullable = false)
    private Double sellingPrice;
    
    @Column(nullable = false)
    private Integer stock;
    
    private LocalDateTime expiryDate;
    
    @Column(nullable = false)
    private Boolean prescriptionRequired = false;
    
    private String description;
    
    private String imageUrl;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt;
    
    // Low stock threshold
    private Integer lowStockThreshold = 10;
    
    @Transient
    public boolean isLowStock() {
        return stock != null && stock <= lowStockThreshold;
    }
    
    @Transient
    public boolean isOutOfStock() {
        return stock != null && stock == 0;
    }
}

