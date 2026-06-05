package com.med.delivery.controller;

import com.med.delivery.dto.CreateOrderRequest;
import com.med.delivery.model.Order;
import com.med.delivery.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            Order order = orderService.createOrder(request);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Order>> getPatientOrders(@PathVariable Long patientId) {
        return ResponseEntity.ok(orderService.getPatientOrders(patientId));
    }

    @GetMapping("/chemist/{chemistId}")
    public ResponseEntity<List<Order>> getChemistOrders(@PathVariable Long chemistId) {
        return ResponseEntity.ok(orderService.getChemistOrders(chemistId));
    }

    @GetMapping("/delivery/{deliveryPartnerId}")
    public ResponseEntity<List<Order>> getDeliveryPartnerOrders(@PathVariable Long deliveryPartnerId) {
        return ResponseEntity.ok(orderService.getDeliveryPartnerOrders(deliveryPartnerId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(orderService.getOrderById(orderId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<Order> getOrderByNumber(@PathVariable String orderNumber) {
        try {
            return ResponseEntity.ok(orderService.getOrderByOrderNumber(orderNumber));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Order.Status status,
            @RequestParam(required = false) Long deliveryPartnerId) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, status, deliveryPartnerId);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable Order.Status status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @GetMapping("/available-for-pickup")
    public ResponseEntity<List<Order>> getAvailableOrders() {
        return ResponseEntity.ok(orderService.getOrdersByStatus(Order.Status.PACKED));
    }
}