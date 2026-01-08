package com.yourorg.healthcare.consultationservice.service;

import com.yourorg.healthcare.consultationservice.model.LigneOrdonnance;
import com.yourorg.healthcare.consultationservice.repository.LigneOrdonnanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LigneOrdonnanceService {
    private final LigneOrdonnanceRepository repo;

    public LigneOrdonnanceService(LigneOrdonnanceRepository repo) { this.repo = repo; }
    public List<LigneOrdonnance> getLignesByOrdonnance(UUID ordonnanceId) {
        return repo.findByOrdonnanceId(ordonnanceId);
    }
}