package com.example.cabinetservice.service;

import com.example.cabinetservice.dto.*;
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

    public CabinetService(CabinetRepository repo) {
        this.repo = repo;
    }

    public Page<Cabinet> list(String q, Integer page, Integer size, String sort) {
        Sort s = Sort.by((sort == null || sort.isBlank()) ? "nom" : sort.split(",")[0]);
        if (sort != null && sort.toLowerCase().endsWith(",desc")) s = s.descending();
        Pageable pageable = PageRequest.of(page == null ? 0 : page, size == null ? 20 : size, s);
        if (q == null || q.isBlank()) return repo.findAll(pageable);
        return repo.findByNomContainingIgnoreCase(q.trim(), pageable);
    }

    public Cabinet get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));
    }

    public Cabinet create(CabinetCreateRequest req) {
        Cabinet c = new Cabinet();
        c.setLogo(req.logo());
        c.setNom(req.nom());
        c.setSpecialite(req.specialite());
        c.setAdresse(req.adresse());
        c.setTel(req.tel());
        c.setService_actif(Boolean.FALSE);
        c.setDate_expiration_service(null);
        return repo.save(c);
    }

    public Cabinet update(Long id, CabinetUpdateRequest req) {
        Cabinet c = get(id);
        c.setLogo(req.logo());
        if (req.nom() != null) c.setNom(req.nom());
        c.setSpecialite(req.specialite());
        c.setAdresse(req.adresse());
        c.setTel(req.tel());
        return repo.save(c);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Cabinet activate(Long id) {
        Cabinet c = get(id);
        LocalDate today = LocalDate.now();
        if (c.getDate_expiration_service() == null || c.getDate_expiration_service().isBefore(today)) {
            throw new IllegalStateException("Subscription expired or not set. Renew or set expiration before activation.");
        }
        c.setService_actif(Boolean.TRUE);
        return repo.save(c);
    }

    public Cabinet deactivate(Long id) {
        Cabinet c = get(id);
        c.setService_actif(Boolean.FALSE);
        return repo.save(c);
    }

    public Cabinet renew(Long id, SubscriptionRenewRequest req) {
        Cabinet c = get(id);
        LocalDate base = c.getDate_expiration_service();
        LocalDate today = LocalDate.now();
        if (base == null || base.isBefore(today)) {
            base = today;
        }
        LocalDate newExp = base.plusMonths(req.months());
        c.setDate_expiration_service(newExp);
        // Si la nouvelle expiration est future, on peut réactiver automatiquement
        c.setService_actif(Boolean.TRUE);
        return repo.save(c);
    }

    public Cabinet setExpiration(Long id, SubscriptionSetExpirationRequest req) {
        Cabinet c = get(id);
        c.setDate_expiration_service(req.expirationDate());
        // Active si future, sinon désactive
        c.setService_actif(req.expirationDate().isAfter(LocalDate.now()));
        return repo.save(c);
    }

    public SubscriptionStatusResponse status(Long id) {
        Cabinet c = get(id);
        LocalDate exp = c.getDate_expiration_service();
        LocalDate today = LocalDate.now();
        boolean expired = (exp == null) || !exp.isAfter(today);
        long remaining = expired ? 0 : ChronoUnit.DAYS.between(today, exp);
        boolean active = Boolean.TRUE.equals(c.getService_actif()) && !expired;
        return new SubscriptionStatusResponse(active, expired, remaining);
    }
}