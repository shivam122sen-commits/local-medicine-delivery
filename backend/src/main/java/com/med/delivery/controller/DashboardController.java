package com.med.delivery.controller;

import com.med.delivery.dto.DashboardStats;
import com.med.delivery.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<DashboardStats> getPatientDashboard(@PathVariable Long patientId) {
        return ResponseEntity.ok(dashboardService.getPatientDashboard(patientId));
    }

    @GetMapping("/chemist/{chemistId}")
    public ResponseEntity<DashboardStats> getChemistDashboard(@PathVariable Long chemistId) {
        return ResponseEntity.ok(dashboardService.getChemistDashboard(chemistId));
    }

    @GetMapping("/delivery/{deliveryPartnerId}")
    public ResponseEntity<DashboardStats> getDeliveryPartnerDashboard(@PathVariable Long deliveryPartnerId) {
        return ResponseEntity.ok(dashboardService.getDeliveryPartnerDashboard(deliveryPartnerId));
    }
}
