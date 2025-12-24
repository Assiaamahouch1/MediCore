package com.yourorg.healthcare.consultationservice.repository;

import com.yourorg.healthcare.consultationservice.model.DocumentMedical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentMedicalRepository extends JpaRepository<DocumentMedical, UUID> {
    List<DocumentMedical> findByConsultationId(UUID consultationId);
}