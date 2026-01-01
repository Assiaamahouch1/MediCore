package com.example.rendezvousservice.controller;

import com.example.rendezvousservice.dto.RendezVousDTO;
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

    @PostMapping
    public RendezVous creerRendezVous(@RequestBody RendezVous rdv) {
        return service.creerRendezVous(rdv);
    }

    @PutMapping("/annuler/{id}")
    public String annulerRdv(@PathVariable Long id) {
        boolean result = service.annulerRendezVous(id);
        return result ? "Rendez-vous annulé" : "Rendez-vous introuvable";
    }
    @GetMapping("/all")
    public List<RendezVous> getAllRendezVous() {
        return service.getAll();
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
                request.getNotes()
        );

        if (updated.isPresent()) {
            return ResponseEntity.ok(updated.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

