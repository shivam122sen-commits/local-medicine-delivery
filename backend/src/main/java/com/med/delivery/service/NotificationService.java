package com.med.delivery.service;

import com.med.delivery.model.Notification;
import com.med.delivery.model.Order;
import com.med.delivery.model.User;
import com.med.delivery.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(Long userId, String title, String message, Notification.NotificationType type, String relatedEntityId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRelatedEntityId(relatedEntityId);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        
        return notificationRepository.save(notification);
    }

    public void createOrderNotification(Order order) {
        // Notify patient
        createNotification(
            order.getPatientId(),
            "Order Placed!",
            "Your order " + order.getOrderNumber() + " has been placed successfully.",
            Notification.NotificationType.ORDER_PLACED,
            order.getOrderNumber()
        );
        
        // Notify chemist
        if (order.getChemistId() != null) {
            createNotification(
                order.getChemistId(),
                "New Order Received",
                "New order " + order.getOrderNumber() + " requires processing.",
                Notification.NotificationType.NEW_ORDER_FOR_CHEMIST,
                order.getOrderNumber()
            );
        }
    }

    public void createOrderDeliveredNotification(Order order) {
        createNotification(
            order.getPatientId(),
            "Order Delivered!",
            "Your order " + order.getOrderNumber() + " has been delivered.",
            Notification.NotificationType.ORDER_DELIVERED,
            order.getOrderNumber()
        );
    }

    public void createPrescriptionVerifiedNotification(Long patientId, String prescriptionId) {
        createNotification(
            patientId,
            "Prescription Verified",
            "Your prescription has been approved.",
            Notification.NotificationType.PRESCRIPTION_VERIFIED,
            prescriptionId
        );
    }

    public void createLowStockNotification(Long chemistId, String medicineName) {
        createNotification(
            chemistId,
            "Low Stock Alert",
            medicineName + " is running low.",
            Notification.NotificationType.LOW_STOCK_ALERT,
            null
        );
    }

    public void createDeliveryAssignedNotification(Order order, User deliveryPartner) {
        createNotification(
            deliveryPartner.getId(),
            "New Delivery Assigned",
            "Order " + order.getOrderNumber() + " has been assigned to you.",
            Notification.NotificationType.DELIVERY_ASSIGNED,
            order.getOrderNumber()
        );
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false);
    }

    public Long getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false);
        notifications.forEach(n -> {
            n.setIsRead(true);
            n.setReadAt(LocalDateTime.now());
        });
        notificationRepository.saveAll(notifications);
    }
}
