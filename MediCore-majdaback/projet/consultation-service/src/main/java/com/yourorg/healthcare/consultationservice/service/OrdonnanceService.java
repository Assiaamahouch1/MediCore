package com.yourorg.healthcare.consultationservice.service;

import com.yourorg.healthcare.consultationservice.model.Ordonnance;
import com.yourorg.healthcare.consultationservice.repository.OrdonnanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrdonnanceService {
    private final OrdonnanceRepository repo;

    public OrdonnanceService(OrdonnanceRepository repo) { this.repo = repo; }

    public List<Ordonnance> getOrdonnancesByConsultation(UUID consultationId) {
        return repo.findByConsultationId(consultationId);
    }
}