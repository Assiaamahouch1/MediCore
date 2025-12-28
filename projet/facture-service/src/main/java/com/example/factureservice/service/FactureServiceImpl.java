package com.example.factureservice.service;

import com.example.factureservice.dto.FactureDTO;
import com.example.factureservice.exception.ResourceNotFoundException;
import com.example.factureservice.mapper.FactureMapper;
import com.example.factureservice.model.Facture;
import com.example.factureservice.repository.FactureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sawssan
 **/
@Service
public class FactureServiceImpl implements FactureService {

    private final FactureRepository repository;
    private final FactureMapper mapper;

    public FactureServiceImpl(FactureRepository repository, FactureMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public FactureDTO create(FactureDTO dto) {
        Facture entity = mapper.toEntity(dto);
        Facture saved = repository.save(entity);
        return mapper.toDTO(saved);
    }


    @Override
    public FactureDTO getById(Long id) {
        Facture entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture not found with id " + id));
        return mapper.toDTO(entity);
    }

    @Override
    public List<FactureDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FactureDTO update(Long id, FactureDTO dto) {
        Facture entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture not found with id " + id));

        entity.setMontant(dto.getMontant());
        entity.setModePaiement(dto.getModePaiement());
        entity.setRendezVousId(dto.getRendezVousId());

        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
