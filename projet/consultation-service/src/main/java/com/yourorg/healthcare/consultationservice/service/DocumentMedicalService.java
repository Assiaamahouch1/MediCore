package com.yourorg.healthcare.consultationservice.service;

import com.yourorg.healthcare.consultationservice.dto.DocumentMedicalRequest;
import com.yourorg.healthcare.consultationservice.dto.DocumentMedicalResponse;
import com.yourorg.healthcare.consultationservice.model.DocumentMedical;
import com.yourorg.healthcare.consultationservice.repository.DocumentMedicalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentMedicalService {
    private final DocumentMedicalRepository repo;

    public DocumentMedicalService(DocumentMedicalRepository repo) { this.repo = repo; }

    public List<DocumentMedicalResponse> list(UUID consultationId) {
        return repo.findByConsultationId(consultationId).stream().map(this::toResponse).toList();
    }

    public DocumentMedicalResponse get(UUID id) {
        return repo.findById(id).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("DocumentMedical not found: " + id));
    }

    public DocumentMedicalResponse create(DocumentMedicalRequest req) {
        DocumentMedical d = DocumentMedical.builder()
                .consultationId(req.consultationId())
                .titre(req.titre())
                .url(req.url())
                .build();
        return toResponse(repo.save(d));
    }

    public DocumentMedicalResponse update(UUID id, DocumentMedicalRequest req) {
        DocumentMedical d = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("DocumentMedical not found: " + id));
        d.setConsultationId(req.consultationId());
        d.setTitre(req.titre());
        d.setUrl(req.url());
        return toResponse(repo.save(d));
    }

    public void delete(UUID id) { repo.deleteById(id); }

    private DocumentMedicalResponse toResponse(DocumentMedical d) {
        return new DocumentMedicalResponse(d.getId(), d.getConsultationId(), d.getTitre(), d.getUrl());
    }
}
