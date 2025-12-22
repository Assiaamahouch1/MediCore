package com.yourorg.healthcare.consultationservice.repository;

import com.yourorg.healthcare.consultationservice.model.Consultation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {
    Page<Consultation> findByPatientId(UUID patientId, Pageable pageable);
    Page<Consultation> findByMedecinId(UUID medecinId, Pageable pageable);
}