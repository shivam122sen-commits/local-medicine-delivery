package com.med.delivery.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private String password; // In production, use BCrypt encryption
    
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        PATIENT, CHEMIST, DELIVERY
    }
}