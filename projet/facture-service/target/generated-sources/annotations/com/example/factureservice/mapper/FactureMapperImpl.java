package com.example.factureservice.mapper;

import com.example.factureservice.dto.FactureDTO;
import com.example.factureservice.model.Facture;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-03T01:18:54+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class FactureMapperImpl implements FactureMapper {

    @Override
    public FactureDTO toDTO(Facture facture) {
        if ( facture == null ) {
            return null;
        }

        FactureDTO factureDTO = new FactureDTO();

        factureDTO.setDate( facture.getDate() );
        factureDTO.setIdFacture( facture.getIdFacture() );
        factureDTO.setMontant( facture.getMontant() );
        factureDTO.setModePaiement( facture.getModePaiement() );
        factureDTO.setRendezVousId( facture.getRendezVousId() );

        return factureDTO;
    }

    @Override
    public Facture toEntity(FactureDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Facture facture = new Facture();

        facture.setIdFacture( dto.getIdFacture() );
        facture.setMontant( dto.getMontant() );
        facture.setModePaiement( dto.getModePaiement() );
        facture.setRendezVousId( dto.getRendezVousId() );
        facture.setDate( dto.getDate() );

        return facture;
    }
}
