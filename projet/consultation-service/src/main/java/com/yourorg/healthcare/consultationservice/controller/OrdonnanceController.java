package com.yourorg.healthcare.consultationservice.controller;

import com.yourorg.healthcare.consultationservice.model.Ordonnance;
import com.yourorg.healthcare.consultationservice.service.OrdonnanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/consultations")
public class OrdonnanceController {

    private final OrdonnanceService service;

    public OrdonnanceController(OrdonnanceService service) { this.service = service; }
    @GetMapping("/ordonance/{consultationId}")
    public ResponseEntity<List<Ordonnance>> getOrdonnancesByConsultation(
            @PathVariable UUID consultationId) {

        return ResponseEntity.ok(
                service.getOrdonnancesByConsultation(consultationId)
        );
    }

}