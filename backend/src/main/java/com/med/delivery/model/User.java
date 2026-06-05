package com.med.delivery.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    
    private String mobile;
    
    private String address;
    
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    private String profileImage; // URL or path
    
    private Double rating; // For chemist and delivery partners
    
    private Integer totalDeliveries; // For delivery partners
    
    private String vehicleNumber; // For delivery partners
    
    private String vehicleType; // For delivery partners
    
    private String pharmacyName; // For chemists
    
    private String licenseNumber; // For chemists
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column(nullable = false)
    private Boolean isOnline = false; // For delivery partners
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt;
    
    // Two-factor authentication
    private Boolean twoFactorEnabled = false;
    
    // Remember me token
    private String rememberMeToken;

    public enum Role {
        PATIENT, CHEMIST, DELIVERY
    }
}

