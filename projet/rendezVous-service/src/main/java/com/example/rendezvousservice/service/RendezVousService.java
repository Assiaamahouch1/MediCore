package com.example.rendezvousservice.service;

import com.example.rendezvousservice.dto.RendezVousDTO;

import java.util.List;

/**
 * @author sawssan
 **/
public interface RendezVousService {
    RendezVousDTO create(RendezVousDTO dto);
    RendezVousDTO getById(Long id);
    List<RendezVousDTO> getAll();
    RendezVousDTO update(Long id, RendezVousDTO dto);
    void delete(Long id);
}
