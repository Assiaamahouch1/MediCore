package com.yourorg.healthcare.consultationservice.repository;

import com.yourorg.healthcare.consultationservice.model.Ordonnance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrdonnanceRepository extends JpaRepository<Ordonnance, UUID> {
    Page<Ordonnance> findByConsultationId(UUID consultationId, Pageable pageable);
}