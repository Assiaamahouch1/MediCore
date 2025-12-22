package com.yourorg.healthcare.consultationservice.service;

import com.yourorg.healthcare.consultationservice.dto.LigneOrdonnanceRequest;
import com.yourorg.healthcare.consultationservice.dto.LigneOrdonnanceResponse;
import com.yourorg.healthcare.consultationservice.model.LigneOrdonnance;
import com.yourorg.healthcare.consultationservice.repository.LigneOrdonnanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LigneOrdonnanceService {
    private final LigneOrdonnanceRepository repo;

    public LigneOrdonnanceService(LigneOrdonnanceRepository repo) { this.repo = repo; }

    public List<LigneOrdonnanceResponse> list(UUID ordonnanceId) {
        return repo.findByOrdonnanceId(ordonnanceId).stream().map(this::toResponse).toList();
    }

    public LigneOrdonnanceResponse get(UUID id) {
        return repo.findById(id).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("LigneOrdonnance not found: " + id));
    }

    public LigneOrdonnanceResponse create(LigneOrdonnanceRequest req) {
        LigneOrdonnance l = LigneOrdonnance.builder()
                .ordonnanceId(req.ordonnanceId())
                .description(req.description())
                .dosage(req.dosage())
                .duree(req.duree())
                .medicamentId(req.medicamentId())
                .build();
        return toResponse(repo.save(l));
    }

    public LigneOrdonnanceResponse update(UUID id, LigneOrdonnanceRequest req) {
        LigneOrdonnance l = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("LigneOrdonnance not found: " + id));
        l.setOrdonnanceId(req.ordonnanceId());
        l.setDescription(req.description());
        l.setDosage(req.dosage());
        l.setDuree(req.duree());
        l.setMedicamentId(req.medicamentId());
        return toResponse(repo.save(l));
    }

    public void delete(UUID id) { repo.deleteById(id); }

    private LigneOrdonnanceResponse toResponse(LigneOrdonnance l) {
        return new LigneOrdonnanceResponse(l.getId(), l.getOrdonnanceId(), l.getDescription(), l.getDosage(), l.getDuree(), l.getMedicamentId());
    }
}