package com.yourorg.healthcare.consultationservice.service;

import com.yourorg.healthcare.consultationservice.dto.ConsultationRequest;
import com.yourorg.healthcare.consultationservice.dto.ConsultationResponse;
import com.yourorg.healthcare.consultationservice.model.Consultation;
import com.yourorg.healthcare.consultationservice.repository.ConsultationRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConsultationService {
    private final ConsultationRepository repo;

    public ConsultationService(ConsultationRepository repo) { this.repo = repo; }

    public Page<ConsultationResponse> list(UUID patientId, UUID medecinId, Integer page, Integer size, String sort) {
        Sort s = Sort.by(sort == null || sort.isBlank() ? "dateConsultation" : sort.split(",")[0]);
        if (sort != null && sort.toLowerCase().endsWith(",desc")) s = s.descending();
        Pageable pageable = PageRequest.of(page == null ? 0 : page, size == null ? 20 : size, s);
        Page<Consultation> result;
        if (patientId != null) result = repo.findByPatientId(patientId, pageable);
        else if (medecinId != null) result = repo.findByMedecinId(medecinId, pageable);
        else result = repo.findAll(pageable);
        return result.map(this::toResponse);
    }

    public ConsultationResponse get(UUID id) {
        return repo.findById(id).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Consultation not found: " + id));
    }

    public ConsultationResponse create(ConsultationRequest req) {
        Consultation c = Consultation.builder()
                .type(req.type())
                .dateConsultation(req.dateConsultation())
                .examenClinique(req.examenClinique())
                .examenComplementaire(req.examenComplementaire())
                .diagnostic(req.diagnostic())
                .traitement(req.traitement())
                .observations(req.observations())
                .patientId(req.patientId())
                .medecinId(req.medecinId())
                .rendezVousId(req.rendezVousId())
                .dossierMedicalId(req.dossierMedicalId())
                .build();
        return toResponse(repo.save(c));
    }

    public ConsultationResponse update(UUID id, ConsultationRequest req) {
        Consultation c = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Consultation not found: " + id));
        c.setType(req.type());
        c.setDateConsultation(req.dateConsultation());
        c.setExamenClinique(req.examenClinique());
        c.setExamenComplementaire(req.examenComplementaire());
        c.setDiagnostic(req.diagnostic());
        c.setTraitement(req.traitement());
        c.setObservations(req.observations());
        c.setPatientId(req.patientId());
        c.setMedecinId(req.medecinId());
        c.setRendezVousId(req.rendezVousId());
        c.setDossierMedicalId(req.dossierMedicalId());
        return toResponse(repo.save(c));
    }

    public void delete(UUID id) { repo.deleteById(id); }

    private ConsultationResponse toResponse(Consultation c) {
        return new ConsultationResponse(
                c.getId(), c.getType(), c.getDateConsultation(),
                c.getExamenClinique(), c.getExamenComplementaire(),
                c.getDiagnostic(), c.getTraitement(), c.getObservations(),
                c.getPatientId(), c.getMedecinId(), c.getRendezVousId(), c.getDossierMedicalId(),
                c.getCreatedAt(), c.getUpdatedAt()
        );
    }
}