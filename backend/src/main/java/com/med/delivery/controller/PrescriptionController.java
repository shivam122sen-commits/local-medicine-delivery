package com.med.delivery.controller;

import com.med.delivery.model.Prescription;
import com.med.delivery.repository.PrescriptionRepository;
import com.med.delivery.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private NotificationService notificationService;

    private final String UPLOAD_DIR = "uploads/prescriptions/";

    @PostMapping("/upload")
    public ResponseEntity<Prescription> uploadPrescription(
            @RequestParam("file") MultipartFile file,
            @RequestParam("patientId") Long patientId) {
        
        try {
            // Create upload directory if not exists
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            
            // Save file
            Files.copy(file.getInputStream(), filePath);

            // Create prescription record
            Prescription prescription = new Prescription();
            prescription.setPatientId(patientId);
            prescription.setFileUrl(filePath.toString());
            prescription.setStatus(Prescription.PrescriptionStatus.PENDING);
            prescription.setUploadedAt(LocalDateTime.now());

            Prescription saved = prescriptionRepository.save(prescription);
            return ResponseEntity.ok(saved);
            
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Prescription>> getPatientPrescriptions(@PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionRepository.findByPatientIdOrderByUploadedAtDesc(patientId));
    }

    @GetMapping("/chemist/{chemistId}")
    public ResponseEntity<List<Prescription>> getChemistPrescriptions(@PathVariable Long chemistId) {
        return ResponseEntity.ok(prescriptionRepository.findByChemistId(chemistId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Prescription>> getPendingPrescriptions() {
        return ResponseEntity.ok(prescriptionRepository.findPendingPrescriptionsOrderByUploadedAt(
                Prescription.PrescriptionStatus.PENDING));
    }

    @PutMapping("/{prescriptionId}/verify")
    public ResponseEntity<Prescription> verifyPrescription(
            @PathVariable Long prescriptionId,
            @RequestParam Long chemistId,
            @RequestParam String verifiedBy) {
        
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        
        prescription.setStatus(Prescription.PrescriptionStatus.VERIFIED);
        prescription.setChemistId(chemistId);
        prescription.setVerifiedBy(verifiedBy);
        prescription.setVerifiedAt(LocalDateTime.now());
        
        Prescription updated = prescriptionRepository.save(prescription);
        
        // Send notification to patient
        notificationService.createPrescriptionVerifiedNotification(
                prescription.getPatientId(), prescriptionId.toString());
        
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{prescriptionId}/reject")
    public ResponseEntity<Prescription> rejectPrescription(
            @PathVariable Long prescriptionId,
            @RequestParam Long chemistId,
            @RequestParam String reason) {
        
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        
        prescription.setStatus(Prescription.PrescriptionStatus.REJECTED);
        prescription.setChemistId(chemistId);
        prescription.setRejectionReason(reason);
        prescription.setVerifiedAt(LocalDateTime.now());
        
        return ResponseEntity.ok(prescriptionRepository.save(prescription));
    }
}
