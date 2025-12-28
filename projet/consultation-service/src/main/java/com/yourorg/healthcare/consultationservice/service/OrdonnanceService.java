package com.yourorg.healthcare.consultationservice.service;

import com.yourorg.healthcare.consultationservice.dto.OrdonnanceRequest;
import com.yourorg.healthcare.consultationservice.dto.OrdonnanceResponse;
import com.yourorg.healthcare.consultationservice.model.Ordonnance;
import com.yourorg.healthcare.consultationservice.repository.OrdonnanceRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrdonnanceService {
    private final OrdonnanceRepository repo;

    public OrdonnanceService(OrdonnanceRepository repo) { this.repo = repo; }

    public Page<OrdonnanceResponse> list(UUID consultationId, Integer page, Integer size, String sort) {
        Pageable pageable = PageRequest.of(page == null ? 0 : page, size == null ? 20 : size,
                Sort.by(sort == null || sort.isBlank() ? "date" : sort));
        Page<Ordonnance> result = (consultationId == null) ? repo.findAll(pageable) : repo.findByConsultationId(consultationId, pageable);
        return result.map(this::toResponse);
    }

    public OrdonnanceResponse get(UUID id) {
        return repo.findById(id).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Ordonnance not found: " + id));
    }

    public OrdonnanceResponse create(OrdonnanceRequest req) {
        Ordonnance o = Ordonnance.builder()
                .date(req.date())
                .type(req.type())
                .consultationId(req.consultationId())
                .build();
        return toResponse(repo.save(o));
    }

    public OrdonnanceResponse update(UUID id, OrdonnanceRequest req) {
        Ordonnance o = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Ordonnance not found: " + id));
        o.setDate(req.date());
        o.setType(req.type());
        o.setConsultationId(req.consultationId());
        return toResponse(repo.save(o));
    }

    public void delete(UUID id) { repo.deleteById(id); }

    private OrdonnanceResponse toResponse(Ordonnance o) {
        return new OrdonnanceResponse(o.getId(), o.getDate(), o.getType(), o.getConsultationId(), o.getCreatedAt());
    }
}