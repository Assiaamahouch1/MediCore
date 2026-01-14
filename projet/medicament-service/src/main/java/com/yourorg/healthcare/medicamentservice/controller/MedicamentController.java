package com.yourorg.healthcare.medicamentservice.controller;

import com.yourorg.healthcare.medicamentservice.model.Medicament;
import com.yourorg.healthcare.medicamentservice.repository.MedicamentRepository;
import com.yourorg.healthcare.medicamentservice.service.ExternalMedicamentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/medicaments")
public class MedicamentController {

    private final ExternalMedicamentService externalMedicamentService;
    private final MedicamentRepository medicamentRepository;

    public MedicamentController(ExternalMedicamentService externalMedicamentService,
                                MedicamentRepository medicamentRepository) {
        this.externalMedicamentService = externalMedicamentService;
        this.medicamentRepository = medicamentRepository;
    }

    // Lancer l'import depuis l'API externe
    @PostMapping("/import")
    public String importMedicaments() {
        externalMedicamentService.fetchAndSaveMedicaments();
        return "Import terminé !";
    }

    // Lister tous les médicaments depuis la base
    @GetMapping
    public List<Medicament> getAllMedicaments() {
        return medicamentRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        externalMedicamentService.delete(id);
    }
    @GetMapping("/suggest")
    public List<Medicament> suggest(@RequestParam(required = false) String q,
                                    @RequestParam(defaultValue = "10") int limit) {
        String query = (q != null && !q.trim().isEmpty()) ? q.trim() : null;
        Pageable pageable = PageRequest.of(0, limit, Sort.by("nom"));
        Page<Medicament> page = medicamentRepository.search(query, pageable);
        return page.getContent();
    }
}