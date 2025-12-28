package com.yourorg.healthcare.consultationservice.controller;

import com.yourorg.healthcare.consultationservice.dto.ConsultationRequest;
import com.yourorg.healthcare.consultationservice.dto.ConsultationResponse;
import com.yourorg.healthcare.consultationservice.service.ConsultationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/consultations")
public class ConsultationController {

    private final ConsultationService service;

    public ConsultationController(ConsultationService service) { this.service = service; }

    @GetMapping
    public Page<ConsultationResponse> list(@RequestParam(required=false) UUID patientId,
                                           @RequestParam(required=false) UUID medecinId,
                                           @RequestParam(required=false) Integer page,
                                           @RequestParam(required=false) Integer size,
                                           @RequestParam(required=false) String sort) {
        return service.list(patientId, medecinId, page, size, sort);
    }

    @GetMapping("/{id}")
    public ConsultationResponse get(@PathVariable UUID id) { return service.get(id); }

    @PostMapping
    public ResponseEntity<ConsultationResponse> create(@Valid @RequestBody ConsultationRequest req) {
        ConsultationResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/consultations/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public ConsultationResponse update(@PathVariable UUID id, @Valid @RequestBody ConsultationRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}