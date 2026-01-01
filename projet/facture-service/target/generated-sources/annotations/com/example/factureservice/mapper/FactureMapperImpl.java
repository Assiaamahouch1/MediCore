package com.example.factureservice.mapper;

import com.example.factureservice.dto.FactureDTO;
import com.example.factureservice.model.Facture;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-01T00:33:07+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class FactureMapperImpl implements FactureMapper {

    @Override
    public FactureDTO toDTO(Facture facture) {
        if ( facture == null ) {
            return null;
        }

        FactureDTO factureDTO = new FactureDTO();

        factureDTO.setIdFacture( facture.getIdFacture() );
        factureDTO.setModePaiement( facture.getModePaiement() );
        factureDTO.setMontant( facture.getMontant() );
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
        facture.setModePaiement( dto.getModePaiement() );
        facture.setMontant( dto.getMontant() );
        facture.setRendezVousId( dto.getRendezVousId() );

        return facture;
    }
}
