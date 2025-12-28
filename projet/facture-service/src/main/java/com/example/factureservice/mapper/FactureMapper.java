package com.example.factureservice.mapper;

import com.example.factureservice.dto.FactureDTO;
import com.example.factureservice.model.Facture;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author sawssan
 **/
@Mapper(componentModel = "spring")
public interface FactureMapper {
    FactureMapper INSTANCE = Mappers.getMapper(FactureMapper.class);

    FactureDTO toDTO(Facture facture);
    Facture toEntity(FactureDTO dto);
}
