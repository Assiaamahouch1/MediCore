package com.example.rendezvousservice.mapper;

import com.example.rendezvousservice.dto.RendezVousDTO;
import com.example.rendezvousservice.model.RendezVous;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-24T12:56:22+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class RendezVousMapperImpl implements RendezVousMapper {

    @Override
    public RendezVousDTO toDTO(RendezVous rendezVous) {
        if ( rendezVous == null ) {
            return null;
        }

        RendezVousDTO rendezVousDTO = new RendezVousDTO();

        return rendezVousDTO;
    }

    @Override
    public RendezVous toEntity(RendezVousDTO dto) {
        if ( dto == null ) {
            return null;
        }

        RendezVous rendezVous = new RendezVous();

        return rendezVous;
    }
}
