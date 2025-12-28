package com.yourorg.healthcare.consultationservice.repository;

import com.yourorg.healthcare.consultationservice.model.LigneOrdonnance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LigneOrdonnanceRepository extends JpaRepository<LigneOrdonnance, UUID> {
    List<LigneOrdonnance> findByOrdonnanceId(UUID ordonnanceId);
}