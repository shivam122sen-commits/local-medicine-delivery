package com.med.delivery.service;

import com.med.delivery.dto.CreateOrderRequest;
import com.med.delivery.model.Order;
import com.med.delivery.model.OrderItem;
import com.med.delivery.model.User;
import com.med.delivery.repository.OrderRepository;
import com.med.delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        Order order = new Order();
        
        // Generate unique order number
        order.setOrderNumber("MED-" + (1000 + new Random().nextInt(9000)));
        order.setPatientId(request.getPatientId());
        order.setChemistId(request.getChemistId());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setPrescriptionRequired(request.getPrescriptionRequired());
        order.setPrescriptionUrl(request.getPrescriptionUrl());
        order.setIsEmergency(request.getIsEmergency() != null ? request.getIsEmergency() : false);
        order.setStatus(Order.Status.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        
        // Calculate pricing
        double subtotal = 0.0;
        List<OrderItem> items = new ArrayList<>();
        
        for (CreateOrderRequest.OrderItemDTO itemDTO : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setInventoryId(itemDTO.getInventoryId());
            item.setMedicineName(itemDTO.getMedicineName());
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(itemDTO.getUnitPrice());
            item.setTotalPrice(itemDTO.getQuantity() * itemDTO.getUnitPrice());
            item.setImageUrl(itemDTO.getImageUrl());
            
            subtotal += item.getTotalPrice();
            items.add(item);
        }
        
        order.setSubtotal(subtotal);
        order.setDeliveryFee(0.0); // FREE delivery as per frontend
        order.setTotal(subtotal);
        order.setItems(items);
        
        // Set estimated delivery time (10 minutes)
        order.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(10));
        
        Order savedOrder = orderRepository.save(order);
        
        // Create notifications
        notificationService.createOrderNotification(savedOrder);
        
        return savedOrder;
    }

    public List<Order> getPatientOrders(Long patientId) {
        return orderRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    public List<Order> getChemistOrders(Long chemistId) {
        return orderRepository.findByChemistIdOrderByCreatedAtDesc(chemistId);
    }

    public List<Order> getDeliveryPartnerOrders(Long deliveryPartnerId) {
        return orderRepository.findByDeliveryPartnerIdOrderByCreatedAtDesc(deliveryPartnerId);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, Order.Status newStatus, Long deliveryPartnerId) {
        Order order = getOrderById(orderId);
        order.setStatus(newStatus);
        
        LocalDateTime now = LocalDateTime.now();
        
        switch (newStatus) {
            case PRESCRIPTION_VERIFIED:
                order.setPrescriptionVerifiedAt(now);
                order.setPrescriptionStatus(Order.PrescriptionStatus.APPROVED);
                break;
            case APPROVED:
                order.setApprovedAt(now);
                break;
            case PACKED:
                order.setPackedAt(now);
                break;
            case DELIVERY_PARTNER_ASSIGNED:
                if (deliveryPartnerId != null) {
                    order.setDeliveryPartnerId(deliveryPartnerId);
                    User deliveryPartner = userRepository.findById(deliveryPartnerId)
                            .orElseThrow(() -> new RuntimeException("Delivery partner not found"));
                    notificationService.createDeliveryAssignedNotification(order, deliveryPartner);
                }
                break;
            case OUT_FOR_DELIVERY:
                order.setOutForDeliveryAt(now);
                order.setEstimatedDeliveryTime(now.plusMinutes(8));
                break;
            case DELIVERED:
                order.setDeliveredAt(now);
                notificationService.createOrderDeliveredNotification(order);
                break;
        }
        
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByStatus(Order.Status status) {
        return orderRepository.findByStatus(status);
    }
}
