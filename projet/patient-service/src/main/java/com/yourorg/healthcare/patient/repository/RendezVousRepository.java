package com.yourorg.healthcare.patient.repository;

import com.yourorg.healthcare.patient.model.RendezVous;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface RendezVousRepository extends JpaRepository<RendezVous, UUID> {
    Page<RendezVous> findByPatientId(UUID patientId, Pageable pageable);
    Page<RendezVous> findByMedecinId(UUID medecinId, Pageable pageable);
    Page<RendezVous> findByDateConsultation(LocalDate date, Pageable pageable);
}
