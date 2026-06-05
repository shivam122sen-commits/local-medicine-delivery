package com.med.delivery.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;
    
    @Column(nullable = false)
    private Boolean isRead = false;
    
    private String relatedEntityId; // e.g., Order ID, Prescription ID
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime readAt;
    
    public enum NotificationType {
        ORDER_PLACED,
        ORDER_DELIVERED,
        PRESCRIPTION_VERIFIED,
        PRESCRIPTION_REJECTED,
        LOW_STOCK_ALERT,
        NEW_ORDER_FOR_CHEMIST,
        DELIVERY_ASSIGNED,
        PAYMENT_RECEIVED,
        GENERAL
    }
}
