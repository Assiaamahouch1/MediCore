package com.example.cabinetservice.service;

import com.example.cabinetservice.dto.*;
import com.example.cabinetservice.mapper.CabinetMapper;
import com.example.cabinetservice.model.Cabinet;
import com.example.cabinetservice.repository.CabinetRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

@Service
public class CabinetService {

    private final CabinetRepository repo;
    private final CabinetMapper mapper;

    public CabinetService(CabinetRepository repo, CabinetMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public Page<CabinetResponse> list(String q, Integer page, Integer size, String sort) {
        Sort s = Sort.by((sort == null || sort.isBlank()) ? "nom" : sort.split(",")[0]);
        if (sort != null && sort.toLowerCase().endsWith(",desc")) s = s.descending();
        Pageable pageable = PageRequest.of(page == null ? 0 : page, size == null ? 20 : size, s);
        Page<Cabinet> result = (q == null || q.isBlank())
                ? repo.findAll(pageable)
                : repo.findByNomContainingIgnoreCase(q.trim(), pageable);
        return result.map(mapper::toResponse);
    }

    public CabinetResponse get(Long id) {
        return repo.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));
    }

    public CabinetResponse create(CabinetCreateRequest req) {
        Cabinet c = mapper.toEntity(req);
        return mapper.toResponse(repo.save(c));
    }

    public CabinetResponse update(Long id, CabinetUpdateRequest req) {
        Cabinet c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));
        mapper.updateEntity(req, c);
        return mapper.toResponse(repo.save(c));
    }

    public void delete(Long id) { repo.deleteById(id); }

    public CabinetResponse activate(Long id) {
        Cabinet c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));
        LocalDate today = LocalDate.now();
        if (c.getDate_expiration_service() == null || !c.getDate_expiration_service().isAfter(today)) {
            throw new IllegalStateException("Subscription expired or not set. Renew or set expiration before activation.");
        }
        c.setService_actif(Boolean.TRUE);
        return mapper.toResponse(repo.save(c));
    }

    public CabinetResponse deactivate(Long id) {
        Cabinet c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));
        c.setService_actif(Boolean.FALSE);
        return mapper.toResponse(repo.save(c));
    }

    public CabinetResponse renew(Long id, SubscriptionRenewRequest req) {
        Cabinet c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));
        LocalDate base = c.getDate_expiration_service();
        LocalDate today = LocalDate.now();
        if (base == null || !base.isAfter(today)) base = today;
        LocalDate newExp = base.plusMonths(req.months());
        c.setDate_expiration_service(newExp);
        c.setService_actif(Boolean.TRUE);
        return mapper.toResponse(repo.save(c));
    }

    public CabinetResponse setExpiration(Long id, SubscriptionSetExpirationRequest req) {
        Cabinet c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));
        c.setDate_expiration_service(req.expirationDate());
        c.setService_actif(req.expirationDate().isAfter(LocalDate.now()));
        return mapper.toResponse(repo.save(c));
    }

    public SubscriptionStatusResponse status(Long id) {
        Cabinet c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));
        LocalDate exp = c.getDate_expiration_service();
        LocalDate today = LocalDate.now();
        boolean expired = (exp == null) || !exp.isAfter(today);
        long remaining = expired ? 0 : ChronoUnit.DAYS.between(today, exp);
        boolean active = Boolean.TRUE.equals(c.getService_actif()) && !expired;
        return new SubscriptionStatusResponse(active, expired, remaining);
    }

    // Utilitaire pour le scheduler (pour test unitaire direct)
    public int deactivateExpired() {
        var expired = repo.findActiveExpired(LocalDate.now());
        expired.forEach(c -> c.setService_actif(Boolean.FALSE));
        repo.saveAll(expired);
        return expired.size();
    }
}