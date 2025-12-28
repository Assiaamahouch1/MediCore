package com.yourorg.healthcare.patient.repository;

import com.yourorg.healthcare.patient.model.DocumentMedical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentMedicalRepository extends JpaRepository<DocumentMedical, UUID> {
    List<DocumentMedical> findByPatientId(UUID patientId);
    List<DocumentMedical> findByDossierMedicalId(UUID dossierId);
}