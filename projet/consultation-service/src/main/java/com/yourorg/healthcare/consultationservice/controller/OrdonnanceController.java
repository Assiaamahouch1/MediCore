package com.yourorg.healthcare.consultationservice.controller;

import com.yourorg.healthcare.consultationservice.dto.OrdonnanceRequest;
import com.yourorg.healthcare.consultationservice.dto.OrdonnanceResponse;
import com.yourorg.healthcare.consultationservice.service.OrdonnanceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/ordonnances")
public class OrdonnanceController {

    private final OrdonnanceService service;

    public OrdonnanceController(OrdonnanceService service) { this.service = service; }

    @GetMapping
    public Page<OrdonnanceResponse> list(@RequestParam(required=false) UUID consultationId,
                                         @RequestParam(required=false) Integer page,
                                         @RequestParam(required=false) Integer size,
                                         @RequestParam(required=false) String sort) {
        return service.list(consultationId, page, size, sort);
    }

    @GetMapping("/{id}")
    public OrdonnanceResponse get(@PathVariable UUID id) { return service.get(id); }

    @PostMapping
    public ResponseEntity<OrdonnanceResponse> create(@Valid @RequestBody OrdonnanceRequest req) {
        OrdonnanceResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/ordonnances/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public OrdonnanceResponse update(@PathVariable UUID id, @Valid @RequestBody OrdonnanceRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}