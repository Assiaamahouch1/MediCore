package com.yourorg.healthcare.consultationservice.controller;

import com.yourorg.healthcare.consultationservice.dto.CreateDocumentMedicalRequest;
import com.yourorg.healthcare.consultationservice.dto.UpdateDocumentMedicalRequest;
import com.yourorg.healthcare.consultationservice.model.DocumentMedical;
import com.yourorg.healthcare.consultationservice.service.DocumentMedicalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/consultations")
public class DocumentMedicalController {

    private final DocumentMedicalService service;

    public DocumentMedicalController(DocumentMedicalService service) { this.service = service; }


    @GetMapping("/patient/{patientId}")
    public ResponseEntity<DocumentMedical> getDossierMedical(
            @PathVariable UUID patientId) {

        DocumentMedical documents =
                service.getDossierMedicalByPatientId(patientId);

        return ResponseEntity.ok(documents);
    }

    @PostMapping("/createDoc")
    public ResponseEntity<DocumentMedical> createDocumentMedical(
            @RequestBody @Valid CreateDocumentMedicalRequest request) {

        DocumentMedical saved = service.createDocumentMedical(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @PutMapping("/updateDoc/{id}")
    public ResponseEntity<DocumentMedical> updateDocument(
            @PathVariable("id") UUID id,
            @RequestBody DocumentMedical doc) {

        // On s’assure que l’ID du corps correspond à l’ID de l’URL
        if (!id.equals(doc.getId())) {
            return ResponseEntity.badRequest().build();
        }

        DocumentMedical updated = service.updateDocument(doc);
        return ResponseEntity.ok(updated);
    }
}