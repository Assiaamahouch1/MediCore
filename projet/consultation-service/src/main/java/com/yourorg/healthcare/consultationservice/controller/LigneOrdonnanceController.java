package com.yourorg.healthcare.consultationservice.controller;

import com.yourorg.healthcare.consultationservice.model.LigneOrdonnance;
import com.yourorg.healthcare.consultationservice.service.LigneOrdonnanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/consultations")
public class LigneOrdonnanceController {

    private final LigneOrdonnanceService service;

    public LigneOrdonnanceController(LigneOrdonnanceService service) { this.service = service; }
    @GetMapping("/ligne/{ordonnanceId}")
    public ResponseEntity<List<LigneOrdonnance>> getLignesOrdonnance(
            @PathVariable UUID ordonnanceId) {

        return ResponseEntity.ok(
                service.getLignesByOrdonnance(ordonnanceId)
        );
    }

}