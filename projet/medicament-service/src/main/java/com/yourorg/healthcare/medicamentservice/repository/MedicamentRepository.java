package com.yourorg.healthcare.medicamentservice.repository;

import com.yourorg.healthcare.medicamentservice.model.Medicament;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MedicamentRepository extends JpaRepository<Medicament, UUID> {
    @Query("""
    select m from Medicament m
    where (:q is null or :q = '' or
           lower(m.nom) like lower(concat('%', :q, '%')) or
           lower(m.atcCode) like lower(concat('%', :q, '%')))
  """)
    Page<Medicament> search(@Param("q") String q, Pageable pageable);

    boolean existsByNomIgnoreCase(String nom);
}