package com.med.delivery.repository;

import com.med.delivery.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByPatientId(Long patientId);
    List<Order> findByChemistId(Long chemistId);
    List<Order> findByStatus(Order.Status status);
}