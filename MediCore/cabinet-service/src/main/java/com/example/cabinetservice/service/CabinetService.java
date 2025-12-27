package com.example.cabinetservice.service;

import com.example.cabinetservice.model.Cabinet;
import com.example.cabinetservice.repository.CabinetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CabinetService {
    @Autowired
    private CabinetRepository repository;



    public Cabinet create(Cabinet cabinet) {
        return repository.save(cabinet);
    }

    public Optional<Cabinet> getById(Long id) {
        return repository.findById(id);
    }

    public List<Cabinet> getAll() {
        return repository.findAll();
    }

    public Cabinet update(Long id, Cabinet cabinet) {
        return repository.findById(id).map(c -> {
            c.setNom(cabinet.getNom());
            c.setLogo(cabinet.getLogo());
            c.setAdresse(cabinet.getAdresse());
            c.setSpecialite(cabinet.getSpecialite());
            c.setTel(cabinet.getTel());
            c.setDate_expiration_service(cabinet.getDate_expiration_service());
            return repository.save(c);
        }).orElseThrow(() -> new RuntimeException("Cabinet not found"));
    }

    public void activate(Long id) {
        repository.findById(id).ifPresent(c -> {
            c.setService_actif(true);
            repository.save(c);
        });
    }

    public void deactivate(Long id) {
        repository.findById(id).ifPresent(c -> {
            c.setService_actif(false);
            repository.save(c);
        });
    }
}
