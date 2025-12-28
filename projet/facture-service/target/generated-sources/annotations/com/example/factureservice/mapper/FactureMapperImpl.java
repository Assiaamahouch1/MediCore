package com.example.factureservice.mapper;

import com.example.factureservice.dto.FactureDTO;
import com.example.factureservice.model.Facture;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-24T00:17:13+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class FactureMapperImpl implements FactureMapper {

    @Override
    public FactureDTO toDTO(Facture facture) {
        if ( facture == null ) {
            return null;
        }

        FactureDTO factureDTO = new FactureDTO();

        return factureDTO;
    }

    @Override
    public Facture toEntity(FactureDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Facture facture = new Facture();

        return facture;
    }
}
