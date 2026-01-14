package com.yourorg.healthcare.consultationservice.controller;

import com.yourorg.healthcare.consultationservice.dto.CreateOrdonnanceRequest;
import com.yourorg.healthcare.consultationservice.model.Ordonnance;
import com.yourorg.healthcare.consultationservice.service.OrdonnanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/createOrdonnance")
    public ResponseEntity<Ordonnance> createOrdonnance(
            @RequestBody @Valid CreateOrdonnanceRequest request) {

        Ordonnance saved = service.createOrdonnance(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

}