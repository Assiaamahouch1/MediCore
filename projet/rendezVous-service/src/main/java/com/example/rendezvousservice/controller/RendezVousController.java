package com.example.rendezvousservice.controller;

import com.example.rendezvousservice.dto.RendezVousUpdateRequest;
import com.example.rendezvousservice.model.RendezVous;
import com.example.rendezvousservice.service.RendezVousService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author sawssan
 **/
@RestController
@RequestMapping("/rendezVous")
public class RendezVousController {

    private final RendezVousService service;

    public RendezVousController(RendezVousService service) {
        this.service = service;
    }

    @PutMapping("/confirmer/{id}")
    public ResponseEntity<Void> confirmer(@PathVariable Long id) {
        boolean success = service.confirmerRendezVous(id);

        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint pour les RDV en attente (déjà utilisé pour le select dans le frontend)
    @GetMapping("/en_attente/{cabinetId}")
    public ResponseEntity<List<RendezVous>> getEnAttente(@PathVariable("cabinetId") Long cabinetId) {
        return ResponseEntity.ok(service.findByStatutEnAttente(cabinetId));
    }

    @GetMapping("/liste/{cabinetId}")
    public ResponseEntity<List<RendezVous>> getConfirme( @PathVariable("cabinetId") Long cabinetId) {
        return ResponseEntity.ok(service.findByStatutConfirmeAujourdhui(cabinetId));
    }


    @PutMapping("/annuler/{id}")
    public String annulerRdv(@PathVariable Long id) {
        boolean result = service.annulerRendezVous(id);
        return result ? "Rendez-vous annulé" : "Rendez-vous introuvable";
    }
    // Récupérer tous les patients
    @GetMapping("/all/{cabinetId}")
    public List<RendezVous> getAllRendezVous(
            @PathVariable("cabinetId") Long cabinetId
    ) {
        return service.getAll(cabinetId);
    }
     @PatchMapping("/{id}/modifier-partiel")
    public ResponseEntity<RendezVous> modifierRendezVousPartiel(
            @PathVariable Long id,
            @RequestBody RendezVousUpdateRequest request) {

        Optional<RendezVous> updated = service.modifierRendezVousPartiel(
                id,
                request.getDateRdv(),
                request.getHeureRdv(),
                request.getMotif(),
                request.getNotes(),
                request.getCabinetId()
        );

        if (updated.isPresent()) {
            return ResponseEntity.ok(updated.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public RendezVous creerRendezVous(@RequestBody RendezVous dto) {
        return service.creerRendezVous(dto);
    }



    @PutMapping("/Arrive/{idRdv}")
    public RendezVous RdvArrive(@PathVariable Long idRdv) {
        RendezVous result = service.marquerCommeArrive(idRdv);
        return result;
    }



    // Récupérer tous les patients
    @GetMapping("/allArrive/{cabinetId}")
    public List<RendezVous> getAllRendezVousArrive(
            @PathVariable("cabinetId") Long cabinetId
    ) {
        return service.getRendezVousArrives(cabinetId);
    }

    @PutMapping("/Termine/{idRdv}")
    public RendezVous termineRdv(@PathVariable Long idRdv) {
        return service.terminerRendezVous(idRdv);
    }

}

