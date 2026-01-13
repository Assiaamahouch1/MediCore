package com.yourorg.healthcare.medicamentservice.repository;

import com.yourorg.healthcare.medicamentservice.model.Medicament;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.UUID;

public interface MedicamentRepository extends JpaRepository<Medicament, UUID> {

    boolean existsByNomIgnoreCase(String nom);

    // Méthode de recherche simple par nom (ignore case)
    @Query("SELECT m FROM Medicament m WHERE :q IS NULL OR LOWER(m.nom) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<Medicament> search(String q, Pageable pageable);
}
