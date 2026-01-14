package com.example.rendezvousservice.service;

import java.util.*;

import com.example.rendezvousservice.model.RendezVous;
import com.example.rendezvousservice.model.StatutRdv;
import com.example.rendezvousservice.repository.RendezVousRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class RendezVousServiceImpl implements RendezVousService {

    private final RendezVousRepository repository;

    public RendezVousServiceImpl(RendezVousRepository repository) {
        this.repository = repository;
    }

    @Override
    public RendezVous creerRendezVous(RendezVous rdv) {
        rdv.setStatut(StatutRdv.EN_ATTENTE);
        return repository.save(rdv);
    }

    @Override
    public List<RendezVous> getUpcomingRendezVous() {
        LocalDate today = LocalDate.now();
        return repository.findByDateRdvGreaterThanEqual(today);
    }
    @Override
    public boolean annulerRendezVous(Long id) {
        Optional<RendezVous> optionalRdv = repository.findById(id);
        if (optionalRdv.isPresent()) {
            RendezVous rdv = optionalRdv.get();
            rdv.setStatut(StatutRdv.ANNULE);  // on marque comme annulé
            repository.save(rdv);
            return true;
        } else {
            return false; // rendez-vous non trouvé
        }
    }

    @Override
    public boolean confirmerRendezVous(Long id) {
        Optional<RendezVous> optionalRdv = repository.findById(id);
        if (optionalRdv.isPresent()) {
            RendezVous rdv = optionalRdv.get();
            rdv.setStatut(StatutRdv.CONFIRME);  // on marque comme annulé
            repository.save(rdv);
            return true;
        } else {
            return false; // rendez-vous non trouvé
        }
    }


    @Override
    @Transactional  // Indispensable pour que les modifications soient persistées
    public List<RendezVous> getAll(Long cabinetId) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zone);

        List<RendezVous> rdvs = repository.findByCabinetId(cabinetId);

        List<RendezVous> toDelete = new ArrayList<>();
        List<RendezVous> toUpdate = new ArrayList<>();

        for (RendezVous rdv : rdvs) {
            // Convertir la date du RDV en LocalDate
            LocalDate rdvDate = rdv.getDateRdv().toInstant()
                    .atZone(zone)
                    .toLocalDate();

            // 1. Supprimer seulement si ANNULÉ ET date passée
            if (rdv.getStatut() == StatutRdv.ANNULE && rdvDate.isBefore(today)) {
                toDelete.add(rdv);
            }
            // 2. Passer à HISTORIQUE si CONFIRME et date passée
            else if (rdv.getStatut() == StatutRdv.CONFIRME && rdvDate.isBefore(today)) {
                rdv.setStatut(StatutRdv.HISTORIQUE);
                toUpdate.add(rdv);
            }
            // Les ANNULÉ dont la date n'est pas encore passée restent visibles
        }

        // Suppression et mise à jour en batch (efficace et sûr avec JPA)
        if (!toDelete.isEmpty()) {
            repository.deleteAllInBatch(toDelete);
        }
        if (!toUpdate.isEmpty()) {
            repository.saveAll(toUpdate);
        }

        // Retourner tous les RDV qui ne sont PAS ANNULÉ (même si leur date est future)
        // Les ANNULÉ avec date future restent visibles
        // Les ANNULÉ avec date passée ont été supprimés
        return rdvs.stream()
                .filter(rdv -> rdv.getStatut() != StatutRdv.ANNULE ||
                        (rdv.getStatut() == StatutRdv.ANNULE &&
                                rdv.getDateRdv().toInstant().atZone(zone).toLocalDate().isAfter(today.minusDays(1))))
                .toList();
    }

    @Override
    public Optional<RendezVous> modifierRendezVousPartiel(
            Long id,
            Date nouvelleDate,
            String nouvelleHeure,
            String nouveauMotif,
            String nouvellesNotes,
            Long cabinetId) {

        Optional<RendezVous> optionalRdv = repository.findById(id);

        if (optionalRdv.isEmpty()) {
            return Optional.empty();
        }

        RendezVous rdv = optionalRdv.get();

        if (nouvelleDate != null) {
            rdv.setDateRdv(nouvelleDate);
        }

        if (nouvelleHeure != null && !nouvelleHeure.trim().isEmpty()) {
            rdv.setHeureRdv(nouvelleHeure.trim());
        }

        if (nouveauMotif != null && !nouveauMotif.trim().isEmpty()) {
            rdv.setMotif(nouveauMotif.trim());
        }

        if (nouvellesNotes != null) {
            rdv.setNotes(nouvellesNotes.trim());
        }

        // ✅ NE MODIFIER cabinetId QUE S’IL EST FOURNI
        if (cabinetId != null) {
            rdv.setCabinetId(cabinetId);
        }

        return Optional.of(repository.save(rdv));
    }

    @Override
    public List<RendezVous> findByStatutEnAttente(Long cabinetId) {
        return repository.findByCabinetIdAndStatut(cabinetId,StatutRdv.EN_ATTENTE);
    }

    @Override
    public List<RendezVous> findByStatutConfirmeAujourdhui(Long cabinetId) {

        Calendar cal = Calendar.getInstance();

        // 🔹 Début de la journée
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date debutJour = cal.getTime();

        // 🔹 Fin de la journée
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date finJour = cal.getTime();

        return repository.findRdvAujourdhui(
                cabinetId,
                StatutRdv.CONFIRME,
                debutJour,
                finJour
        );
    }



    @Override
    public List<RendezVous> getRendezVousArrives(Long cabinetId) {
        List<RendezVous> rdv = repository.findByCabinetIdAndStatut(cabinetId,StatutRdv.ARRIVE);
       return rdv;
    }

    @Override
    public RendezVous marquerCommeArrive(Long idRdv) {
        RendezVous rdv = repository.findById(idRdv)
                .orElseThrow(() -> new EntityNotFoundException("rdv non trouvé avec l'id : " + idRdv));



        rdv.setStatut(StatutRdv.ARRIVE);


        RendezVous updated = repository.save(rdv);
        return updated;
    }

    @Transactional
    public RendezVous terminerRendezVous(Long idRdv) {
        RendezVous rdv = repository.findById(idRdv)
                .orElseThrow(() -> new RuntimeException("Rendez-vous non trouvé avec l'id: " + idRdv));
        rdv.setStatut(StatutRdv.TERMINE);
        return repository.save(rdv);
    }

}
