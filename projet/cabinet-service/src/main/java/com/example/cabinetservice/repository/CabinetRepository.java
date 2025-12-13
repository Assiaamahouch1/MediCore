package com.example.cabinetservice.repository;

import com.example.cabinetservice.model.Cabinet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CabinetRepository extends JpaRepository<Cabinet, Long> {
}
