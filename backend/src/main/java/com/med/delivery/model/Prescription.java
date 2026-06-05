package com.med.delivery.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long patientId;
    
    private Long chemistId; // Who verified it
    
    @Column(nullable = false)
    private String fileUrl; // Path to uploaded prescription image/PDF
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrescriptionStatus status = PrescriptionStatus.PENDING;
    
    private String verifiedBy; // Pharmacist name
    
    private LocalDateTime verifiedAt;
    
    private String rejectionReason;
    
    @Column(nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();
    
    private Long orderId; // Linked order if created from prescription
    
    public enum PrescriptionStatus {
        PENDING,
        VERIFIED,
        REJECTED
    }
}
