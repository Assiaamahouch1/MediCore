package com.example.rendezvousservice.service;

import java.util.*;
import java.util.stream.Collectors;

import com.example.rendezvousservice.dto.ConsultationStatsDTO;
import com.example.rendezvousservice.dto.DashboardStatsDTO;
import com.example.rendezvousservice.model.RendezVous;
import com.example.rendezvousservice.model.StatutRdv;
import com.example.rendezvousservice.repository.RendezVousRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;

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

    // ============ DASHBOARD STATS IMPLEMENTATIONS ============

    @Override
    public DashboardStatsDTO getDashboardStats(Long cabinetId) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zone);
        
        // Début et fin de la journée
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date debutJour = cal.getTime();
        
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date finJour = cal.getTime();

        // Patients en salle d'attente (statut ARRIVE)
        long patientsEnAttenteSalle = repository.findByCabinetIdAndStatut(cabinetId, StatutRdv.ARRIVE)
                .stream()
                .filter(rdv -> {
                    LocalDate rdvDate = rdv.getDateRdv().toInstant().atZone(zone).toLocalDate();
                    return rdvDate.equals(today);
                })
                .count();

        // RDV confirmés pour aujourd'hui
        long rdvConfirmesAujourdhui = repository.findRdvAujourdhui(cabinetId, StatutRdv.CONFIRME, debutJour, finJour).size();

        // RDV en attente de confirmation
        long rdvEnAttenteConfirmation = repository.findByCabinetIdAndStatut(cabinetId, StatutRdv.EN_ATTENTE).size();

        // Consultations effectuées aujourd'hui (ceux passés en HISTORIQUE ou terminés)
        long consultationsAujourdhui = repository.findRdvAujourdhui(cabinetId, StatutRdv.HISTORIQUE, debutJour, finJour).size();

        // Total RDV du jour (tous statuts)
        List<RendezVous> allRdvs = repository.findByCabinetId(cabinetId);
        long totalRdvJour = allRdvs.stream()
                .filter(rdv -> {
                    LocalDate rdvDate = rdv.getDateRdv().toInstant().atZone(zone).toLocalDate();
                    return rdvDate.equals(today);
                })
                .count();

        return DashboardStatsDTO.builder()
                .patientsEnAttenteSalle(patientsEnAttenteSalle)
                .rdvConfirmesAujourdhui(rdvConfirmesAujourdhui)
                .rdvEnAttenteConfirmation(rdvEnAttenteConfirmation)
                .consultationsAujourdhui(consultationsAujourdhui)
                .totalRdvJour(totalRdvJour)
                .build();
    }

    @Override
    public List<ConsultationStatsDTO> getConsultationsLast7Days(Long cabinetId) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        List<ConsultationStatsDTO> stats = new ArrayList<>();
        
        // Récupérer tous les RDV du cabinet qui sont HISTORIQUE ou ARRIVE (consultations terminées)
        List<RendezVous> allRdvs = repository.findByCabinetId(cabinetId);
        
        // Pour les 7 derniers jours
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            
            long count = allRdvs.stream()
                    .filter(rdv -> {
                        LocalDate rdvDate = rdv.getDateRdv().toInstant().atZone(zone).toLocalDate();
                        // Compter les consultations effectuées (HISTORIQUE) pour ce jour
                        return rdvDate.equals(date) && 
                               (rdv.getStatut() == StatutRdv.HISTORIQUE || rdv.getStatut() == StatutRdv.ARRIVE);
                    })
                    .count();
            
            String jourSemaine = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.FRENCH);
            
            stats.add(ConsultationStatsDTO.builder()
                    .date(date.format(formatter))
                    .jour(jourSemaine)
                    .count(count)
                    .build());
        }
        
        return stats;
    }

    @Override
    public Map<String, Long> getConsultationTypesRepartition(Long cabinetId) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zone);
        LocalDate thirtyDaysAgo = today.minusDays(30);
        
        List<RendezVous> allRdvs = repository.findByCabinetId(cabinetId);
        
        // Grouper par motif pour les 30 derniers jours
        return allRdvs.stream()
                .filter(rdv -> {
                    LocalDate rdvDate = rdv.getDateRdv().toInstant().atZone(zone).toLocalDate();
                    return !rdvDate.isBefore(thirtyDaysAgo) && 
                           (rdv.getStatut() == StatutRdv.HISTORIQUE || 
                            rdv.getStatut() == StatutRdv.ARRIVE ||
                            rdv.getStatut() == StatutRdv.CONFIRME);
                })
                .collect(Collectors.groupingBy(
                        rdv -> rdv.getMotif() != null && !rdv.getMotif().trim().isEmpty() 
                               ? rdv.getMotif() 
                               : "Non spécifié",
                        Collectors.counting()
                ));
    }

}
