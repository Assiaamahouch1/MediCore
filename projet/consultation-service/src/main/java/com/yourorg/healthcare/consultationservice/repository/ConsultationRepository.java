package com.yourorg.healthcare.consultationservice.repository;
import com.yourorg.healthcare.consultationservice.model.Consultation;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.UUID;

public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {
    Page<Consultation> findByPatientId(UUID patientId, Pageable pageable);
    Page<Consultation> findByDoctorId(UUID doctorId, Pageable pageable);
    Page<Consultation> findByDateBetween(Instant start, Instant end, Pageable pageable);
}