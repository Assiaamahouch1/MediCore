package com.example.cabinetservice.repository;

import com.example.cabinetservice.model.Cabinet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CabinetRepository extends JpaRepository<Cabinet, Long> {
    Page<Cabinet> findByNomContainingIgnoreCase(String nom, Pageable pageable);

    @Query("select c from Cabinet c " +
            "where c.service_actif = true " +
            "and c.date_expiration_service is not null " +
            "and c.date_expiration_service <= :today")
    List<Cabinet> findActiveExpired(@Param("today") LocalDate today);

    // Trouver les cabinets qui expirent bientôt (entre aujourd'hui et la date seuil)
    @Query("SELECT c FROM Cabinet c " +
            "WHERE c.service_actif = true " +
            "AND c.date_expiration_service IS NOT NULL " +
            "AND c.date_expiration_service > :today " +
            "AND c.date_expiration_service <= :threshold " +
            "ORDER BY c.date_expiration_service ASC")
    List<Cabinet> findExpiringCabinets(
            @Param("today") LocalDate today,
            @Param("threshold") LocalDate threshold
    );
}