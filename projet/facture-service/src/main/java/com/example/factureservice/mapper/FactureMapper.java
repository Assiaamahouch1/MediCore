package com.example.factureservice.mapper;

import com.example.factureservice.dto.FactureDTO;
import com.example.factureservice.model.Facture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author sawssan
 **/
@Mapper(componentModel = "spring")
public interface FactureMapper {

    @Mapping(target = "date", source = "date")
    FactureDTO toDTO(Facture facture);
    Facture toEntity(FactureDTO dto);
}
