package com.example.factureservice.repository;

import com.example.factureservice.model.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author sawssan
 **/
@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {
}
