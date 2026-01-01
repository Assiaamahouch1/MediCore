package com.yourorg.healthcare.patient.repository;

import com.yourorg.healthcare.patient.model.Patient;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    List<Patient> findByActifTrue();
    List<Patient> findByActifFalse();

    Patient findByEmail(String username);
}