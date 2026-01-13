package com.yourorg.healthcare.consultationservice.controller;

import com.yourorg.healthcare.consultationservice.dto.CreateConsultationRequest;
import com.yourorg.healthcare.consultationservice.model.Consultation;
import com.yourorg.healthcare.consultationservice.service.ConsultationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/consultations")
public class ConsultationController {

    private final ConsultationService service;

    public ConsultationController(ConsultationService service) { this.service = service; }

    @GetMapping("/consultation/{patientId}")
    public ResponseEntity<List<Consultation>> getConsultationsDossierMedical(
            @PathVariable UUID patientId) {

        return ResponseEntity.ok(
                service.getConsultationsByPatient(patientId)
        );
    }



    @PostMapping("/createCons")
    public ResponseEntity<Consultation> createConsultation(
            @RequestBody @Valid CreateConsultationRequest request) {

        Consultation saved = service.createConsultation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

}