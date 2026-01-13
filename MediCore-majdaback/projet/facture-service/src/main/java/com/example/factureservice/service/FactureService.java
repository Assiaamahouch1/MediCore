package com.example.factureservice.service;

import com.example.factureservice.dto.FactureDTO;
import com.example.factureservice.model.Facture;

import java.util.List;

/**
 * @author sawssan
 **/
public interface FactureService {
    FactureDTO create(FactureDTO dto);
    FactureDTO getById(Long id);
    List<FactureDTO> getAll();
    FactureDTO update(Long id, FactureDTO dto);
    void delete(Long id);
}
