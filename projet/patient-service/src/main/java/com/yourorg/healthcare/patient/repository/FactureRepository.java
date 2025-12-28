package com.yourorg.healthcare.patient.repository;
import com.yourorg.healthcare.patient.model.Facture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FactureRepository extends JpaRepository<Facture, UUID> {
    Page<Facture> findByPatientId(UUID patientId, Pageable pageable);
}