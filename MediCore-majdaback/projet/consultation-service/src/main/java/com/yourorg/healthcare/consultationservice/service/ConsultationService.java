package com.yourorg.healthcare.consultationservice.service;

import com.yourorg.healthcare.consultationservice.dto.CreateConsultationRequest;
import com.yourorg.healthcare.consultationservice.model.Consultation;
import com.yourorg.healthcare.consultationservice.repository.ConsultationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ConsultationService {
    private final ConsultationRepository repo;

    public ConsultationService(ConsultationRepository repo) { this.repo = repo; }

    public List<Consultation> getConsultationsByPatient(UUID patientId) {
        return repo.findByPatientId(patientId);
    }

    public Consultation createConsultation(CreateConsultationRequest request) {

        Consultation consultation = Consultation.builder()
                .id(UUID.randomUUID())
                .type(request.type())
                .dateConsultation(request.dateConsultation())
                .examenClinique(request.examenClinique())
                .examenComplementaire(request.examenComplementaire())
                .diagnostic(request.diagnostic())
                .traitement(request.traitement())
                .observations(request.observations())
                .patientId(request.patientId())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return repo.save(consultation);
    }
}