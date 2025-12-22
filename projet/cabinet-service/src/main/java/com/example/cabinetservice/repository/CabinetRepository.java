package com.example.cabinetservice.repository;

import com.example.cabinetservice.model.Cabinet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CabinetRepository extends JpaRepository<Cabinet, Long> {
    Page<Cabinet> findByNomContainingIgnoreCase(String nom, Pageable pageable);
}