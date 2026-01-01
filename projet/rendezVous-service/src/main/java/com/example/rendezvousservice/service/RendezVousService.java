package com.example.rendezvousservice.service;

import com.example.rendezvousservice.dto.RendezVousDTO;
import com.example.rendezvousservice.model.RendezVous;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author sawssan
 **/
public interface RendezVousService {
    public RendezVous creerRendezVous(RendezVous rdv);
    public List<RendezVous> getUpcomingRendezVous();
    public boolean annulerRendezVous(Long id);
    public boolean confirmerRendezVous(Long id);
    public List<RendezVous> getAll();
    public Optional<RendezVous> modifierRendezVousPartiel(
            Long id,
           Date nouvelleDate,
            String nouvelleHeure,
            String nouveauMotif,
            String nouvellesNotes);
}
