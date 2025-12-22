package com.yourorg.healthcare.patient.repository;

import com.yourorg.healthcare.patient.model.Patient;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    @Query("select p from Patient p where p.actif = true")
    Page<Patient> findActive(Pageable pageable);

    @Query("""
      select p from Patient p
      where p.actif = true
        and (
          lower(p.nom) like lower(concat('%', :q, '%')) or
          lower(p.prenom) like lower(concat('%', :q, '%')) or
          lower(p.numTel) like lower(concat('%', :q, '%')) or
          lower(p.email)  like lower(concat('%', :q, '%'))
        )
      """)
    Page<Patient> searchActive(@Param("q") String query, Pageable pageable);

    Optional<Patient> findByIdAndActifTrue(UUID id);
    Page<Patient> findByNomContainingIgnoreCase(String nom, Pageable pageable);
    boolean existsByCin(String cin);
}