package com.example.factureservice.controller;

import com.example.factureservice.dto.FactureDTO;
import com.example.factureservice.model.Facture;
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
        System.out.println(dto);
        return service.create(dto);
    }


    @GetMapping("/{id}")
    public FactureDTO getById(@PathVariable Long id) {
        System.out.println(service.getById(id));
        return service.getById(id);
    }

    @GetMapping("/all")
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
