package com.example.rendezvousservice.service;

import com.example.rendezvousservice.dto.RendezVousDTO;
import com.example.rendezvousservice.exception.ResourceNotFoundException;
import com.example.rendezvousservice.mapper.RendezVousMapper;
import com.example.rendezvousservice.model.RendezVous;
import com.example.rendezvousservice.repository.RendezVousRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sawssan
 **/
@Service
public class RendezVousServiceImpl implements RendezVousService {

    private final RendezVousRepository repository;
    private final RendezVousMapper mapper;

    public RendezVousServiceImpl(RendezVousRepository repository, RendezVousMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public RendezVousDTO create(RendezVousDTO dto) {
        RendezVous entity = mapper.toEntity(dto);
        RendezVous saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public RendezVousDTO getById(Long id) {
        RendezVous entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RendezVous not found with id " + id));
        return mapper.toDTO(entity);
    }

    @Override
    public List<RendezVousDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RendezVousDTO update(Long id, RendezVousDTO dto) {
        RendezVous entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RendezVous not found with id " + id));

        entity.setDateRdv(dto.getDateRdv());
        entity.setHeureRdv(dto.getHeureRdv());
        entity.setMotif(dto.getMotif());
        entity.setStatut(dto.getStatut());
        entity.setNotes(dto.getNotes());
        entity.setPatientId(dto.getPatientId());
        entity.setUserId(dto.getUserId());

        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
