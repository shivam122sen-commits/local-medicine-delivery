package com.med.delivery.repository;

import com.med.delivery.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatientIdOrderByUploadedAtDesc(Long patientId);
    List<Prescription> findByChemistId(Long chemistId);
    List<Prescription> findByStatus(Prescription.PrescriptionStatus status);
    
    @Query("SELECT COUNT(p) FROM Prescription p WHERE p.chemistId = ?1 AND p.status = ?2")
    Long countByChemistIdAndStatus(Long chemistId, Prescription.PrescriptionStatus status);
    
    @Query("SELECT p FROM Prescription p WHERE p.status = ?1 ORDER BY p.uploadedAt ASC")
    List<Prescription> findPendingPrescriptionsOrderByUploadedAt(Prescription.PrescriptionStatus status);
}
