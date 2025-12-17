package com.example.cabinetservice.controller;

import com.example.cabinetservice.model.Cabinet;
import com.example.cabinetservice.service.CabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cabinets")
public class CabinetController {
    @Autowired
    private CabinetService service;



    @PostMapping
    public ResponseEntity<Cabinet> create(@RequestBody Cabinet cabinet) {
        return ResponseEntity.ok(service.create(cabinet));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cabinet> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Cabinet>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cabinet> update(@PathVariable Long id, @RequestBody Cabinet cabinet) {
        return ResponseEntity.ok(service.update(id, cabinet));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        service.deactivate(id);
        return ResponseEntity.ok().build();
    }
}
