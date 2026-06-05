package com.med.delivery.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private String label; // Home, Work, etc.
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String addressLine;
    
    private String city;
    
    private String state;
    
    private String pincode;
    
    private Double latitude;
    
    private Double longitude;
    
    @Column(nullable = false)
    private Boolean isDefault = false;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
