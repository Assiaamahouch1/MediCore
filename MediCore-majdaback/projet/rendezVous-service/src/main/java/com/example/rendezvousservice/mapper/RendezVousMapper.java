package com.example.rendezvousservice.mapper;

/**
 * @author sawssan
 **/
import com.example.rendezvousservice.dto.RendezVousDTO;
import com.example.rendezvousservice.model.RendezVous;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RendezVousMapper {

    RendezVousDTO toDTO(RendezVous rendezVous);

    RendezVous toEntity(RendezVousDTO dto);
}
