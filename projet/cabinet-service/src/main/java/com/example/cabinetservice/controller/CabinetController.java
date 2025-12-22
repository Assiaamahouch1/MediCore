package com.example.cabinetservice.controller;

import com.example.cabinetservice.dto.*;
import com.example.cabinetservice.model.Cabinet;
import com.example.cabinetservice.service.CabinetService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/cabinets")
public class CabinetController {

    private final CabinetService service;

    public CabinetController(CabinetService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Cabinet> list(@RequestParam(required = false) String q,
                              @RequestParam(required = false) Integer page,
                              @RequestParam(required = false) Integer size,
                              @RequestParam(required = false) String sort) {
        return service.list(q, page, size, sort);
    }

    @GetMapping("/{id}")
    public Cabinet get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<Cabinet> create(@Valid @RequestBody CabinetCreateRequest req) {
        Cabinet created = service.create(req);
        return ResponseEntity.created(URI.create("/cabinets/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public Cabinet update(@PathVariable Long id, @Valid @RequestBody CabinetUpdateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Activation / Désactivation
    @PostMapping("/{id}/activate")
    public Cabinet activate(@PathVariable Long id) {
        return service.activate(id);
    }

    @PostMapping("/{id}/deactivate")
    public Cabinet deactivate(@PathVariable Long id) {
        return service.deactivate(id);
    }

    // Abonnement
    @PostMapping("/{id}/subscription/renew")
    public Cabinet renew(@PathVariable Long id, @Valid @RequestBody SubscriptionRenewRequest req) {
        return service.renew(id, req);
    }

    @PostMapping("/{id}/subscription/set-expiration")
    public Cabinet setExpiration(@PathVariable Long id, @Valid @RequestBody SubscriptionSetExpirationRequest req) {
        return service.setExpiration(id, req);
    }

    @GetMapping("/{id}/subscription/status")
    public SubscriptionStatusResponse status(@PathVariable Long id) {
        return service.status(id);
    }
}