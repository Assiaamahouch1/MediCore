package com.example.cabinetservice.controller;

import com.example.cabinetservice.dto.*;
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

    public CabinetController(CabinetService service) { this.service = service; }

    @GetMapping
    public Page<CabinetResponse> list(@RequestParam(required = false) String q,
                                      @RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer size,
                                      @RequestParam(required = false) String sort) {
        return service.list(q, page, size, sort);
    }

    @GetMapping("/{id}")
    public CabinetResponse get(@PathVariable Long id) { return service.get(id); }

    @PostMapping
    public ResponseEntity<CabinetResponse> create(@Valid @RequestBody CabinetCreateRequest req) {
        CabinetResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/cabinets/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public CabinetResponse update(@PathVariable Long id, @Valid @RequestBody CabinetUpdateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activate")
    public CabinetResponse activate(@PathVariable Long id) { return service.activate(id); }

    @PostMapping("/{id}/deactivate")
    public CabinetResponse deactivate(@PathVariable Long id) { return service.deactivate(id); }

    @PostMapping("/{id}/subscription/renew")
    public CabinetResponse renew(@PathVariable Long id, @Valid @RequestBody SubscriptionRenewRequest req) {
        return service.renew(id, req);
    }

    @PostMapping("/{id}/subscription/set-expiration")
    public CabinetResponse setExpiration(@PathVariable Long id, @Valid @RequestBody SubscriptionSetExpirationRequest req) {
        return service.setExpiration(id, req);
    }

    @GetMapping("/{id}/subscription/status")
    public SubscriptionStatusResponse status(@PathVariable Long id) {
        return service.status(id);
    }
}