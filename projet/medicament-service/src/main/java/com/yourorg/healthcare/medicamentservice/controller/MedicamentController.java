package com.yourorg.healthcare.medicamentservice.controller;


import com.yourorg.healthcare.medicamentservice.model.Medicament;
import com.yourorg.healthcare.medicamentservice.repository.MedicamentRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/medicaments")
public class MedicamentController {
    private final MedicamentRepository repo;

    public MedicamentController(MedicamentRepository repo) { this.repo = repo; }

    @GetMapping
    public Page<Medicament> list(@RequestParam(required=false) String q,
                                 @RequestParam(defaultValue="0") int page,
                                 @RequestParam(defaultValue="20") int size,
                                 @RequestParam(defaultValue="nom") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return repo.search(q, pageable);
    }

    @GetMapping("/{id}")
    public Medicament get(@PathVariable UUID id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found: " + id));
    }

    @PostMapping
    public ResponseEntity<Medicament> create(@Valid @RequestBody Medicament m) {
        if (m.getId() == null) m.setId(UUID.randomUUID());
        if (repo.existsByNomIgnoreCase(m.getNom()))
            throw new IllegalArgumentException("Medicament déjà existant: " + m.getNom());
        Medicament created = repo.save(m);
        return ResponseEntity.created(URI.create("/medicaments/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public Medicament update(@PathVariable UUID id, @Valid @RequestBody Medicament payload) {
        Medicament m = get(id);
        m.setNom(payload.getNom());
        m.setDescription(payload.getDescription());
        m.setAtcCode(payload.getAtcCode());
        m.setForme(payload.getForme());
        m.setDosageUnite(payload.getDosageUnite());
        return repo.save(m);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // endpoint pour autocomplete léger
    @GetMapping("/suggest")
    public List<Map<String, String>> suggest(@RequestParam String q,
                                             @RequestParam(defaultValue="10") int limit) {
        Page<Medicament> page = repo.search(q, PageRequest.of(0, limit, Sort.by("nom")));
        return page.map(m -> Map.of("id", m.getId().toString(), "nom", m.getNom())).getContent();
    }
}