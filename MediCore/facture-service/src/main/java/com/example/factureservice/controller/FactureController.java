package com.example.factureservice.controller;

import com.example.factureservice.dto.FactureDTO;
import com.example.factureservice.service.FactureService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sawssan
 **/
@RestController
@RequestMapping("/factures")
public class FactureController {
    private final FactureService service;

    public FactureController(FactureService service) {
        this.service = service;
    }

    @PostMapping
    public FactureDTO create(@RequestBody FactureDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public FactureDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<FactureDTO> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public FactureDTO update(@PathVariable Long id, @RequestBody FactureDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
