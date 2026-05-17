package com.med.delivery.controller;

import com.med.delivery.model.Order;
import com.med.delivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/create")
    public Order createOrder(@RequestBody Order order) {
        order.setStatus(Order.Status.PENDING);
        return orderRepository.save(order);
    }

    @GetMapping("/patient/{patientId}")
    public List<Order> getPatientOrders(@PathVariable Long patientId) {
        return orderRepository.findByPatientId(patientId);
    }

    @GetMapping("/chemist/{chemistId}")
    public List<Order> getChemistOrders(@PathVariable Long chemistId) {
        return orderRepository.findByChemistId(chemistId);
    }

    @GetMapping("/ready-for-pickup")
    public List<Order> getApprovedOrders() {
        return orderRepository.findByStatus(Order.Status.APPROVED);
    }

    @PutMapping("/update-status/{orderId}")
    public Order updateStatus(@PathVariable Long orderId, @RequestParam Order.Status status, @RequestParam(required = false) Long deliveryPartnerId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        if (deliveryPartnerId != null) {
            order.setDeliveryPartnerId(deliveryPartnerId);
        }
        return orderRepository.save(order);
    }
}