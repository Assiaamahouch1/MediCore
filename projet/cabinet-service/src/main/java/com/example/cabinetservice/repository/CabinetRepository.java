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
}