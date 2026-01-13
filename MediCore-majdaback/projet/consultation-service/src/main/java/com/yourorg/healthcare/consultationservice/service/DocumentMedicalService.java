package com.yourorg.healthcare.consultationservice.service;

import com.yourorg.healthcare.consultationservice.dto.CreateDocumentMedicalRequest;
import com.yourorg.healthcare.consultationservice.dto.UpdateDocumentMedicalRequest;
import com.yourorg.healthcare.consultationservice.model.DocumentMedical;
import com.yourorg.healthcare.consultationservice.repository.DocumentMedicalRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentMedicalService {
    private final DocumentMedicalRepository repo;

    public DocumentMedicalService(DocumentMedicalRepository repo) { this.repo = repo; }
    public DocumentMedical getDossierMedicalByPatientId(UUID patientId) {
        return repo.findByPatientId(patientId);
    }

    public DocumentMedical createDocumentMedical(CreateDocumentMedicalRequest request) {

        DocumentMedical document = DocumentMedical.builder()
                .id(UUID.randomUUID())
                .antMedicaux(request.antMedicaux())
                .antChirug(request.antChirug())
                .allergies(request.allergies())
                .traitement(request.traitement())
                .habitudes(request.habitudes())
                .patientId(request.patientId())
                .createdAt(Instant.now())
                .build();

        return repo.save(document);
    }


    public DocumentMedical updateDocument(DocumentMedical doc) {
        DocumentMedical existing = repo.findById(doc.getId())
                .orElseThrow(() -> new RuntimeException("Document médical introuvable"));

        // On met à jour les champs modifiables
        existing.setAntMedicaux(doc.getAntMedicaux());
        existing.setAntChirug(doc.getAntChirug());
        existing.setAllergies(doc.getAllergies());
        existing.setTraitement(doc.getTraitement());
        existing.setHabitudes(doc.getHabitudes());

        return repo.save(existing);
    }
}
