package com.med.delivery.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber; // e.g., MED-2047

    @Column(nullable = false)
    private Long patientId;
    
    private Long chemistId;
    
    private Long deliveryPartnerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    @Column(nullable = false)
    private Double subtotal;
    
    @Column(nullable = false)
    private Double deliveryFee = 0.0;
    
    @Column(nullable = false)
    private Double total;
    
    private String deliveryAddress;
    
    private String prescriptionUrl; // Path to uploaded prescription
    
    @Column(nullable = false)
    private Boolean prescriptionRequired = false;
    
    @Enumerated(EnumType.STRING)
    private PrescriptionStatus prescriptionStatus;
    
    private String prescriptionVerifiedBy; // Pharmacist name
    
    private LocalDateTime prescriptionVerifiedAt;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime approvedAt;
    
    private LocalDateTime packedAt;
    
    private LocalDateTime outForDeliveryAt;
    
    private LocalDateTime deliveredAt;
    
    private LocalDateTime estimatedDeliveryTime;
    
    // GPS coordinates
    private Double deliveryLatitude;
    
    private Double deliveryLongitude;
    
    // Rating
    private Integer rating; // 1-5
    
    private String reviewComment;
    
    // Emergency delivery flag
    @Column(nullable = false)
    private Boolean isEmergency = false;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items;

    public enum Status {
        PENDING,
        PRESCRIPTION_VERIFIED,
        APPROVED,
        PACKED,
        DELIVERY_PARTNER_ASSIGNED,
        OUT_FOR_DELIVERY,
        DELIVERED,
        CANCELLED
    }
    
    public enum PrescriptionStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}


