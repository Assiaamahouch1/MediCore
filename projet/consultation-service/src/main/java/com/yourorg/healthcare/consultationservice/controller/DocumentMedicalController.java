package com.yourorg.healthcare.consultationservice.controller;

import com.yourorg.healthcare.consultationservice.dto.DocumentMedicalRequest;
import com.yourorg.healthcare.consultationservice.dto.DocumentMedicalResponse;
import com.yourorg.healthcare.consultationservice.service.DocumentMedicalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/consultations/{consultationId}/documents")
public class DocumentMedicalController {

    private final DocumentMedicalService service;

    public DocumentMedicalController(DocumentMedicalService service) { this.service = service; }

    @GetMapping
    public List<DocumentMedicalResponse> list(@PathVariable UUID consultationId) {
        return service.list(consultationId);
    }

    @GetMapping("/{id}")
    public DocumentMedicalResponse get(@PathVariable UUID id) { return service.get(id); }

    @PostMapping
    public ResponseEntity<DocumentMedicalResponse> create(@PathVariable UUID consultationId,
                                                          @Valid @RequestBody DocumentMedicalRequest req) {
        DocumentMedicalRequest fixed = new DocumentMedicalRequest(consultationId, req.titre(), req.url());
        DocumentMedicalResponse created = service.create(fixed);
        return ResponseEntity.created(URI.create("/consultations/" + consultationId + "/documents/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public DocumentMedicalResponse update(@PathVariable UUID consultationId,
                                          @PathVariable UUID id,
                                          @Valid @RequestBody DocumentMedicalRequest req) {
        DocumentMedicalRequest fixed = new DocumentMedicalRequest(consultationId, req.titre(), req.url());
        return service.update(id, fixed);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}