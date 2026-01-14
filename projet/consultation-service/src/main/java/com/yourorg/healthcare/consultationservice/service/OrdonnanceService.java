package com.yourorg.healthcare.consultationservice.service;

import com.yourorg.healthcare.consultationservice.dto.CreateOrdonnanceRequest;
import com.yourorg.healthcare.consultationservice.model.LigneOrdonnance;
import com.yourorg.healthcare.consultationservice.model.Ordonnance;
import com.yourorg.healthcare.consultationservice.repository.LigneOrdonnanceRepository;
import com.yourorg.healthcare.consultationservice.repository.OrdonnanceRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdonnanceService {
    private final OrdonnanceRepository repo;
    private final LigneOrdonnanceRepository ligneRepo;

    public OrdonnanceService(OrdonnanceRepository repo, LigneOrdonnanceRepository ligneRepo) {
        this.repo = repo;
        this.ligneRepo = ligneRepo;
    }

    public List<Ordonnance> getOrdonnancesByConsultation(UUID consultationId) {
        return repo.findByConsultationId(consultationId);
    }
    @Transactional
    public Ordonnance createOrdonnance(CreateOrdonnanceRequest request) {
        Ordonnance ordonnance = Ordonnance.builder()
                .id(UUID.randomUUID())
                .consultationId(request.consultationId())
                .type(request.type())
                .date(Instant.now())
                .createdAt(Instant.now())
                .build();

        Ordonnance saved = repo.save(ordonnance);

        // Créer les lignes d'ordonnance
        if (request.lignes() != null && !request.lignes().isEmpty()) {
            List<LigneOrdonnance> lignes = request.lignes().stream()
                    .map(ligneReq -> LigneOrdonnance.builder()
                            .id(UUID.randomUUID())
                            .ordonnanceId(saved.getId())
                            .nom(ligneReq.nom())
                            .dosage(ligneReq.dosage())
                            .duree(ligneReq.duree())
                            .medicamentId(ligneReq.medicamentId())
                            .createdAt(Instant.now())
                            .build())
                    .toList();
            ligneRepo.saveAll(lignes);
        }

        return saved;
    }
}