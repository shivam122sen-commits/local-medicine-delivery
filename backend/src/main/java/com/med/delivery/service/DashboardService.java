package com.med.delivery.service;

import com.med.delivery.dto.DashboardStats;
import com.med.delivery.model.Order;
import com.med.delivery.model.Prescription;
import com.med.delivery.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DashboardService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private UserRepository userRepository;

    public DashboardStats getPatientDashboard(Long patientId) {
        DashboardStats stats = new DashboardStats();
        
        stats.setTotalOrders(orderRepository.countByPatientId(patientId));
        stats.setActiveOrders(orderRepository.findByPatientIdAndStatus(patientId, Order.Status.OUT_FOR_DELIVERY).size() + 
                             orderRepository.findByPatientIdAndStatus(patientId, Order.Status.PACKED).size() + 0L);
        stats.setPendingOrders(orderRepository.findByPatientIdAndStatus(patientId, Order.Status.PENDING).size() + 
                              orderRepository.findByPatientIdAndStatus(patientId, Order.Status.APPROVED).size() + 0L);
        stats.setDeliveredOrders(orderRepository.findByPatientIdAndStatus(patientId, Order.Status.DELIVERED).size() + 0L);
        
        return stats;
    }

    public DashboardStats getChemistDashboard(Long chemistId) {
        DashboardStats stats = new DashboardStats();
        
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        
        // Order stats
        stats.setTotalOrders(orderRepository.countByChemistId(chemistId));
        
        // Today's orders
        Long todayOrders = orderRepository.findByChemistIdAndCreatedAtAfter(chemistId, todayStart).size() + 0L;
        
        // Revenue
        Double totalRevenue = orderRepository.sumTotalByChemistIdAndCreatedAtAfterAndStatus(
            chemistId, todayStart, Order.Status.DELIVERED);
        stats.setTodayRevenue(totalRevenue != null ? totalRevenue : 0.0);
        
        // Low stock
        stats.setLowStockItems(inventoryRepository.countLowStockByChemistId(chemistId));
        
        // Pending prescriptions
        stats.setPendingPrescriptions(prescriptionRepository.countByChemistIdAndStatus(
            chemistId, Prescription.PrescriptionStatus.PENDING));
        
        return stats;
    }

    public DashboardStats getDeliveryPartnerDashboard(Long deliveryPartnerId) {
        DashboardStats stats = new DashboardStats();
        
        // Total deliveries
        stats.setTotalOrders(orderRepository.countByDeliveryPartnerId(deliveryPartnerId));
        
        // Today's deliveries
        stats.setTodayDeliveries(orderRepository.countByDeliveryPartnerIdAndStatus(
            deliveryPartnerId, Order.Status.DELIVERED));
        
        // Earnings
        Double totalEarnings = orderRepository.sumDeliveryFeeByDeliveryPartnerIdAndStatus(
            deliveryPartnerId, Order.Status.DELIVERED);
        stats.setTodayEarnings(totalEarnings != null ? totalEarnings : 0.0);
        
        // Rating
        userRepository.findById(deliveryPartnerId).ifPresent(user -> {
            stats.setRating(user.getRating());
        });
        
        return stats;
    }
}
