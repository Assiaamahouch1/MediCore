package com.example.rendezvousservice.repository;



/**
 * @author sawssan
 **/
import com.example.rendezvousservice.model.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

}
