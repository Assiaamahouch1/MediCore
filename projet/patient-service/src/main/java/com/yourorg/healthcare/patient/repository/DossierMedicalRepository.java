package com.yourorg.healthcare.patient.repository;
import com.yourorg.healthcare.patient.model.DossierMedical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DossierMedicalRepository extends JpaRepository<DossierMedical, UUID> {
    List<DossierMedical> findByPatientId(UUID patientId);
}