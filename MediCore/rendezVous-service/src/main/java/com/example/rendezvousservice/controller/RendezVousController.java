package com.example.rendezvousservice.controller;

import com.example.rendezvousservice.dto.RendezVousDTO;
import com.example.rendezvousservice.service.RendezVousService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sawssan
 **/
@RestController
@RequestMapping("/rendezVous")
public class RendezVousController {

    private final RendezVousService service;

    public RendezVousController(RendezVousService service) {
        this.service = service;
    }

    @PostMapping
    public RendezVousDTO create(@RequestBody RendezVousDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public RendezVousDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<RendezVousDTO> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public RendezVousDTO update(@PathVariable Long id, @RequestBody RendezVousDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
