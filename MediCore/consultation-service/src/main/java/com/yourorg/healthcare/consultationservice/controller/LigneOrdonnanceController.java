package com.yourorg.healthcare.consultationservice.controller;

import com.yourorg.healthcare.consultationservice.dto.LigneOrdonnanceRequest;
import com.yourorg.healthcare.consultationservice.dto.LigneOrdonnanceResponse;
import com.yourorg.healthcare.consultationservice.service.LigneOrdonnanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ordonnances/{ordonnanceId}/lignes")
public class LigneOrdonnanceController {

    private final LigneOrdonnanceService service;

    public LigneOrdonnanceController(LigneOrdonnanceService service) { this.service = service; }

    @GetMapping
    public List<LigneOrdonnanceResponse> list(@PathVariable UUID ordonnanceId) {
        return service.list(ordonnanceId);
    }

    @GetMapping("/{id}")
    public LigneOrdonnanceResponse get(@PathVariable UUID id) { return service.get(id); }

    @PostMapping
    public ResponseEntity<LigneOrdonnanceResponse> create(@PathVariable UUID ordonnanceId,
                                                          @Valid @RequestBody LigneOrdonnanceRequest req) {
        // Forcer l'ordonnanceId depuis l'URL
        LigneOrdonnanceRequest fixed = new LigneOrdonnanceRequest(
                ordonnanceId, req.description(), req.dosage(), req.duree(), req.medicamentId()
        );
        LigneOrdonnanceResponse created = service.create(fixed);
        return ResponseEntity.created(URI.create("/ordonnances/" + ordonnanceId + "/lignes/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public LigneOrdonnanceResponse update(@PathVariable UUID ordonnanceId,
                                          @PathVariable UUID id,
                                          @Valid @RequestBody LigneOrdonnanceRequest req) {
        LigneOrdonnanceRequest fixed = new LigneOrdonnanceRequest(
                ordonnanceId, req.description(), req.dosage(), req.duree(), req.medicamentId()
        );
        return service.update(id, fixed);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}