package com.med.delivery.repository;

import com.med.delivery.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByPatientId(Long patientId);
    List<Order> findByChemistId(Long chemistId);
    List<Order> findByDeliveryPartnerId(Long deliveryPartnerId);
    List<Order> findByStatus(Order.Status status);
    Optional<Order> findByOrderNumber(String orderNumber);
    
    List<Order> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<Order> findByChemistIdOrderByCreatedAtDesc(Long chemistId);
    List<Order> findByDeliveryPartnerIdOrderByCreatedAtDesc(Long deliveryPartnerId);
    
    List<Order> findByPatientIdAndStatus(Long patientId, Order.Status status);
    List<Order> findByChemistIdAndStatus(Long chemistId, Order.Status status);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.patientId = ?1")
    Long countByPatientId(Long patientId);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.chemistId = ?1")
    Long countByChemistId(Long chemistId);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.deliveryPartnerId = ?1")
    Long countByDeliveryPartnerId(Long deliveryPartnerId);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.deliveryPartnerId = ?1 AND o.status = ?2")
    Long countByDeliveryPartnerIdAndStatus(Long deliveryPartnerId, Order.Status status);
    
    @Query("SELECT o FROM Order o WHERE o.chemistId = ?1 AND o.createdAt >= ?2")
    List<Order> findByChemistIdAndCreatedAtAfter(Long chemistId, LocalDateTime date);
    
    @Query("SELECT SUM(o.total) FROM Order o WHERE o.chemistId = ?1 AND o.createdAt >= ?2 AND o.status = ?3")
    Double sumTotalByChemistIdAndCreatedAtAfterAndStatus(Long chemistId, LocalDateTime date, Order.Status status);
    
    @Query("SELECT SUM(o.deliveryFee) FROM Order o WHERE o.deliveryPartnerId = ?1 AND o.status = ?2")
    Double sumDeliveryFeeByDeliveryPartnerIdAndStatus(Long deliveryPartnerId, Order.Status status);
}
