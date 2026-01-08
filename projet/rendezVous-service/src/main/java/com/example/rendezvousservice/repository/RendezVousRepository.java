package com.example.rendezvousservice.repository;



/**
 * @author sawssan
 **/
import com.example.rendezvousservice.model.RendezVous;
import com.example.rendezvousservice.model.StatutRdv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    List<RendezVous> findByDateRdvGreaterThanEqual(LocalDate date);
    List<RendezVous> findByStatut(StatutRdv statut);
    List<RendezVous> findByCabinetId(Long cabinetId);
    List<RendezVous> findByCabinetIdAndStatut(Long cabinetId, StatutRdv statut);

    @Query("""
    SELECT r FROM RendezVous r
    WHERE r.cabinetId = :cabinetId
      AND r.statut = :statut
      AND r.dateRdv BETWEEN :debut AND :fin
""")
    List<RendezVous> findRdvAujourdhui(
            @Param("cabinetId") Long cabinetId,
            @Param("statut") StatutRdv statut,
            @Param("debut") Date debut,
            @Param("fin") Date fin
    );
}
